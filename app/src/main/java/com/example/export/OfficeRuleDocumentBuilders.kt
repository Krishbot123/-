package com.example.export

import com.example.data.CalculationEngine
import com.example.model.BuilderTemplateType
import com.example.model.DocumentSection
import com.example.model.FormatFormData
import com.example.model.FormatItem
import com.example.model.GeneratedDocumentContent

object OfficeRuleDocumentBuilders {

    fun build(
        format: FormatItem,
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent? {
        return when (format.templateType) {
            BuilderTemplateType.DA_ARREAR_60_63 -> buildDa60To63Doc(data, dept, subOffice, refLine)
            BuilderTemplateType.ADHOC_BONUS_2025_26,
            BuilderTemplateType.BONUS_OFFICE_ORDER -> buildBonusDoc(format, data, dept, subOffice, refLine)
            BuilderTemplateType.BUDGET_PROPOSAL_2026_27 -> buildBudgetDoc(format, data, dept, subOffice, refLine)
            BuilderTemplateType.PENSION_CALCULATOR_KULAK -> buildPensionDoc(data, dept, subOffice, refLine)
            BuilderTemplateType.PAY_FIXATION_PROMOTION,
            BuilderTemplateType.NOTIONAL_ORDER_FITMENT,
            BuilderTemplateType.OFFICE_ORDER_EXCEL_7PAY -> buildPromotionOrNotionalDoc(format, data, dept, subOffice, refLine)
            BuilderTemplateType.TA_BILL_EXCEL -> buildTaBillDoc(data, dept, subOffice, refLine)
            BuilderTemplateType.LEAVE_SANCTION_WORD,
            BuilderTemplateType.LEAVE_SANCTION_PDF,
            BuilderTemplateType.MEDICAL_CERT_GAZETTED,
            BuilderTemplateType.MEDICAL_CERT_NON_GAZETTED,
            BuilderTemplateType.CCL_FORM,
            BuilderTemplateType.PL_APPLICATION,
            BuilderTemplateType.JOINING_TIME_PL,
            BuilderTemplateType.LEAVES_SHORT_CHART -> buildLeaveDoc(format, data, dept, subOffice, refLine)
            BuilderTemplateType.FIRST_APPOINTMENT_OATH_WORD -> buildFirstAppointmentOath(format, data, dept, subOffice, refLine)
            BuilderTemplateType.SIXTH_PAY_ARREAR,
            BuilderTemplateType.FIFTH_PAY_ARREAR -> buildLegacyPayArrearDoc(format, data, dept, subOffice, refLine)
            BuilderTemplateType.CIRCULAR_OR_RULE_REFERENCE,
            BuilderTemplateType.SOFTWARE_TOOL -> buildCircularOrToolDoc(format, data, dept, subOffice, refLine)
            else -> null
        }
    }

    private fun buildDa60To63Doc(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val daData = data.copy(
            oldBasicPay = data.oldBasicPay,
            newBasicPay = data.oldBasicPay,
            oldDaPercent = data.oldDaPercent.ifBlank { "60" },
            newDaPercent = data.newDaPercent.ifBlank { "63" }
        )
        val summary = CalculationEngine.calculateArrear(daData)
        return GeneratedDocumentContent(
            fileBaseName = "DA_Arrear_60_to_63_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "महंगाई भत्ता (DA) 60% से 63% एरियर गणना विवरण (IFMS 3.0)",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "कार्मिक एवं DA वृद्धि विवरण (+3% महंगाई भत्ता)",
                    bodyLines = listOf(
                        "कार्मिक: ${data.employeeName} (${data.designation}) | ID: ${data.employeeId}",
                        "मूल वेतन: ₹${daData.oldBasicPay} (पे-लेवल ${data.oldPayLevel}) | DA वृद्धि: ${daData.oldDaPercent}% से ${daData.newDaPercent}%",
                        "नियमानुसार जुलाई से अक्टूबर तक एरियर राशि GPF खाते में एवं नवंबर से नकद वेतन में देय होगी।"
                    )
                ),
                DocumentSection(
                    heading = "माहवार DA अंतर गणना",
                    tableHeaders = listOf("क्र.", "माह", "पूर्व DA (${daData.oldDaPercent}%)", "नवीन DA (${daData.newDaPercent}%)", "अंतर", "शुद्ध देय"),
                    tableRows = summary.rows.map { r ->
                        listOf(r.serial.toString(), r.monthLabel, "₹${r.drawnDa}", "₹${r.dueDa}", "₹${r.grossDifference}", "₹${r.netPayable}")
                    } + listOf(listOf("", "कुल योग", "", "", "₹${summary.totalGrossDifference}", "₹${summary.totalNetPayable}"))
                )
            ),
            leftFooter = "लेखा लिपिक / कार्मिक",
            rightFooter = "डीडीओ हस्ताक्षर व मुहर\n${data.ddoName}"
        )
    }

    private fun buildBonusDoc(
        format: FormatItem,
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val bonus = CalculationEngine.calculateBonus2025_26(data)
        return GeneratedDocumentContent(
            fileBaseName = "Adhoc_Bonus_2025_26_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "${format.title} — वित्तीय वर्ष 2025-26",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "तदर्थ बोनस (Ad-hoc Bonus 2025-26) स्वीकृति एवं गणना",
                    bodyLines = listOf(
                        "राजस्थान सरकार वित्त विभाग के आदेशानुसार पे-लेवल L-12 तक अराजपत्रित कार्मिकों को वर्ष 2025-26 हेतु अधिकतम मासिक परिलब्धि ₹7,000/- के आधार पर तदर्थ बोनस देय है (75% नकद एवं 25% GPF जमा)।",
                        if (!bonus.isEligible) "नोट: पे-लेवल ${bonus.payLevel} या कार्य अवधि ${bonus.monthsWorked} माह नियमानुसार पात्रता सीमा से बाहर हो सकती है।" else "पात्रता स्थिति: पात्र (Eligible)"
                    ),
                    tableHeaders = listOf("विवरण", "गणना / राशि"),
                    tableRows = listOf(
                        listOf("कार्मिक व ID", "${data.employeeName} (${data.employeeId})"),
                        listOf("पे-लेवल व कार्य अवधि", "${bonus.payLevel} — ${bonus.monthsWorked} माह"),
                        listOf("कुल स्वीकृत बोनस राशि (Max ₹6,774)", "₹${bonus.grossBonus}/-"),
                        listOf("नकद भुगतान (75% Cash)", "₹${bonus.cashAmount75}/-"),
                        listOf("GPF खाते में जमा (25% GPF)", "₹${bonus.gpfAmount25}/-")
                    )
                )
            ),
            leftFooter = "संस्थापन शाखा",
            rightFooter = "प्रधानाचार्य एवं डीडीओ\n${data.ddoName}"
        )
    }

    private fun buildBudgetDoc(
        format: FormatItem,
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val budget = CalculationEngine.calculateBudgetProposal(data)
        val mggsText = if (data.isMggsSchool) "महात्मा गांधी राजकीय विद्यालय (MGGS - अंग्रेजी माध्यम)" else "राजकीय विद्यालय"
        return GeneratedDocumentContent(
            fileBaseName = "Budget_Proposal_2026_27_${data.udiseCode}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "${format.title} ($mggsText)",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "वित्तीय वर्ष 2026-27 मदवार वार्षिक बजट मांग प्रस्ताव",
                    tableHeaders = listOf("क्र.", "बजट मद (Object Head)", "अनुमानित मांग (₹)"),
                    tableRows = listOf(
                        listOf("1", "01 - वेतन, महंगाई भत्ता एवं एरियर (Salary Head)", "₹${budget.salaryHead}"),
                        listOf("2", "05 - कार्यालय व्यय, बिजली/पानी एवं स्टेशनरी", "₹${budget.officeExpenseHead}"),
                        listOf("3", "18 - भवन मरम्मत एवं रखरखाव (Repair & Maint.)", "₹${budget.repairMaintenanceHead}"),
                        listOf("", "कुल वार्षिक बजट मांग (Total Demand)", "₹${budget.totalDemand}/-")
                    )
                )
            ),
            leftFooter = "लेखा प्रभारी",
            rightFooter = "संस्था प्रधान / पीईईओ\n${data.ddoName}"
        )
    }

    private fun buildPensionDoc(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val p = CalculationEngine.calculatePension(data)
        return GeneratedDocumentContent(
            fileBaseName = "Pension_Kulak_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "सेवानिवृत्ति पेंशन, कम्यूटेशन एवं ग्रेच्युटी कुलक (IFMS 3.0)",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "पेंशन एवं परिलाभ गणना तालिका",
                    tableHeaders = listOf("विवरण", "गणना / राशि"),
                    tableRows = listOf(
                        listOf("कार्मिक नाम व ID", "${data.employeeName} (${data.employeeId})"),
                        listOf("जन्मतिथि व सेवानिवृत्ति तिथि", "${data.dob} | ${data.proposedVrsDate}"),
                        listOf("अंतिम मूल वेतन व DA (${p.daPercent}%)", "₹${p.lastBasicPay} + ₹${p.daOnBasic} = ₹${p.totalEmoluments}"),
                        listOf("कुल अर्हक सेवा (Qualifying Service)", "${p.qualifyingYears} वर्ष"),
                        listOf("पूर्ण मासिक पेंशन (50% Basic)", "₹${p.fullBasicPension}/- प्रतिमाह"),
                        listOf("कम्यूटेशन (${p.commutationPercent}% राशि)", "₹${p.commutedMonthlyPension}/- (एकमुश्त: ₹${p.commutationLumpSum}/-)"),
                        listOf("कम्यूटेशन उपरांत शेष मासिक पेंशन", "₹${p.residualMonthlyPension}/- प्रतिमाह (+ DR)"),
                        listOf("सेवानिवृत्ति उपदान (DCRG Gratuity)", "₹${p.dcrgGratuity}/-")
                    )
                )
            ),
            leftFooter = "पेंशनर हस्ताक्षर\n${data.employeeName}",
            rightFooter = "कार्यालयध्यक्ष व मुहर\n${data.ddoName}"
        )
    }

    private fun buildPromotionOrNotionalDoc(
        format: FormatItem,
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val nextPay = CalculationEngine.calculateAcpFixationPay(
            data.oldPayLevel,
            data.oldBasicPay.toLongOrNull() ?: 39300L,
            data.newPayLevel
        )
        return GeneratedDocumentContent(
            fileBaseName = "Office_Order_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = format.title,
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "कार्यालय आदेश — पदोन्नति / नोशनल वेतन नियतन",
                    bodyLines = listOf(
                        "संदर्भित आदेश क्रमांक ${data.previousOrderRef} की अनुपालना में श्री/श्रीमती ${data.employeeName}, ${data.designation} (ID: ${data.employeeId}) का वेतन नियतन दिनांक ${data.effectiveDate} से पे-लेवल ${data.oldPayLevel} (₹${data.oldBasicPay}/-) से लेवल ${data.newPayLevel} में मूल वेतन ₹${data.newBasicPay.ifBlank { nextPay.second.toString() }}/- पर निर्धारित किया जाता है।",
                        "आगामी वार्षिक वेतन वृद्धि दिनांक 01 जुलाई को देय होगी।"
                    )
                )
            ),
            leftFooter = "संस्थापन शाखा",
            rightFooter = "आहरण एवं वितरण अधिकारी\n${data.ddoName}"
        )
    }

    private fun buildTaBillDoc(
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val ta = CalculationEngine.calculateTaBill(data)
        return GeneratedDocumentContent(
            fileBaseName = "TA_Bill_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "यात्रा भत्ता (Travelling Allowance - TA Bill) प्रपत्र (IFMS 3.0)",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "यात्रा विवरण एवं दावा राशि",
                    tableHeaders = listOf("विवरण", "प्रविष्टि / राशि"),
                    tableRows = listOf(
                        listOf("कार्मिक नाम व पे-लेवल", "${data.employeeName} (${data.oldPayLevel} - ₹${data.oldBasicPay})"),
                        listOf("यात्रा मार्ग (From → To)", "${data.taJourneyFrom} से ${data.taJourneyTo}"),
                        listOf("साधन व दूरी", data.taModeAndKm),
                        listOf("वास्तविक किराया (Fare)", "₹${ta.fareAmount}/-"),
                        listOf("दैनिक भत्ता (DA: ${ta.daDays} दिवस × ₹${ta.dailyRate})", "₹${ta.totalDaAmount}/-"),
                        listOf("कुल देय TA दावा राशि", "₹${ta.totalClaimAmount}/-")
                    )
                )
            ),
            leftFooter = "दावाकर्ता के हस्ताक्षर\n${data.employeeName}",
            rightFooter = "नियंत्रक अधिकारी व मुहर\n${data.ddoName}"
        )
    }

    private fun buildLeaveDoc(
        format: FormatItem,
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        return GeneratedDocumentContent(
            fileBaseName = "Leave_${format.templateType.name}_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = format.title,
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "अवकाश / चिकित्सा प्रमाण-पत्र विवरण",
                    bodyLines = listOf(
                        "कार्मिक: ${data.employeeName} (${data.designation}), Employee ID: ${data.employeeId}, विद्यालय: ${data.schoolName}।",
                        "अवकाश प्रकार: ${data.leaveType} | अवधि: दिनांक ${data.leaveFromDate} से ${data.leaveToDate} तक (कुल ${data.leaveDays} दिवस)।",
                        "कारण / विवरण: ${data.leaveReason}। अवकाश उपरांत सेवा पुस्तिका एवं शाला दर्पण पोर्टल पर प्रविष्टि सुनिश्चित की जावे।"
                    )
                )
            ),
            leftFooter = "हस्ताक्षर कार्मिक / चिकित्सक",
            rightFooter = "स्वीकृतिकर्ता अधिकारी\n${data.ddoName}"
        )
    }

    private fun buildFirstAppointmentOath(
        format: FormatItem,
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        return GeneratedDocumentContent(
            fileBaseName = "First_Appointment_Oath_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = format.title,
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "नवनियुक्ति संयुक्त शपथ-पत्र (दहेज, धूम्रपान, संतान व चरित्र घोषणा)",
                    bodyLines = listOf(
                        "मैं ${data.employeeName} पुत्र/पत्नी श्री ${data.fatherOrHusbandName}, नव चयनित पद ${data.designation} (${data.subject}) निम्नानुसार शपथ लेता/लेती हूँ कि:",
                        "1. मैंने विवाह में किसी प्रकार का दहेज नहीं लिया है और न ही भविष्य में दहेज लूँगा/लूँगी।",
                        "2. मैं किसी भी प्रकार के धूम्रपान एवं तंबाकू उत्पाद का सेवन नहीं करता/करती हूँ।",
                        "3. दिनांक 01-06-2002 के पश्चात मेरी जीवित संतानों की संख्या 2 से अधिक नहीं है।",
                        "4. मैं भारत के संविधान एवं राजस्थान सिविल सेवा आचरण नियमों के प्रति पूर्ण निष्ठा रखूँगा/रखूँगी।"
                    )
                )
            ),
            leftFooter = "स्थान: ${data.block}, ${data.district}",
            rightFooter = "हस्ताक्षर शपथकर्ता\n${data.employeeName}"
        )
    }

    private fun buildLegacyPayArrearDoc(
        format: FormatItem,
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        val summary = CalculationEngine.calculateArrear(data)
        return GeneratedDocumentContent(
            fileBaseName = "${format.templateType.name}_${data.employeeId}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = "${format.title} — बकाया एरियर विवरण",
            refNumberLine = refLine,
            sections = listOf(
                DocumentSection(
                    heading = "आहरित बनाम देय बकाया एरियर तालिका",
                    tableHeaders = listOf("क्र.", "माह", "आहरित", "देय", "अंतर", "कटौती", "शुद्ध देय"),
                    tableRows = summary.rows.map {
                        listOf(it.serial.toString(), it.monthLabel, "₹${it.drawnGross}", "₹${it.dueGross}", "₹${it.grossDifference}", "₹${it.npsDeduction}", "₹${it.netPayable}")
                    } + listOf(listOf("", "योग", "₹${summary.totalDrawn}", "₹${summary.totalDue}", "₹${summary.totalGrossDifference}", "₹${summary.totalNpsDeduction}", "₹${summary.totalNetPayable}"))
                )
            ),
            leftFooter = "कार्मिक हस्ताक्षर\n${data.employeeName}",
            rightFooter = "डीडीओ हस्ताक्षर\n${data.ddoName}"
        )
    }

    private fun buildCircularOrToolDoc(
        format: FormatItem,
        data: FormatFormData,
        dept: String,
        subOffice: String,
        refLine: String
    ): GeneratedDocumentContent {
        return GeneratedDocumentContent(
            fileBaseName = "RajTeachers_Order_${format.id}",
            headerDepartment = dept,
            headerSubOffice = subOffice,
            documentTitle = format.title,
            refNumberLine = "श्रेणी: ${format.category.title}  |  दिनांक: ${format.dateLabel}",
            sections = listOf(
                DocumentSection(
                    heading = "विभागीय संदर्भ एवं प्रमुख बिंदु",
                    bodyLines = listOf(
                        "विवरण: ${format.subtitle}",
                        "टैग / स्रोत: ${format.authorOrTag} (राजस्थान शिक्षा विभाग / राजटीचर्स पोर्टल संग्रह)",
                        "संबंधित विद्यालय / कार्यालय अभिलेख: ${data.schoolName}, ब्लॉक ${data.block}, जिला ${data.district} (UDISE: ${data.udiseCode})",
                        "नोट: इस आदेश / नियम / मार्गदर्शिका की अनुपालना राजस्थान सेवा नियम (RSR) एवं अद्यतन विभागीय परिपत्रों के अनुसार कार्यालय एवं शाला दर्पण / IFMS 3.0 अभिलेख में दर्ज की जाती है।"
                    )
                ),
                DocumentSection(
                    heading = "कार्यालय अभिलेख संदर्भ (Office Endorsement)",
                    tableHeaders = listOf("कार्मिक / संदर्भ", "विवरण"),
                    tableRows = listOf(
                        listOf("कार्मिक / प्रभारी का नाम", "${data.employeeName} (${data.employeeId})"),
                        listOf("पद व विषय", "${data.designation} (${data.subject})"),
                        listOf("कार्यालय आदेश संदर्भ", data.orderNumber),
                        listOf("अवलोकन तिथि", data.orderDate)
                    )
                )
            ),
            leftFooter = "संस्थापन पंजिका में रक्षित",
            rightFooter = "संस्था प्रधान / पीईईओ\n${data.ddoName}"
        )
    }
}
