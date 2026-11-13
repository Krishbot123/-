package com.example.data

import com.example.model.BuilderTemplateType
import com.example.model.ContributorItem
import com.example.model.DocumentFileType
import com.example.model.FormatCategory
import com.example.model.FormatItem
import com.example.model.OnlineServiceItem
import com.example.model.PortalResource
import com.example.model.SoftwareToolItem
import com.example.model.StateCircularItem

object PortalHomeCatalog {

    val latestUpdates: List<FormatItem> = listOf(
        FormatItem(
            id = "lu_01_da_arrear_60_63",
            serialNumber = 1,
            title = "DA ARREARS 60% TO 63% EXCEL",
            subtitle = "महंगाई भत्ता 60% से 63% एरियर (जुलाई-अक्टूबर GPF व नकद) ऑटो एक्सेल शीट",
            category = FormatCategory.LATEST_UPDATES,
            fileType = DocumentFileType.EXCEL,
            authorOrTag = "DA Hike 60%→63% • Excel",
            templateType = BuilderTemplateType.DA_ARREAR_60_63,
            dateLabel = "Updated 2026"
        ),
        FormatItem(
            id = "lu_02_adhoc_bonus_2025_26",
            serialNumber = 2,
            title = "वर्ष 2025-26 के तदर्थ बोनस हेतु एक्सेल प्रोग्राम",
            subtitle = "अराजपत्रित कार्मिक तदर्थ बोनस (₹6,774 / 75% Cash + 25% GPF) एक्सेल प्रोग्राम",
            category = FormatCategory.LATEST_UPDATES,
            fileType = DocumentFileType.EXCEL,
            authorOrTag = "Ad-hoc Bonus • Excel",
            templateType = BuilderTemplateType.ADHOC_BONUS_2025_26,
            dateLabel = "2025-26"
        ),
        FormatItem(
            id = "lu_03_bonus_office_order",
            serialNumber = 3,
            title = "Bonus Office Order",
            subtitle = "वर्ष 2025-26 तदर्थ बोनस स्वीकृति कार्यालय आदेश संपादन योग्य वर्ड फाइल",
            category = FormatCategory.LATEST_UPDATES,
            fileType = DocumentFileType.WORD,
            authorOrTag = "Office Order • Word",
            templateType = BuilderTemplateType.BONUS_OFFICE_ORDER,
            dateLabel = "2025-26"
        ),
        FormatItem(
            id = "lu_04_estimated_mdm_2026",
            serialNumber = 4,
            title = "Estimated MDM BILL CALCULATOR 2026",
            subtitle = "अनुमानित मिड-डे मील (PM POSHAN) बिल व खाद्यान्न गणना एक्सेल शीट",
            category = FormatCategory.LATEST_UPDATES,
            fileType = DocumentFileType.EXCEL,
            authorOrTag = "MDM 2026 • Excel",
            templateType = BuilderTemplateType.MDM_MONTHLY,
            dateLabel = "2026"
        ),
        FormatItem(
            id = "lu_05_mggs_budget_2026_27",
            serialNumber = 5,
            title = "MGGS बजट प्रस्ताव 2026-27 Excel Program By: Ummed Tarad",
            subtitle = "महात्मा गांधी राजकीय विद्यालय (MGGS) वित्तीय वर्ष 2026-27 बजट प्रस्ताव शीट",
            category = FormatCategory.LATEST_UPDATES,
            fileType = DocumentFileType.EXCEL,
            authorOrTag = "By: Ummed Tarad",
            templateType = BuilderTemplateType.BUDGET_PROPOSAL_2026_27,
            dateLabel = "2026-27"
        ),
        FormatItem(
            id = "lu_06_budget_proposal_2026_27",
            serialNumber = 6,
            title = "वित्तीय वर्ष 2026-27 बजट प्रस्ताव Excel Program By: Ummed tarad",
            subtitle = "विद्यालय/कार्यालय मदवार वार्षिक बजट मांग प्रस्ताव एक्सेल प्रोग्राम",
            category = FormatCategory.LATEST_UPDATES,
            fileType = DocumentFileType.EXCEL,
            authorOrTag = "By: Ummed Tarad",
            templateType = BuilderTemplateType.BUDGET_PROPOSAL_2026_27,
            dateLabel = "2026-27"
        ),
        FormatItem(
            id = "lu_07_budget_programme_2026_27",
            serialNumber = 7,
            title = "BUDGET PROGRAMME 2026-27",
            subtitle = "राजकीय विद्यालय आय-व्यय एवं बजट अनुमान 2026-27 एक्सेल प्रोग्राम",
            category = FormatCategory.LATEST_UPDATES,
            fileType = DocumentFileType.EXCEL,
            authorOrTag = "Budget • Excel",
            templateType = BuilderTemplateType.BUDGET_PROPOSAL_2026_27,
            dateLabel = "2026-27"
        ),
        FormatItem(
            id = "lu_08_budget_software_parmanand",
            serialNumber = 8,
            title = "Budget Software 2026-27 By: Parmanand Meghwal",
            subtitle = "संस्थापन, वेतन एवं कार्यालय व्यय बजट सॉफ्टवेयर 2026-27",
            category = FormatCategory.LATEST_UPDATES,
            fileType = DocumentFileType.EXCEL,
            authorOrTag = "By: Parmanand Meghwal",
            templateType = BuilderTemplateType.BUDGET_PROPOSAL_2026_27,
            dateLabel = "2026-27"
        ),
        FormatItem(
            id = "lu_09_4th_grade_joining_app",
            serialNumber = 9,
            title = "4th Grade Joining Application",
            subtitle = "चतुर्थ श्रेणी कर्मचारी नवनियुक्ति कार्यग्रहण प्रार्थना पत्र (Word Format)",
            category = FormatCategory.LATEST_UPDATES,
            fileType = DocumentFileType.WORD,
            authorOrTag = "4th Grade • Word",
            templateType = BuilderTemplateType.FIRST_APPOINTMENT_OATH_WORD,
            dateLabel = "02-07-2026"
        ),
        FormatItem(
            id = "lu_10_shivira_panchang_2026_27",
            serialNumber = 10,
            title = "शिविरा पंचांग 2026–27",
            subtitle = "माध्यमिक एवं प्रारंभिक शिक्षा विभाग राजस्थान वार्षिक शिविरा पंचांग व अवकाश तालिका",
            category = FormatCategory.LATEST_UPDATES,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Shivira Calendar • PDF",
            templateType = BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE,
            dateLabel = "2026-27"
        )
    )

    val latestFormats: List<FormatItem> = listOf(
        FormatItem(
            id = "lf_01_ifms3_pension_kulak",
            serialNumber = 1,
            title = "IFMS 3.0 Pension Kulak Excel Program",
            subtitle = "सेवानिवृत्ति पेंशन, कम्यूटेशन एवं ग्रेच्युटी (DCRG) कुलक एक्सेल प्रोग्राम",
            category = FormatCategory.LATEST_FORMATS,
            fileType = DocumentFileType.EXCEL,
            authorOrTag = "IFMS 3.0 Pension • Excel",
            templateType = BuilderTemplateType.PENSION_CALCULATOR_KULAK,
            dateLabel = "2026"
        ),
        FormatItem(
            id = "lf_02_income_tax_mobile_2026",
            serialNumber = 2,
            title = "INCOME-TAX-CALCULATION-PROGRAM-Mobile-Version-Dt. 11.07.2026",
            subtitle = "आयकर गणना प्रोग्राम मोबाइल वर्जन (Old vs New Regime 2026-27)",
            category = FormatCategory.LATEST_FORMATS,
            fileType = DocumentFileType.EXCEL,
            authorOrTag = "Dt. 11.07.2026 • Excel",
            templateType = BuilderTemplateType.INCOME_TAX_CALC,
            dateLabel = "11-07-2026"
        ),
        FormatItem(
            id = "lf_03_cwsn_forms",
            serialNumber = 3,
            title = "CWSN FORMS",
            subtitle = "विशेष आवश्यकता वाले बालक-बालिका (CWSN) चिन्हांकन एवं भत्ता प्रपत्र",
            category = FormatCategory.LATEST_FORMATS,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Samagra Shiksha • PDF",
            templateType = BuilderTemplateType.ADMISSION_FORM_BLANK,
            dateLabel = "2026"
        ),
        FormatItem(
            id = "lf_04_auto_arrear_2026",
            serialNumber = 4,
            title = "Auto Arrear Excel Sheet 2026",
            subtitle = "स्वचालित वेतन व महंगाई भत्ता एरियर गणना एक्सेल शीट 2026",
            category = FormatCategory.LATEST_FORMATS,
            fileType = DocumentFileType.EXCEL,
            authorOrTag = "Auto Arrear • Excel",
            templateType = BuilderTemplateType.AUTO_SALARY_DA_ARREAR,
            dateLabel = "2026"
        ),
        FormatItem(
            id = "lf_05_hlb_record_format",
            serialNumber = 5,
            title = "Offline data HLB RECORD FORMATE",
            subtitle = "HLB ऑफलाइन डेटा संकलन एवं विद्यालय अभिलेख प्रारूप एक्सेल",
            category = FormatCategory.LATEST_FORMATS,
            fileType = DocumentFileType.EXCEL,
            authorOrTag = "Offline Record • Excel",
            templateType = BuilderTemplateType.HLB_RECORD_EXCEL,
            dateLabel = "2026"
        ),
        FormatItem(
            id = "lf_06_admission_form_blank",
            serialNumber = 6,
            title = "ADDMISSION FORM - BLANK",
            subtitle = "राजकीय विद्यालय विद्यार्थी प्रवेश आवेदन पत्र (Blank / Filled PDF)",
            category = FormatCategory.LATEST_FORMATS,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Praveshotsav • PDF",
            templateType = BuilderTemplateType.ADMISSION_FORM_BLANK,
            dateLabel = "2026-27"
        ),
        FormatItem(
            id = "lf_07_sports_fee_format",
            serialNumber = 7,
            title = "क्रीड़ा शुल्क प्रारूप",
            subtitle = "जिला / राज्य स्तरीय विद्यालयी खेलकूद क्रीड़ा शुल्क चालान व प्रपत्र",
            category = FormatCategory.LATEST_FORMATS,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Sports Fee • PDF",
            templateType = BuilderTemplateType.SPORTS_FEE_FORM,
            dateLabel = "2026-27"
        ),
        FormatItem(
            id = "lf_08_da_arrear_60_parmanand",
            serialNumber = 8,
            title = "DA Arrear Excel Sheet - 60% By: Parmanand Meghwal",
            subtitle = "60% महंगाई भत्ता (DA) एरियर गणना एवं GPF अंतरण शीट",
            category = FormatCategory.LATEST_FORMATS,
            fileType = DocumentFileType.EXCEL,
            authorOrTag = "By: Parmanand Meghwal",
            templateType = BuilderTemplateType.DA_ARREAR_60_63,
            dateLabel = "2026"
        ),
        FormatItem(
            id = "lf_09_household_survey_2026",
            serialNumber = 9,
            title = "HOUSE HOLD सर्वे प्रोग्राम 2026",
            subtitle = "हाउस होल्ड सर्वे एवं आउट ऑफ स्कूल बालक-बालिका संकलन एक्सेल प्रोग्राम",
            category = FormatCategory.LATEST_FORMATS,
            fileType = DocumentFileType.EXCEL,
            authorOrTag = "Survey 2026 • Excel",
            templateType = BuilderTemplateType.HOUSEHOLD_SURVEY_EXCEL,
            dateLabel = "2026"
        ),
        FormatItem(
            id = "lf_10_mdm_all_uc_auto",
            serialNumber = 10,
            title = "MDM AUTOMATIC CALCULATION ALL UC",
            subtitle = "MDM पोषाहार समस्त मासिक व त्रैमासिक उपयोगिता प्रमाण पत्र ऑटो एक्सेल",
            category = FormatCategory.LATEST_FORMATS,
            fileType = DocumentFileType.EXCEL,
            authorOrTag = "MDM All UC • Excel",
            templateType = BuilderTemplateType.MDM_ALL_UC,
            dateLabel = "2026"
        )
    )

    val stateCircularOrdersList: List<FormatItem> = listOf(
        FormatItem(
            id = "sc_01_budget_circular_2026_27",
            serialNumber = 1,
            title = "बजट परिपत्र वित्तीय वर्ष 2026-27 एवं अनुमान वर्ष 2027-28",
            subtitle = "वित्त विभाग राजस्थान — बजट मांग प्रपत्र व अनुमान संकलन परिपत्र",
            category = FormatCategory.SHIKSHA_ADESH,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Finance Circular • PDF",
            templateType = BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE,
            dateLabel = "07-08-2026"
        ),
        FormatItem(
            id = "sc_02_crc_grant_2026_27",
            serialNumber = 2,
            title = "संकुल संदर्भ केंद्र ग्रांट (CRC Grant) दिशा निर्देश सत्र 2026-27",
            subtitle = "समग्र शिक्षा CRC ग्रांट व्यय व उपयोगिता प्रमाण पत्र दिशा-निर्देश",
            category = FormatCategory.SHIKSHA_ADESH,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Samagra Shiksha • PDF",
            templateType = BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE,
            dateLabel = "28-07-2026"
        ),
        FormatItem(
            id = "sc_03_poshahar_menu",
            serialNumber = 3,
            title = "पोषाहार मेन्यू निर्देश।",
            subtitle = "प्रधानमंत्री पोषण शक्ति निर्माण (MDM) साप्ताहिक मेन्यू एवं खाद्यान्न मानक",
            category = FormatCategory.SHIKSHA_ADESH,
            fileType = DocumentFileType.PDF,
            authorOrTag = "MDM Circular • PDF",
            templateType = BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE,
            dateLabel = "28-07-2026"
        ),
        FormatItem(
            id = "sc_04_family_pension_ifms3",
            serialNumber = 4,
            title = "Family Pension Upon Death after Retirement on IFMS 3.0",
            subtitle = "सेवानिवृत्ति उपरांत निधन पर IFMS 3.0 पोर्टल से पारिवारिक पेंशन प्रक्रिया",
            category = FormatCategory.SHIKSHA_ADESH,
            fileType = DocumentFileType.PDF,
            authorOrTag = "IFMS 3.0 • PDF",
            templateType = BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE,
            dateLabel = "28-07-2026"
        ),
        FormatItem(
            id = "sc_05_doit_transfer",
            serialNumber = 5,
            title = "सूचना प्रौद्योगिकी एवं संचार विभाग - राज्यअधीनस्थ सेवा एवं मंत्रालयिक सेवा स्थानांतरण",
            subtitle = "DoIT एवं मंत्रालयिक संवर्ग स्थानांतरण आदेश एवं कार्यमुक्ति निर्देश",
            category = FormatCategory.SHIKSHA_ADESH,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Transfer Order • PDF",
            templateType = BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE,
            dateLabel = "10-07-2026"
        ),
        FormatItem(
            id = "sc_06_prapatra_10_date",
            serialNumber = 6,
            title = "कार्मिक के प्रपत्र 10 लॉक करने की तिथि 20 जुलाई 2026",
            subtitle = "शाला दर्पण कार्मिक प्रपत्र-10 अद्यतन एवं अंतिम सत्यापन समय-सारणी",
            category = FormatCategory.SHIKSHA_ADESH,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Shala Darpan • PDF",
            templateType = BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE,
            dateLabel = "09-07-2026"
        ),
        FormatItem(
            id = "sc_07_prapatra_10_verification",
            serialNumber = 7,
            title = "शालादर्पण पोर्टल कार्मिक विस्तृत विवरण (प्रपत्र-10) लॉक व सत्यापन के संबंध में",
            subtitle = "कार्मिक सेवा अभिलेख एवं प्रपत्र-10 डीडीओ सत्यापन निर्देश",
            category = FormatCategory.SHIKSHA_ADESH,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Shala Darpan • PDF",
            templateType = BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE,
            dateLabel = "09-07-2026"
        ),
        FormatItem(
            id = "sc_08_praveshotsav_2026_27",
            serialNumber = 8,
            title = "प्रवेशोत्सव 2026-27 दिशा-निर्देश।",
            subtitle = "प्रवेशोत्सव अभियान, हाउस होल्ड सर्वे एवं नव प्रवेश नामांकन निर्देश",
            category = FormatCategory.SHIKSHA_ADESH,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Praveshotsav • PDF",
            templateType = BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE,
            dateLabel = "02-07-2026"
        ),
        FormatItem(
            id = "sc_09_4th_grade_appointment",
            serialNumber = 9,
            title = "4th Grade सचिवालय नियुक्ति आदेश।",
            subtitle = "चतुर्थ श्रेणी कर्मचारी नियुक्ति, पदस्थापन एवं कार्यग्रहण दिशा-निर्देश",
            category = FormatCategory.SHIKSHA_ADESH,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Appointment • PDF",
            templateType = BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE,
            dateLabel = "02-07-2026"
        ),
        FormatItem(
            id = "sc_10_mdm_order_02072026",
            serialNumber = 10,
            title = "MDM दिशा निर्देश आदेश दिनांक 02/07/2026",
            subtitle = "मिड-डे मील कुकिंग कन्वर्जन दर, खाद्यान्न आवंटन एवं निरीक्षण निर्देश",
            category = FormatCategory.SHIKSHA_ADESH,
            fileType = DocumentFileType.PDF,
            authorOrTag = "MDM Order • PDF",
            templateType = BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE,
            dateLabel = "02-07-2026"
        )
    )

    val stateCircularTable: List<StateCircularItem> = stateCircularOrdersList.map {
        StateCircularItem(
            serialNumber = it.serialNumber,
            title = it.title,
            date = it.dateLabel,
            linkedFormatId = it.id
        )
    }

    val onlineServiceItems: List<FormatItem> = listOf(
        FormatItem(
            id = "os_01_7th_pay_statement",
            serialNumber = 1,
            title = "7th Pay Statement",
            subtitle = "7वें वेतन आयोग पे-मैट्रिक्स वेतन नियतन स्टेटमेंट व आदेश",
            category = FormatCategory.ONLINE_SERVICES,
            fileType = DocumentFileType.WORD,
            authorOrTag = "Online Builder • Word",
            templateType = BuilderTemplateType.FIXATION_ORDER,
            dateLabel = "Live Tool"
        ),
        FormatItem(
            id = "os_02_pension_documents",
            serialNumber = 2,
            title = "Pension Related Documents",
            subtitle = "सेवानिवृत्ति पेंशन केस, कुलक, शपथ पत्र प्रारूप-6 व GA-141",
            category = FormatCategory.ONLINE_SERVICES,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Pension Pack • PDF",
            templateType = BuilderTemplateType.PENSION_CALCULATOR_KULAK,
            dateLabel = "Live Tool"
        ),
        FormatItem(
            id = "os_03_income_tax_2026_27",
            serialNumber = 3,
            title = "Income Tax Calculator 2026-27",
            subtitle = "Old vs New Tax Regime आयकर गणना व एक्सेल शीट जनरेटर",
            category = FormatCategory.ONLINE_SERVICES,
            fileType = DocumentFileType.EXCEL,
            authorOrTag = "FY 2026-27 • Excel",
            templateType = BuilderTemplateType.INCOME_TAX_CALC,
            dateLabel = "Live Tool"
        ),
        FormatItem(
            id = "os_04_pension_calculator",
            serialNumber = 4,
            title = "Pension Calculator",
            subtitle = "बेसिक पेंशन (50%), कम्यूटेशन (40%) व DCRG ग्रेच्युटी कैलकुलेटर",
            category = FormatCategory.ONLINE_SERVICES,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Pension Calc • PDF",
            templateType = BuilderTemplateType.PENSION_CALCULATOR_KULAK,
            dateLabel = "Live Tool"
        ),
        FormatItem(
            id = "os_05_7th_pay_arrear",
            serialNumber = 5,
            title = "7th Pay Salary Arrear",
            subtitle = "7th Pay माहवार आहरित बनाम देय वेतन व DA 60%→63% एरियर शीट",
            category = FormatCategory.ONLINE_SERVICES,
            fileType = DocumentFileType.EXCEL,
            authorOrTag = "7th Pay • Excel",
            templateType = BuilderTemplateType.DA_ARREAR_60_63,
            dateLabel = "Live Tool"
        ),
        FormatItem(
            id = "os_06_6th_pay_arrear",
            serialNumber = 6,
            title = "6th Pay Salary Arrear",
            subtitle = "छठे वेतन आयोग (Grade Pay / Band Pay) बकाया एरियर गणना शीट",
            category = FormatCategory.ONLINE_SERVICES,
            fileType = DocumentFileType.EXCEL,
            authorOrTag = "6th Pay • Excel",
            templateType = BuilderTemplateType.SIXTH_PAY_ARREAR,
            dateLabel = "Live Tool"
        ),
        FormatItem(
            id = "os_07_5th_pay_arrear",
            serialNumber = 7,
            title = "5th Pay Salary Arrear",
            subtitle = "पाँचवे वेतन आयोग बकाया वेतन एरियर विवरण एक्सेल शीट",
            category = FormatCategory.ONLINE_SERVICES,
            fileType = DocumentFileType.EXCEL,
            authorOrTag = "5th Pay • Excel",
            templateType = BuilderTemplateType.FIFTH_PAY_ARREAR,
            dateLabel = "Live Tool"
        ),
        FormatItem(
            id = "os_08_office_order_generator",
            serialNumber = 8,
            title = "Office Order Generator",
            subtitle = "Increment, PL Surrender, ACP/MACP, Fixation, Notional, Leave आदेश",
            category = FormatCategory.ONLINE_SERVICES,
            fileType = DocumentFileType.WORD,
            authorOrTag = "All Orders • Word",
            templateType = BuilderTemplateType.ACP_FITMENT_ORDER,
            dateLabel = "Live Tool"
        ),
        FormatItem(
            id = "os_09_ta_bill_generator",
            serialNumber = 9,
            title = "TA Bill Generator (IFMS 3.0)",
            subtitle = "यात्रा भत्ता (TA/DA) बिल गणना व IFMS 3.0 एक्सेल प्रारूप",
            category = FormatCategory.ONLINE_SERVICES,
            fileType = DocumentFileType.EXCEL,
            authorOrTag = "TA Bill • Excel",
            templateType = BuilderTemplateType.TA_BILL_EXCEL,
            dateLabel = "Live Tool"
        ),
        FormatItem(
            id = "os_10_scholarship_2026_27",
            serialNumber = 10,
            title = "Rajasthan Scholarship & Death Kulak",
            subtitle = "छात्रवृत्ति एवं सेवाकाल मृत्यु अनुकंपा / पेंशन कुलक प्रपत्र",
            category = FormatCategory.ONLINE_SERVICES,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Special Content • PDF",
            templateType = BuilderTemplateType.HITKARI_SCHOLARSHIP,
            dateLabel = "2026-27"
        )
    )

    val onlineServiceCards: List<OnlineServiceItem> = onlineServiceItems.map {
        OnlineServiceItem(
            id = it.id,
            title = it.title,
            subtitle = it.subtitle,
            badge = it.fileType.label,
            linkedFormatId = it.id
        )
    }

    val computerSoftwares: List<FormatItem> = listOf(
        FormatItem(
            id = "sw_01_hindi_unicode",
            serialNumber = 1,
            title = "Hindi Tools Unicode Download",
            subtitle = "Google Input / Mangal Unicode हिंदी टाइपिंग व कन्वर्जन गाइड",
            category = FormatCategory.COMPUTER_SOFTWARES,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Unicode Tool • Guide",
            templateType = BuilderTemplateType.SOFTWARE_TOOL,
            dateLabel = "Utility"
        ),
        FormatItem(
            id = "sw_02_hindi_tools_kit",
            serialNumber = 2,
            title = "Hindi Tools Kit Download",
            subtitle = "Kruti Dev ↔ Unicode (Mangal) कन्वर्टर एवं टाइपिंग किट उपयोग निर्देश",
            category = FormatCategory.COMPUTER_SOFTWARES,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Hindi Kit • Guide",
            templateType = BuilderTemplateType.SOFTWARE_TOOL,
            dateLabel = "Utility"
        ),
        FormatItem(
            id = "sw_03_kruti_dev_010",
            serialNumber = 3,
            title = "Kruti Dev 010 Download",
            subtitle = "राजस्थान सरकारी कार्यालयी कार्य हेतु Kruti Dev 010 फॉन्ट इंस्टॉलेशन निर्देश",
            category = FormatCategory.COMPUTER_SOFTWARES,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Font • Kruti Dev 010",
            templateType = BuilderTemplateType.SOFTWARE_TOOL,
            dateLabel = "Font"
        ),
        FormatItem(
            id = "sw_04_calibri_font",
            serialNumber = 4,
            title = "Calibri Font Download",
            subtitle = "अंग्रेजी एवं एक्सेल प्रपत्रों हेतु मानक Calibri फॉन्ट सेटअप निर्देश",
            category = FormatCategory.COMPUTER_SOFTWARES,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Font • Calibri",
            templateType = BuilderTemplateType.SOFTWARE_TOOL,
            dateLabel = "Font"
        ),
        FormatItem(
            id = "sw_05_bootable_pendrive",
            serialNumber = 5,
            title = "Bootable Pen Drive",
            subtitle = "विद्यालय ICT लैब कंप्यूटर हेतु बूटेबल पेनड्राइव निर्माण चरणबद्ध गाइड",
            category = FormatCategory.COMPUTER_SOFTWARES,
            fileType = DocumentFileType.PDF,
            authorOrTag = "ICT Lab • Guide",
            templateType = BuilderTemplateType.SOFTWARE_TOOL,
            dateLabel = "Guide"
        ),
        FormatItem(
            id = "sw_06_excel_to_pdf",
            serialNumber = 6,
            title = "Excel To Pdf Converter",
            subtitle = "वेतन/एरियर/टैक्स एक्सेल शीट को सीधे A4 PDF में प्रिंट व एक्सपोर्ट करने की विधि",
            category = FormatCategory.COMPUTER_SOFTWARES,
            fileType = DocumentFileType.PDF,
            authorOrTag = "Built-in Converter",
            templateType = BuilderTemplateType.SOFTWARE_TOOL,
            dateLabel = "Built-in"
        )
    )

    val softwareLinks: List<SoftwareToolItem> = listOf(
        SoftwareToolItem("sw_01_hindi_unicode", "Hindi Tools Unicode Download", "Mangal / Unicode Typing Tools", "https://rajteachers.co/", "sw_01_hindi_unicode"),
        SoftwareToolItem("sw_02_hindi_tools_kit", "Hindi Tools Kit Download", "Kruti Dev ↔ Unicode Converter Kit", "https://rajteachers.co/", "sw_02_hindi_tools_kit"),
        SoftwareToolItem("sw_03_kruti_dev_010", "Kruti Dev 010 Download", "Standard Rajasthan Govt Hindi Font", "https://rajteachers.co/", "sw_03_kruti_dev_010"),
        SoftwareToolItem("sw_04_calibri_font", "Calibri Font Download", "Standard Office Excel/Word Font", "https://rajteachers.co/", "sw_04_calibri_font"),
        SoftwareToolItem("sw_05_bootable_pendrive", "Bootable Pen Drive", "ICT Lab Windows Setup Utility", "https://rajteachers.co/", "sw_05_bootable_pendrive"),
        SoftwareToolItem("sw_06_excel_to_pdf", "Excel To Pdf Converter", "App के भीतर PDF/Excel एक्सपोर्ट उपलब्ध", "https://rajteachers.co/", "sw_06_excel_to_pdf")
    )

    val contributors: List<ContributorItem> = listOf(
        ContributorItem("Parmanand Meghwal", "ACP Fitment, 7th Pay Excel, DA Arrear, Budget Software & Notional Orders"),
        ContributorItem("Ummed Tarad", "MGGS Budget Proposal 2026-27 & Annual Budget Excel Programs"),
        ContributorItem("Shivraj Meghwanshi", "State Circulars, School Establishment & Teacher Utilities"),
        ContributorItem("Bhagirath Mal Kalwaniya", "Service Rules, Fixation Formats & Administrative Orders"),
        ContributorItem("Heera Lal Jat", "Probation & Promotion Pay Fixation Word Formats"),
        ContributorItem("Sunil Kumar Mahawar", "Office Orders, Income Tax & PayManager Support"),
        ContributorItem("Surendra Pareek", "School Formats, MDM & Leave Rules Compilation"),
        ContributorItem("Hans Raj Joshi", "Educational Service Rules & Teacher Welfare Formats")
    )

    val portals: List<PortalResource> = listOf(
        PortalResource("RajTeachers.co", "मूल पोर्टल — शिक्षक प्रपत्र, एक्सेल शीट्स एवं समाचार", "https://rajteachers.co/", "PORTAL"),
        PortalResource("Shala Darpan (शाला दर्पण)", "स्टाफ लॉगिन, प्रपत्र-10, विद्यालय डेटा, स्थानांतरण व प्रमाण-पत्र", "https://rajshaladarpan.nic.in/", "GOVT"),
        PortalResource("IFMS 3.0 Portal", "वेतन बिल, DA एरियर, तदर्थ बोनस, TA बिल एवं पेंशन कुलक", "https://ifms.rajasthan.gov.in/", "IFMS 3.0"),
        PortalResource("SSO Rajasthan", "सिंगल साइन-ऑन (SIPF, RGHS, राजकाज, ई-सर्विस बुक)", "https://sso.rajasthan.gov.in/", "SSO"),
        PortalResource("PayManager Portal", "पे-स्लिप, GA-55 एवं पुरानी वेतन पर्चियां", "https://paymanager.rajasthan.gov.in/", "PAY"),
        PortalResource("SIPF Portal (SI & GPF)", "राज्य बीमा पॉलिसी, GPA नॉमिनेशन, जीपीएफ लेजर एवं एनपीएस", "https://sipf.rajasthan.gov.in/", "SIPF"),
        PortalResource("RGHS Rajasthan", "राजस्थान सरकारी स्वास्थ्य योजना कार्ड व अस्पताल सूची", "https://rghs.rajasthan.gov.in/", "HEALTH")
    )
}
