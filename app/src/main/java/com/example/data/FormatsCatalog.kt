package com.example.data

import com.example.model.BuilderTemplateType
import com.example.model.DocumentFileType
import com.example.model.FormatCategory
import com.example.model.FormatItem
import com.example.model.PortalResource

object FormatsCatalog {

    // 1 - 15: EXACT ITEMS FROM "TEACHERS RELATED FORMATES" SCREENSHOT
    val teachersRelatedFormats: List<FormatItem> = listOf(
        FormatItem(
            id = "sn_01_lecturer_arrear",
            serialNumber = 1,
            title = "Lecturer Arrear Sheet",
            subtitle = "व्याख्याता एरियर गणना शीट (Drawn vs Due Auto Calculation)",
            category = FormatCategory.TEACHERS_RELATED,
            fileType = DocumentFileType.EXCEL,
            authorOrTag = "7th Pay • Excel Auto Sheet",
            templateType = BuilderTemplateType.ARREAR_SHEET,
            isFromScreenshot = true
        ),
        FormatItem(
            id = "sn_02_lecturer_fixation",
            serialNumber = 2,
            title = "Lecturer Fixation Order",
            subtitle = "व्याख्याता वेतन नियतन कार्यालय आदेश प्रारूप",
            category = FormatCategory.TEACHERS_RELATED,
            fileType = DocumentFileType.WORD,
            authorOrTag = "Office Order • Editable Word",
            templateType = BuilderTemplateType.FIXATION_ORDER,
            isFromScreenshot = true
        ),
        FormatItem(
            id = "sn_03_acp_fitment_order",
            serialNumber = 3,
            title = "कार्यालय आदेश - एसीपी पर वेतन नियतन (ACP FITMENT ORDER) By: Parmanand meghwal",
            subtitle = "9/18/27 एवं 10/20/30 वर्षीय एसीपी पर पे-मैट्रिक्स वेतन नियतन आदेश",
            category = FormatCategory.TEACHERS_RELATED,
            fileType = DocumentFileType.WORD,
            authorOrTag = "By: Parmanand Meghwal",
            templateType = BuilderTemplateType.ACP_FITMENT_ORDER,
            isFromScreenshot = true
        ),
        FormatItem(
            id = "sn_04_acp_format_3rd",
            serialNumber = 4,
            title = "ACP FORMAT - चयनित वेतनमान आवेदन फॉर्म व कार्यालयध्यक्ष का प्रमाण पत्र (तृतीय श्रेणी कर्मचारी)",
            subtitle = "9/18/27 वर्षीय चयनित वेतनमान आवेदन पत्र एवं संतोषप्रद सेवा प्रमाण-पत्र",
            category = FormatCategory.TEACHERS_RELATED,
            fileType = DocumentFileType.PDF,
            authorOrTag = "3rd Grade • PDF Form",
            templateType = BuilderTemplateType.ACP_FORM_3RD_GRADE,
            isFromScreenshot = true
        ),
        FormatItem(
            id = "sn_05_acp_format_2nd",
            serialNumber = 5,
            title = "ACP FORMAT - चयनित वेतनमान आवेदन फॉर्म व कार्यालयध्यक्ष का प्रमाण पत्र (द्वितीय श्रेणी कर्मचारी)",
            subtitle = "वरिष्ठ अध्यापक / द्वितीय श्रेणी कार्मिक एसीपी आवेदन व प्रमाण-पत्र",
            category = FormatCategory.TEACHERS_RELATED,
            fileType = DocumentFileType.PDF,
            authorOrTag = "2nd Grade • PDF Form",
            templateType = BuilderTemplateType.ACP_FORM_2ND_GRADE,
            isFromScreenshot = true
        ),
        FormatItem(
            id = "sn_06_sthaikaran_3rd",
            serialNumber = 6,
            title = "स्थाईकरण फॉर्म PDF - तृतीय श्रेणी अध्यापक, शारीरिक शिक्षक, प्रयोगशाला सहायक, पुस्तकालयाध्यक्ष, प्रयोगशाला सेवक, कनिष्ठ लिपिक, चतुर्थ श्रेणी कर्मचारी, जमादार",
            subtitle = "परिवीक्षाकाल पूर्ण होने पर स्थाईकरण आवेदन एवं सत्यापन प्रपत्र",
            category = FormatCategory.TEACHERS_RELATED,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Probation Confirmation • PDF",
            templateType = BuilderTemplateType.STHAIKARAN_3RD_GRADE,
            isFromScreenshot = true
        ),
        FormatItem(
            id = "sn_07_sthaikaran_rpsc",
            serialNumber = 7,
            title = "स्थाईकरण फॉर्म - RPSC से चयनित व्याख्याता/प्रधानाध्यापक/वरिष्ठ अध्यापक/शारीरिक शिक्षक",
            subtitle = "RPSC चयनित राजपत्रित एवं अधीनस्थ कार्मिक स्थाईकरण प्रस्ताव प्रपत्र",
            category = FormatCategory.TEACHERS_RELATED,
            fileType = DocumentFileType.PDF,
            authorOrTag = "RPSC Selected • PDF",
            templateType = BuilderTemplateType.STHAIKARAN_RPSC,
            isFromScreenshot = true
        ),
        FormatItem(
            id = "sn_08_sthaikaran_lecturer",
            serialNumber = 8,
            title = "स्थाईकरण फॉर्म - व्याख्याता (PDF)",
            subtitle = "व्याख्याता (स्कूल शिक्षा) परिवीक्षा समाप्ति व स्थाईकरण प्रपत्र",
            category = FormatCategory.TEACHERS_RELATED,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Lecturer • PDF",
            templateType = BuilderTemplateType.STHAIKARAN_LECTURER,
            isFromScreenshot = true
        ),
        FormatItem(
            id = "sn_09_vrs_application",
            serialNumber = 9,
            title = "स्वैच्छिक सेवानिवृति लेने हेतु प्रार्थना पत्र",
            subtitle = "राजस्थान सिविल सेवा (पेंशन) नियम के अंतर्गत VRS आवेदन पत्र",
            category = FormatCategory.TEACHERS_RELATED,
            fileType = DocumentFileType.WORD,
            authorOrTag = "VRS Application • Word",
            templateType = BuilderTemplateType.VRS_APPLICATION,
            isFromScreenshot = true
        ),
        FormatItem(
            id = "sn_10_retirement_oath_6",
            serialNumber = 10,
            title = "सेवानिवृति हेतु शपथ पत्र प्रारूप - 6",
            subtitle = "सेवानिवृत्ति / पेंशन प्रकरण हेतु निर्धारित शपथ पत्र प्रारूप संख्या-6",
            category = FormatCategory.TEACHERS_RELATED,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Pension Prarup-6 • PDF",
            templateType = BuilderTemplateType.RETIREMENT_OATH_6,
            isFromScreenshot = true
        ),
        FormatItem(
            id = "sn_11_ga141_service",
            serialNumber = 11,
            title = "निरंतर एवं योग्य सेवा का प्रमाण-पत्र-GA-141",
            subtitle = "कार्यालयध्यक्ष द्वारा प्रदत्त निरंतर एवं योग्य सेवा प्रमाण-पत्र (जी.ए.-141)",
            category = FormatCategory.TEACHERS_RELATED,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Form GA-141 • PDF",
            templateType = BuilderTemplateType.GA_141_CONTINUOUS_SERVICE,
            isFromScreenshot = true
        ),
        FormatItem(
            id = "sn_12_satisfactory_service",
            serialNumber = 12,
            title = "संतोष प्रद सेवा का प्रमाण पत्र",
            subtitle = "स्थाईकरण / एसीपी / पदोन्नति हेतु संतोषप्रद सेवा प्रमाण-पत्र",
            category = FormatCategory.TEACHERS_RELATED,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Service Certificate • PDF",
            templateType = BuilderTemplateType.SATISFACTORY_SERVICE,
            isFromScreenshot = true
        ),
        FormatItem(
            id = "sn_13_qualification_ab",
            serialNumber = 13,
            title = "योग्यता अभिवृद्धि हेतु प्रपत्र क और ख",
            subtitle = "सेवा पुस्तिका एवं शाला दर्पण में उच्च योग्यता अंकन हेतु प्रपत्र क व ख",
            category = FormatCategory.TEACHERS_RELATED,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Prapatra Ka & Kha • PDF",
            templateType = BuilderTemplateType.QUALIFICATION_UPGRADE_AB,
            isFromScreenshot = true
        ),
        FormatItem(
            id = "sn_14_qualification_seniority",
            serialNumber = 14,
            title = "योग्यता अभिवृद्धि वरिष्ठता सूची नामांकन संशोधन हेतु आवेदन फॉर्म",
            subtitle = "DPC / वरिष्ठता सूची में शैक्षणिक योग्यता जोड़ने व संशोधन आवेदन",
            category = FormatCategory.TEACHERS_RELATED,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Seniority List • PDF",
            templateType = BuilderTemplateType.QUALIFICATION_SENIORITY_EDIT,
            isFromScreenshot = true
        ),
        FormatItem(
            id = "sn_15_acp_children_decl",
            serialNumber = 15,
            title = "ACP-संतान संबंधी घोषणा पत्र",
            subtitle = "01-06-2002 के पूर्व एवं पश्चात संतान विवरण घोषणा पत्र",
            category = FormatCategory.TEACHERS_RELATED,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Declaration • PDF",
            templateType = BuilderTemplateType.ACP_CHILDREN_DECLARATION,
            isFromScreenshot = true
        )
    )

    val coreUtilitiesAndOffice: List<FormatItem> = listOf(
        FormatItem("ex_16_auto_salary_arrear", 16, "Auto Salary & DA Arrear Calculation Sheet (7th Pay)", "महंगाई भत्ता (DA) / वेतन वृद्धि / पे-लेवल एरियर ऑटो गणना एक्सेल शीट", FormatCategory.EXCEL_UTILITIES, DocumentFileType.EXCEL, "Auto Formula • Excel (.xlsx)", BuilderTemplateType.AUTO_SALARY_DA_ARREAR),
        FormatItem("ex_17_income_tax", 17, "Income Tax Calculator Sheet (Old vs New Regime)", "आयकर गणना प्रपत्र FY 2025-26 / 2026-27 (GA-55 व छूट सहित)", FormatCategory.EXCEL_UTILITIES, DocumentFileType.EXCEL, "Tax Software • Excel (.xlsx)", BuilderTemplateType.INCOME_TAX_CALC),
        FormatItem("ex_18_pl_surrender", 18, "PL Surrender (उपार्जित अवकाश समर्पण) Bill & Order Sheet", "15 दिवसीय पी.एल. समर्पण स्वीकृति एवं बिल गणना शीट", FormatCategory.EXCEL_UTILITIES, DocumentFileType.EXCEL, "Leave Encashment • Excel", BuilderTemplateType.PL_SURRENDER),
        FormatItem("ex_19_mdm_sheet", 19, "MDM (मिड-डे मील) मासिक उपयोगिता एवं राशन गणना शीट", "कक्षा 1-5 व 6-8 छात्र उपस्थिति, गेहूं/चावल व कुकिंग कन्वर्जन राशि शीट", FormatCategory.EXCEL_UTILITIES, DocumentFileType.EXCEL, "PM Poshan / MDM • Excel", BuilderTemplateType.MDM_MONTHLY),
        FormatItem("ex_20_annual_increment", 20, "Annual Increment (वार्षिक वेतन वृद्धि - 1 जुलाई) Order Sheet", "समस्त विद्यालय स्टाफ की वार्षिक वेतन वृद्धि गणना व आदेश शीट", FormatCategory.EXCEL_UTILITIES, DocumentFileType.EXCEL, "1 July Increment • Excel", BuilderTemplateType.ANNUAL_INCREMENT),
        FormatItem("of_21_joining_ga13", 21, "कार्यग्रहण / कार्यमुक्ति एवं चार्ज हस्तांतरण प्रपत्र (GA-13)", "नवीन पदस्थापन / स्थानांतरण / पदोन्नति पर कार्यभार ग्रहण व मुक्ति रिपोर्ट", FormatCategory.OFFICE_SCHOOL, DocumentFileType.PDF, "Form GA-13 • PDF", BuilderTemplateType.JOINING_RELIEVING_GA13),
        FormatItem("of_22_leave_application", 22, "अवकाश प्रार्थना पत्र (CL / PL / Medical / CCL / Maternity)", "आकस्मिक, उपार्जित, चिकित्सा एवं चाइल्ड केयर अवकाश आवेदन प्रारूप", FormatCategory.OFFICE_SCHOOL, DocumentFileType.WORD, "Leave Format • Word (.docx)", BuilderTemplateType.LEAVE_APPLICATION),
        FormatItem("of_23_lpc_certificate", 23, "अंतिम वेतन प्रमाण पत्र (Last Pay Certificate - LPC)", "स्थानांतरण होने पर कार्मिक का वेतन, कटौतियां व ऋण विवरण प्रमाण-पत्र", FormatCategory.OFFICE_SCHOOL, DocumentFileType.PDF, "LPC • PDF Form", BuilderTemplateType.LPC_CERTIFICATE),
        FormatItem("of_24_student_certificate", 24, "छात्र अध्ययन एवं चरित्र प्रमाण पत्र (Study & Character Certificate)", "नियमित / पूर्व विद्यार्थियों के लिए विद्यालय अध्ययन व चरित्र प्रमाण-पत्र", FormatCategory.OFFICE_SCHOOL, DocumentFileType.WORD, "School Format • Word (.docx)", BuilderTemplateType.STUDENT_STUDY_CHAR),
        FormatItem("of_25_school_grant_uc", 25, "विद्यालय अनुदान उपयोगिता प्रमाण पत्र (Utilization Certificate - UC)", "कम्पोजिट स्कूल ग्रांट / समग्र शिक्षा अनुदान व्यय उपयोगिता प्रमाण-पत्र", FormatCategory.OFFICE_SCHOOL, DocumentFileType.PDF, "Samagra Shiksha UC • PDF", BuilderTemplateType.SCHOOL_GRANT_UC),
        FormatItem("of_26_gpf_si_decl", 26, "राज्य बीमा (SI) प्रथम / अतिरिक्त कटौती व GPF घोषणा प्रपत्र", "SIPF पोर्टल एवं कार्मिक वेतन से राज्य बीमा / जीपीएफ कटौती घोषणा पत्र", FormatCategory.OFFICE_SCHOOL, DocumentFileType.PDF, "SIPF / GPF • PDF", BuilderTemplateType.GPF_SI_DECLARATION)
    )

    val allFormats: List<FormatItem> by lazy {
        teachersRelatedFormats +
            coreUtilitiesAndOffice +
            PortalHomeCatalog.latestUpdates +
            PortalHomeCatalog.latestFormats +
            PortalHomeCatalog.onlineServiceItems +
            PortalHomeCatalog.stateCircularOrdersList +
            PortalHomeCatalog.computerSoftwares +
            SchemesSchoolCatalog.gpfItems +
            SchemesSchoolCatalog.mdmItems +
            SchemesSchoolCatalog.allUcItems +
            SchemesSchoolCatalog.gpaItems +
            SchemesSchoolCatalog.siItems +
            SchemesSchoolCatalog.schoolItems +
            SchemesSchoolCatalog.npsOpsItems +
            SchemesSchoolCatalog.hitkariItems +
            SchemesSchoolCatalog.sdmcItems +
            LeaveOfficeRulesCatalog.leaveItems +
            LeaveOfficeRulesCatalog.officeItems +
            LeaveOfficeRulesCatalog.shikshaAdeshItems +
            LeaveOfficeRulesCatalog.rsrRulesItems +
            LeaveOfficeRulesCatalog.financeOrderItems +
            LeaveOfficeRulesCatalog.dpcTransferItems
    }

    // All 18 Downloads Categories matching the rajteachers.co sidebar/grid + Latest sections
    val downloadCategories: List<FormatCategory> = listOf(
        FormatCategory.TEACHERS_RELATED,
        FormatCategory.OFFICE_FORMS,
        FormatCategory.LEAVE_ORDERS,
        FormatCategory.SHIKSHA_ADESH,
        FormatCategory.SDMC_SMC,
        FormatCategory.HITKARI_NIDHI,
        FormatCategory.GPA,
        FormatCategory.SI_FORMS,
        FormatCategory.GPF,
        FormatCategory.MDM_RELATED,
        FormatCategory.ALL_UC_FORMAT,
        FormatCategory.SCHOOL_RELATED,
        FormatCategory.NPS_OPS,
        FormatCategory.RSR_RULES,
        FormatCategory.FINANCE_ORDERS,
        FormatCategory.COMPUTER_SOFTWARES,
        FormatCategory.DPC_TRANSFER,
        FormatCategory.LATEST_UPDATES,
        FormatCategory.LATEST_FORMATS,
        FormatCategory.ONLINE_SERVICES
    )

    fun itemsByCategory(category: FormatCategory): List<FormatItem> {
        return when (category) {
            FormatCategory.SHIKSHA_ADESH -> LeaveOfficeRulesCatalog.shikshaAdeshItems
            else -> allFormats.filter { it.category == category }
        }
    }

    fun findById(id: String): FormatItem? = allFormats.firstOrNull { it.id == id }

    // Rajasthan 7th Pay Matrix Levels (L-1 to L-18) — First 12 cells per level
    val payMatrixLevels: Map<String, List<Long>> = linkedMapOf(
        "L-1" to listOf(17700, 18200, 18700, 19300, 19900, 20500, 21100, 21700, 22400, 23100, 23800, 24500),
        "L-2" to listOf(18500, 19100, 19700, 20300, 20900, 21500, 22100, 22800, 23500, 24200, 24900, 25600),
        "L-4" to listOf(19200, 19800, 20400, 21000, 21600, 22200, 22900, 23600, 24300, 25000, 25800, 26600),
        "L-5" to listOf(20800, 21400, 22000, 22700, 23400, 24100, 24800, 25500, 26300, 27100, 27900, 28700),
        "L-8" to listOf(26300, 27100, 27900, 28700, 29600, 30500, 31400, 32300, 33300, 34300, 35300, 36400),
        "L-10" to listOf(33800, 34800, 35800, 36900, 38000, 39100, 40300, 41500, 42700, 44000, 45300, 46700),
        "L-11" to listOf(37800, 38900, 40100, 41300, 42500, 43800, 45100, 46500, 47900, 49300, 50800, 52300),
        "L-12" to listOf(44300, 45600, 47000, 48400, 49900, 51400, 52900, 54500, 56100, 57800, 59500, 61300),
        "L-13" to listOf(47800, 49200, 50700, 52200, 53800, 55400, 57100, 58800, 60600, 62400, 64300, 66200),
        "L-14" to listOf(51500, 53000, 54600, 56200, 57900, 59600, 61400, 63200, 65100, 67100, 69100, 71200),
        "L-15" to listOf(56100, 57800, 59500, 61300, 63100, 65000, 67000, 69000, 71100, 73200, 75400, 77700),
        "L-16" to listOf(63700, 65600, 67600, 69600, 71700, 73900, 76100, 78400, 80800, 83200, 85700, 88300),
        "L-17" to listOf(67300, 69300, 71400, 73500, 75700, 78000, 80300, 82700, 85200, 87800, 90400, 93100),
        "L-18" to listOf(72800, 75000, 77300, 79600, 82000, 84500, 87000, 89600, 92300, 95100, 98000, 100900)
    )

    val portals: List<PortalResource> = PortalHomeCatalog.portals
}
