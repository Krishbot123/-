package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.CalculationEngine
import com.example.model.BuilderTemplateType
import com.example.model.FormatFormData
import com.example.model.FormatItem
import com.example.model.GeneratedDocumentContent
import com.example.ui.components.FileTypeBadge
import com.example.ui.components.FileTypeIcon
import com.example.ui.theme.DevanagariFontFamily
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormatBuilderScreen(
    format: FormatItem,
    formData: FormatFormData,
    previewDoc: GeneratedDocumentContent?,
    isExporting: Boolean,
    onUpdateForm: ((FormatFormData) -> FormatFormData) -> Unit,
    onAutoFillProfile: () -> Unit,
    onExport: (saveToDownloads: Boolean) -> Unit,
    onNavigateBack: () -> Unit
) {
    BackHandler { onNavigateBack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = format.title,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "S.N. ${format.serialNumber} • Interactive ${format.fileType.label} Builder",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("builder_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    FileTypeBadge(
                        fileType = format.fileType,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { onExport(true) },
                        enabled = !isExporting,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("builder_export_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = format.fileType.accentColor,
                            contentColor = Color.White
                        )
                    ) {
                        if (isExporting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(10.dp))
                            Text("Generating ${format.fileType.label}...")
                        } else {
                            Icon(
                                imageVector = FileTypeIcon(format.fileType),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Generate & Download ${format.fileType.label} (.${format.fileType.extension})",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 760.dp),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Quick Profile Auto-Fill Banner
                item(key = "autofill_card") {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "विवरण भरें और ${format.fileType.label} बनाएं",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "नीचे अपनी जानकारी दर्ज करें — नीचे लाइव प्रीव्यू और रियल .${format.fileType.extension} फाइल अपने आप तैयार होगी।",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Spacer(Modifier.width(8.dp))
                            OutlinedButton(
                                onClick = onAutoFillProfile,
                                modifier = Modifier.testTag("builder_autofill_btn"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Filled.AutoAwesome, null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Auto-Fill")
                            }
                        }
                    }
                }

                // Section 1: Common Employee & Office Fields
                item(key = "common_fields") {
                    FormSectionCard(title = "1. कार्मिक व विद्यालय विवरण (Employee & School)") {
                        FormField("कार्मिक का नाम (Employee Name)", formData.employeeName) { v ->
                            onUpdateForm { it.copy(employeeName = v) }
                        }
                        FormField("पिता / पति का नाम", formData.fatherOrHusbandName) { v ->
                            onUpdateForm { it.copy(fatherOrHusbandName = v) }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FormField("Employee ID", formData.employeeId, Modifier.weight(1f)) { v ->
                                onUpdateForm { it.copy(employeeId = v) }
                            }
                            FormField("जन्मतिथि (DOB)", formData.dob, Modifier.weight(1f)) { v ->
                                onUpdateForm { it.copy(dob = v) }
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FormField("पद (Designation)", formData.designation, Modifier.weight(1f)) { v ->
                                onUpdateForm { it.copy(designation = v) }
                            }
                            FormField("विषय (Subject)", formData.subject, Modifier.weight(1f)) { v ->
                                onUpdateForm { it.copy(subject = v) }
                            }
                        }
                        FormField("विद्यालय / कार्यालय का नाम", formData.schoolName) { v ->
                            onUpdateForm { it.copy(schoolName = v) }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FormField("ब्लॉक (Block)", formData.block, Modifier.weight(1f)) { v ->
                                onUpdateForm { it.copy(block = v) }
                            }
                            FormField("जिला (District)", formData.district, Modifier.weight(1f)) { v ->
                                onUpdateForm { it.copy(district = v) }
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FormField("आदेश क्रमांक (Order No.)", formData.orderNumber, Modifier.weight(1f)) { v ->
                                onUpdateForm { it.copy(orderNumber = v) }
                            }
                            FormField("दिनांक (Date)", formData.orderDate, Modifier.weight(1f)) { v ->
                                onUpdateForm { it.copy(orderDate = v) }
                            }
                        }
                    }
                }

                // Section 2: Template-Specific Interactive Fields
                item(key = "specific_fields") {
                    TemplateSpecificFields(
                        templateType = format.templateType,
                        formData = formData,
                        onUpdateForm = onUpdateForm
                    )
                }

                // Section 3: Live Calculation Summary (for Arrear / Tax / MDM / Pension / Bonus / TA / Budget)
                if (format.templateType in setOf(
                        BuilderTemplateType.ARREAR_SHEET,
                        BuilderTemplateType.AUTO_SALARY_DA_ARREAR,
                        BuilderTemplateType.DA_ARREAR_60_63,
                        BuilderTemplateType.SIXTH_PAY_ARREAR,
                        BuilderTemplateType.FIFTH_PAY_ARREAR,
                        BuilderTemplateType.INCOME_TAX_CALC,
                        BuilderTemplateType.MDM_MONTHLY,
                        BuilderTemplateType.MDM_DEMAND_FORM,
                        BuilderTemplateType.MDM_ALL_UC,
                        BuilderTemplateType.PENSION_CALCULATOR_KULAK,
                        BuilderTemplateType.ADHOC_BONUS_2025_26,
                        BuilderTemplateType.BONUS_OFFICE_ORDER,
                        BuilderTemplateType.TA_BILL_EXCEL,
                        BuilderTemplateType.BUDGET_PROPOSAL_2026_27
                    )
                ) {
                    item(key = "live_calc_summary") {
                        LiveCalculationHighlightCard(format.templateType, formData)
                    }
                }

                // Section 4: Live Document Preview
                if (previewDoc != null) {
                    item(key = "live_preview") {
                        LiveDocumentPreviewCard(previewDoc)
                    }
                }

                item(key = "bottom_pad") {
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

// Moved FormSectionCard, FormField, TemplateSpecificFields, and LiveCalculationHighlightCard to BuilderSpecificFields.kt

@Composable
private fun LiveDocumentPreviewCard(previewDoc: GeneratedDocumentContent) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Visibility,
                    contentDescription = "Document preview",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Live Format Preview (प्रपत्र पूर्वावलोकन)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            HorizontalDivider()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFAFAFA), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = previewDoc.headerDepartment,
                    fontFamily = DevanagariFontFamily,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0B3B49)
                )
                Text(
                    text = previewDoc.headerSubOffice,
                    fontFamily = DevanagariFontFamily,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF475569)
                )
                Text(
                    text = previewDoc.refNumberLine,
                    fontFamily = DevanagariFontFamily,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = previewDoc.documentTitle,
                    fontFamily = DevanagariFontFamily,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                previewDoc.sections.forEach { sec ->
                    if (sec.heading.isNotBlank()) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = sec.heading,
                            fontFamily = DevanagariFontFamily,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0B3B49)
                        )
                    }
                    sec.bodyLines.forEach { line ->
                        Text(
                            text = line,
                            fontFamily = DevanagariFontFamily,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF1E293B)
                        )
                    }
                    if (sec.tableHeaders.isNotEmpty()) {
                        Spacer(Modifier.height(2.dp))
                        Surface(
                            color = Color(0xFF0B3B49),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 6.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                sec.tableHeaders.forEach { h ->
                                    Text(
                                        text = h,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                        sec.tableRows.take(6).forEach { row ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 6.dp, vertical = 3.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                sec.tableHeaders.indices.forEach { idx ->
                                    Text(
                                        text = row.getOrElse(idx) { "" },
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF0F172A),
                                        modifier = Modifier.weight(1f),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            HorizontalDivider(color = Color(0xFFE2E8F0))
                        }
                        if (sec.tableRows.size > 6) {
                            Text(
                                text = "... + ${sec.tableRows.size - 6} और पंक्तियां (निर्यात फाइल में पूर्ण उपलब्ध)",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = previewDoc.leftFooter,
                        fontFamily = DevanagariFontFamily,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF334155)
                    )
                    Text(
                        text = previewDoc.rightFooter,
                        fontFamily = DevanagariFontFamily,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }
            }
        }
    }
}
