package com.example.export

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.example.model.DocumentFileType
import com.example.model.FormatFormData
import com.example.model.FormatItem
import com.example.model.GeneratedFileInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FileExportManager(private val context: Context) {

    private val exportsDir: File
        get() = File(context.filesDir, "exports").apply { mkdirs() }

    suspend fun generateDocument(
        format: FormatItem,
        formData: FormatFormData
    ): GeneratedFileInfo = withContext(Dispatchers.IO) {
        val content = DocumentContentBuilder.build(format, formData)
        val stamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val fileName = "${content.fileBaseName}_$stamp.${format.fileType.extension}"
        val outFile = File(exportsDir, fileName)

        when (format.fileType) {
            DocumentFileType.PDF -> PdfDocumentExporter.export(content, outFile)
            DocumentFileType.EXCEL -> XlsxDocumentExporter.export(format, formData, outFile)
            DocumentFileType.WORD -> DocxDocumentExporter.export(content, outFile)
        }

        GeneratedFileInfo(
            name = outFile.name,
            path = outFile.absolutePath,
            fileType = format.fileType,
            sizeBytes = outFile.length(),
            lastModifiedMillis = outFile.lastModified()
        )
    }

    suspend fun saveToPublicDownloads(fileInfo: GeneratedFileInfo): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val source = File(fileInfo.path)
            require(source.exists()) { "Generated file not found" }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, fileInfo.name)
                    put(MediaStore.Downloads.MIME_TYPE, fileInfo.fileType.mimeType)
                    put(MediaStore.Downloads.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/RajTeachers")
                    put(MediaStore.Downloads.IS_PENDING, 1)
                }
                val resolver = context.contentResolver
                val uri: Uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                    ?: error("Could not create file in Downloads")
                resolver.openOutputStream(uri)?.use { out ->
                    source.inputStream().use { input -> input.copyTo(out) }
                }
                values.clear()
                values.put(MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(uri, values, null, null)
                "Downloads/RajTeachers/${fileInfo.name}"
            } else {
                // On API < 29 without broad storage permission, share sheet / internal archive handles it cleanly
                fileInfo.name
            }
        }
    }

    suspend fun listSavedFiles(): List<GeneratedFileInfo> = withContext(Dispatchers.IO) {
        exportsDir.listFiles()
            ?.filter { it.isFile }
            ?.sortedByDescending { it.lastModified() }
            ?.mapNotNull { f ->
                val type = when (f.extension.lowercase(Locale.US)) {
                    "pdf" -> DocumentFileType.PDF
                    "xlsx", "xls" -> DocumentFileType.EXCEL
                    "docx", "doc" -> DocumentFileType.WORD
                    else -> null
                } ?: return@mapNotNull null
                GeneratedFileInfo(
                    name = f.name,
                    path = f.absolutePath,
                    fileType = type,
                    sizeBytes = f.length(),
                    lastModifiedMillis = f.lastModified()
                )
            } ?: emptyList()
    }

    suspend fun deleteFile(fileInfo: GeneratedFileInfo) = withContext(Dispatchers.IO) {
        File(fileInfo.path).delete()
    }

    fun openFileIntent(fileInfo: GeneratedFileInfo): Intent {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            File(fileInfo.path)
        )
        return Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, fileInfo.fileType.mimeType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    fun shareFileIntent(fileInfo: GeneratedFileInfo): Intent {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            File(fileInfo.path)
        )
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = fileInfo.fileType.mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, fileInfo.name)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        return Intent.createChooser(sendIntent, "Share ${fileInfo.fileType.label} File").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}
