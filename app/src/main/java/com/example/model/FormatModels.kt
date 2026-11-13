package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.ExcelGreen
import com.example.ui.theme.ExcelGreenLight
import com.example.ui.theme.PdfRed
import com.example.ui.theme.PdfRedLight
import com.example.ui.theme.WordBlue
import com.example.ui.theme.WordBlueLight

enum class DocumentFileType(
    val label: String,
    val extension: String,
    val mimeType: String,
    val accentColor: Color,
    val containerColor: Color
) {
    PDF(
        label = "PDF",
        extension = "pdf",
        mimeType = "application/pdf",
        accentColor = PdfRed,
        containerColor = PdfRedLight
    ),
    EXCEL(
        label = "EXCEL",
        extension = "xlsx",
        mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        accentColor = ExcelGreen,
        containerColor = ExcelGreenLight
    ),
    WORD(
        label = "WORD",
        extension = "docx",
        mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        accentColor = WordBlue,
        containerColor = WordBlueLight
    )
}

enum class FormatCategory(
    val title: String,
    val hindiTitle: String,
    val subtitle: String,
    val fileCountLabel: String
) {
    TEACHERS_RELATED(
        title = "TEACHERS RELATED FORMATES",
        hindiTitle = "शिक्षक संबंधित प्रपत्र",
        subtitle = "ACP, Fixation, स्थाईकरण, VRS, GA-141, योग्यता अभिवृद्धि",
        fileCountLabel = "15 files"
    ),
    LATEST_UPDATES(
        title = "Latest Updates (2026-27)",
        hindiTitle = "नवीनतम अपडेट्स",
        subtitle = "DA 60→63%, Bonus 2025-26, Budget 2026-27, शिविरा पंचांग",
        fileCountLabel = "10 files"
    ),
    LATEST_FORMATS(
        title = "Latest Formats",
        hindiTitle = "नवीनतम प्रपत्र",
        subtitle = "Pension Kulak, Income Tax Mobile, CWSN, HLB, Household Survey",
        fileCountLabel = "10 files"
    ),
    OFFICE_FORMS(
        title = "OFFICE RELATED FORMS",
        hindiTitle = "कार्यालय संबंधित प्रपत्र",
        subtitle = "ACP, PL, Pay Fixation, Notional Order, TA Bill, 7th Pay Excel",
        fileCountLabel = "13 files"
    ),
    LEAVE_ORDERS(
        title = "LEAVE RELATED ORDER & FORMS",
        hindiTitle = "अवकाश आदेश एवं प्रपत्र",
        subtitle = "CL, PL, CCL, Medical Cert, Quarantine & RSR Leave Rules",
        fileCountLabel = "17 files"
    ),
    SHIKSHA_ADESH(
        title = "शिक्षा विभाग के महत्वपूर्ण आदेश",
        hindiTitle = "महत्वपूर्ण विभागीय आदेश",
        subtitle = "30 जून रिटायर्ड, पदोन्नति वेतन, संतान नियम, अनुकंपा, शाला दर्पण",
        fileCountLabel = "25 files"
    ),
    SDMC_SMC(
        title = "विद्यालय विकास एवं प्रबंधन समिति (SDMC)",
        hindiTitle = "SDMC / SMC प्रपत्र",
        subtitle = "संविधान प्रारूप, शपथ पत्र, 80G, धारा 4(क) व रजिस्ट्रेशन निर्देश",
        fileCountLabel = "12 files"
    ),
    HITKARI_NIDHI(
        title = "हितकारी निधि योजना",
        hindiTitle = "हितकारी निधि कल्याण योजना",
        subtitle = "बालिका उपहार (विवाह), छात्रवृत्ति, निधन सहायता व विदेश यात्रा",
        fileCountLabel = "10 files"
    ),
    GPA(
        title = "सामूहिक दुर्घटना बीमा (GPA)",
        hindiTitle = "GPA बीमा प्रपत्र",
        subtitle = "Nomination, Deduction Schedule, Covering Letter, Death Claim",
        fileCountLabel = "7 files"
    ),
    SI_FORMS(
        title = "राज्य बीमा सम्बन्धी प्रपत्र (SI)",
        hindiTitle = "SI / SIPF प्रपत्र",
        subtitle = "प्रथम घोषणा, GA-79, नॉमिनी, मृत्यु/परिपक्वता दावा व DDO निर्देश",
        fileCountLabel = "6 files"
    ),
    GPF(
        title = "सामान्य प्रावधायी निधि (GPF)",
        hindiTitle = "GPF प्रपत्र व नियम",
        subtitle = "GPF नियम 2021, क्षतिपूर्ति बांड फॉर्म व आहरण निर्देश",
        fileCountLabel = "3 files"
    ),
    MDM_RELATED(
        title = "MDM RELATED FORMATES",
        hindiTitle = "मिड-डे मील (पोषाहार)",
        subtitle = "पोषाहार व कुक मांग प्रपत्र, मासिक व All UC उपयोगिता",
        fileCountLabel = "3 files"
    ),
    ALL_UC_FORMAT(
        title = "उपयोगिता प्रमाण पत्र (All UC Format)",
        hindiTitle = "समस्त उपयोगिता प्रमाण-पत्र",
        subtitle = "RMSA निर्माण कार्य व विद्यालय अनुदान उपयोगिता प्रमाण पत्र",
        fileCountLabel = "1 file"
    ),
    SCHOOL_RELATED(
        title = "SCHOOL RELATED FORMATES",
        hindiTitle = "विद्यालय संबंधित प्रपत्र",
        subtitle = "GA-13 चार्ज रिपोर्ट, संस्थापन प्रपत्र, CWSN, DDO DSC व Shala Darpan",
        fileCountLabel = "7 files"
    ),
    NPS_OPS(
        title = "नवीन पेंशन योजना (NPS / OPS)",
        hindiTitle = "NPS एवं OPS परिपत्र",
        subtitle = "OPS बहाली परिपत्र, कटौती सत्यापन, लेजर व मृत्यु दावा",
        fileCountLabel = "7 files"
    ),
    RSR_RULES(
        title = "राजस्थान सेवा नियम (RSR RULES)",
        hindiTitle = "RSR सेवा नियम",
        subtitle = "RSR Part I & II, Educational Service Rules 1970, 1971, 2010, 2021",
        fileCountLabel = "7 files"
    ),
    FINANCE_ORDERS(
        title = "Finance Department Orders",
        hindiTitle = "वित्त विभाग व IFMS 3.0 आदेश",
        subtitle = "IFMS 3.0 TA बिल, सेवा विवरण सत्यापन, RMS व R-ITMS आदेश",
        fileCountLabel = "4 files"
    ),
    COMPUTER_SOFTWARES(
        title = "Computer Softwares",
        hindiTitle = "कंप्यूटर सॉफ्टवेयर व फॉन्ट्स",
        subtitle = "Hindi Unicode, Kruti Dev 010, Calibri, Bootable USB & Converter",
        fileCountLabel = "6 tools"
    ),
    DPC_TRANSFER(
        title = "DPC & Transfer Orders",
        hindiTitle = "DPC पदस्थापन व स्थानांतरण",
        subtitle = "DPC कार्यग्रहण, स्थानांतरण कार्यमुक्ति व TA/Joining Time प्रपत्र",
        fileCountLabel = "4 files"
    ),
    ONLINE_SERVICES(
        title = "Online Services & Generators",
        hindiTitle = "ऑनलाइन सेवाएं व जनरेटर",
        subtitle = "7th/6th/5th Pay Arrear, Pension, Tax, Office Order Generator",
        fileCountLabel = "10 tools"
    ),
    EXCEL_UTILITIES(
        title = "Excel & Calculators",
        hindiTitle = "एक्सेल शीट व कैलकुलेटर",
        subtitle = "DA Arrear, Tax, Bonus, PL Surrender, MDM, Increment",
        fileCountLabel = "5 files"
    ),
    OFFICE_SCHOOL(
        title = "Office & School",
        hindiTitle = "कार्यालय व विद्यालय प्रपत्र",
        subtitle = "GA-13, Leave, LPC, Study/Character, UC & GPF/SI",
        fileCountLabel = "6 files"
    )
}

enum class BuilderTemplateType {
    ARREAR_SHEET,
    FIXATION_ORDER,
    ACP_FITMENT_ORDER,
    ACP_FORM_3RD_GRADE,
    ACP_FORM_2ND_GRADE,
    STHAIKARAN_3RD_GRADE,
    STHAIKARAN_RPSC,
    STHAIKARAN_LECTURER,
    VRS_APPLICATION,
    RETIREMENT_OATH_6,
    GA_141_CONTINUOUS_SERVICE,
    SATISFACTORY_SERVICE,
    QUALIFICATION_UPGRADE_AB,
    QUALIFICATION_SENIORITY_EDIT,
    ACP_CHILDREN_DECLARATION,
    AUTO_SALARY_DA_ARREAR,
    INCOME_TAX_CALC,
    PL_SURRENDER,
    MDM_MONTHLY,
    ANNUAL_INCREMENT,
    JOINING_RELIEVING_GA13,
    LEAVE_APPLICATION,
    LPC_CERTIFICATE,
    STUDENT_STUDY_CHAR,
    SCHOOL_GRANT_UC,
    GPF_SI_DECLARATION,
    // Expanded Screenshot Templates
    DA_ARREAR_60_63,
    ADHOC_BONUS_2025_26,
    BONUS_OFFICE_ORDER,
    BUDGET_PROPOSAL_2026_27,
    PENSION_CALCULATOR_KULAK,
    PAY_FIXATION_PROMOTION,
    NOTIONAL_ORDER_FITMENT,
    OFFICE_ORDER_EXCEL_7PAY,
    TA_BILL_EXCEL,
    GA_13_PDF,
    GA_13_EXCEL,
    SCHOOL_ESTABLISHMENT_EXCEL,
    MDM_DEMAND_FORM,
    MDM_ALL_UC,
    GPA_NOMINATION,
    GPA_DEDUCTION_EXCEL,
    GPA_COVERING_WORD,
    GPA_CLAIM,
    SI_FIRST_DECL,
    SI_GA79,
    SI_NOMINATION,
    SI_CLAIM,
    GPF_INDEMNITY_BOND,
    SDMC_AFFIDAVIT_WORD,
    SDMC_CONSTITUTION_WORD,
    SDMC_SECTION_4A,
    LEAVE_SANCTION_WORD,
    LEAVE_SANCTION_PDF,
    MEDICAL_CERT_GAZETTED,
    MEDICAL_CERT_NON_GAZETTED,
    CCL_FORM,
    PL_APPLICATION,
    JOINING_TIME_PL,
    LEAVES_SHORT_CHART,
    HITKARI_BALIKA_UPHAAR,
    HITKARI_SCHOLARSHIP,
    FIRST_APPOINTMENT_OATH_WORD,
    ADMISSION_FORM_BLANK,
    SPORTS_FEE_FORM,
    HLB_RECORD_EXCEL,
    HOUSEHOLD_SURVEY_EXCEL,
    SIXTH_PAY_ARREAR,
    FIFTH_PAY_ARREAR,
    CIRCULAR_OR_RULE_REFERENCE,
    SOFTWARE_TOOL
}

data class FormatItem(
    val id: String,
    val serialNumber: Int,
    val title: String,
    val subtitle: String,
    val category: FormatCategory,
    val fileType: DocumentFileType,
    val authorOrTag: String,
    val templateType: BuilderTemplateType,
    val dateLabel: String = "—",
    val isFromScreenshot: Boolean = false
)

data class TeacherProfile(
    val employeeName: String = "राजेश कुमार शर्मा",
    val fatherOrHusbandName: String = "श्री रामेश्वर लाल शर्मा",
    val employeeId: String = "RJJP201618024512",
    val designation: String = "अध्यापक (तृतीय श्रेणी) लेवल-1",
    val subject: String = "सामान्य / गणित",
    val schoolName: String = "रा.उ.मा.वि. आदर्श नगर",
    val udiseCode: String = "08120401502",
    val block: String = "सांगानेर",
    val district: String = "जयपुर",
    val payLevel: String = "L-10",
    val basicPay: String = "39300",
    val joiningDate: String = "01-08-2016",
    val dob: String = "15-07-1990",
    val mobile: String = "9829012345",
    val panNumber: String = "ABCPS1234K",
    val ddoName: String = "प्रधानाचार्य एवं पदेन पीईईओ"
)

data class FormatFormData(
    // Common Employee & School Fields
    val employeeName: String = "राजेश कुमार शर्मा",
    val fatherOrHusbandName: String = "श्री रामेश्वर लाल शर्मा",
    val employeeId: String = "RJJP201618024512",
    val designation: String = "व्याख्याता (स्कूल शिक्षा)",
    val subject: String = "हिन्दी / राजनीति विज्ञान",
    val schoolName: String = "रा.उ.मा.वि. आदर्श नगर",
    val udiseCode: String = "08120401502",
    val block: String = "सांगानेर",
    val district: String = "जयपुर",
    val ddoName: String = "प्रधानाचार्य, रा.उ.मा.वि. आदर्श नगर",
    val orderNumber: String = "राउमावि/संस्थापन/2026/142",
    val orderDate: String = "12-11-2026",
    val effectiveDate: String = "01-08-2025",
    val joiningDate: String = "01-08-2016",
    val dob: String = "15-07-1990",
    val mobile: String = "9829012345",

    // Pay / Fixation / ACP / Arrear Fields
    val oldPayLevel: String = "L-10",
    val newPayLevel: String = "L-12",
    val oldBasicPay: String = "39300",
    val newBasicPay: String = "44300",
    val oldDaPercent: String = "60",
    val newDaPercent: String = "63",
    val hraPercent: String = "9",
    val arrearMonths: String = "4",
    val arrearStartMonth: String = "जुलाई 2026",
    val includeNpsDeduction: Boolean = true,
    val acpMilestone: String = "9 वर्षीय एसीपी (प्रथम)",
    val previousOrderRef: String = "निदेशक/माध्य.शिक्षा/बीकानेर/2016/884",

    // Probation / Sthaikaran Fields
    val probationCompletionDate: String = "01-08-2018",
    val rpscMeritNo: String = "MERIT-412 / 2016",
    val lwpDays: String = "0",

    // VRS / Retirement / Pension Fields
    val proposedVrsDate: String = "31-03-2027",
    val totalServiceYears: String = "30",
    val vrsReason: String = "पारिवारिक एवं स्वास्थ्य कारणों से",
    val retirementAge: String = "60",
    val commutationPercent: String = "40",

    // Qualification & Children Declaration Fields
    val acquiredDegree: String = "M.A. (हिन्दी) / B.Ed.",
    val universityName: String = "राजस्थान विश्वविद्यालय, जयपुर",
    val examPassingYear: String = "2024",
    val senioritySerialNo: String = "1458 / 2022-23",
    val childrenBeforeCutoff: String = "1",
    val childrenAfterCutoff: String = "1",
    val childrenDetails: String = "1. आरव शर्मा (जन्म: 10-05-2017), 2. अनन्या शर्मा (जन्म: 18-09-2020)",

    // Income Tax Fields
    val grossAnnualSalary: String = "864000",
    val hraExemption: String = "48000",
    val deduction80C: String = "150000",
    val nps80CCD1B: String = "50000",
    val medical80D: String = "25000",
    val homeLoanInterest: String = "0",
    val taxFinancialYear: String = "FY 2026-27 (AY 2027-28)",

    // Leave / PL Surrender / MDM / School / Student Fields
    val leaveType: String = "उपार्जित अवकाश (PL) / आकस्मिक अवकाश (CL)",
    val leaveFromDate: String = "15-11-2026",
    val leaveToDate: String = "29-11-2026",
    val leaveDays: String = "15",
    val leaveReason: String = "आवश्यक पारिवारिक कार्य हेतु",

    val mdmMonth: String = "अक्टूबर 2026",
    val mdmPrimaryCount: String = "120",
    val mdmUpperPrimaryCount: String = "95",
    val mdmWorkingDays: String = "22",

    val studentName: String = "दीपक मीणा",
    val studentFatherName: String = "श्री मोहन लाल मीणा",
    val studentClass: String = "कक्षा 10",
    val srNumber: String = "1042",
    val academicSession: String = "2026-27",

    val grantName: String = "कम्पोजिट स्कूल ग्रांट (CSG)",
    val grantAmount: String = "50000",
    val spentAmount: String = "50000",
    val gpfSiPolicyNo: String = "SI-784512 / GPF-44521",

    // Bonus 2025-26, TA Bill, Budget 2026-27 Fields
    val bonusMonthsWorked: String = "12",
    val gpfAccountNo: String = "GPF-RAJ-445210",
    val taJourneyFrom: String = "जयपुर",
    val taJourneyTo: String = "बीकानेर",
    val taModeAndKm: String = "रोडवेज बस / रेल (335 किमी)",
    val taFareAmount: String = "1450",
    val taDaDays: String = "3",
    val taDailyRate: String = "450",
    val budgetSalaryDemand: String = "4800000",
    val budgetOfficeExpense: String = "120000",
    val budgetBuildingRepair: String = "250000",
    val isMggsSchool: Boolean = false,

    // GPA / SI Nominee, SDMC & Hitkari Nidhi Fields
    val nomineeName: String = "श्रीमती सुनीता शर्मा",
    val nomineeRelation: String = "पत्नी (Wife)",
    val nomineeAge: String = "33",
    val nomineeSharePercent: String = "100",
    val gpaPremiumSlab: String = "₹2,100 (₹30 लाख बीमा)",
    val sdmcPresidentName: String = "प्रधानाचार्य / अध्यक्ष SDMC",
    val sdmcSecretaryName: String = "वरिष्ठ अध्यापक / सदस्य सचिव",
    val sdmcRegistrationNo: String = "COOP/2016/JAIPUR/4512",
    val hitkariBeneficiaryName: String = "कु. अदिति शर्मा",
    val hitkariEventDate: String = "22-11-2026",
    val officeOrderSubject: String = "वार्षिक वेतन वृद्धि / PL समर्पण / वेतन नियतन आदेश"
)
