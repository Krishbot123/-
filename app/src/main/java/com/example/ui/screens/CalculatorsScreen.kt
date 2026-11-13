package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.CalculationEngine
import com.example.data.FormatsCatalog
import com.example.model.DocumentFileType
import com.example.model.FormatFormData
import com.example.model.FormatItem
import com.example.ui.components.FileTypeBadge
import com.example.ui.theme.ExcelGreen
import com.example.ui.theme.WordBlue

private data class OfficeOrderOption(val label: String, val formatId: String)

@Composable
fun CalculatorsScreen(
    onOpenFormatBuilder: (FormatItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val excelItems = FormatsCatalog.allFormats
        .filter { it.fileType == DocumentFileType.EXCEL }
        .distinctBy { it.id }

    val orderOptions = remember {
        listOf(
            OfficeOrderOption("ACP Fitment (Word)", "sn_03_acp_fitment_order"),
            OfficeOrderOption("Lecturer Fixation (Word)", "sn_02_lecturer_fixation"),
            OfficeOrderOption("Promotion Fixation (Word)", "of_08_promo_sr_to_lect"),
            OfficeOrderOption("Notional Order (Word)", "of_11_notional_order_fitment"),
            OfficeOrderOption("Bonus 2025-26 (Word)", "lu_03_bonus_office_order"),
            OfficeOrderOption("Leave Sanction (Word)", "lv_01_sanction_word"),
            OfficeOrderOption("Annual Increment (Excel)", "ex_20_annual_increment")
        )
    }
    var selectedOrderIdx by remember { mutableIntStateOf(0) }

    // Pension Quick Calculator state
    val pensionBasics = listOf(44300L, 56100L, 67300L, 78400L, 90400L)
    var selectedPensionBasic by remember { mutableStateOf(67300L) }
    var selectedServiceYears by remember { mutableIntStateOf(30) }
    val pensionResult = CalculationEngine.calculatePension(
        FormatFormData(
            newBasicPay = selectedPensionBasic.toString(),
            newDaPercent = "60",
            totalServiceYears = selectedServiceYears.toString(),
            commutationPercent = "40"
        )
    )

    // 7th Pay Matrix & ACP Lookup state
    val levelKeys = FormatsCatalog.payMatrixLevels.keys.toList()
    var selectedLevel by remember { mutableStateOf("L-10") }
    var selectedCellIndex by remember { mutableIntStateOf(4) }
    var targetAcpLevel by remember { mutableStateOf("L-11") }

    val currentCells = FormatsCatalog.payMatrixLevels[selectedLevel].orEmpty()
    val safeIndex = selectedCellIndex.coerceIn(0, (currentCells.size - 1).coerceAtLeast(0))
    val currentBasic = currentCells.getOrElse(safeIndex) { 38000L }
    val (incrementPay, acpFixationPay) = CalculationEngine.calculateAcpFixationPay(
        selectedLevel,
        currentBasic,
        targetAcpLevel
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 760.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item(key = "calc_header") {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Calculate,
                            contentDescription = "Calculators",
                            tint = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.size(34.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "ऑनलाइन सेवाएं एवं शिक्षक कैलकुलेटर",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Office Order Generator, Pension, DA 60→63%, Bonus, Tax & Pay Matrix",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }

            // 1. Office Order Generator Card
            item(key = "office_order_generator") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, WordBlue.copy(alpha = 0.45f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Gavel, null, tint = WordBlue)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Office Order Generator (कार्यालय आदेश जनरेटर)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = WordBlue
                            )
                        }
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            orderOptions.forEachIndexed { idx, opt ->
                                FilterChip(
                                    selected = selectedOrderIdx == idx,
                                    onClick = { selectedOrderIdx = idx },
                                    label = { Text(opt.label) }
                                )
                            }
                        }
                        Button(
                            onClick = {
                                FormatsCatalog.findById(orderOptions[selectedOrderIdx].formatId)
                                    ?.let(onOpenFormatBuilder)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("open_office_order_generator_btn"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = WordBlue)
                        ) {
                            Text("विवरण भरें व कार्यालय आदेश (.docx / .xlsx) बनाएं", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // 2. Pension & Gratuity Quick Calculator (IFMS 3.0)
            item(key = "pension_calculator_card") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.45f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.AccountBalance, null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Pension & DCRG Gratuity Calculator (IFMS 3.0)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text("अंतिम मूल वेतन चुनें:", style = MaterialTheme.typography.labelLarge)
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            pensionBasics.forEach { basic ->
                                FilterChip(
                                    selected = selectedPensionBasic == basic,
                                    onClick = { selectedPensionBasic = basic },
                                    label = { Text("₹$basic") }
                                )
                            }
                        }
                        Text("अर्हक सेवा (वर्ष):", style = MaterialTheme.typography.labelLarge)
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(20, 25, 28, 30, 33).forEach { yr ->
                                FilterChip(
                                    selected = selectedServiceYears == yr,
                                    onClick = { selectedServiceYears = yr },
                                    label = { Text("$yr वर्ष") }
                                )
                            }
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                                Text("• पूर्ण बेसिक पेंशन (50%): ₹${pensionResult.fullBasicPension}/माह")
                                Text("• कम्यूटेशन एकमुश्त (40%): ₹${pensionResult.commutationLumpSum}/- | शेष पेंशन: ₹${pensionResult.residualMonthlyPension}/माह")
                                Text(
                                    text = "• अनुमानित DCRG ग्रेच्युटी: ₹${pensionResult.dcrgGratuity}/-",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Button(
                            onClick = {
                                FormatsCatalog.findById("lf_01_ifms3_pension_kulak")?.let(onOpenFormatBuilder)
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ExcelGreen)
                        ) {
                            Text("IFMS 3.0 Pension Kulak Excel बनाएं", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // 3. 7th Pay Matrix & ACP Fixation Lookup
            item(key = "pay_matrix_tool") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.45f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "7th Pay Matrix & ACP वेतन नियतन कैलकुलेटर",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text("वर्तमान पे-लेवल चुनें:", style = MaterialTheme.typography.labelLarge)
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            levelKeys.forEach { lvl ->
                                FilterChip(
                                    selected = selectedLevel == lvl,
                                    onClick = {
                                        selectedLevel = lvl
                                        val nextIdx = (levelKeys.indexOf(lvl) + 1).coerceAtMost(levelKeys.lastIndex)
                                        targetAcpLevel = levelKeys[nextIdx]
                                    },
                                    label = { Text(lvl) }
                                )
                            }
                        }
                        Text("वर्तमान मूल वेतन (Cell ${safeIndex + 1}) चुनें:", style = MaterialTheme.typography.labelLarge)
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            currentCells.forEachIndexed { idx, pay ->
                                FilterChip(
                                    selected = safeIndex == idx,
                                    onClick = { selectedCellIndex = idx },
                                    label = { Text("₹$pay") }
                                )
                            }
                        }
                        Text("ACP / पदोन्नति लक्ष्य लेवल चुनें:", style = MaterialTheme.typography.labelLarge)
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            levelKeys.forEach { lvl ->
                                FilterChip(
                                    selected = targetAcpLevel == lvl,
                                    onClick = { targetAcpLevel = lvl },
                                    label = { Text(lvl) }
                                )
                            }
                        }
                        HorizontalDivider()
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text("नियतन परिणाम (Rajasthan Pay Matrix):", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                                Text("• वर्तमान वेतन ($selectedLevel): ₹$currentBasic")
                                Text("• एक वेतन वृद्धि के बाद ($selectedLevel): ₹$incrementPay")
                                Text(
                                    text = "• $targetAcpLevel में नियत मूल वेतन: ₹$acpFixationPay/-",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            item(key = "excel_tools_grid") {
                Text(
                    text = "सभी Excel (.xlsx) जनरेटर टूल्स (${excelItems.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            excelItems.forEach { item ->
                item(key = "calc_ex_${item.id}") {
                    val icon = when {
                        item.id.contains("tax") || item.id.contains("bonus") || item.id.contains("budget") -> Icons.Filled.Payments
                        item.id.contains("mdm") -> Icons.Filled.Restaurant
                        item.id.contains("increment") -> Icons.Filled.TrendingUp
                        else -> Icons.Filled.TableChart
                    }
                    CalculatorToolCard(
                        item = item,
                        icon = icon,
                        onOpen = { onOpenFormatBuilder(item) }
                    )
                }
            }

            item(key = "calc_bottom_space") {
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun CalculatorToolCard(
    item: FormatItem,
    icon: ImageVector,
    onOpen: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("calc_card_${item.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.45f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        color = ExcelGreen.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = ExcelGreen,
                            modifier = Modifier.padding(10.dp).size(22.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(item.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(item.subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Spacer(Modifier.width(8.dp))
                FileTypeBadge(fileType = item.fileType)
            }
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = onOpen,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ExcelGreen, contentColor = Color.White)
            ) {
                Text("डिटेल्स भरें व Excel (.xlsx) बनाएं", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
