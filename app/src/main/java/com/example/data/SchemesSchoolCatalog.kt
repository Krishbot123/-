package com.example.data

import com.example.model.BuilderTemplateType
import com.example.model.DocumentFileType
import com.example.model.FormatCategory
import com.example.model.FormatItem

object SchemesSchoolCatalog {

    val gpfItems: List<FormatItem> = listOf(
        FormatItem("gpf_01_rules_2021", 1, "सामान्य प्रावधायी निधि (GPF) नियम 2021", "राजस्थान सामान्य प्रावधायी निधि नियम, कटौती व ब्याज दर मार्गदर्शिका", FormatCategory.GPF, DocumentFileType.PDF, "GPF Rules • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE),
        FormatItem("gpf_02_indemnity_bond", 2, "GPF-क्षतिपूर्ति बांड फॉर्म", "जीपीएफ अंतिम भुगतान / मृत्यु दावा हेतु क्षतिपूर्ति बंध-पत्र प्रपत्र", FormatCategory.GPF, DocumentFileType.PDF, "Indemnity Bond • PDF", BuilderTemplateType.GPF_INDEMNITY_BOND),
        FormatItem("gpf_03_withdrawal_guide", 3, "जीपीएफ (GPF) आहरण निर्देश", "GPF अस्थाई एवं स्थाई आहरण (75%/90% Withdrawal) नियम व पात्रता", FormatCategory.GPF, DocumentFileType.PDF, "GPF Withdrawal • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE)
    )

    val mdmItems: List<FormatItem> = listOf(
        FormatItem("mdm_01_demand_form", 1, "मांग प्रपत्र - पोषाहार एवं कुक कम हेल्पर्स हेतु", "MDM खाद्यान्न, कुकिंग कन्वर्जन एवं कुक कम हेल्पर मानदेय मांग प्रपत्र", FormatCategory.MDM_RELATED, DocumentFileType.PDF, "MDM Demand • PDF", BuilderTemplateType.MDM_DEMAND_FORM),
        FormatItem("mdm_02_monthly_uc", 2, "MDM पोषाहार - मासिक उपयोगिता प्रमाण पत्र", "कक्षा 1-5 एवं 6-8 मासिक पोषाहार उपयोगिता व राशन गणना एक्सेल शीट", FormatCategory.MDM_RELATED, DocumentFileType.EXCEL, "Monthly UC • Excel", BuilderTemplateType.MDM_MONTHLY),
        FormatItem("mdm_03_all_uc_formats", 3, "MDM ALL UC FORMATS - उपयोगिता प्रमाण पत्र", "समस्त मदवार MDM उपयोगिता प्रमाण-पत्र ऑटो कैलकुलेशन एक्सेल", FormatCategory.MDM_RELATED, DocumentFileType.EXCEL, "All UC • Excel", BuilderTemplateType.MDM_ALL_UC)
    )

    val allUcItems: List<FormatItem> = listOf(
        FormatItem("uc_01_rmsa_nirman", 1, "RMSA-निर्माण कार्यों के सम्बन्ध में मार्गदर्शिका", "RMSA / समग्र शिक्षा निर्माण कार्य एवं उपयोगिता प्रमाण-पत्र मार्गदर्शिका", FormatCategory.ALL_UC_FORMAT, DocumentFileType.PDF, "RMSA UC • PDF", BuilderTemplateType.SCHOOL_GRANT_UC)
    )

    val gpaItems: List<FormatItem> = listOf(
        FormatItem("gpa_01_circular", 1, "GPA - परिपत्र", "सामूहिक व्यक्तिगत दुर्घटना बीमा (GPA) वार्षिक कटौती व प्रीमियम स्लैब परिपत्र", FormatCategory.GPA, DocumentFileType.PDF, "GPA Circular • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE),
        FormatItem("gpa_02_fidelity_guarantee", 2, "GPA - Fidelity Guarantee Insurance Form", "कोष/रोकड़ अभिरक्षा कार्मिक फिडेलिटी गारंटी बीमा प्रस्ताव प्रपत्र", FormatCategory.GPA, DocumentFileType.PDF, "Fidelity Form • PDF", BuilderTemplateType.GPA_NOMINATION),
        FormatItem("gpa_03_nomination_form", 3, "GPA - NOMINATION FORM", "सामूहिक दुर्घटना बीमा (GPA) उत्तराधिकारी नामांकन (Nomination) प्रपत्र", FormatCategory.GPA, DocumentFileType.PDF, "Nomination • PDF", BuilderTemplateType.GPA_NOMINATION),
        FormatItem("gpa_04_deduction_schedule", 4, "GPA - DEDUCTION SCHEDULE", "समस्त स्टाफ GPA कटौती (₹700 / ₹1400 / ₹2100) मासिक शेड्यूल एक्सेल", FormatCategory.GPA, DocumentFileType.EXCEL, "Schedule • Excel", BuilderTemplateType.GPA_DEDUCTION_EXCEL),
        FormatItem("gpa_05_covering_letter", 5, "GPA - COVERING LETTER", "SIPF कार्यालय को GPA प्रपत्र व शेड्यूल प्रेषण कवरिंग लेटर (Word)", FormatCategory.GPA, DocumentFileType.WORD, "Covering Letter • Word", BuilderTemplateType.GPA_COVERING_WORD),
        FormatItem("gpa_06_claim_other_than_death", 6, "दुर्घटना बीमा योजना दावा पत्र (मृत्यु के अतिरिक्त)", "स्थाई अपंगता / अंग-भंग दुर्घटना बीमा योजना दावा आवेदन प्रपत्र", FormatCategory.GPA, DocumentFileType.PDF, "Disability Claim • PDF", BuilderTemplateType.GPA_CLAIM),
        FormatItem("gpa_07_claim_death", 7, "दुर्घटना बीमा योजना दावा पत्र (मृत्यु की दशा में)", "दुर्घटना में मृत्यु की दशा में आश्रित द्वारा GPA बीमा दावा प्रपत्र", FormatCategory.GPA, DocumentFileType.PDF, "Death Claim • PDF", BuilderTemplateType.GPA_CLAIM)
    )

    val siItems: List<FormatItem> = listOf(
        FormatItem("si_01_first_further_decl", 1, "State Insurance प्रथम घोषणा व अभिवृद्धि प्रपत्र", "SIPF राज्य बीमा प्रथम कटौती एवं स्लैब अभिवृद्धि (Further) घोषणा पत्र", FormatCategory.SI_FORMS, DocumentFileType.PDF, "First/Further • PDF", BuilderTemplateType.SI_FIRST_DECL),
        FormatItem("si_02_ga79_form", 2, "प्रथम घोषणा के साथ लगाया जाने वाला प्रपत्र (GA-79)", "राज्य बीमा प्रथम घोषणा के साथ संलग्न की जाने वाली चिकित्सा जांच प्रपत्र GA-79", FormatCategory.SI_FORMS, DocumentFileType.PDF, "Form GA-79 • PDF", BuilderTemplateType.SI_GA79),
        FormatItem("si_03_claim_maturity_surrender", 3, "SI-मृत्यु दावा प्रपत्र परिपक्वता/अध्यर्पण प्रपत्र", "राज्य बीमा परिपक्वता (Maturity), मृत्यु दावा एवं पॉलिसी अध्यर्पण प्रपत्र", FormatCategory.SI_FORMS, DocumentFileType.PDF, "Maturity/Death Claim • PDF", BuilderTemplateType.SI_CLAIM),
        FormatItem("si_04_nominee_form", 4, "राज्य बीमा नॉमिनी फॉर्म", "राज्य बीमा पॉलिसी (SI) में नॉमिनी जोड़ने व संशोधन हेतु प्रपत्र", FormatCategory.SI_FORMS, DocumentFileType.PDF, "SI Nominee • PDF", BuilderTemplateType.SI_NOMINATION),
        FormatItem("si_05_maturity_guidelines", 5, "राज्य बीमा Guidelines For Maturity Claims", "सेवानिवृत्ति पर राज्य बीमा परिपक्वता दावा ऑनलाइन निस्तारण दिशा-निर्देश", FormatCategory.SI_FORMS, DocumentFileType.PDF, "SI Guidelines • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE),
        FormatItem("si_06_ddo_instructions", 6, "SIPF - DDO Instructions", "SIPF पोर्टल पर आहरण एवं वितरण अधिकारी (DDO) हेतु महत्वपूर्ण निर्देश", FormatCategory.SI_FORMS, DocumentFileType.PDF, "DDO Instructions • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE)
    )

    val schoolItems: List<FormatItem> = listOf(
        FormatItem("sch_01_ga13_pdf", 1, "कार्यभार हस्तान्तरण-प्रपत्र PDF FORMAT (GA-13)", "अधिकारी/कार्मिक कार्यभार ग्रहण व मुक्ति हस्तांतरण प्रमाण-पत्र (GA-13 PDF)", FormatCategory.SCHOOL_RELATED, DocumentFileType.PDF, "Form GA-13 • PDF", BuilderTemplateType.GA_13_PDF),
        FormatItem("sch_02_ga13_excel", 2, "कार्यभार हस्तान्तरण-प्रपत्र EXCEL FORMAT (GA-13)", "संपादित करने योग्य GA-13 कार्यभार हस्तांतरण एक्सेल प्रारूप", FormatCategory.SCHOOL_RELATED, DocumentFileType.EXCEL, "Form GA-13 • Excel", BuilderTemplateType.GA_13_EXCEL),
        FormatItem("sch_03_establishment_excel", 3, "विद्यालय संस्थापन प्रपत्र - EXCEL FORMAT (By: Parmanand Meghwal)", "विद्यालय स्वीकृत, कार्यरत एवं रिक्त पदों का संपूर्ण संस्थापन रजिस्टर एक्सेल", FormatCategory.SCHOOL_RELATED, DocumentFileType.EXCEL, "By: Parmanand Meghwal", BuilderTemplateType.SCHOOL_ESTABLISHMENT_EXCEL),
        FormatItem("sch_04_monitoring_prapatra", 4, "School Monitoring Prapatra", "अधिकारियों द्वारा विद्यालय निरीक्षण एवं अवलोकन प्रपत्र (Monitoring Form)", FormatCategory.SCHOOL_RELATED, DocumentFileType.PDF, "Inspection • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE),
        FormatItem("sch_05_cwsn_all_forms", 5, "CWSN ALL FORMS", "दिव्यांग / विशेष आवश्यकता वाले विद्यार्थियों के लिए समस्त CWSN प्रपत्र", FormatCategory.SCHOOL_RELATED, DocumentFileType.PDF, "CWSN Forms • PDF", BuilderTemplateType.ADMISSION_FORM_BLANK),
        FormatItem("sch_06_employee_id_shaladarpan", 6, "Change/Correction employee ID on Shala Darpan", "शाला दर्पण पोर्टल पर कार्मिक की Employee ID संशोधन प्रक्रिया मार्गदर्शिका", FormatCategory.SCHOOL_RELATED, DocumentFileType.PDF, "Shala Darpan • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE),
        FormatItem("sch_07_ddo_dsc_docs", 7, "DDO बदलने पर डिजिटल साइन के लिए आवश्यक दस्तावेज़", "नवीन DDO पदस्थापन पर PayManager/IFMS DSC मैपिंग आवश्यक दस्तावेज सूची", FormatCategory.SCHOOL_RELATED, DocumentFileType.PDF, "DDO DSC • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE)
    )

    val npsOpsItems: List<FormatItem> = listOf(
        FormatItem("nps_01_ops_boards", 1, "OPS for Boards, Corporations & Universities", "बोर्ड, निगम एवं विश्वविद्यालयों में पुरानी पेंशन योजना (OPS) लागू आदेश", FormatCategory.NPS_OPS, DocumentFileType.PDF, "OPS Order • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE),
        FormatItem("nps_02_circular_27012025", 2, "NPS New Circular date-27.01.2025", "एनपीएस एवं जीपीएफ लेजर समायोजन नवीनतम परिपत्र दिनांक 27.01.2025", FormatCategory.NPS_OPS, DocumentFileType.PDF, "27-01-2025 • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE),
        FormatItem("nps_03_notification", 3, "NPS संबंधित अधिसूचना", "राजस्थान सिविल सेवा पेंशन एवं अंशदायी पेंशन नियम संशोधन अधिसूचना", FormatCategory.NPS_OPS, DocumentFileType.PDF, "Notification • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE),
        FormatItem("nps_04_nps_to_ops_guidelines", 4, "NPS की जगह OPS लागू किए जाने पर नवीन दिशा निर्देश परिपत्र", "01-01-2004 के पश्चात नियुक्त कार्मिकों हेतु OPS बहाली व GPF-2004 निर्देश", FormatCategory.NPS_OPS, DocumentFileType.PDF, "OPS Guidelines • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE),
        FormatItem("nps_05_deduction_verification", 5, "NPS राशि की प्रथम कटौती से अन्तिम कटौती तक सत्यापन", "कार्मिक की प्रथम NPS कटौती से मार्च 2022 तक राशि सत्यापन प्रपत्र", FormatCategory.NPS_OPS, DocumentFileType.PDF, "Verification • PDF", BuilderTemplateType.GPF_SI_DECLARATION),
        FormatItem("nps_06_ops_circular_2", 6, "NPS की जगह OPS परिपत्र-2", "OPS विकल्प एवं GPF खाते में राशि हस्तांतरण स्पष्टीकरण परिपत्र-2", FormatCategory.NPS_OPS, DocumentFileType.PDF, "Circular-2 • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE),
        FormatItem("nps_07_ledger_death_claim", 7, "NPS अंशदान लेजर एवं मृत्यु दावा भुगतान परिपत्र", "एनपीएस अंशदान लेजर अद्यतन एवं दिवंगत कार्मिक मृत्यु दावा भुगतान निर्देश", FormatCategory.NPS_OPS, DocumentFileType.PDF, "Death Claim • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE)
    )

    val hitkariItems: List<FormatItem> = listOf(
        FormatItem("hit_01_corona_aid", 1, "कोरोना मृतक कार्मिक 1.50 लाख सहायता", "कोविड-19 से दिवंगत शिक्षा विभाग कार्मिक आश्रित ₹1.50 लाख आर्थिक सहायता", FormatCategory.HITKARI_NIDHI, DocumentFileType.PDF, "Corona Aid • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE),
        FormatItem("hit_02_prof_edu_aid", 2, "बच्चों को व्यावसायिक शिक्षा वित्तीय सहायता अधिसूचना", "शिक्षक संतानों को तकनीकी/व्यावसायिक उच्च शिक्षा हेतु हितकारी निधि सहायता", FormatCategory.HITKARI_NIDHI, DocumentFileType.PDF, "Notification • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE),
        FormatItem("hit_03_foreign_travel", 3, "विदेश यात्रा अनुमति दिशा-निर्देश", "शिक्षा विभाग कार्मिक विदेश यात्रा पूर्वानुमति व अनापत्ति दिशा-निर्देश", FormatCategory.HITKARI_NIDHI, DocumentFileType.PDF, "Foreign Travel • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE),
        FormatItem("hit_04_scholarship_2021", 4, "व्यावसायिक शिक्षा छात्रवृत्ति 2021", "हितकारी निधि व्यावसायिक शिक्षा छात्रवृत्ति नियम व आय सीमा परिपत्र", FormatCategory.HITKARI_NIDHI, DocumentFileType.PDF, "Scholarship • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE),
        FormatItem("hit_05_scholarship_rules", 5, "व्यावसायिक शिक्षा छात्रवृत्ति", "MBBS, B.Tech, B.Ed. आदि पाठ्यक्रम हेतु छात्रवृत्ति पात्रता सूची", FormatCategory.HITKARI_NIDHI, DocumentFileType.PDF, "Scholarship • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE),
        FormatItem("hit_06_daughter_marriage", 6, "कार्मिक पुत्री विवाह आवेदन फॉर्म (बालिका उपहार योजना)", "हितकारी निधि अंतर्गत कार्मिक की पुत्री विवाह आर्थिक सहायता आवेदन पत्र", FormatCategory.HITKARI_NIDHI, DocumentFileType.PDF, "Balika Uphaar • PDF", BuilderTemplateType.HITKARI_BALIKA_UPHAAR),
        FormatItem("hit_07_scholarship_form", 7, "छात्रवृत्ति आवेदन फॉर्म", "हितकारी निधि शिक्षक संतान छात्रवृत्ति ऑनलाइन/ऑफलाइन आवेदन प्रपत्र", FormatCategory.HITKARI_NIDHI, DocumentFileType.PDF, "Application • PDF", BuilderTemplateType.HITKARI_SCHOLARSHIP),
        FormatItem("hit_08_death_financial_aid", 8, "शिक्षा विभाग कार्मिक निधन वित्तीय सहायता", "सेवाकाल में कार्मिक निधन पर आश्रित परिवार को ₹1 लाख तत्काल सहायता प्रपत्र", FormatCategory.HITKARI_NIDHI, DocumentFileType.PDF, "Death Aid • PDF", BuilderTemplateType.HITKARI_BALIKA_UPHAAR),
        FormatItem("hit_09_national_foundation", 9, "राष्ट्रीय शिक्षक कल्याण प्रतिष्ठान", "गंभीर रोग चिकित्सा व संकट सहायता हेतु राष्ट्रीय शिक्षक कल्याण प्रतिष्ठान योजना", FormatCategory.HITKARI_NIDHI, DocumentFileType.PDF, "NFTW • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE),
        FormatItem("hit_10_welfare_details", 10, "हितकारी निधि कल्याणकारी योजना विवरण", "हितकारी निधि की समस्त कल्याणकारी योजनाओं की राशि व नियम सारणी", FormatCategory.HITKARI_NIDHI, DocumentFileType.PDF, "All Schemes • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE)
    )

    val sdmcItems: List<FormatItem> = listOf(
        FormatItem("sdmc_01_new_guidelines_2026", 1, "SMC New Guidelines 2026", "विद्यालय प्रबंधन समिति (SMC) नवीनतम गठन व कार्यकारिणी दिशा-निर्देश 2026", FormatCategory.SDMC_SMC, DocumentFileType.PDF, "2026 Guidelines • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE, "2026"),
        FormatItem("sdmc_02_constitution_word", 2, "नवीन संशोधित संविधान प्रारूप", "SDMC / SMC संस्था पंजीकरण हेतु नवीन संशोधित संविधान प्रारूप (Word)", FormatCategory.SDMC_SMC, DocumentFileType.WORD, "Constitution • Word", BuilderTemplateType.SDMC_CONSTITUTION_WORD),
        FormatItem("sdmc_03_reg_affidavit_word", 3, "SDMC/SMC रजिस्ट्रेशन शपथ पत्र", "सहकारी सोसायटी पंजीयन हेतु अध्यक्ष/सचिव संयुक्त शपथ-पत्र (Word)", FormatCategory.SDMC_SMC, DocumentFileType.WORD, "Affidavit • Word", BuilderTemplateType.SDMC_AFFIDAVIT_WORD),
        FormatItem("sdmc_04_no_amendment_oath", 4, "संविधान संशोधन नहीं होने का शपथ पत्र", "वार्षिक कार्यकारिणी सूची प्रस्तुत करते समय संविधान अपरिवर्तित शपथ-पत्र (Word)", FormatCategory.SDMC_SMC, DocumentFileType.WORD, "Affidavit • Word", BuilderTemplateType.SDMC_AFFIDAVIT_WORD),
        FormatItem("sdmc_05_online_process", 5, "SDMC REGISTRATION ONLINE PROCESS", "SSO के माध्यम से सहकारिता पोर्टल पर SDMC ऑनलाइन पंजीकरण प्रक्रिया", FormatCategory.SDMC_SMC, DocumentFileType.PDF, "Online Guide • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE),
        FormatItem("sdmc_06_section_4a", 6, "धारा 4(क) सूचना नयी कार्यकारिणी रजिस्ट्रेशन", "संस्था रजिस्ट्रेशन अधिनियम धारा 4(क) अंतर्गत नयी कार्यकारिणी सूचना प्रपत्र", FormatCategory.SDMC_SMC, DocumentFileType.PDF, "Section 4(A) • PDF", BuilderTemplateType.SDMC_SECTION_4A),
        FormatItem("sdmc_07_80g_registration", 7, "SDMC REGISTRATION - 80G", "भामाशाहों को आयकर छूट हेतु SDMC का 80G व 12A पंजीकरण दिशा-निर्देश", FormatCategory.SDMC_SMC, DocumentFileType.PDF, "80G Guide • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE),
        FormatItem("sdmc_08_structure_2015", 8, "SDMC संरचना 27-10-2015", "माध्यमिक/उच्च माध्यमिक विद्यालय SDMC संरचना व सदस्य विवरण आदेश", FormatCategory.SDMC_SMC, DocumentFileType.PDF, "27-10-2015 • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE, "27-10-2015"),
        FormatItem("sdmc_09_guidelines_21012015", 9, "SMC/SDMC दिशा निर्देश 21-01-2015", "SMC एवं SDMC संचालन, बैठक कोरम एवं वित्तीय अधिकार दिशा-निर्देश", FormatCategory.SDMC_SMC, DocumentFileType.PDF, "21-01-2015 • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE, "21-01-2015"),
        FormatItem("sdmc_10_chhatra_vikas_kosh", 10, "छात्रकोष-विकास कोष 27-02-2015", "छात्रकोष व विकास कोष संचालन, रसीद बुक एवं व्यय सीमा नियम आदेश", FormatCategory.SDMC_SMC, DocumentFileType.PDF, "27-02-2015 • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE, "27-02-2015"),
        FormatItem("sdmc_11_smc_gathan", 11, "SMC गठन दिशा निर्देश", "प्रारंभिक शिक्षा RTE अंतर्गत SMC गठन व अभिभावक निर्वाचन निर्देश", FormatCategory.SDMC_SMC, DocumentFileType.PDF, "SMC Formation • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE),
        FormatItem("sdmc_12_reg_process", 12, "SMC/SDMC Registration Process", "संस्था पंजीकरण के संपूर्ण आवश्यक दस्तावेजों की चेकलिस्ट व प्रक्रिया", FormatCategory.SDMC_SMC, DocumentFileType.PDF, "Checklist • PDF", BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE)
    )
}
