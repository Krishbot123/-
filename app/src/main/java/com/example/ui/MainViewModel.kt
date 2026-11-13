package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.FormatsCatalog
import com.example.data.TeacherProfileRepository
import com.example.export.DocumentContentBuilder
import com.example.export.FileExportManager
import com.example.model.DocumentFileType
import com.example.model.FormatCategory
import com.example.model.FormatFormData
import com.example.model.FormatItem
import com.example.model.GeneratedDocumentContent
import com.example.model.GeneratedFileInfo
import com.example.model.TeacherProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class FormatFilter(val label: String) {
    ALL("All"),
    PDF("PDF"),
    EXCEL("Excel"),
    WORD("Word"),
    TEACHERS_15("Teachers (15)"),
    LATEST("Latest 2026-27")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val profileRepo = TeacherProfileRepository(application)
    val exportManager = FileExportManager(application)

    val teacherProfile: StateFlow<TeacherProfile> = profileRepo.profileFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TeacherProfile()
    )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow(FormatFilter.ALL)
    val selectedFilter: StateFlow<FormatFilter> = _selectedFilter.asStateFlow()

    val filteredFormats: StateFlow<List<FormatItem>> = combine(
        _searchQuery,
        _selectedFilter
    ) { query, filter ->
        val q = query.trim().lowercase()
        FormatsCatalog.allFormats.filter { item ->
            val matchesFilter = when (filter) {
                FormatFilter.ALL -> true
                FormatFilter.PDF -> item.fileType == DocumentFileType.PDF
                FormatFilter.EXCEL -> item.fileType == DocumentFileType.EXCEL
                FormatFilter.WORD -> item.fileType == DocumentFileType.WORD
                FormatFilter.TEACHERS_15 -> item.isFromScreenshot
                FormatFilter.LATEST -> item.category == FormatCategory.LATEST_UPDATES ||
                    item.category == FormatCategory.LATEST_FORMATS
            }
            val matchesQuery = q.isEmpty() ||
                item.title.lowercase().contains(q) ||
                item.subtitle.lowercase().contains(q) ||
                item.authorOrTag.lowercase().contains(q) ||
                item.category.title.lowercase().contains(q) ||
                item.category.hindiTitle.lowercase().contains(q) ||
                item.fileType.label.lowercase().contains(q)
            matchesFilter && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FormatsCatalog.allFormats
    )

    private val _currentFormat = MutableStateFlow<FormatItem?>(null)
    val currentFormat: StateFlow<FormatItem?> = _currentFormat.asStateFlow()

    private val _formData = MutableStateFlow(FormatFormData())
    val formData: StateFlow<FormatFormData> = _formData.asStateFlow()

    val livePreviewDocument: StateFlow<GeneratedDocumentContent?> = combine(
        _currentFormat,
        _formData
    ) { fmt, data ->
        fmt?.let { DocumentContentBuilder.build(it, data) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    private val _savedFiles = MutableStateFlow<List<GeneratedFileInfo>>(emptyList())
    val savedFiles: StateFlow<List<GeneratedFileInfo>> = _savedFiles.asStateFlow()

    private val _isExporting = MutableStateFlow(false)
    val isExporting: StateFlow<Boolean> = _isExporting.asStateFlow()

    private val _lastExportedFile = MutableStateFlow<GeneratedFileInfo?>(null)
    val lastExportedFile: StateFlow<GeneratedFileInfo?> = _lastExportedFile.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    init {
        refreshSavedFiles()
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectFilter(filter: FormatFilter) {
        _selectedFilter.value = filter
    }

    fun openFormatBuilder(formatId: String) {
        val found = FormatsCatalog.findById(formatId) ?: return
        _currentFormat.value = found
        applyProfileFieldsToForm(teacherProfile.value)
    }

    fun updateFormData(transform: (FormatFormData) -> FormatFormData) {
        _formData.value = transform(_formData.value)
    }

    fun applySavedProfileToForm() {
        applyProfileFieldsToForm(teacherProfile.value)
        _snackbarMessage.value = "प्रोफ़ाइल विवरण फॉर्म में भर दिया गया है"
    }

    private fun applyProfileFieldsToForm(profile: TeacherProfile) {
        _formData.value = _formData.value.copy(
            employeeName = profile.employeeName,
            fatherOrHusbandName = profile.fatherOrHusbandName,
            employeeId = profile.employeeId,
            designation = profile.designation,
            subject = profile.subject,
            schoolName = profile.schoolName,
            udiseCode = profile.udiseCode,
            block = profile.block,
            district = profile.district,
            ddoName = profile.ddoName,
            oldPayLevel = profile.payLevel,
            oldBasicPay = profile.basicPay,
            joiningDate = profile.joiningDate,
            dob = profile.dob,
            mobile = profile.mobile
        )
    }

    fun saveProfile(profile: TeacherProfile) {
        viewModelScope.launch {
            profileRepo.saveProfile(profile)
            applyProfileFieldsToForm(profile)
            _snackbarMessage.value = "शिक्षक प्रोफ़ाइल सुरक्षित की गई"
        }
    }

    fun exportCurrentFormat(saveToDownloads: Boolean = false) {
        val fmt = _currentFormat.value ?: return
        exportFormatWithData(fmt, _formData.value, saveToDownloads)
    }

    fun quickDownloadFormat(format: FormatItem) {
        applyProfileFieldsToForm(teacherProfile.value)
        exportFormatWithData(format, _formData.value, saveToDownloads = true)
    }

    private fun exportFormatWithData(
        format: FormatItem,
        data: FormatFormData,
        saveToDownloads: Boolean
    ) {
        viewModelScope.launch {
            _isExporting.value = true
            try {
                val fileInfo = exportManager.generateDocument(format, data)
                if (saveToDownloads) {
                    exportManager.saveToPublicDownloads(fileInfo)
                        .onSuccess { path ->
                            _snackbarMessage.value = "${format.fileType.label} तैयार: $path"
                        }
                }
                _lastExportedFile.value = fileInfo
                refreshSavedFiles()
            } catch (e: Exception) {
                _snackbarMessage.value = "फाइल बनाने में त्रुटि: ${e.localizedMessage ?: "Error"}"
            } finally {
                _isExporting.value = false
            }
        }
    }

    fun saveFileToDownloads(fileInfo: GeneratedFileInfo) {
        viewModelScope.launch {
            exportManager.saveToPublicDownloads(fileInfo)
                .onSuccess { path ->
                    _snackbarMessage.value = "Downloads में सेव किया गया: $path"
                }
                .onFailure {
                    _snackbarMessage.value = "Downloads में सेव नहीं हो सका"
                }
        }
    }

    fun deleteSavedFile(fileInfo: GeneratedFileInfo) {
        viewModelScope.launch {
            exportManager.deleteFile(fileInfo)
            refreshSavedFiles()
            _snackbarMessage.value = "फाइल हटाई गई"
        }
    }

    fun refreshSavedFiles() {
        viewModelScope.launch {
            _savedFiles.value = exportManager.listSavedFiles()
        }
    }

    fun dismissExportDialog() {
        _lastExportedFile.value = null
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun showMessage(message: String) {
        _snackbarMessage.value = message
    }
}
