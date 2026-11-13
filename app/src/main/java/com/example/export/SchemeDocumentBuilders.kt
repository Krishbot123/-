package com.example.export

import com.example.data.CalculationEngine
import com.example.model.BuilderTemplateType
import com.example.model.DocumentSection
import com.example.model.FormatFormData
import com.example.model.FormatItem
import com.example.model.GeneratedDocumentContent
import java.util.Locale

object SchemeDocumentBuilders {

    fun build(
        format: FormatItem,
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent? {
        return when (format.templateType) {
            BuilderTemplateType.GPF_INDEMNITY_BOND -> buildGpfIndemnityBond(data, dept, subOffice, refLine)
            BuilderTemplateType.MDM_DEMAND_FORM -> buildMdmDemandForm(data, dept, subOffice, refLine)
            BuilderTemplateType.MDM_ALL_UC -> buildMdmAllUc(data, dept, subOffice, refLine)
            BuilderTemplateType.GPA_NOMINATION,
            BuilderTemplateType.GPA_DEDUCTION_EXCEL,
            BuilderTemplateType.GPA_COVERING_WORD,
            BuilderTemplateType.GPA_CLAIM -> buildGpaDoc(format, data, dept, subOffice, refLine)
            BuilderTemplateType.SI_FIRST_DECL,
            BuilderTemplateType.SI_GA79,
            BuilderTemplateType.SI_NOMINATION,
            BuilderTemplateType.SI_CLAIM -> buildSiDoc(format, data, dept, subOffice, refLine)
            BuilderTemplateType.SDMC_AFFIDAVIT_WORD,
            BuilderTemplateType.SDMC_CONSTITUTION_WORD,
            BuilderTemplateType.SDMC_SECTION_4A -> buildSdmcDoc(format, data, dept, subOffice, refLine)
            BuilderTemplateType.HITKARI_BALIKA_UPHAAR,
            BuilderTemplateType.HITKARI_SCHOLARSHIP -> buildHitkariDoc(format, data, dept, subOffice, refLine)
            BuilderTemplateType.ADMISSION_FORM_BLANK,
            BuilderTemplateType.SPORTS_FEE_FORM,
            BuilderTemplateType.HLB_RECORD_EXCEL,
            BuilderTemplateType.HOUSEHOLD_SURVEY_EXCEL -> buildSchoolFormsDoc(format, data, dept, subOffice, refLine)
            BuilderTemplateType.GA_13_PDF,
            BuilderTemplateType.GA_13_EXCEL,
            BuilderTemplateType.SCHOOL_ESTABLISHMENT_EXCEL -> buildGa13OrEstablishmentDoc(format, data, dept, subOffice, refLine)
            else -> null
        }
    }

    private fun buildGpfIndemnityBond(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        return GeneratedDocumentContent(
            fileBaseName = "GPF_Indemnity_Bond_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "सामान्य प्रावधायी निधि (GPF) क्षतिपूर्ति बंध-पत्र (Indemnity Bond)",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "क्षतिपूर्ति बांड प्रपत्र",
                    bodyLines = listOf(
                        "यह क्षतिपूर्ति बंध-पत्र श्री/श्रीमती ${data.employeeName} (आश्रित/कार्मिक), पिता/पति ${data.fatherOrHusbandName}, Employee ID ${data.employeeId}, GPF खाता संख्या ${data.gpfAccountNo} द्वारा महामहिम राज्यपाल महोदय, राजस्थान सरकार के पक्ष में निष्पादित किया जाता है।",
                        "यदि GPF अंतिम भुगतान / दावा राशि के निस्तारण के पश्चात किसी अन्य वैध उत्तराधिकारी द्वारा दावा प्रस्तुत किया जाता है या कोई अधिक भुगतान पाया जाता है तो मैं संपूर्ण राशि ब्याज सहित राजकीय कोष में जमा कराने के लिए बाध्य रहूँगा/रहूँगी।"
                    ),
                    tableHeaders = listOf("विवरण", "प्रविष्टि"),
                    tableRows = listOf(
                        listOf("कार्मिक का नाम व ID", "${data.employeeName} (${data.employeeId})"),
                        listOf("GPF खाता / SIPF नंबर", data.gpfAccountNo),
                        listOf("नॉमिनी का नाम व संबंध", "${data.nomineeName} (${data.nomineeRelation})"),
                        listOf("कार्यालय / विद्यालय", "${data.schoolName}, ${data.district}")
                    )
                )
            ),
            leftFooter = "गवाह 1 व 2 के हस्ताक्षर",
            rightFooter = "निष्पादनकर्ता हस्ताक्षर\n${data.employeeName}"
        )
    }

    private fun buildMdmDemandForm(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val mdm = CalculationEngine.calculateMdm(data)
        return GeneratedDocumentContent(
            fileBaseName = "MDM_Demand_Form_${data.udiseCode}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "मांग प्रपत्र - पोषाहार (MDM) एवं कुक कम हेल्पर्स मानदेय हेतु",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "मासिक खाद्यान्न एवं कुकिंग कन्वर्जन मांग (${data.mdmMonth})",
                    tableHeaders = listOf("वर्ग", "छात्र संख्या", "कार्यदिवस", "खाद्यान्न मांग", "कन्वर्जन राशि"),
                    tableRows = listOf(
                        listOf("कक्षा 1 से 5", data.mdmPrimaryCount, data.mdmWorkingDays, "${String.format(Locale.US, "%.2f", mdm.primaryGrainKg)} किग्रा", "₹${String.format(Locale.US, "%.2f", mdm.primaryCookingCost)}"),
                        listOf("कक्षा 6 से 8", data.mdmUpperPrimaryCount, data.mdmWorkingDays, "${String.format(Locale.US, "%.2f", mdm.upperPrimaryGrainKg)} किग्रा", "₹${String.format(Locale.US, "%.2f", mdm.upperPrimaryCookingCost)}"),
                        listOf("कुल मांग", "${(data.mdmPrimaryCount.toIntOrNull() ?: 0) + (data.mdmUpperPrimaryCount.toIntOrNull() ?: 0)}", data.mdmWorkingDays, "${String.format(Locale.US, "%.2f", mdm.totalGrainKg)} किग्रा", "₹${String.format(Locale.US, "%.2f", mdm.totalCookingCost)}")
                    )
                ),
                DocumentSection(
                    heading = "कुक कम हेल्पर मानदेय प्रमाण-पत्र",
                    bodyLines = listOf(
                        "प्रमाणित किया जाता है कि विद्यालय ${data.schoolName} (UDISE: ${data.udiseCode}) में कार्यरत कुक कम हेल्पर्स द्वारा माह ${data.mdmMonth} में नियमित रूप से मध्यान्ह भोजन पकाया गया है। मानदेय राशि बैंक खाते में अंतरित करने की अनुशंसा की जाती है।"
                    )
                )
            ),
            leftFooter = "MDM प्रभारी अध्यापक\n${data.employeeName}",
            rightFooter = "संस्था प्रधान व मुहर\n${data.schoolName}"
        )
    }

    private fun buildMdmAllUc(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val mdm = CalculationEngine.calculateMdm(data)
        return GeneratedDocumentContent(
            fileBaseName = "MDM_ALL_UC_${data.udiseCode}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "MDM ALL UC FORMATS — समेकित उपयोगिता प्रमाण-पत्र",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "सत्र ${data.academicSession} / माह ${data.mdmMonth} उपयोगिता विवरण",
                    tableHeaders = listOf("मद", "विवरण / राशि"),
                    tableRows = listOf(
                        listOf("प्राथमिक व उच्च प्राथमिक कुल भोजन", "${mdm.primaryStudentMeals + mdm.upperPrimaryStudentMeals} छात्र दिवस"),
                        listOf("कुल व्यय खाद्यान्न (गेहूं/चावल)", "${String.format(Locale.US, "%.2f", mdm.totalGrainKg)} किग्रा"),
                        listOf("कुल कुकिंग कन्वर्जन व्यय", "₹${String.format(Locale.US, "%.2f", mdm.totalCookingCost)}"),
                        listOf("UDISE एवं बैंक खाता सत्यापन", "${data.udiseCode} — सत्यापित")
                    )
                )
            ),
            leftFooter = "अध्यक्ष SMC/SDMC",
            rightFooter = "प्रधानाध्यापक / प्रधानाचार्य\n${data.ddoName}"
        )
    }

    private fun buildGpaDoc(
        format: FormatItem,
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val isCovering = format.templateType == BuilderTemplateType.GPA_COVERING_WORD
        val isClaim = format.templateType == BuilderTemplateType.GPA_CLAIM
        return GeneratedDocumentContent(
            fileBaseName = "GPA_${format.templateType.name}_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = format.title,
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = if (isCovering) "प्रेषण पत्र — श्रीमान संयुक्त निदेशक, राज्य बीमा एवं प्रावधायी निधि विभाग, ${data.district}"
                    else if (isClaim) "सामूहिक व्यक्तिगत दुर्घटना बीमा (GPA) दावा आवेदन विवरण"
                    else "GPA नामांकन एवं प्रीमियम स्लैब विवरण",
                    bodyLines = if (isCovering) listOf(
                        "विषय: विद्यालय ${data.schoolName} के समस्त कार्मिकों के सामूहिक दुर्घटना बीमा (GPA) प्रस्ताव एवं कटौती शेड्यूल प्रेषित करने बाबत।",
                        "महोदय, उपरोक्त विषयांतर्गत लेख है कि इस कार्यालय के समस्त कार्मिकों के GPA नामांकन प्रपत्र एवं अप्रैल माह की वेतन कटौती सूची संलग्न कर आवश्यक कार्यवाही हेतु सादर प्रेषित है।"
                    ) else emptyList(),
                    tableHeaders = listOf("विवरण", "प्रविष्टि"),
                    tableRows = listOf(
                        listOf("कार्मिक का नाम व ID", "${data.employeeName} (${data.employeeId})"),
                        listOf("पद व मोबाइल", "${data.designation} | ${data.mobile}"),
                        listOf("चयनित GPA प्रीमियम स्लैब", data.gpaPremiumSlab),
                        listOf("नॉमिनी का नाम", data.nomineeName),
                        listOf("नॉमिनी से संबंध व आयु", "${data.nomineeRelation} (${data.nomineeAge} वर्ष)"),
                        listOf("नामांकन अंश (%)", "${data.nomineeSharePercent}%")
                    )
                )
            ),
            leftFooter = "हस्ताक्षर कार्मिक\n${data.employeeName}",
            rightFooter = "डीडीओ सत्यापन व मुहर\n${data.ddoName}"
        )
    }

    private fun buildSiDoc(
        format: FormatItem,
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        return GeneratedDocumentContent(
            fileBaseName = "SI_${format.templateType.name}_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = format.title,
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "राज्य बीमा एवं प्रावधायी निधि (SIPF) प्रपत्र",
                    tableHeaders = listOf("बिंदु", "प्रविष्टि"),
                    tableRows = listOf(
                        listOf("बीमाधारक का नाम व पिता/पति", "${data.employeeName} / ${data.fatherOrHusbandName}"),
                        listOf("Employee ID & SI Policy No.", "${data.employeeId} | ${data.gpfSiPolicyNo}"),
                        listOf("जन्मतिथि एवं नियुक्ति तिथि", "${data.dob} | ${data.joiningDate}"),
                        listOf("पद, लेवल व मूल वेतन", "${data.designation} (${data.oldPayLevel} - ₹${data.oldBasicPay})"),
                        listOf("नॉमिनी नाम व संबंध", "${data.nomineeName} (${data.nomineeRelation}, ${data.nomineeSharePercent}%)"),
                        listOf("स्वस्थ होने का चिकित्सा प्रमाण (GA-79)", "पूर्णतः स्वस्थ — कोई गंभीर रोग नहीं")
                    )
                ),
                DocumentSection(
                    heading = "कार्मिक घोषणा",
                    bodyLines = listOf(
                        "मैं सत्यनिष्ठा से घोषणा करता/करती हूँ कि उपरोक्त सूचनाएं एवं स्वास्थ्य/नामांकन विवरण पूर्णतः सत्य हैं तथा राज्य बीमा नियम के अंतर्गत कटौती स्वीकार करता/करती हूँ।"
                    )
                )
            ),
            leftFooter = "घोषणाकर्ता हस्ताक्षर\n${data.employeeName}",
            rightFooter = "आहरण एवं वितरण अधिकारी\n${data.ddoName}"
        )
    }

    private fun buildSdmcDoc(
        format: FormatItem,
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val isConstitution = format.templateType == BuilderTemplateType.SDMC_CONSTITUTION_WORD
        val isSection4A = format.templateType == BuilderTemplateType.SDMC_SECTION_4A
        return GeneratedDocumentContent(
            fileBaseName = "SDMC_${format.templateType.name}_${data.udiseCode}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = format.title,
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = when {
                        isConstitution -> "विद्यालय विकास एवं प्रबंधन समिति (SDMC) संविधान प्रारूप"
                        isSection4A -> "राजस्थान संस्था रजिस्ट्रीकरण अधिनियम 1958 — धारा 4(क) सूचना"
                        else -> "SDMC / SMC रजिस्ट्रेशन शपथ-पत्र"
                    },
                    bodyLines = listOf(
                        "संस्था का नाम: विद्यालय विकास एवं प्रबंधन समिति (SDMC/SMC), ${data.schoolName}, ब्लॉक ${data.block}, जिला ${data.district} (UDISE: ${data.udiseCode})।",
                        "पंजीयन संख्या: ${data.sdmcRegistrationNo} | सत्र: ${data.academicSession}",
                        if (isConstitution) {
                            "1. उद्देश्य: विद्यालय के शैक्षिक, भौतिक एवं सह-शैक्षिक विकास हेतु जनसहयोग प्राप्त करना व समग्र शिक्षा अनुदान का पारदर्शी उपयोग करना।\n2. कार्यकारिणी: पदेन अध्यक्ष (संस्था प्रधान), सदस्य सचिव व निर्वाचित अभिभावक/भामाशाह सदस्य।"
                        } else if (isSection4A) {
                            "सहकारिता रजिस्ट्रार महोदय, ${data.district} को धारा 4(क) के तहत नवगठित कार्यकारिणी सत्र ${data.academicSession} की सूची अनुमोदन हेतु प्रेषित की जाती है।"
                        } else {
                            "हम ${data.sdmcPresidentName} (अध्यक्ष) एवं ${data.sdmcSecretaryName} (सचिव) सत्यनिष्ठापूर्वक शपथ लेते हैं कि संस्था के संविधान में कोई अनधिकृत संशोधन नहीं किया गया है तथा प्रस्तुत कार्यकारिणी सूची पूर्णतः वैध है।"
                        }
                    ),
                    tableHeaders = listOf("पदाधिकारी पद", "नाम / विवरण"),
                    tableRows = listOf(
                        listOf("अध्यक्ष (पदेन संस्था प्रधान)", data.sdmcPresidentName),
                        listOf("सदस्य सचिव", data.sdmcSecretaryName),
                        listOf("पंजीयन क्रमांक", data.sdmcRegistrationNo),
                        listOf("विद्यालय व जिला", "${data.schoolName}, ${data.district}")
                    )
                )
            ),
            leftFooter = "हस्ताक्षर सचिव\n${data.sdmcSecretaryName}",
            rightFooter = "हस्ताक्षर अध्यक्ष व मुहर\n${data.sdmcPresidentName}"
        )
    }

    private fun buildHitkariDoc(
        format: FormatItem,
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val isMarriage = format.templateType == BuilderTemplateType.HITKARI_BALIKA_UPHAAR
        return GeneratedDocumentContent(
            fileBaseName = "Hitkari_Nidhi_${data.employeeId}",
            headerDepartment = "राजस्थान शिक्षा विभाग — राज्य स्तरीय शिक्षक हितकारी निधि, बीकानेर",
            headerSubOffice = subOffice,
            documentTitle = format.title,
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = if (isMarriage) "बालिका उपहार / आर्थिक सहायता आवेदन पत्र" else "हितकारी निधि छात्रवृत्ति आवेदन पत्र",
                    tableHeaders = listOf("क्र.", "बिंदु", "विवरण"),
                    tableRows = listOf(
                        listOf("1", "कार्मिक का नाम व ID", "${data.employeeName} (${data.employeeId})"),
                        listOf("2", "पद व विद्यालय", "${data.designation}, ${data.schoolName}"),
                        listOf("3", if (isMarriage) "पुत्री / आश्रित का नाम" else "विद्यार्थी का नाम", data.hitkariBeneficiaryName),
                        listOf("4", if (isMarriage) "विवाह / घटना दिनांक" else "उत्तीर्ण कक्षा / पाठ्यक्रम", if (isMarriage) data.hitkariEventDate else "${data.acquiredDegree} (${data.universityName})"),
                        listOf("5", "सत्र व मोबाइल नंबर", "${data.academicSession} | ${data.mobile}")
                    )
                ),
                DocumentSection(
                    heading = "कार्यालयध्यक्ष की अनुशंसा",
                    bodyLines = listOf(
                        "प्रमाणित किया जाता है कि कार्मिक शिक्षा विभाग के नियमित अंशदायी सदस्य हैं एवं प्रस्तुत प्रपत्र नियमानुसार स्वीकृति हेतु अनुशंसित किया जाता है।"
                    )
                )
            ),
            leftFooter = "आवेदक कार्मिक\n${data.employeeName}",
            rightFooter = "डीडीओ / पीईईओ अनुशंसा\n${data.ddoName}"
        )
    }

    private fun buildSchoolFormsDoc(
        format: FormatItem,
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        return GeneratedDocumentContent(
            fileBaseName = "School_Format_${data.udiseCode}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = format.title,
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "विद्यालय एवं विद्यार्थी प्रपत्र विवरण",
                    tableHeaders = listOf("विवरण", "प्रविष्टि"),
                    tableRows = listOf(
                        listOf("विद्यालय का नाम व UDISE", "${data.schoolName} (${data.udiseCode})"),
                        listOf("विद्यार्थी का नाम व पिता का नाम", "${data.studentName} / ${data.studentFatherName}"),
                        listOf("कक्षा व SR क्रमांक", "${data.studentClass} | SR: ${data.srNumber}"),
                        listOf("शैक्षणिक सत्र", data.academicSession),
                        listOf("जन्मतिथि व मोबाइल", "${data.dob} | ${data.mobile}"),
                        listOf("प्रभारी शिक्षक", "${data.employeeName} (${data.designation})")
                    )
                )
            ),
            leftFooter = "प्रभारी / अभिभावक हस्ताक्षर",
            rightFooter = "प्रधानाचार्य / संस्था प्रधान\n${data.schoolName}"
        )
    }

    private fun buildGa13OrEstablishmentDoc(
        format: FormatItem,
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val isEst = format.templateType == BuilderTemplateType.SCHOOL_ESTABLISHMENT_EXCEL
        return GeneratedDocumentContent(
            fileBaseName = if (isEst) "School_Establishment_${data.udiseCode}" else "GA_13_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = format.title,
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = if (isEst) "विद्यालय संस्थापन रजिस्टर (स्वीकृत / कार्यरत / रिक्त पद)" else "कार्यभार हस्तांतरण प्रमाण-पत्र (GA-13)",
                    tableHeaders = listOf("क्र.", "कार्मिक / पदनाम", "पे-लेवल व बेसिक", "तिथि / स्थिति"),
                    tableRows = listOf(
                        listOf("1", "${data.employeeName} (${data.designation})", "${data.oldPayLevel} — ₹${data.oldBasicPay}", "कार्यरत (${data.joiningDate})"),
                        listOf("2", "विषय: ${data.subject} | ID: ${data.employeeId}", "UDISE: ${data.udiseCode}", data.effectiveDate)
                    )
                )
            ),
            leftFooter = "संस्थापन लिपिक / कार्मिक",
            rightFooter = "संस्था प्रधान / पीईईओ\n${data.ddoName}"
        )
    }
}
