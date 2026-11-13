package com.example.export

import com.example.data.CalculationEngine
import com.example.model.BuilderTemplateType
import com.example.model.FormatFormData
import com.example.model.FormatItem

internal sealed class XCell(val style: Int) {
    class Text(val value: String, style: Int = 3) : XCell(style)
    class Num(val value: Double, style: Int = 3) : XCell(style)
    class Formula(val formula: String, val cachedValue: Double, style: Int = 4) : XCell(style)
}

internal object ExtraXlsxSheets {

    fun build(format: FormatItem, data: FormatFormData): Pair<String, List<List<XCell>>>? {
        return when (format.templateType) {
            BuilderTemplateType.DA_ARREAR_60_63 -> buildDa60To63Sheet(data)
            BuilderTemplateType.ADHOC_BONUS_2025_26 -> buildBonusSheet(data)
            BuilderTemplateType.PENSION_CALCULATOR_KULAK -> buildPensionKulakSheet(data)
            BuilderTemplateType.BUDGET_PROPOSAL_2026_27 -> buildBudgetSheet(format, data)
            BuilderTemplateType.TA_BILL_EXCEL -> buildTaBillSheet(data)
            BuilderTemplateType.GPA_DEDUCTION_EXCEL -> buildGpaScheduleSheet(data)
            BuilderTemplateType.SCHOOL_ESTABLISHMENT_EXCEL -> buildSchoolEstablishmentSheet(data)
            BuilderTemplateType.GA_13_EXCEL -> buildGa13Sheet(data)
            BuilderTemplateType.OFFICE_ORDER_EXCEL_7PAY -> buildOfficeOrder7PaySheet(data)
            BuilderTemplateType.MDM_ALL_UC -> buildMdmAllUcSheet(data)
            BuilderTemplateType.LEAVES_SHORT_CHART -> buildLeavesShortChartSheet(data)
            BuilderTemplateType.HLB_RECORD_EXCEL,
            BuilderTemplateType.HOUSEHOLD_SURVEY_EXCEL -> buildSurveyOrHlbSheet(format, data)
            else -> null
        }
    }

    private fun buildDa60To63Sheet(data: FormatFormData): Pair<String, List<List<XCell>>> {
        val basic = data.oldBasicPay.toDoubleOrNull() ?: 39300.0
        val oldDaPct = data.oldDaPercent.toDoubleOrNull() ?: 60.0
        val newDaPct = data.newDaPercent.toDoubleOrNull() ?: 63.0
        val months = (data.arrearMonths.toIntOrNull() ?: 4).coerceIn(1, 12)
        val rows = mutableListOf<List<XCell>>()
        rows.add(listOf(XCell.Text("DA Arrear Sheet (${oldDaPct.toInt()}% to ${newDaPct.toInt()}%) — ${data.schoolName}", 1)))
        rows.add(listOf(XCell.Text("Employee: ${data.employeeName} (${data.employeeId}) | Level: ${data.oldPayLevel} | GPF: ${data.gpfAccountNo}", 0)))
        rows.add(emptyList())
        rows.add(listOf("S.N.", "Month", "Basic Pay", "Drawn DA (${oldDaPct.toInt()}%)", "Due DA (${newDaPct.toInt()}%)", "DA Diff", "GPF/Cash Mode").map { XCell.Text(it, 2) })
        val monthNames = listOf("July 2026", "August 2026", "September 2026", "October 2026", "November 2026", "December 2026")
        val drawnDa = kotlin.math.round(basic * oldDaPct / 100.0)
        val dueDa = kotlin.math.round(basic * newDaPct / 100.0)
        val diff = dueDa - drawnDa
        for (i in 0 until months) {
            val r = 5 + i
            rows.add(
                listOf(
                    XCell.Num((i + 1).toDouble(), 3),
                    XCell.Text(monthNames.getOrElse(i) { "Month ${i + 1}" }, 3),
                    XCell.Num(basic, 3),
                    XCell.Formula("ROUND(C$r*$oldDaPct/100,0)", drawnDa, 3),
                    XCell.Formula("ROUND(C$r*$newDaPct/100,0)", dueDa, 3),
                    XCell.Formula("E$r-D$r", diff, 3),
                    XCell.Text(if (i < 4) "GPF Credit" else "Cash with Salary", 3)
                )
            )
        }
        val endR = 4 + months
        rows.add(
            listOf(
                XCell.Text("", 4),
                XCell.Text("TOTAL", 4),
                XCell.Formula("SUM(C5:C$endR)", basic * months, 4),
                XCell.Formula("SUM(D5:D$endR)", drawnDa * months, 4),
                XCell.Formula("SUM(E5:E$endR)", dueDa * months, 4),
                XCell.Formula("SUM(F5:F$endR)", diff * months, 4),
                XCell.Text("IFMS 3.0 Verified", 4)
            )
        )
        return "DA_60_to_63" to rows
    }

    private fun buildBonusSheet(data: FormatFormData): Pair<String, List<List<XCell>>> {
        val bonus = CalculationEngine.calculateBonus2025_26(data)
        val rows = listOf(
            listOf(XCell.Text("Ad-hoc Bonus Program (FY 2025-26) — ${data.schoolName}", 1)),
            listOf(XCell.Text("Employee: ${data.employeeName} (${data.employeeId}) | GPF: ${data.gpfAccountNo}", 0)),
            emptyList(),
            listOf("Employee Name", "Pay Level", "Months Worked", "Ceiling (INR)", "Gross Bonus", "75% Cash Payable", "25% GPF Credit").map { XCell.Text(it, 2) },
            listOf(
                XCell.Text(data.employeeName, 3),
                XCell.Text(bonus.payLevel, 3),
                XCell.Num(bonus.monthsWorked.toDouble(), 3),
                XCell.Num(bonus.maxCeiling.toDouble(), 3),
                XCell.Formula("ROUND((D5*30/31)*(C5/12),0)", bonus.grossBonus.toDouble(), 4),
                XCell.Formula("ROUND(E5*0.75,0)", bonus.cashAmount75.toDouble(), 4),
                XCell.Formula("E5-F5", bonus.gpfAmount25.toDouble(), 4)
            )
        )
        return "Adhoc_Bonus_2025_26" to rows
    }

    private fun buildPensionKulakSheet(data: FormatFormData): Pair<String, List<List<XCell>>> {
        val p = CalculationEngine.calculatePension(data)
        val rows = listOf(
            listOf(XCell.Text("IFMS 3.0 Pension Kulak Program — ${data.schoolName}", 1)),
            listOf(XCell.Text("Employee: ${data.employeeName} (${data.employeeId}) | DOB: ${data.dob} | Retirement: ${data.proposedVrsDate}", 0)),
            emptyList(),
            listOf("Particulars", "Input / Value", "Formula / Amount (INR)").map { XCell.Text(it, 2) },
            listOf(XCell.Text("Last Basic Pay", 3), XCell.Text(data.newPayLevel, 3), XCell.Num(p.lastBasicPay.toDouble(), 3)),
            listOf(XCell.Text("DA Rate (%)", 3), XCell.Num(p.daPercent, 3), XCell.Formula("ROUND(C5*B6/100,0)", p.daOnBasic.toDouble(), 3)),
            listOf(XCell.Text("Total Emoluments (Basic+DA)", 3), XCell.Text("For Gratuity", 3), XCell.Formula("C5+C6", p.totalEmoluments.toDouble(), 3)),
            listOf(XCell.Text("Qualifying Service (Years)", 3), XCell.Num(p.qualifyingYears.toDouble(), 3), XCell.Formula("ROUND(C5*0.5,0)", p.fullBasicPension.toDouble(), 4)),
            listOf(XCell.Text("Commutation (${p.commutationPercent}%)", 3), XCell.Num(p.commutationPercent.toDouble(), 3), XCell.Formula("ROUND(C8*B9/100,0)", p.commutedMonthlyPension.toDouble(), 3)),
            listOf(XCell.Text("Commutation Lump Sum", 3), XCell.Text("Factor 8.194", 3), XCell.Formula("ROUND(C9*12*8.194,0)", p.commutationLumpSum.toDouble(), 4)),
            listOf(XCell.Text("Residual Monthly Pension", 3), XCell.Text("Basic - Commuted", 3), XCell.Formula("C8-C9", p.residualMonthlyPension.toDouble(), 4)),
            listOf(XCell.Text("DCRG Gratuity", 3), XCell.Text("Max 25 Lakh", 3), XCell.Num(p.dcrgGratuity.toDouble(), 4))
        )
        return "Pension_Kulak_IFMS3" to rows
    }

    private fun buildBudgetSheet(format: FormatItem, data: FormatFormData): Pair<String, List<List<XCell>>> {
        val b = CalculationEngine.calculateBudgetProposal(data)
        val rows = listOf(
            listOf(XCell.Text("${format.title} — ${data.schoolName}", 1)),
            listOf(XCell.Text("UDISE: ${data.udiseCode} | Block: ${data.block} | District: ${data.district}", 0)),
            emptyList(),
            listOf("S.N.", "Budget Object Head", "FY 2026-27 Demand (INR)").map { XCell.Text(it, 2) },
            listOf(XCell.Num(1.0, 3), XCell.Text("01 - Pay, DA, HRA & Arrears (Salary Head)", 3), XCell.Num(b.salaryHead.toDouble(), 3)),
            listOf(XCell.Num(2.0, 3), XCell.Text("05 - Office Expenses & Electricity/Water", 3), XCell.Num(b.officeExpenseHead.toDouble(), 3)),
            listOf(XCell.Num(3.0, 3), XCell.Text("18 - School Building Repair & Maintenance", 3), XCell.Num(b.repairMaintenanceHead.toDouble(), 3)),
            listOf(XCell.Text("", 4), XCell.Text("TOTAL ANNUAL BUDGET DEMAND", 4), XCell.Formula("SUM(C5:C7)", b.totalDemand.toDouble(), 4))
        )
        return "Budget_2026_27" to rows
    }

    private fun buildTaBillSheet(data: FormatFormData): Pair<String, List<List<XCell>>> {
        val ta = CalculationEngine.calculateTaBill(data)
        val rows = listOf(
            listOf(XCell.Text("TA Bill Format (IFMS 3.0) — ${data.schoolName}", 1)),
            listOf(XCell.Text("Employee: ${data.employeeName} (${data.employeeId}) | Level: ${data.oldPayLevel}", 0)),
            emptyList(),
            listOf("From", "To", "Mode / Distance", "Fare (INR)", "DA Days", "Daily Rate (INR)", "DA Total", "Grand Total Claim").map { XCell.Text(it, 2) },
            listOf(
                XCell.Text(data.taJourneyFrom, 3),
                XCell.Text(data.taJourneyTo, 3),
                XCell.Text(data.taModeAndKm, 3),
                XCell.Num(ta.fareAmount.toDouble(), 3),
                XCell.Num(ta.daDays.toDouble(), 3),
                XCell.Num(ta.dailyRate.toDouble(), 3),
                XCell.Formula("E5*F5", ta.totalDaAmount.toDouble(), 3),
                XCell.Formula("D5+G5", ta.totalClaimAmount.toDouble(), 4)
            )
        )
        return "TA_Bill_Sheet" to rows
    }

    private fun buildGpaScheduleSheet(data: FormatFormData): Pair<String, List<List<XCell>>> {
        val premium = Regex("\\d+").find(data.gpaPremiumSlab.replace(",", ""))?.value?.toDoubleOrNull() ?: 2100.0
        val rows = listOf(
            listOf(XCell.Text("GPA Deduction Schedule — ${data.schoolName} (${data.udiseCode})", 1)),
            listOf(XCell.Text("DDO: ${data.ddoName} | Session: ${data.academicSession}", 0)),
            emptyList(),
            listOf("S.N.", "Employee Name", "Employee ID", "Designation", "Nominee Name", "Relation", "GPA Deduction (INR)").map { XCell.Text(it, 2) },
            listOf(
                XCell.Num(1.0, 3),
                XCell.Text(data.employeeName, 3),
                XCell.Text(data.employeeId, 3),
                XCell.Text(data.designation, 3),
                XCell.Text(data.nomineeName, 3),
                XCell.Text(data.nomineeRelation, 3),
                XCell.Num(premium, 3)
            ),
            listOf(XCell.Text("", 4), XCell.Text("TOTAL GPA SCHEDULE", 4), XCell.Text("", 4), XCell.Text("", 4), XCell.Text("", 4), XCell.Text("", 4), XCell.Formula("SUM(G5:G5)", premium, 4))
        )
        return "GPA_Schedule" to rows
    }

    private fun buildSchoolEstablishmentSheet(data: FormatFormData): Pair<String, List<List<XCell>>> {
        val rows = listOf(
            listOf(XCell.Text("School Establishment Register — ${data.schoolName} (${data.udiseCode})", 1)),
            listOf(XCell.Text("Block: ${data.block} | District: ${data.district} | Session: ${data.academicSession}", 0)),
            emptyList(),
            listOf("S.N.", "Post Name", "Sanctioned", "Working", "Vacant", "Employee Details").map { XCell.Text(it, 2) },
            listOf(XCell.Num(1.0, 3), XCell.Text(data.designation, 3), XCell.Num(2.0, 3), XCell.Num(1.0, 3), XCell.Formula("C5-D5", 1.0, 3), XCell.Text("${data.employeeName} (${data.employeeId})", 3)),
            listOf(XCell.Text("", 4), XCell.Text("TOTAL POSTS", 4), XCell.Formula("SUM(C5:C5)", 2.0, 4), XCell.Formula("SUM(D5:D5)", 1.0, 4), XCell.Formula("SUM(E5:E5)", 1.0, 4), XCell.Text("By: Parmanand Meghwal", 4))
        )
        return "Establishment" to rows
    }

    private fun buildGa13Sheet(data: FormatFormData): Pair<String, List<List<XCell>>> {
        val rows = listOf(
            listOf(XCell.Text("Form GA-13 Charge Report — ${data.schoolName}", 1)),
            emptyList(),
            listOf("Field", "Entry").map { XCell.Text(it, 2) },
            listOf(XCell.Text("Employee Name & ID", 3), XCell.Text("${data.employeeName} (${data.employeeId})", 3)),
            listOf(XCell.Text("Designation & Subject", 3), XCell.Text("${data.designation} (${data.subject})", 3)),
            listOf(XCell.Text("Pay Level & Basic", 3), XCell.Text("${data.oldPayLevel} — INR ${data.oldBasicPay}", 3)),
            listOf(XCell.Text("Order No & Effective Date", 3), XCell.Text("${data.previousOrderRef} | ${data.effectiveDate}", 3))
        )
        return "GA_13_Charge" to rows
    }

    private fun buildOfficeOrder7PaySheet(data: FormatFormData): Pair<String, List<List<XCell>>> {
        val oldBasic = data.oldBasicPay.toLongOrNull() ?: 39300L
        val nextPay = CalculationEngine.calculateAcpFixationPay(data.oldPayLevel, oldBasic, data.newPayLevel)
        val rows = listOf(
            listOf(XCell.Text("7th Pay Office Order Excel — ${data.schoolName}", 1)),
            listOf(XCell.Text("Order No: ${data.orderNumber} | Date: ${data.orderDate}", 0)),
            emptyList(),
            listOf("Employee", "ID", "Old Level", "Old Basic", "New Level", "Fixed Basic", "Increment").map { XCell.Text(it, 2) },
            listOf(
                XCell.Text(data.employeeName, 3),
                XCell.Text(data.employeeId, 3),
                XCell.Text(data.oldPayLevel, 3),
                XCell.Num(oldBasic.toDouble(), 3),
                XCell.Text(data.newPayLevel, 3),
                XCell.Num(nextPay.second.toDouble(), 3),
                XCell.Formula("F5-D5", (nextPay.second - oldBasic).toDouble(), 4)
            )
        )
        return "Office_Order_7Pay" to rows
    }

    private fun buildMdmAllUcSheet(data: FormatFormData): Pair<String, List<List<XCell>>> {
        val mdm = CalculationEngine.calculateMdm(data)
        val rows = listOf(
            listOf(XCell.Text("MDM Automatic All UC — ${data.schoolName} (${data.udiseCode})", 1)),
            listOf(XCell.Text("Month: ${mdm.month} | Days: ${data.mdmWorkingDays}", 0)),
            emptyList(),
            listOf("Category", "Meals", "Grain (kg)", "Cooking Cost (INR)").map { XCell.Text(it, 2) },
            listOf(XCell.Text("Primary (1-5)", 3), XCell.Num(mdm.primaryStudentMeals.toDouble(), 3), XCell.Num(mdm.primaryGrainKg, 3), XCell.Num(mdm.primaryCookingCost, 3)),
            listOf(XCell.Text("Upper Primary (6-8)", 3), XCell.Num(mdm.upperPrimaryStudentMeals.toDouble(), 3), XCell.Num(mdm.upperPrimaryGrainKg, 3), XCell.Num(mdm.upperPrimaryCookingCost, 3)),
            listOf(XCell.Text("TOTAL UC", 4), XCell.Formula("SUM(B5:B6)", (mdm.primaryStudentMeals + mdm.upperPrimaryStudentMeals).toDouble(), 4), XCell.Formula("SUM(C5:C6)", mdm.totalGrainKg, 4), XCell.Formula("SUM(D5:D6)", mdm.totalCookingCost, 4))
        )
        return "MDM_All_UC" to rows
    }

    private fun buildLeavesShortChartSheet(data: FormatFormData): Pair<String, List<List<XCell>>> {
        val rows = listOf(
            listOf(XCell.Text("Rajasthan Service Rules — Leaves Short Chart", 1)),
            listOf(XCell.Text("School: ${data.schoolName} | Session: ${data.academicSession}", 0)),
            emptyList(),
            listOf("Leave Type", "Annual Entitlement", "Max Accumulation", "Sanction Authority").map { XCell.Text(it, 2) },
            listOf(XCell.Text("Casual Leave (CL)", 3), XCell.Text("15 Days / Session", 3), XCell.Text("Non-accumulative", 3), XCell.Text("Principal / HM", 3)),
            listOf(XCell.Text("Privilege Leave (PL)", 3), XCell.Text("15 Days (Teachers) / 30", 3), XCell.Text("300 Days", 3), XCell.Text("DDO / DEO", 3)),
            listOf(XCell.Text("Half Pay / Commuted", 3), XCell.Text("20 Days / Year", 3), XCell.Text("Unlimited", 3), XCell.Text("DDO / DEO", 3)),
            listOf(XCell.Text("Child Care Leave (CCL)", 3), XCell.Text("Up to 730 Days", 3), XCell.Text("Till Child 18 Yrs", 3), XCell.Text("Appointing / DEO", 3)),
            listOf(XCell.Text("Maternity / Paternity", 3), XCell.Text("180 Days / 15 Days", 3), XCell.Text("Per Child (Max 2)", 3), XCell.Text("DDO / Principal", 3))
        )
        return "Leaves_Chart" to rows
    }

    private fun buildSurveyOrHlbSheet(format: FormatItem, data: FormatFormData): Pair<String, List<List<XCell>>> {
        val rows = listOf(
            listOf(XCell.Text("${format.title} — ${data.schoolName}", 1)),
            listOf(XCell.Text("UDISE: ${data.udiseCode} | Block: ${data.block} | District: ${data.district}", 0)),
            emptyList(),
            listOf("S.N.", "Student / Record Name", "Father Name", "Class / Group", "SR No.", "Session").map { XCell.Text(it, 2) },
            listOf(XCell.Num(1.0, 3), XCell.Text(data.studentName, 3), XCell.Text(data.studentFatherName, 3), XCell.Text(data.studentClass, 3), XCell.Text(data.srNumber, 3), XCell.Text(data.academicSession, 3))
        )
        return "Survey_Record" to rows
    }
}
