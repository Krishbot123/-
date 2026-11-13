package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.FormatsCatalog
import com.example.data.PortalHomeCatalog
import com.example.model.FormatItem
import com.example.model.GeneratedFileInfo
import com.example.model.TeacherProfile
import com.example.ui.components.SavedFileCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilesAndPortalsScreen(
    teacherProfile: TeacherProfile,
    savedFiles: List<GeneratedFileInfo>,
    onSaveProfile: (TeacherProfile) -> Unit,
    onOpenFormatBuilder: (FormatItem) -> Unit,
    onOpenFile: (GeneratedFileInfo) -> Unit,
    onShareFile: (GeneratedFileInfo) -> Unit,
    onSaveFileToDownloads: (GeneratedFileInfo) -> Unit,
    onDeleteFile: (GeneratedFileInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("My Files (${savedFiles.size})", "Teacher Profile", "Portals & Tools")

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PrimaryTabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.widthIn(max = 760.dp)
        ) {
            tabs.forEachIndexed { idx, label ->
                Tab(
                    selected = selectedTab == idx,
                    onClick = { selectedTab = idx },
                    modifier = Modifier.testTag("files_portals_tab_$idx"),
                    text = {
                        Text(
                            text = label,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = if (selectedTab == idx) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            when (selectedTab) {
                0 -> SavedFilesTab(
                    savedFiles = savedFiles,
                    onOpenFile = onOpenFile,
                    onShareFile = onShareFile,
                    onSaveFileToDownloads = onSaveFileToDownloads,
                    onDeleteFile = onDeleteFile
                )
                1 -> TeacherProfileTab(
                    teacherProfile = teacherProfile,
                    onSaveProfile = onSaveProfile
                )
                else -> PortalsTab(onOpenFormatBuilder = onOpenFormatBuilder)
            }
        }
    }
}

@Composable
private fun SavedFilesTab(
    savedFiles: List<GeneratedFileInfo>,
    onOpenFile: (GeneratedFileInfo) -> Unit,
    onShareFile: (GeneratedFileInfo) -> Unit,
    onSaveFileToDownloads: (GeneratedFileInfo) -> Unit,
    onDeleteFile: (GeneratedFileInfo) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .widthIn(max = 760.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (savedFiles.isEmpty()) {
            item(key = "no_files") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 56.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.FolderOpen,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "अभी कोई फाइल तैयार नहीं की गई है",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Formats टैब से कोई भी PDF, Excel या Word प्रपत्र भरकर जनरेट करें।",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(savedFiles, key = { it.path }) { file ->
                SavedFileCard(
                    file = file,
                    onOpen = { onOpenFile(file) },
                    onShare = { onShareFile(file) },
                    onSaveDownloads = { onSaveFileToDownloads(file) },
                    onDelete = { onDeleteFile(file) }
                )
            }
        }
    }
}

@Composable
private fun TeacherProfileTab(
    teacherProfile: TeacherProfile,
    onSaveProfile: (TeacherProfile) -> Unit
) {
    var draft by remember { mutableStateOf(teacherProfile) }
    LaunchedEffect(teacherProfile) { draft = teacherProfile }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .widthIn(max = 760.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item(key = "profile_info_banner") {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "एक बार अपनी शिक्षक प्रोफ़ाइल सेव करें — सभी ${FormatsCatalog.allFormats.size}+ PDF, Excel और Word प्रपत्रों में यह जानकारी स्वतः भर जाएगी।",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        item(key = "profile_fields") {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ProfileField("कार्मिक का नाम (Employee Name)", draft.employeeName) { draft = draft.copy(employeeName = it) }
                ProfileField("पिता / पति का नाम", draft.fatherOrHusbandName) { draft = draft.copy(fatherOrHusbandName = it) }
                ProfileField("Employee ID (RJ...)", draft.employeeId) { draft = draft.copy(employeeId = it) }
                ProfileField("पद (Designation)", draft.designation) { draft = draft.copy(designation = it) }
                ProfileField("विषय (Subject)", draft.subject) { draft = draft.copy(subject = it) }
                ProfileField("विद्यालय / कार्यालय का नाम", draft.schoolName) { draft = draft.copy(schoolName = it) }
                ProfileField("UDISE / Shala Darpan Code", draft.udiseCode) { draft = draft.copy(udiseCode = it) }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ProfileField("ब्लॉक (Block)", draft.block, Modifier.weight(1f)) { draft = draft.copy(block = it) }
                    ProfileField("जिला (District)", draft.district, Modifier.weight(1f)) { draft = draft.copy(district = it) }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ProfileField("Pay Level", draft.payLevel, Modifier.weight(1f)) { draft = draft.copy(payLevel = it) }
                    ProfileField("Basic Pay (₹)", draft.basicPay, Modifier.weight(1f)) { draft = draft.copy(basicPay = it) }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ProfileField("Joining Date", draft.joiningDate, Modifier.weight(1f)) { draft = draft.copy(joiningDate = it) }
                    ProfileField("DOB", draft.dob, Modifier.weight(1f)) { draft = draft.copy(dob = it) }
                }
                ProfileField("DDO / कार्यालयध्यक्ष पदनाम", draft.ddoName) { draft = draft.copy(ddoName = it) }
            }
        }

        item(key = "save_profile_btn") {
            Button(
                onClick = { onSaveProfile(draft) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_profile_btn"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Filled.Save, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Save Teacher Profile", fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
private fun PortalsTab(
    onOpenFormatBuilder: (FormatItem) -> Unit
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .widthIn(max = 760.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item(key = "portals_header") {
            Text(
                text = "Important Govt Portals (महत्वपूर्ण पोर्टल)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        items(FormatsCatalog.portals, key = { it.url + it.title }) { portal ->
            Card(
                onClick = {
                    runCatching {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(portal.url)).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("portal_card_${portal.badge.lowercase()}"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.45f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Language,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(10.dp).size(22.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(portal.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(6.dp)) {
                                Text(portal.badge, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Spacer(Modifier.height(2.dp))
                        Text(portal.subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(Icons.AutoMirrored.Filled.OpenInNew, "Open ${portal.title}", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }

        item(key = "softwares_header") {
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Computer Softwares & Fonts (हिंदी टूल्स व फॉन्ट्स)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        items(PortalHomeCatalog.softwareLinks, key = { it.id }) { sw ->
            Card(
                onClick = {
                    FormatsCatalog.findById(sw.linkedFormatId)?.let(onOpenFormatBuilder)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.45f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(10.dp)) {
                        Icon(Icons.Filled.Computer, null, tint = MaterialTheme.colorScheme.onSecondaryContainer, modifier = Modifier.padding(10.dp).size(20.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(sw.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(sw.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Icon(Icons.AutoMirrored.Filled.OpenInNew, "Open ${sw.title}", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }

        item(key = "contributors_card") {
            Spacer(Modifier.height(6.dp))
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.45f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Groups, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "RajTeachers Contributors (योगदानकर्ता शिक्षक)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    HorizontalDivider()
                    PortalHomeCatalog.contributors.forEach { contributor ->
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(contributor.name, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                            Text(contributor.role, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}
