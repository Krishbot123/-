package com.example.export

import com.example.data.CalculationEngine
import com.example.model.BuilderTemplateType
import com.example.model.DocumentSection
import com.example.model.FormatFormData
import com.example.model.FormatItem
import com.example.model.GeneratedDocumentContent
import java.util.Locale

object DocumentContentBuilder {

    fun build(format: FormatItem, data: FormatFormData): GeneratedDocumentContent {
        val dept = "कार्यालय ${data.ddoName} (${data.schoolName})"
        val subOffice = "ब्लॉक: ${data.block}, जिला: ${data.district} (राज.) • UDISE: ${data.udiseCode}"
        val refLine = "क्रमांक: ${data.orderNumber}    |    दिनांक: ${data.orderDate}"

        return when (format.templateType) {
            BuilderTemplateType.ARREAR_SHEET,
            BuilderTemplateType.AUTO_SALARY_DA_ARREAR -> buildArrearDoc(format, data, dept, subOffice, refLine)

            BuilderTemplateType.FIXATION_ORDER -> buildFixationOrder(data, dept, subOffice, refLine)

            BuilderTemplateType.ACP_FITMENT_ORDER -> buildAcpFitmentOrder(data, dept, subOffice, refLine)

            BuilderTemplateType.ACP_FORM_3RD_GRADE,
            BuilderTemplateType.ACP_FORM_2ND_GRADE -> buildAcpApplicationForm(format, data, dept, subOffice, refLine)

            BuilderTemplateType.STHAIKARAN_3RD_GRADE,
            BuilderTemplateType.STHAIKARAN_RPSC,
            BuilderTemplateType.STHAIKARAN_LECTURER -> buildSthaikaranForm(format, data, dept, subOffice, refLine)

            BuilderTemplateType.VRS_APPLICATION -> buildVrsApplication(data, dept, subOffice, refLine)

            BuilderTemplateType.RETIREMENT_OATH_6 -> buildRetirementOath6(data, dept, subOffice, refLine)

            BuilderTemplateType.GA_141_CONTINUOUS_SERVICE -> buildGa141Certificate(data, dept, subOffice, refLine)

            BuilderTemplateType.SATISFACTORY_SERVICE -> buildSatisfactoryCertificate(data, dept, subOffice, refLine)

            BuilderTemplateType.QUALIFICATION_UPGRADE_AB -> buildQualificationUpgradeAB(data, dept, subOffice, refLine)

            BuilderTemplateType.QUALIFICATION_SENIORITY_EDIT -> buildQualificationSeniorityForm(data, dept, subOffice, refLine)

            BuilderTemplateType.ACP_CHILDREN_DECLARATION -> buildChildrenDeclaration(data, dept, subOffice, refLine)

            BuilderTemplateType.INCOME_TAX_CALC -> buildIncomeTaxDoc(data, dept, subOffice, refLine)

            BuilderTemplateType.PL_SURRENDER -> buildPlSurrenderDoc(data, dept, subOffice, refLine)

            BuilderTemplateType.MDM_MONTHLY -> buildMdmDoc(data, dept, subOffice, refLine)

            BuilderTemplateType.ANNUAL_INCREMENT -> buildAnnualIncrementDoc(data, dept, subOffice, refLine)

            BuilderTemplateType.JOINING_RELIEVING_GA13 -> buildJoiningGa13Doc(data, dept, subOffice, refLine)

            BuilderTemplateType.LEAVE_APPLICATION -> buildLeaveApplicationDoc(data, dept, subOffice, refLine)

            BuilderTemplateType.LPC_CERTIFICATE -> buildLpcCertificateDoc(data, dept, subOffice, refLine)

            BuilderTemplateType.STUDENT_STUDY_CHAR -> buildStudentCertificateDoc(data, dept, subOffice, refLine)

            BuilderTemplateType.SCHOOL_GRANT_UC -> buildSchoolGrantUcDoc(data, dept, subOffice, refLine)

            BuilderTemplateType.GPF_SI_DECLARATION -> buildGpfSiDeclarationDoc(data, dept, subOffice, refLine)

            else -> {
                SchemeDocumentBuilders.build(format, data, dept, subOffice, refLine)
                    ?: OfficeRuleDocumentBuilders.build(format, data, dept, subOffice, refLine)
                    ?: buildGpfSiDeclarationDoc(data, dept, subOffice, refLine)
            }
        }
    }

    private fun buildArrearDoc(
        format: FormatItem,
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val summary = CalculationEngine.calculateArrear(data)
        val tableRows = summary.rows.map { r ->
            listOf(
                r.serial.toString(),
                r.monthLabel,
                "${r.drawnBasic}+${r.drawnDa}+${r.drawnHra}=${r.drawnGross}",
                "${r.dueBasic}+${r.dueDa}+${r.dueHra}=${r.dueGross}",
                "₹${r.grossDifference}",
                "₹${r.npsDeduction}",
                "₹${r.netPayable}"
            )
        } + listOf(
            listOf(
                "",
                "कुल योग (TOTAL)",
                "₹${summary.totalDrawn}",
                "₹${summary.totalDue}",
                "₹${summary.totalGrossDifference}",
                "₹${summary.totalNpsDeduction}",
                "₹${summary.totalNetPayable}"
            )
        )

        return GeneratedDocumentContent(
            fileBaseName = sanitizeName(format.title, data.employeeId),
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "${format.title} — एरियर गणना विवरण (7th Pay Matrix)",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "कार्मिक एवं वेतन विवरण",
                    bodyLines = listOf(
                        "कार्मिक का नाम: ${data.employeeName} (Employee ID: ${data.employeeId})",
                        "पद एवं विषय: ${data.designation} (${data.subject})",
                        "पूर्व वेतन (Drawn): लेवल ${data.oldPayLevel}, मूल वेतन ₹${data.oldBasicPay}, DA ${data.oldDaPercent}%, HRA ${data.hraPercent}%",
                        "देय वेतन (Due): लेवल ${data.newPayLevel}, मूल वेतन ₹${data.newBasicPay}, DA ${data.newDaPercent}%, HRA ${data.hraPercent}%",
                        "एरियर अवधि: ${data.arrearStartMonth} से ${data.arrearMonths} माह तक"
                    )
                ),
                DocumentSection(
                    heading = "माहवार एरियर गणना तालिका (Drawn vs Due Statement)",
                    tableHeaders = listOf("क्र.", "माह", "आहरित (Drawn)", "देय (Due)", "अंतर", "NPS/GPF", "शुद्ध देय"),
                    tableRows = tableRows
                ),
                DocumentSection(
                    heading = "सत्यापन",
                    bodyLines = listOf(
                        "कुल शुद्ध भुगतान योग्य एरियर राशि: ₹${summary.totalNetPayable}/- मात्र।",
                        "प्रमाणित किया जाता है कि उपरोक्त आहरित एवं देय वेतन की गणना सेवा पुस्तिका एवं पे-मैनेजर / IFMS 3.0 अभिलेख के अनुसार सही है।"
                    )
                )
            ),
            leftFooter = "हस्ताक्षर कार्मिक\n(${data.employeeName})",
            rightFooter = "हस्ताक्षर एवं मुहर\n(${data.ddoName})"
        )
    }

    private fun buildFixationOrder(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val nextPay = CalculationEngine.calculateAcpFixationPay(data.oldPayLevel, data.oldBasicPay.toLongOrNull() ?: 39300L, data.newPayLevel)
        return GeneratedDocumentContent(
            fileBaseName = "Lecturer_Fixation_Order_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "कार्यालय आदेश — व्याख्याता वेतन नियतन (Fixation Order)",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "वेतन नियतन आदेश",
                    bodyLines = listOf(
                        "निदेशालय माध्यमिक शिक्षा राजस्थान, बीकानेर के आदेश क्रमांक ${data.previousOrderRef} की अनुपालना में श्री/श्रीमती/सुश्री ${data.employeeName}, ${data.designation} (विषय: ${data.subject}, Employee ID: ${data.employeeId}) का राजस्थान सिविल सेवा (पुनरीक्षित वेतन) नियम 2017 (7th Pay Commission) के अंतर्गत वेतन नियतन दिनांक ${data.effectiveDate} से निम्नानुसार किया जाता है:",
                        "1. कार्मिक का नाम एवं पिता का नाम: ${data.employeeName} / ${data.fatherOrHusbandName}",
                        "2. पदस्थापन विद्यालय: ${data.schoolName}, ब्लॉक ${data.block}, जिला ${data.district}",
                        "3. पूर्व वेतन लेवल एवं मूल वेतन: पे-लेवल ${data.oldPayLevel}, मूल वेतन ₹${data.oldBasicPay}/-",
                        "4. एक वेतन वृद्धि के पश्चात वेतन: ₹${nextPay.first}/-",
                        "5. नवीन नियत पे-लेवल एवं मूल वेतन: पे-लेवल ${data.newPayLevel}, मूल वेतन ₹${data.newBasicPay.ifBlank { nextPay.second.toString() }}/-",
                        "6. आगामी वार्षिक वेतन वृद्धि दिनांक: 01 जुलाई (नियमानुसार)"
                    )
                ),
                DocumentSection(
                    heading = "प्रतिलिपि सूचनार्थ एवं आवश्यक कार्यवाही हेतु",
                    bodyLines = listOf(
                        "1. संबंधित जिला शिक्षा अधिकारी (मुख्यालय) माध्यमिक, ${data.district}।",
                        "2. कोषाधिकारी / उपकोषाधिकारी, ${data.block}।",
                        "3. संबंधित कार्मिक श्री/श्रीमती ${data.employeeName} (सेवा पुस्तिका में इंद्राज हेतु) एवं रक्षित पत्रावली।"
                    )
                )
            ),
            leftFooter = "तैयारकर्ता / लिपिक",
            rightFooter = "आहरण एवं वितरण अधिकारी\n${data.ddoName}"
        )
    }

    private fun buildAcpFitmentOrder(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        return GeneratedDocumentContent(
            fileBaseName = "ACP_Fitment_Order_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "कार्यालय आदेश - एसीपी पर वेतन नियतन (ACP FITMENT ORDER)",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "एसीपी वेतन नियतन विवरण (By: Parmanand Meghwal Format)",
                    bodyLines = listOf(
                        "संदर्भित आदेश क्रमांक ${data.previousOrderRef} के तहत ${data.acpMilestone} स्वीकृत होने के फलस्वरूप कार्मिक का वेतन नियतन राजस्थान सिविल सेवा (पुनरीक्षित वेतन) नियम 2017 के नियम 14/15 के अंतर्गत निम्नानुसार निर्धारित किया जाता है:"
                    ),
                    tableHeaders = listOf("विवरण", "प्रविष्टि"),
                    tableRows = listOf(
                        listOf("कार्मिक का नाम व पद", "${data.employeeName} (${data.designation})"),
                        listOf("Employee ID", data.employeeId),
                        listOf("प्रथम नियुक्ति तिथि", data.joiningDate),
                        listOf("स्वीकृत ACP स्तर", data.acpMilestone),
                        listOf("ACP प्रभावी दिनांक", data.effectiveDate),
                        listOf("विद्यमान पे-लेवल व बेसिक", "लेवल ${data.oldPayLevel} — ₹${data.oldBasicPay}/-"),
                        listOf("नवीन ACP पे-लेवल व नियत बेसिक", "लेवल ${data.newPayLevel} — ₹${data.newBasicPay}/-"),
                        listOf("आगामी वेतन वृद्धि", "01 जुलाई (नियमानुसार)")
                    )
                ),
                DocumentSection(
                    heading = "नोट एवं प्रमाणन",
                    bodyLines = listOf(
                        "संबंधित कार्मिक द्वारा वेतन नियतन का विकल्प निर्धारित समयावधि में प्रस्तुत किया जा चुका है। यदि भविष्य में किसी प्रकार की अधिक राशि का भुगतान होना पाया जाता है तो वसूली योग्य होगी।"
                    )
                )
            ),
            leftFooter = "लिपिक / संस्थापन शाखा",
            rightFooter = "प्रधानाचार्य एवं डीडीओ\n${data.schoolName}"
        )
    }

    private fun buildAcpApplicationForm(
        format: FormatItem,
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val gradeTitle = if (format.templateType == BuilderTemplateType.ACP_FORM_3RD_GRADE) {
            "तृतीय श्रेणी कर्मचारी (3rd Grade)"
        } else {
            "द्वितीय श्रेणी कर्मचारी (2nd Grade / Sr. Teacher)"
        }
        return GeneratedDocumentContent(
            fileBaseName = "ACP_Application_${gradeTitle.take(9)}_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "ACP आवेदन फॉर्म व कार्यालयध्यक्ष का प्रमाण पत्र ($gradeTitle)",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "भाग-1 : कार्मिक द्वारा भरा जाने वाला आवेदन प्रपत्र",
                    tableHeaders = listOf("क्र.", "बिंदु", "विवरण"),
                    tableRows = listOf(
                        listOf("1", "कार्मिक का नाम", data.employeeName),
                        listOf("2", "पिता / पति का नाम", data.fatherOrHusbandName),
                        listOf("3", "पद एवं विषय", "${data.designation} (${data.subject})"),
                        listOf("4", "Employee ID & जन्मतिथि", "${data.employeeId} | ${data.dob}"),
                        listOf("5", "प्रथम नियुक्ति तिथि एवं पद", "${data.joiningDate} (${data.designation})"),
                        listOf("6", "स्थाईकरण तिथि", data.probationCompletionDate),
                        listOf("7", "चाही गई ACP व प्रभावी तिथि", "${data.acpMilestone} — ${data.effectiveDate}"),
                        listOf("8", "वर्तमान वेतन लेवल व मूल वेतन", "${data.oldPayLevel} — ₹${data.oldBasicPay}/-"),
                        listOf("9", "संतान विवरण (01-06-2002)", "पूर्व: ${data.childrenBeforeCutoff}, पश्चात: ${data.childrenAfterCutoff}")
                    )
                ),
                DocumentSection(
                    heading = "भाग-2 : कार्यालयध्यक्ष का संतोषप्रद सेवा प्रमाण-पत्र",
                    bodyLines = listOf(
                        "प्रमाणित किया जाता है कि श्री/श्रीमती ${data.employeeName}, ${data.designation} (ID: ${data.employeeId}) का कार्य एवं आचरण पूर्णतः संतोषप्रद रहा है। इनके विरुद्ध किसी प्रकार की विभागीय जांच (CCA नियम 16/17), अनुशासनात्मक कार्यवाही या न्यायिक प्रकरण लंबित नहीं है तथा इनकी कोई अवैतनिक अवकाश (LWP: ${data.lwpDays} दिवस) देय अवधि को प्रभावित नहीं करती है। इन्हें ${data.acpMilestone} स्वीकृत करने की अनुशंसा की जाती है।"
                    )
                )
            ),
            leftFooter = "आवेदक के हस्ताक्षर\n${data.employeeName}",
            rightFooter = "कार्यालयध्यक्ष हस्ताक्षर व मुहर\n${data.ddoName}"
        )
    }

    private fun buildSthaikaranForm(
        format: FormatItem,
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        return GeneratedDocumentContent(
            fileBaseName = sanitizeName(format.title, data.employeeId),
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = format.title,
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "परिवीक्षाकाल समाप्ति एवं स्थाईकरण प्रपत्र",
                    tableHeaders = listOf("क्र.", "विवरण", "प्रविष्टि"),
                    tableRows = listOf(
                        listOf("1", "कार्मिक का पूरा नाम", data.employeeName),
                        listOf("2", "पिता / पति का नाम", data.fatherOrHusbandName),
                        listOf("3", "पद एवं विषय", "${data.designation} (${data.subject})"),
                        listOf("4", "Employee ID / PAN", "${data.employeeId} / ${data.mobile}"),
                        listOf("5", "चयन / मेरिट क्रमांक व आदेश", "${data.rpscMeritNo} | ${data.previousOrderRef}"),
                        listOf("6", "नियुक्ति पर कार्यग्रहण तिथि", data.joiningDate),
                        listOf("7", "2 वर्ष परिवीक्षा पूर्ण होने की तिथि", data.probationCompletionDate),
                        listOf("8", "परिवीक्षाकाल में उपभोगित असाधारण अवकाश (LWP)", "${data.lwpDays} दिवस"),
                        listOf("9", "स्थाईकरण उपरांत वेतन लेवल व मूल वेतन", "${data.newPayLevel} — ₹${data.newBasicPay}/-"),
                        listOf("10", "वर्तमान पदस्थापन विद्यालय", "${data.schoolName}, ${data.block}, ${data.district}")
                    )
                ),
                DocumentSection(
                    heading = "सत्यापन एवं अनुशंसा",
                    bodyLines = listOf(
                        "प्रमाणित किया जाता है कि कार्मिक ने दो वर्ष का परिवीक्षाकाल संतोषप्रद रूप से पूर्ण कर लिया है। पुलिस चरित्र सत्यापन एवं चिकित्सा स्वास्थ्य प्रमाण-पत्र सेवा पुस्तिका में संलग्न है। विभागीय नियमों के अनुसार दिनांक ${data.probationCompletionDate} से स्थाईकरण एवं नियमित वेतन श्रृंखला स्वीकृत किए जाने की अनुशंसा की जाती है।"
                    )
                )
            ),
            leftFooter = "कार्मिक के हस्ताक्षर\n${data.employeeName}",
            rightFooter = "प्रधानाचार्य / नियंत्रण अधिकारी\n${data.ddoName}"
        )
    }

    private fun buildVrsApplication(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        return GeneratedDocumentContent(
            fileBaseName = "VRS_Application_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "स्वैच्छिक सेवानिवृति लेने हेतु प्रार्थना पत्र (VRS Application)",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "सेवा में,",
                    bodyLines = listOf(
                        "श्रीमान निदेशक / संयुक्त निदेशक / जिला शिक्षा अधिकारी (माध्यमिक / प्रारंभिक),",
                        "राजस्थान शिक्षा विभाग, जिला ${data.district} (द्वारा: ${data.ddoName})",
                        "विषय: राजस्थान सिविल सेवा (पेंशन) नियम 1996 के नियम 50(1) के अंतर्गत स्वैच्छिक सेवानिवृत्ति (VRS) स्वीकृत करने बाबत।",
                        "महोदय,",
                        "विनम्र निवेदन है कि मैं ${data.employeeName}, पद ${data.designation} (Employee ID: ${data.employeeId}) वर्तमान में ${data.schoolName}, ब्लॉक ${data.block}, जिला ${data.district} में कार्यरत हूँ। मेरी प्रथम नियुक्ति तिथि ${data.joiningDate} है तथा मैंने कुल ${data.totalServiceYears} की अर्हक सेवा पूर्ण कर ली है।",
                        "मैं ${data.vrsReason} के कारण दिनांक ${data.proposedVrsDate} (मध्यान्ह पश्चात) से स्वैच्छिक सेवानिवृत्ति लेना चाहता/चाहती हूँ। कृपया इस प्रार्थना-पत्र को तीन माह के पूर्व नोटिस के रूप में स्वीकार कर दिनांक ${data.proposedVrsDate} से मुझे स्वैच्छिक सेवानिवृत्ति प्रदान करने की कृपा करावें।",
                        "संलग्न: 1. अदेयता प्रमाण-पत्र, 2. सेवा विवरण एवं शपथ-पत्र प्रारूप-6।"
                    )
                )
            ),
            leftFooter = "दिनांक: ${data.orderDate}\nस्थान: ${data.block}, ${data.district}",
            rightFooter = "प्रार्थी के हस्ताक्षर\n${data.employeeName}\nID: ${data.employeeId}"
        )
    }

    private fun buildRetirementOath6(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        return GeneratedDocumentContent(
            fileBaseName = "Retirement_Oath_Prarup_6_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "सेवानिवृति हेतु शपथ पत्र प्रारूप - 6",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "शपथ-पत्र (प्रारूप - 6)",
                    bodyLines = listOf(
                        "मैं ${data.employeeName} पुत्र/पत्नी श्री ${data.fatherOrHusbandName}, पद ${data.designation}, कर्मचारी आईडी ${data.employeeId}, निवासी/कार्यरत ${data.schoolName}, ब्लॉक ${data.block}, जिला ${data.district} सत्यनिष्ठापूर्वक शपथ लेता/लेती हूँ कि:",
                        "1. यह कि मेरी जन्मतिथि ${data.dob} एवं प्रथम राजकीय सेवा में नियुक्ति तिथि ${data.joiningDate} है।",
                        "2. यह कि मैं दिनांक ${data.proposedVrsDate} को राजकीय सेवा से सेवानिवृत्त हो रहा/रही हूँ।",
                        "3. यह कि मेरे विरुद्ध वर्तमान में किसी भी प्रकार की विभागीय जांच, एसीबी प्रकरण या न्यायिक वाद लंबित नहीं है।",
                        "4. यह कि यदि मेरे पेंशन प्रकरण के निस्तारण उपरांत किसी भी प्रकार की राजकीय राशि बकाया अथवा अधिक भुगतान होना पाया जाता है, तो मैं उसे राजकीय कोष में जमा कराने अथवा पेंशन/ग्रेच्युटी से समायोजित कराने के लिए सहमत हूँ।",
                        "5. यह कि मेरे द्वारा प्रस्तुत सभी बैंक, पारिवारिक एवं नामांकन विवरण पूर्णतः सत्य एवं प्रमाणित हैं।"
                    )
                )
            ),
            leftFooter = "सत्यापनकर्ता / गवाह",
            rightFooter = "हस्ताक्षर शपथकर्ता\n(${data.employeeName})"
        )
    }

    private fun buildGa141Certificate(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        return GeneratedDocumentContent(
            fileBaseName = "GA_141_Continuous_Service_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "निरंतर एवं योग्य सेवा का प्रमाण-पत्र (Form GA-141)",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "प्रपत्र जी.ए.-141 (निरंतर सेवा सत्यापन)",
                    bodyLines = listOf(
                        "प्रमाणित किया जाता है कि श्री/श्रीमती ${data.employeeName} पुत्र/पत्नी ${data.fatherOrHusbandName}, पद ${data.designation} (Employee ID: ${data.employeeId}) ने दिनांक ${data.joiningDate} से दिनांक ${data.orderDate} तक (कुल अवधि: ${data.totalServiceYears}) राजकीय सेवा में निरंतर एवं योग्य सेवा संपादित की है।",
                        "सेवा पुस्तिका में उपलब्ध अभिलेख के अनुसार उक्त अवधि का सेवा सत्यापन पूर्ण कर लिया गया है तथा इस अवधि में कोई अनियमित व्यवधान (Break in Service) नहीं है। कुल असाधारण अवैतनिक अवकाश (EOL/LWP): ${data.lwpDays} दिवस है।",
                        "यह प्रमाण-पत्र एसीपी / पेंशन / स्थाईकरण प्रकरण हेतु जारी किया जाता है।"
                    )
                )
            ),
            leftFooter = "हस्ताक्षर संस्थापन लिपिक",
            rightFooter = "कार्यालयध्यक्ष हस्ताक्षर व मुहर\n${data.ddoName}"
        )
    }

    private fun buildSatisfactoryCertificate(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        return GeneratedDocumentContent(
            fileBaseName = "Satisfactory_Service_Cert_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "संतोष प्रद सेवा का प्रमाण पत्र",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "कार्यालयध्यक्ष प्रमाण-पत्र",
                    bodyLines = listOf(
                        "प्रमाणित किया जाता है कि श्री/श्रीमती ${data.employeeName}, पद ${data.designation} (विषय: ${data.subject}, Employee ID: ${data.employeeId}) वर्तमान में ${data.schoolName}, ब्लॉक ${data.block}, जिला ${data.district} में कार्यरत हैं।",
                        "कार्मिक की प्रथम नियुक्ति तिथि ${data.joiningDate} से अद्यतन कार्य, व्यवहार, विद्यालय समय की पाबंदी, परीक्षा परिणाम एवं आचरण पूर्णतः संतोषप्रद रहा है। इनके विरुद्ध कोई शिकायत, अनुशासनात्मक जांच या सीसीए नियम 16/17 की कार्यवाही लंबित अथवा विचाराधीन नहीं है।",
                        "इनका सेवा रिकॉर्ड पूर्णतः स्वच्छ होने से स्थाईकरण / एसीपी / पदोन्नति हेतु संस्तुति की जाती है।"
                    )
                )
            ),
            leftFooter = "दिनांक: ${data.orderDate}",
            rightFooter = "हस्ताक्षर कार्यालयध्यक्ष व मुहर\n${data.ddoName}"
        )
    }

    private fun buildQualificationUpgradeAB(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        return GeneratedDocumentContent(
            fileBaseName = "Qualification_Upgrade_Ka_Kha_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "योग्यता अभिवृद्धि हेतु प्रपत्र 'क' एवं 'ख'",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "प्रपत्र 'क' — कार्मिक आवेदन एवं शैक्षणिक योग्यता विवरण",
                    tableHeaders = listOf("विवरण", "प्रविष्टि"),
                    tableRows = listOf(
                        listOf("कार्मिक का नाम व पद", "${data.employeeName} (${data.designation})"),
                        listOf("Employee ID", data.employeeId),
                        listOf("अर्जित नवीन शैक्षणिक योग्यता", data.acquiredDegree),
                        listOf("विश्वविद्यालय / बोर्ड का नाम", data.universityName),
                        listOf("उत्तीर्ण वर्ष", data.examPassingYear),
                        listOf("विभागीय अनुमति आदेश क्रमांक", data.previousOrderRef)
                    )
                ),
                DocumentSection(
                    heading = "प्रपत्र 'ख' — कार्यालयध्यक्ष द्वारा मूल दस्तावेज सत्यापन",
                    bodyLines = listOf(
                        "प्रमाणित किया जाता है कि कार्मिक द्वारा प्रस्तुत नवीन योग्यता (${data.acquiredDegree}, वर्ष ${data.examPassingYear}, विश्वविद्यालय: ${data.universityName}) की मूल अंकतालिका एवं डिग्री का मिलान कर लिया गया है तथा उक्त योग्यता नियमानुसार विभागीय अनुमति / अवकाश के उपरांत अर्जित की गई है। अतः सेवा पुस्तिका एवं शाला दर्पण पोर्टल पर योग्यता अभिवृद्धि अंकन की अनुशंसा की जाती है।"
                    )
                )
            ),
            leftFooter = "हस्ताक्षर कार्मिक\n${data.employeeName}",
            rightFooter = "सत्यापनकर्ता अधिकारी व मुहर\n${data.ddoName}"
        )
    }

    private fun buildQualificationSeniorityForm(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        return GeneratedDocumentContent(
            fileBaseName = "Seniority_Qualification_Edit_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "योग्यता अभिवृद्धि वरिष्ठता सूची नामांकन संशोधन हेतु आवेदन फॉर्म",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "वरिष्ठता सूची में योग्यता संशोधन आवेदन",
                    bodyLines = listOf(
                        "सेवा में, श्रीमान संयुक्त निदेशक / जिला शिक्षा अधिकारी (माध्यमिक/प्रारंभिक), जिला ${data.district}।",
                        "विषय: अस्थाई/स्थाई वरिष्ठता सूची में नवीन अर्जित शैक्षणिक/प्रशैक्षणिक योग्यता जोड़ने व नामांकन संशोधन करने बाबत।"
                    ),
                    tableHeaders = listOf("बिंदु", "विवरण"),
                    tableRows = listOf(
                        listOf("कार्मिक का नाम एवं ID", "${data.employeeName} (${data.employeeId})"),
                        listOf("पद व विषय", "${data.designation} (${data.subject})"),
                        listOf("वर्तमान वरिष्ठता क्रमांक", data.senioritySerialNo),
                        listOf("संशोधित / जोड़ी जाने वाली योग्यता", "${data.acquiredDegree} (${data.examPassingYear})"),
                        listOf("विश्वविद्यालय", data.universityName),
                        listOf("विद्यालय एवं ब्लॉक", "${data.schoolName}, ${data.block}")
                    )
                ),
                DocumentSection(
                    heading = "संलग्न दस्तावेज",
                    bodyLines = listOf(
                        "1. स्वप्रमाणित अंकतालिका व डिग्री प्रति, 2. विभागीय अध्ययन अनुमति आदेश, 3. सेवा पुस्तिका प्रविष्टि पृष्ठ प्रति, 4. प्रपत्र 'क' व 'ख'।"
                    )
                )
            ),
            leftFooter = "प्रार्थी के हस्ताक्षर\n${data.employeeName}",
            rightFooter = "अग्रसारित कार्यालयध्यक्ष\n${data.ddoName}"
        )
    }

    private fun buildChildrenDeclaration(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        return GeneratedDocumentContent(
            fileBaseName = "ACP_Children_Declaration_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "ACP - संतान संबंधी घोषणा पत्र (Children Declaration)",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "संतान संबंधी घोषणा-पत्र",
                    bodyLines = listOf(
                        "मैं ${data.employeeName} पुत्र/पत्नी श्री ${data.fatherOrHusbandName}, पद ${data.designation}, Employee ID ${data.employeeId}, कार्यरत विद्यालय ${data.schoolName}, ब्लॉक ${data.block}, जिला ${data.district} एतद्वारा सत्यनिष्ठापूर्वक घोषणा करता/करती हूँ कि:",
                        "1. दिनांक 01-06-2002 से पूर्व जीवित संतानों की संख्या: ${data.childrenBeforeCutoff}",
                        "2. दिनांक 01-06-2002 एवं उसके पश्चात जीवित संतानों की संख्या: ${data.childrenAfterCutoff}",
                        "3. कुल जीवित संतानों का नाम व जन्मतिथि विवरण: ${data.childrenDetails}",
                        "4. यह कि मैंने दिनांक 01-06-2002 के पश्चात दो से अधिक संतान उत्पन्न नहीं की है तथा यदि मेरी यह घोषणा असत्य पाई जाती है तो विभाग द्वारा स्वीकृत ACP / पदोन्नति निरस्त कर नियमानुसार कार्यवाही की जा सकेगी।"
                    )
                )
            ),
            leftFooter = "दिनांक: ${data.orderDate}\nस्थान: ${data.block}",
            rightFooter = "हस्ताक्षर कार्मिक\n${data.employeeName}"
        )
    }

    private fun buildIncomeTaxDoc(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val tax = CalculationEngine.calculateIncomeTax(data)
        return GeneratedDocumentContent(
            fileBaseName = "Income_Tax_Sheet_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "आयकर गणना प्रपत्र — ${tax.financialYear}",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "करदाता विवरण",
                    bodyLines = listOf(
                        "नाम: ${data.employeeName} | पद: ${data.designation} | Employee ID: ${data.employeeId}",
                        "वित्तीय वर्ष: ${tax.financialYear} | सकल वार्षिक वेतन (Gross Salary): ₹${tax.grossIncome}/-"
                    )
                ),
                DocumentSection(
                    heading = "Old Regime vs New Regime तुलनात्मक तालिका",
                    tableHeaders = listOf("विवरण", "पुरानी व्यवस्था (Old)", "नई व्यवस्था (New)"),
                    tableRows = listOf(
                        listOf("सकल वेतन (Gross Income)", "₹${tax.grossIncome}", "₹${tax.grossIncome}"),
                        listOf("कुल कटौतियां एवं मानक छूट", "₹${tax.oldTotalDeductions}", "₹${tax.newStandardDeduction}"),
                        listOf("कर योग्य आय (Taxable)", "₹${tax.oldTaxableIncome}", "₹${tax.newTaxableIncome}"),
                        listOf("आयकर (87A छूट उपरांत)", "₹${tax.oldBaseTax}", "₹${tax.newBaseTax}"),
                        listOf("स्वास्थ्य एवं शिक्षा उपकर (4%)", "₹${tax.oldCess}", "₹${tax.newCess}"),
                        listOf("कुल देय आयकर", "₹${tax.oldTotalTax}", "₹${tax.newTotalTax}")
                    )
                ),
                DocumentSection(
                    heading = "निष्कर्ष एवं बचत",
                    bodyLines = listOf(
                        "लाभकारी विकल्प: ${tax.recommendedRegime}",
                        "कुल संभावित कर बचत: ₹${tax.taxSavings}/- मात्र।"
                    )
                )
            ),
            leftFooter = "हस्ताक्षर कार्मिक\n${data.employeeName}",
            rightFooter = "डीडीओ / लेखा सत्यापन\n${data.ddoName}"
        )
    }

    private fun buildPlSurrenderDoc(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val basic = data.newBasicPay.toLongOrNull() ?: data.oldBasicPay.toLongOrNull() ?: 44300L
        val da = data.newDaPercent.toDoubleOrNull() ?: 55.0
        val amount = CalculationEngine.calculatePlSurrenderAmount(basic, da, 15)
        return GeneratedDocumentContent(
            fileBaseName = "PL_Surrender_Order_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "उपार्जित अवकाश समर्पण (15 Days PL Surrender) स्वीकृति आदेश व बिल",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "PL Surrender स्वीकृति एवं गणना विवरण",
                    bodyLines = listOf(
                        "राजस्थान सिविल सेवा नियम 91(A) के अंतर्गत श्री/श्रीमती ${data.employeeName}, ${data.designation} (ID: ${data.employeeId}) के अवकाश लेखे में पर्याप्त उपार्जित अवकाश शेष होने से वित्तीय वर्ष ${data.academicSession} हेतु 15 दिवस के उपार्जित अवकाश समर्पण के बदले नकद भुगतान की स्वीकृति प्रदान की जाती है।"
                    ),
                    tableHeaders = listOf("मद", "राशि / विवरण"),
                    tableRows = listOf(
                        listOf("वर्तमान मूल वेतन (Basic Pay)", "₹$basic"),
                        listOf("महंगाई भत्ता (${data.newDaPercent}%)", "₹${(basic * da / 100.0).toLong()}"),
                        listOf("कुल मासिक परिलब्धियां (Basic + DA)", "₹${basic + (basic * da / 100.0).toLong()}"),
                        listOf("15 दिवस की देय नकद राशि", "₹$amount/-")
                    )
                )
            ),
            leftFooter = "हस्ताक्षर कार्मिक\n${data.employeeName}",
            rightFooter = "स्वीकृतिकर्ता डीडीओ\n${data.ddoName}"
        )
    }

    private fun buildMdmDoc(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val mdm = CalculationEngine.calculateMdm(data)
        return GeneratedDocumentContent(
            fileBaseName = "MDM_Monthly_Sheet_${data.udiseCode}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "PM POSHAN / मिड-डे मील (MDM) मासिक उपयोगिता विवरण — ${mdm.month}",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "मासिक खाद्यान्न एवं कुकिंग कन्वर्जन गणना",
                    tableHeaders = listOf("वर्ग", "छात्र दिवस", "खाद्यान्न (किग्रा)", "कुकिंग राशि (₹)"),
                    tableRows = listOf(
                        listOf(
                            "प्राथमिक (कक्षा 1 से 5)",
                            mdm.primaryStudentMeals.toString(),
                            String.format(Locale.US, "%.2f", mdm.primaryGrainKg),
                            String.format(Locale.US, "₹%.2f", mdm.primaryCookingCost)
                        ),
                        listOf(
                            "उच्च प्राथमिक (कक्षा 6 से 8)",
                            mdm.upperPrimaryStudentMeals.toString(),
                            String.format(Locale.US, "%.2f", mdm.upperPrimaryGrainKg),
                            String.format(Locale.US, "₹%.2f", mdm.upperPrimaryCookingCost)
                        ),
                        listOf(
                            "कुल योग (Total)",
                            (mdm.primaryStudentMeals + mdm.upperPrimaryStudentMeals).toString(),
                            String.format(Locale.US, "%.2f किग्रा", mdm.totalGrainKg),
                            String.format(Locale.US, "₹%.2f", mdm.totalCookingCost)
                        )
                    )
                ),
                DocumentSection(
                    heading = "प्रमाण-पत्र",
                    bodyLines = listOf(
                        "प्रमाणित किया जाता है कि माह ${mdm.month} में कुल ${data.mdmWorkingDays} कार्यदिवसों में छात्र उपस्थिति पंजिका के अनुसार मध्यान्ह भोजन वितरित किया गया।"
                    )
                )
            ),
            leftFooter = "MDM प्रभारी अध्यापक\n${data.employeeName}",
            rightFooter = "संस्था प्रधान व मुहर\n${data.schoolName}"
        )
    }

    private fun buildAnnualIncrementDoc(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val oldBasic = data.oldBasicPay.toLongOrNull() ?: 39300L
        val nextPay = CalculationEngine.calculateAcpFixationPay(data.oldPayLevel, oldBasic, data.oldPayLevel)
        return GeneratedDocumentContent(
            fileBaseName = "Annual_Increment_Order_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "वार्षिक वेतन वृद्धि आदेश (Annual Increment - 01 July)",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "वेतन वृद्धि स्वीकृति तालिका",
                    tableHeaders = listOf("क्र.", "कार्मिक नाम व ID", "पद व लेवल", "पूर्व बेसिक", "नवीन बेसिक (1 जुलाई)"),
                    tableRows = listOf(
                        listOf(
                            "1",
                            "${data.employeeName}\n(${data.employeeId})",
                            "${data.designation}\n(${data.oldPayLevel})",
                            "₹$oldBasic",
                            "₹${nextPay.first}"
                        )
                    )
                ),
                DocumentSection(
                    heading = "आदेश",
                    bodyLines = listOf(
                        "उक्त कार्मिक की वार्षिक वेतन वृद्धि दिनांक 01 जुलाई से नियमानुसार स्वीकृत की जाती है एवं सेवा पुस्तिका में इंद्राज किया जावे।"
                    )
                )
            ),
            leftFooter = "लिपिक संस्थापन",
            rightFooter = "प्रधानाचार्य एवं डीडीओ\n${data.ddoName}"
        )
    }

    private fun buildJoiningGa13Doc(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        return GeneratedDocumentContent(
            fileBaseName = "GA_13_Charge_Report_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "कार्यभार ग्रहण / हस्तांतरण प्रतिवेदन (प्रपत्र GA-13)",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "प्रपत्र जी.ए.-13 (Charge Assumption / Relinquishment Report)",
                    bodyLines = listOf(
                        "आदेश क्रमांक ${data.previousOrderRef} की अनुपालना में मैं ${data.employeeName}, पद ${data.designation} (विषय: ${data.subject}, ID: ${data.employeeId}) द्वारा आज दिनांक ${data.effectiveDate} को (पूर्वाह्न / मध्यान्ह पश्चात) ${data.schoolName}, ब्लॉक ${data.block}, जिला ${data.district} में अपना कार्यभार विधिवत ग्रहण / हस्तांतरित किया गया।"
                    ),
                    tableHeaders = listOf("विवरण", "प्रविष्टि"),
                    tableRows = listOf(
                        listOf("कार्मिक का नाम व ID", "${data.employeeName} (${data.employeeId})"),
                        listOf("पद व विषय", "${data.designation} (${data.subject})"),
                        listOf("विद्यालय एवं UDISE", "${data.schoolName} (${data.udiseCode})"),
                        listOf("कार्यभार ग्रहण/मुक्ति तिथि", data.effectiveDate),
                        listOf("मूल वेतन व लेवल", "${data.oldPayLevel} — ₹${data.oldBasicPay}")
                    )
                )
            ),
            leftFooter = "कार्यभार हस्तांतरित करने वाले के हस्ताक्षर",
            rightFooter = "कार्यभार ग्रहण करने वाले व डीडीओ के हस्ताक्षर\n${data.employeeName}"
        )
    }

    private fun buildLeaveApplicationDoc(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        return GeneratedDocumentContent(
            fileBaseName = "Leave_Application_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "अवकाश प्रार्थना-पत्र (${data.leaveType})",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "सेवा में,",
                    bodyLines = listOf(
                        "श्रीमान ${data.ddoName}, ${data.schoolName}, जिला ${data.district}।",
                        "विषय: ${data.leaveDays} दिवस के ${data.leaveType} स्वीकृत करने बाबत।",
                        "महोदय,",
                        "नम्र निवेदन है कि मुझे ${data.leaveReason} के कारण दिनांक ${data.leaveFromDate} से ${data.leaveToDate} तक कुल ${data.leaveDays} दिवस का ${data.leaveType} आवश्यक है। अवकाश अवधि में मेरा शिक्षण/विद्यालय कार्य वैकल्पिक व्यवस्था से संपादित रहेगा।",
                        "अतः कृपया मुझे उक्त ${data.leaveDays} दिवस का अवकाश मय मुख्यालय छोड़ने की अनुमति सहित स्वीकृत करने की कृपा करावें।"
                    )
                )
            ),
            leftFooter = "दिनांक: ${data.orderDate}\nमोबाइल: ${data.mobile}",
            rightFooter = "प्रार्थी के हस्ताक्षर\n${data.employeeName}\n(${data.designation})"
        )
    }

    private fun buildLpcCertificateDoc(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val basic = data.oldBasicPay.toLongOrNull() ?: 39300L
        val daPct = data.newDaPercent.toDoubleOrNull() ?: 55.0
        val hraPct = data.hraPercent.toDoubleOrNull() ?: 9.0
        val daAmt = (basic * daPct / 100.0).toLong()
        val hraAmt = (basic * hraPct / 100.0).toLong()
        return GeneratedDocumentContent(
            fileBaseName = "LPC_Certificate_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "अंतिम वेतन प्रमाण-पत्र (Last Pay Certificate - LPC)",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "कार्मिक का वेतन एवं कटौती विवरण",
                    tableHeaders = listOf("विवरण", "राशि / जानकारी"),
                    tableRows = listOf(
                        listOf("कार्मिक का नाम व ID", "${data.employeeName} (${data.employeeId})"),
                        listOf("पद एवं पे-लेवल", "${data.designation} (${data.oldPayLevel})"),
                        listOf("मूल वेतन (Basic Pay)", "₹$basic"),
                        listOf("महंगाई भत्ता (${data.newDaPercent}%)", "₹$daAmt"),
                        listOf("मकान किराया भत्ता (${data.hraPercent}%)", "₹$hraAmt"),
                        listOf("कुल सकल वेतन (Gross)", "₹${basic + daAmt + hraAmt}"),
                        listOf("GPF / SI / Policy No.", data.gpfSiPolicyNo),
                        listOf("कार्यमुक्ति दिनांक", data.effectiveDate)
                    )
                ),
                DocumentSection(
                    heading = "डीडीओ प्रमाणन",
                    bodyLines = listOf(
                        "प्रमाणित किया जाता है कि उक्त कार्मिक का वेतन माह ${data.arrearStartMonth} तक इस कार्यालय से आहरित किया जा चुका है तथा कोई राजकीय ऋण/अग्रिम बकाया नहीं है।"
                    )
                )
            ),
            leftFooter = "लेखापाल / बाबू",
            rightFooter = "आहरण एवं वितरण अधिकारी\n${data.ddoName}"
        )
    }

    private fun buildStudentCertificateDoc(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        return GeneratedDocumentContent(
            fileBaseName = "Student_Study_Char_Cert_${data.srNumber}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "अध्ययन एवं चरित्र प्रमाण-पत्र (Study & Character Certificate)",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "प्रमाण-पत्र",
                    bodyLines = listOf(
                        "प्रमाणित किया जाता है कि विद्यार्थी ${data.studentName} पुत्र/पुत्री ${data.studentFatherName} इस विद्यालय ${data.schoolName} (UDISE: ${data.udiseCode}) के एस.आर. क्रमांक ${data.srNumber} के अनुसार शैक्षणिक सत्र ${data.academicSession} में ${data.studentClass} का नियमित विद्यार्थी रहा/रही है।",
                        "विद्यालय अभिलेख के अनुसार विद्यार्थी की जन्मतिथि ${data.dob} है। विद्यालय अध्ययन काल में इनका आचरण एवं चरित्र उत्तम रहा है।",
                        "हम इनके उज्ज्वल भविष्य की कामना करते हैं।"
                    )
                )
            ),
            leftFooter = "कक्षाध्यापक / प्रभारी\n${data.employeeName}",
            rightFooter = "संस्था प्रधान हस्ताक्षर व मुहर\n${data.schoolName}"
        )
    }

    private fun buildSchoolGrantUcDoc(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val sanctioned = data.grantAmount.toLongOrNull() ?: 50000L
        val spent = data.spentAmount.toLongOrNull() ?: 50000L
        val balance = (sanctioned - spent).coerceAtLeast(0L)
        return GeneratedDocumentContent(
            fileBaseName = "School_Grant_UC_${data.udiseCode}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "उपयोगिता प्रमाण-पत्र (Utilization Certificate - UC)",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "समग्र शिक्षा / विद्यालय अनुदान व्यय विवरण",
                    tableHeaders = listOf("विवरण", "राशि / जानकारी"),
                    tableRows = listOf(
                        listOf("विद्यालय का नाम व UDISE", "${data.schoolName} (${data.udiseCode})"),
                        listOf("योजना / मद का नाम", data.grantName),
                        listOf("शैक्षणिक सत्र", data.academicSession),
                        listOf("स्वीकृत प्राप्त राशि", "₹$sanctioned/-"),
                        listOf("वास्तविक व्यय राशि", "₹$spent/-"),
                        listOf("अवशेष राशि (Balance)", "₹$balance/-")
                    )
                ),
                DocumentSection(
                    heading = "प्रमाणीकरण",
                    bodyLines = listOf(
                        "प्रमाणित किया जाता है कि ${data.grantName} के अंतर्गत प्राप्त राशि ₹$sanctioned/- में से ₹$spent/- का व्यय विभागीय दिशा-निर्देशों के अनुरूप विद्यालय हित एवं SDMC/SMC प्रस्ताव अनुसार ही किया गया है तथा समस्त वाउचर एवं बिल विद्यालय अभिलेख में सुरक्षित हैं।"
                    )
                )
            ),
            leftFooter = "हस्ताक्षर अध्यक्ष SDMC/SMC",
            rightFooter = "हस्ताक्षर संस्था प्रधान व मुहर\n${data.ddoName}"
        )
    }

    private fun buildGpfSiDeclarationDoc(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        return GeneratedDocumentContent(
            fileBaseName = "GPF_SI_Declaration_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "राज्य बीमा (SI) / GPF मासिक कटौती घोषणा-पत्र",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "कार्मिक एवं कटौती विवरण",
                    tableHeaders = listOf("विवरण", "प्रविष्टि"),
                    tableRows = listOf(
                        listOf("कार्मिक का नाम व ID", "${data.employeeName} (${data.employeeId})"),
                        listOf("पिता / पति का नाम", data.fatherOrHusbandName),
                        listOf("पद व मूल वेतन", "${data.designation} (₹${data.oldBasicPay})"),
                        listOf("SI पॉलिसी / GPF क्रमांक", data.gpfSiPolicyNo),
                        listOf("जन्मतिथि एवं नियुक्ति तिथि", "${data.dob} / ${data.joiningDate}"),
                        listOf("कार्यालय एवं ब्लॉक", "${data.schoolName}, ${data.block}")
                    )
                ),
                DocumentSection(
                    heading = "घोषणा",
                    bodyLines = listOf(
                        "मैं यह घोषणा करता/करती हूँ कि मेरी वेतन स्लैब के अनुसार राज्य बीमा (SI) एवं GPF की नियमानुसार मासिक कटौती मार्च पेड अप्रैल के वेतन से की जावे। उपरोक्त विवरण सत्य एवं सही है।"
                    )
                )
            ),
            leftFooter = "आवेदक के हस्ताक्षर\n${data.employeeName}",
            rightFooter = "डीडीओ अग्रेषण व मुहर\n${data.ddoName}"
        )
    }

    private fun sanitizeName(title: String, suffix: String): String {
        val ascii = title.replace(Regex("[^A-Za-z0-9]+"), "_").trim('_').take(28)
        val base = if (ascii.isBlank()) "RajTeachers_Format" else ascii
        val cleanSuffix = suffix.replace(Regex("[^A-Za-z0-9]"), "").take(12)
        return if (cleanSuffix.isNotEmpty()) "${base}_$cleanSuffix" else base
    }
}
