package com.example.model

data class ArrearMonthRow(
    val serial: Int,
    val monthLabel: String,
    val drawnBasic: Long,
    val drawnDa: Long,
    val drawnHra: Long,
    val drawnGross: Long,
    val dueBasic: Long,
    val dueDa: Long,
    val dueHra: Long,
    val dueGross: Long,
    val grossDifference: Long,
    val npsDeduction: Long,
    val netPayable: Long
)

data class ArrearSummary(
    val rows: List<ArrearMonthRow>,
    val totalDrawn: Long,
    val totalDue: Long,
    val totalGrossDifference: Long,
    val totalNpsDeduction: Long,
    val totalNetPayable: Long
)

data class TaxComparisonResult(
    val financialYear: String,
    val grossIncome: Long,
    val oldTotalDeductions: Long,
    val oldTaxableIncome: Long,
    val oldBaseTax: Long,
    val oldCess: Long,
    val oldTotalTax: Long,
    val newStandardDeduction: Long,
    val newTaxableIncome: Long,
    val newBaseTax: Long,
    val newCess: Long,
    val newTotalTax: Long,
    val recommendedRegime: String,
    val taxSavings: Long
)

data class MdmCalculationResult(
    val month: String,
    val primaryStudentMeals: Long,
    val upperPrimaryStudentMeals: Long,
    val primaryGrainKg: Double,
    val upperPrimaryGrainKg: Double,
    val totalGrainKg: Double,
    val primaryCookingCost: Double,
    val upperPrimaryCookingCost: Double,
    val totalCookingCost: Double
)

data class PensionResult(
    val lastBasicPay: Long,
    val daPercent: Double,
    val daOnBasic: Long,
    val totalEmoluments: Long,
    val qualifyingYears: Int,
    val fullBasicPension: Long,
    val commutationPercent: Int,
    val commutedMonthlyPension: Long,
    val commutationLumpSum: Long,
    val residualMonthlyPension: Long,
    val dcrgGratuity: Long
)

data class BonusResult(
    val payLevel: String,
    val monthsWorked: Int,
    val maxCeiling: Long,
    val grossBonus: Long,
    val cashAmount75: Long,
    val gpfAmount25: Long,
    val isEligible: Boolean
)

data class TaBillResult(
    val fareAmount: Long,
    val daDays: Int,
    val dailyRate: Long,
    val totalDaAmount: Long,
    val totalClaimAmount: Long
)

data class BudgetResult(
    val salaryHead: Long,
    val officeExpenseHead: Long,
    val repairMaintenanceHead: Long,
    val totalDemand: Long
)

data class GeneratedFileInfo(
    val name: String,
    val path: String,
    val fileType: DocumentFileType,
    val sizeBytes: Long,
    val lastModifiedMillis: Long
)

data class PortalResource(
    val title: String,
    val subtitle: String,
    val url: String,
    val badge: String
)

data class StateCircularItem(
    val serialNumber: Int,
    val title: String,
    val date: String,
    val linkedFormatId: String
)

data class OnlineServiceItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val badge: String,
    val linkedFormatId: String
)

data class ContributorItem(
    val name: String,
    val role: String
)

data class SoftwareToolItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val url: String,
    val linkedFormatId: String
)

data class DocumentSection(
    val heading: String = "",
    val bodyLines: List<String> = emptyList(),
    val tableHeaders: List<String> = emptyList(),
    val tableRows: List<List<String>> = emptyList()
)

data class GeneratedDocumentContent(
    val fileBaseName: String,
    val headerDepartment: String,
    val headerSubOffice: String,
    val documentTitle: String,
    val refNumberLine: String,
    val sections: List<DocumentSection>,
    val leftFooter: String,
    val rightFooter: String
)
