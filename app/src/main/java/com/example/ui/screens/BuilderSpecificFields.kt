package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.CalculationEngine
import com.example.model.BuilderTemplateType
import com.example.model.FormatFormData
import java.util.Locale

@Composable
internal fun FormSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            content()
        }
    }
}

@Composable
internal fun FormField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    isNumber: Boolean = false,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = if (isNumber) KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
internal fun TemplateSpecificFields(
    templateType: BuilderTemplateType,
    formData: FormatFormData,
    onUpdateForm: ((FormatFormData) -> FormatFormData) -> Unit
) {
    when (templateType) {
        BuilderTemplateType.ARREAR_SHEET,
        BuilderTemplateType.AUTO_SALARY_DA_ARREAR,
        BuilderTemplateType.DA_ARREAR_60_63,
        BuilderTemplateType.SIXTH_PAY_ARREAR,
        BuilderTemplateType.FIFTH_PAY_ARREAR -> {
            val isDaOnly = templateType == BuilderTemplateType.DA_ARREAR_60_63
            FormSectionCard(if (isDaOnly) "2. DA 60% → 63% एरियर गणना विवरण" else "2. एरियर गणना विवरण (Drawn vs Due)") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("Drawn Level", formData.oldPayLevel, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(oldPayLevel = v) } }
                    FormField("Due Level", formData.newPayLevel, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(newPayLevel = v) } }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("Drawn Basic (₹)", formData.oldBasicPay, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(oldBasicPay = v) } }
                    FormField("Due Basic (₹)", if (isDaOnly) formData.oldBasicPay else formData.newBasicPay, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(newBasicPay = v) } }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("Old DA %", formData.oldDaPercent, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(oldDaPercent = v) } }
                    FormField("New DA %", formData.newDaPercent, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(newDaPercent = v) } }
                    FormField("HRA %", formData.hraPercent, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(hraPercent = v) } }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("प्रारंभ माह", formData.arrearStartMonth, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(arrearStartMonth = v) } }
                    FormField("कुल माह", formData.arrearMonths, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(arrearMonths = v) } }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("NPS/GPF 10% कटौती लागू करें", style = MaterialTheme.typography.bodyMedium)
                    Switch(checked = formData.includeNpsDeduction, onCheckedChange = { c -> onUpdateForm { it.copy(includeNpsDeduction = c) } })
                }
            }
        }

        BuilderTemplateType.ADHOC_BONUS_2025_26,
        BuilderTemplateType.BONUS_OFFICE_ORDER -> {
            FormSectionCard("2. तदर्थ बोनस (2025-26) विवरण") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("पे-लेवल (L-1 से L-12)", formData.oldPayLevel, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(oldPayLevel = v) } }
                    FormField("कार्य अवधि (माह 6-12)", formData.bonusMonthsWorked, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(bonusMonthsWorked = v) } }
                }
                FormField("GPF खाता संख्या (25% जमा हेतु)", formData.gpfAccountNo) { v -> onUpdateForm { it.copy(gpfAccountNo = v) } }
            }
        }

        BuilderTemplateType.PENSION_CALCULATOR_KULAK -> {
            FormSectionCard("2. पेंशन, कम्यूटेशन व ग्रेच्युटी विवरण (IFMS 3.0)") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("अंतिम मूल वेतन (₹)", formData.newBasicPay, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(newBasicPay = v) } }
                    FormField("वर्तमान DA %", formData.newDaPercent, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(newDaPercent = v) } }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("अर्हक सेवा (वर्ष)", formData.totalServiceYears, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(totalServiceYears = v) } }
                    FormField("कम्यूटेशन % (0-40)", formData.commutationPercent, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(commutationPercent = v) } }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("सेवानिवृत्ति आयु", formData.retirementAge, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(retirementAge = v) } }
                    FormField("सेवानिवृत्ति तिथि", formData.proposedVrsDate, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(proposedVrsDate = v) } }
                }
            }
        }

        BuilderTemplateType.TA_BILL_EXCEL -> {
            FormSectionCard("2. यात्रा भत्ता (TA/DA Bill) विवरण") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("प्रस्थान स्थान (From)", formData.taJourneyFrom, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(taJourneyFrom = v) } }
                    FormField("गंतव्य स्थान (To)", formData.taJourneyTo, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(taJourneyTo = v) } }
                }
                FormField("यात्रा साधन व दूरी (Km)", formData.taModeAndKm) { v -> onUpdateForm { it.copy(taModeAndKm = v) } }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("किराया (Fare ₹)", formData.taFareAmount, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(taFareAmount = v) } }
                    FormField("DA दिवस", formData.taDaDays, Modifier.weight(0.7f), isNumber = true) { v -> onUpdateForm { it.copy(taDaDays = v) } }
                    FormField("DA दर (₹)", formData.taDailyRate, Modifier.weight(0.8f), isNumber = true) { v -> onUpdateForm { it.copy(taDailyRate = v) } }
                }
            }
        }

        BuilderTemplateType.BUDGET_PROPOSAL_2026_27 -> {
            FormSectionCard("2. वित्तीय वर्ष 2026-27 बजट प्रस्ताव विवरण") {
                FormField("01 - वेतन व भत्ते मांग (₹)", formData.budgetSalaryDemand, isNumber = true) { v -> onUpdateForm { it.copy(budgetSalaryDemand = v) } }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("05 - कार्यालय व्यय (₹)", formData.budgetOfficeExpense, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(budgetOfficeExpense = v) } }
                    FormField("18 - भवन मरम्मत (₹)", formData.budgetBuildingRepair, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(budgetBuildingRepair = v) } }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("MGGS (महात्मा गांधी अंग्रेजी विद्यालय)", style = MaterialTheme.typography.bodyMedium)
                    Switch(checked = formData.isMggsSchool, onCheckedChange = { c -> onUpdateForm { it.copy(isMggsSchool = c) } })
                }
            }
        }

        BuilderTemplateType.FIXATION_ORDER,
        BuilderTemplateType.ACP_FITMENT_ORDER,
        BuilderTemplateType.ACP_FORM_3RD_GRADE,
        BuilderTemplateType.ACP_FORM_2ND_GRADE,
        BuilderTemplateType.PAY_FIXATION_PROMOTION,
        BuilderTemplateType.NOTIONAL_ORDER_FITMENT,
        BuilderTemplateType.OFFICE_ORDER_EXCEL_7PAY -> {
            FormSectionCard("2. वेतन नियतन व ACP / पदोन्नति विवरण") {
                FormField("ACP / पदोन्नति स्तर", formData.acpMilestone) { v -> onUpdateForm { it.copy(acpMilestone = v) } }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("प्रथम नियुक्ति तिथि", formData.joiningDate, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(joiningDate = v) } }
                    FormField("प्रभावी दिनांक", formData.effectiveDate, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(effectiveDate = v) } }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("विद्यमान पे-लेवल", formData.oldPayLevel, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(oldPayLevel = v) } }
                    FormField("विद्यमान बेसिक (₹)", formData.oldBasicPay, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(oldBasicPay = v) } }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("नवीन पे-लेवल", formData.newPayLevel, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(newPayLevel = v) } }
                    FormField("नवीन बेसिक (₹)", formData.newBasicPay, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(newBasicPay = v) } }
                }
                FormField("संदर्भित विभागीय आदेश क्रमांक", formData.previousOrderRef) { v -> onUpdateForm { it.copy(previousOrderRef = v) } }
            }
        }

        BuilderTemplateType.GPA_NOMINATION,
        BuilderTemplateType.GPA_DEDUCTION_EXCEL,
        BuilderTemplateType.GPA_COVERING_WORD,
        BuilderTemplateType.GPA_CLAIM,
        BuilderTemplateType.SI_FIRST_DECL,
        BuilderTemplateType.SI_GA79,
        BuilderTemplateType.SI_NOMINATION,
        BuilderTemplateType.SI_CLAIM,
        BuilderTemplateType.GPF_INDEMNITY_BOND -> {
            FormSectionCard("2. GPA / SI / GPF एवं नॉमिनी विवरण") {
                FormField("SI / GPF / Policy नंबर", formData.gpfSiPolicyNo) { v -> onUpdateForm { it.copy(gpfSiPolicyNo = v) } }
                FormField("GPA प्रीमियम स्लैब", formData.gpaPremiumSlab) { v -> onUpdateForm { it.copy(gpaPremiumSlab = v) } }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("नॉमिनी का नाम", formData.nomineeName, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(nomineeName = v) } }
                    FormField("संबंध", formData.nomineeRelation, Modifier.weight(0.8f)) { v -> onUpdateForm { it.copy(nomineeRelation = v) } }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("नॉमिनी आयु", formData.nomineeAge, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(nomineeAge = v) } }
                    FormField("अंश (%)", formData.nomineeSharePercent, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(nomineeSharePercent = v) } }
                }
            }
        }

        BuilderTemplateType.SDMC_AFFIDAVIT_WORD,
        BuilderTemplateType.SDMC_CONSTITUTION_WORD,
        BuilderTemplateType.SDMC_SECTION_4A -> {
            FormSectionCard("2. SDMC / SMC कार्यकारिणी व पंजीयन विवरण") {
                FormField("अध्यक्ष (संस्था प्रधान)", formData.sdmcPresidentName) { v -> onUpdateForm { it.copy(sdmcPresidentName = v) } }
                FormField("सदस्य सचिव", formData.sdmcSecretaryName) { v -> onUpdateForm { it.copy(sdmcSecretaryName = v) } }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("रजिस्ट्रेशन नंबर", formData.sdmcRegistrationNo, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(sdmcRegistrationNo = v) } }
                    FormField("सत्र", formData.academicSession, Modifier.weight(0.7f)) { v -> onUpdateForm { it.copy(academicSession = v) } }
                }
            }
        }

        BuilderTemplateType.HITKARI_BALIKA_UPHAAR,
        BuilderTemplateType.HITKARI_SCHOLARSHIP -> {
            FormSectionCard("2. हितकारी निधि लाभार्थी विवरण") {
                FormField("पुत्री / विद्यार्थी / लाभार्थी नाम", formData.hitkariBeneficiaryName) { v -> onUpdateForm { it.copy(hitkariBeneficiaryName = v) } }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("विवाह / परीक्षा तिथि", formData.hitkariEventDate, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(hitkariEventDate = v) } }
                    FormField("कक्षा / कोर्स", formData.acquiredDegree, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(acquiredDegree = v) } }
                }
            }
        }

        BuilderTemplateType.STHAIKARAN_3RD_GRADE,
        BuilderTemplateType.STHAIKARAN_RPSC,
        BuilderTemplateType.STHAIKARAN_LECTURER -> {
            FormSectionCard("2. परिवीक्षा एवं स्थाईकरण विवरण") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("कार्यग्रहण तिथि", formData.joiningDate, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(joiningDate = v) } }
                    FormField("2 वर्ष पूर्ण तिथि", formData.probationCompletionDate, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(probationCompletionDate = v) } }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("RPSC मेरिट क्रमांक", formData.rpscMeritNo, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(rpscMeritNo = v) } }
                    FormField("LWP दिवस", formData.lwpDays, Modifier.weight(0.7f), isNumber = true) { v -> onUpdateForm { it.copy(lwpDays = v) } }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("स्थाईकरण पे-लेवल", formData.newPayLevel, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(newPayLevel = v) } }
                    FormField("नियमित बेसिक (₹)", formData.newBasicPay, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(newBasicPay = v) } }
                }
            }
        }

        BuilderTemplateType.INCOME_TAX_CALC -> {
            FormSectionCard("2. आयकर गणना विवरण (Old vs New)") {
                FormField("वित्तीय वर्ष", formData.taxFinancialYear) { v -> onUpdateForm { it.copy(taxFinancialYear = v) } }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("सकल वार्षिक वेतन (₹)", formData.grossAnnualSalary, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(grossAnnualSalary = v) } }
                    FormField("HRA छूट (₹)", formData.hraExemption, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(hraExemption = v) } }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("80C (GPF/SI/LIC)", formData.deduction80C, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(deduction80C = v) } }
                    FormField("80CCD(1B) NPS", formData.nps80CCD1B, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(nps80CCD1B = v) } }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("80D RGHS/Medical", formData.medical80D, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(medical80D = v) } }
                    FormField("Home Loan Int (24b)", formData.homeLoanInterest, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(homeLoanInterest = v) } }
                }
            }
        }

        BuilderTemplateType.MDM_MONTHLY,
        BuilderTemplateType.MDM_DEMAND_FORM,
        BuilderTemplateType.MDM_ALL_UC -> {
            FormSectionCard("2. मिड-डे मील (MDM) मासिक विवरण") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("माह (Month)", formData.mdmMonth, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(mdmMonth = v) } }
                    FormField("कार्यदिवस (Days)", formData.mdmWorkingDays, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(mdmWorkingDays = v) } }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("कक्षा 1-5 औसत छात्र", formData.mdmPrimaryCount, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(mdmPrimaryCount = v) } }
                    FormField("कक्षा 6-8 औसत छात्र", formData.mdmUpperPrimaryCount, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(mdmUpperPrimaryCount = v) } }
                }
            }
        }

        BuilderTemplateType.LEAVE_APPLICATION,
        BuilderTemplateType.LEAVE_SANCTION_WORD,
        BuilderTemplateType.LEAVE_SANCTION_PDF,
        BuilderTemplateType.MEDICAL_CERT_GAZETTED,
        BuilderTemplateType.MEDICAL_CERT_NON_GAZETTED,
        BuilderTemplateType.CCL_FORM,
        BuilderTemplateType.PL_APPLICATION,
        BuilderTemplateType.JOINING_TIME_PL -> {
            FormSectionCard("2. अवकाश एवं स्वीकृति विवरण") {
                FormField("अवकाश का प्रकार", formData.leaveType) { v -> onUpdateForm { it.copy(leaveType = v) } }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("प्रारंभ दिनांक", formData.leaveFromDate, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(leaveFromDate = v) } }
                    FormField("समाप्ति दिनांक", formData.leaveToDate, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(leaveToDate = v) } }
                    FormField("दिवस", formData.leaveDays, Modifier.weight(0.6f), isNumber = true) { v -> onUpdateForm { it.copy(leaveDays = v) } }
                }
                FormField("अवकाश का कारण", formData.leaveReason) { v -> onUpdateForm { it.copy(leaveReason = v) } }
            }
        }

        BuilderTemplateType.STUDENT_STUDY_CHAR,
        BuilderTemplateType.ADMISSION_FORM_BLANK,
        BuilderTemplateType.SPORTS_FEE_FORM,
        BuilderTemplateType.HLB_RECORD_EXCEL,
        BuilderTemplateType.HOUSEHOLD_SURVEY_EXCEL -> {
            FormSectionCard("2. विद्यार्थी / प्रवेश विवरण") {
                FormField("विद्यार्थी का नाम", formData.studentName) { v -> onUpdateForm { it.copy(studentName = v) } }
                FormField("पिता का नाम", formData.studentFatherName) { v -> onUpdateForm { it.copy(studentFatherName = v) } }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("कक्षा (Class)", formData.studentClass, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(studentClass = v) } }
                    FormField("SR क्रमांक", formData.srNumber, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(srNumber = v) } }
                    FormField("सत्र", formData.academicSession, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(academicSession = v) } }
                }
            }
        }

        BuilderTemplateType.QUALIFICATION_UPGRADE_AB,
        BuilderTemplateType.QUALIFICATION_SENIORITY_EDIT -> {
            FormSectionCard("2. उच्च शैक्षणिक योग्यता विवरण") {
                FormField("अर्जित योग्यता (Degree / Exam)", formData.acquiredDegree) { v -> onUpdateForm { it.copy(acquiredDegree = v) } }
                FormField("विश्वविद्यालय / बोर्ड का नाम", formData.universityName) { v -> onUpdateForm { it.copy(universityName = v) } }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("उत्तीर्ण वर्ष", formData.examPassingYear, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(examPassingYear = v) } }
                    FormField("वरिष्ठता क्रमांक", formData.senioritySerialNo, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(senioritySerialNo = v) } }
                }
            }
        }

        BuilderTemplateType.ACP_CHILDREN_DECLARATION -> {
            FormSectionCard("2. संतान घोषणा विवरण (01-06-2002 कटऑफ)") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("01-06-2002 से पूर्व संतान", formData.childrenBeforeCutoff, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(childrenBeforeCutoff = v) } }
                    FormField("01-06-2002 के पश्चात संतान", formData.childrenAfterCutoff, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(childrenAfterCutoff = v) } }
                }
                FormField("संतानों के नाम व जन्मतिथि विवरण", formData.childrenDetails) { v -> onUpdateForm { it.copy(childrenDetails = v) } }
            }
        }

        BuilderTemplateType.SCHOOL_GRANT_UC -> {
            FormSectionCard("2. अनुदान उपयोगिता (UC) विवरण") {
                FormField("योजना / अनुदान का नाम", formData.grantName) { v -> onUpdateForm { it.copy(grantName = v) } }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("प्राप्त राशि (₹)", formData.grantAmount, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(grantAmount = v) } }
                    FormField("व्यय राशि (₹)", formData.spentAmount, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(spentAmount = v) } }
                }
            }
        }

        else -> {
            FormSectionCard("2. वेतन एवं अतिरिक्त विवरण") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("पे-लेवल", formData.oldPayLevel, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(oldPayLevel = v) } }
                    FormField("मूल वेतन (₹)", formData.oldBasicPay, Modifier.weight(1f), isNumber = true) { v -> onUpdateForm { it.copy(oldBasicPay = v) } }
                    FormField("DA %", formData.newDaPercent, Modifier.weight(0.8f), isNumber = true) { v -> onUpdateForm { it.copy(newDaPercent = v) } }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField("प्रभावी दिनांक", formData.effectiveDate, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(effectiveDate = v) } }
                    FormField("SI / GPF नंबर", formData.gpfSiPolicyNo, Modifier.weight(1f)) { v -> onUpdateForm { it.copy(gpfSiPolicyNo = v) } }
                }
            }
        }
    }
}

@Composable
internal fun LiveCalculationHighlightCard(
    templateType: BuilderTemplateType,
    formData: FormatFormData
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "लाइव ऑटो कैलकुलेशन (Live Result)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            when (templateType) {
                BuilderTemplateType.ARREAR_SHEET,
                BuilderTemplateType.AUTO_SALARY_DA_ARREAR,
                BuilderTemplateType.DA_ARREAR_60_63,
                BuilderTemplateType.SIXTH_PAY_ARREAR,
                BuilderTemplateType.FIFTH_PAY_ARREAR -> {
                    val calcData = if (templateType == BuilderTemplateType.DA_ARREAR_60_63) {
                        formData.copy(newBasicPay = formData.oldBasicPay)
                    } else formData
                    val s = CalculationEngine.calculateArrear(calcData)
                    Text("कुल आहरित: ₹${s.totalDrawn} | कुल देय: ₹${s.totalDue}", style = MaterialTheme.typography.bodyMedium)
                    Text("अंतर: ₹${s.totalGrossDifference} | NPS/GPF: ₹${s.totalNpsDeduction}", style = MaterialTheme.typography.bodyMedium)
                    Text("शुद्ध भुगतान योग्य एरियर: ₹${s.totalNetPayable}/-", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                BuilderTemplateType.INCOME_TAX_CALC -> {
                    val t = CalculationEngine.calculateIncomeTax(formData)
                    Text("Old Regime Tax: ₹${t.oldTotalTax} | New Regime Tax: ₹${t.newTotalTax}", style = MaterialTheme.typography.bodyMedium)
                    Text("सर्वोत्तम: ${t.recommendedRegime} (बचत ₹${t.taxSavings})", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                BuilderTemplateType.MDM_MONTHLY,
                BuilderTemplateType.MDM_DEMAND_FORM,
                BuilderTemplateType.MDM_ALL_UC -> {
                    val m = CalculationEngine.calculateMdm(formData)
                    Text("कुल खाद्यान्न: ${String.format(Locale.US, "%.2f", m.totalGrainKg)} किग्रा", style = MaterialTheme.typography.bodyMedium)
                    Text("कुल कुकिंग कन्वर्जन राशि: ₹${String.format(Locale.US, "%.2f", m.totalCookingCost)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                BuilderTemplateType.PENSION_CALCULATOR_KULAK -> {
                    val p = CalculationEngine.calculatePension(formData)
                    Text("पूर्ण पेंशन (50%): ₹${p.fullBasicPension}/माह | कम्यूटेशन एकमुश्त: ₹${p.commutationLumpSum}", style = MaterialTheme.typography.bodyMedium)
                    Text("शेष पेंशन: ₹${p.residualMonthlyPension}/माह | DCRG ग्रेच्युटी: ₹${p.dcrgGratuity}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                BuilderTemplateType.ADHOC_BONUS_2025_26,
                BuilderTemplateType.BONUS_OFFICE_ORDER -> {
                    val b = CalculationEngine.calculateBonus2025_26(formData)
                    Text("75% Cash: ₹${b.cashAmount75} | 25% GPF: ₹${b.gpfAmount25}", style = MaterialTheme.typography.bodyMedium)
                    Text("कुल बोनस राशि: ₹${b.grossBonus}/-", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                BuilderTemplateType.TA_BILL_EXCEL -> {
                    val ta = CalculationEngine.calculateTaBill(formData)
                    Text("किराया: ₹${ta.fareAmount} + दैनिक भत्ता: ₹${ta.totalDaAmount}", style = MaterialTheme.typography.bodyMedium)
                    Text("कुल देय TA राशि: ₹${ta.totalClaimAmount}/-", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                BuilderTemplateType.BUDGET_PROPOSAL_2026_27 -> {
                    val b = CalculationEngine.calculateBudgetProposal(formData)
                    Text("कुल वार्षिक बजट मांग (FY 2026-27): ₹${b.totalDemand}/-", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                else -> Unit
            }
        }
    }
}
