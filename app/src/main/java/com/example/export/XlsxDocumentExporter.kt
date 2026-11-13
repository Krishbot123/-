package com.example.export

import com.example.data.CalculationEngine
import com.example.model.BuilderTemplateType
import com.example.model.FormatFormData
import com.example.model.FormatItem
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object XlsxDocumentExporter {

    fun export(format: FormatItem, data: FormatFormData, outFile: File): File {
        val (sheetName, rows) = buildSheetRows(format, data)
        val sheetXml = buildWorksheetXml(rows)

        outFile.parentFile?.mkdirs()
        ZipOutputStream(FileOutputStream(outFile)).use { zip ->
            writeZipEntry(zip, "[Content_Types].xml", CONTENT_TYPES_XML)
            writeZipEntry(zip, "_rels/.rels", ROOT_RELS_XML)
            writeZipEntry(zip, "xl/workbook.xml", workbookXml(sheetName))
            writeZipEntry(zip, "xl/_rels/workbook.xml.rels", WORKBOOK_RELS_XML)
            writeZipEntry(zip, "xl/styles.xml", STYLES_XML)
            writeZipEntry(zip, "xl/worksheets/sheet1.xml", sheetXml)
        }
        return outFile
    }

    private fun buildSheetRows(
        format: FormatItem,
        data: FormatFormData
    ): Pair<String, List<List<XCell>>> {
        return when (format.templateType) {
            BuilderTemplateType.ARREAR_SHEET,
            BuilderTemplateType.AUTO_SALARY_DA_ARREAR,
            BuilderTemplateType.SIXTH_PAY_ARREAR,
            BuilderTemplateType.FIFTH_PAY_ARREAR -> buildArrearSheet(format, data)

            BuilderTemplateType.INCOME_TAX_CALC -> buildIncomeTaxSheet(data)

            BuilderTemplateType.PL_SURRENDER -> buildPlSurrenderSheet(data)

            BuilderTemplateType.MDM_MONTHLY -> buildMdmSheet(data)

            BuilderTemplateType.ANNUAL_INCREMENT -> buildAnnualIncrementSheet(data)

            else -> {
                ExtraXlsxSheets.build(format, data) ?: run {
                    val doc = DocumentContentBuilder.build(format, data)
                    val rows = mutableListOf<List<XCell>>()
                    rows.add(listOf(XCell.Text(doc.headerDepartment, 1)))
                    rows.add(listOf(XCell.Text(doc.documentTitle, 1)))
                    rows.add(listOf(XCell.Text(doc.refNumberLine, 0)))
                    doc.sections.forEach { sec ->
                        if (sec.heading.isNotBlank()) rows.add(listOf(XCell.Text(sec.heading, 2)))
                        sec.bodyLines.forEach { line -> rows.add(listOf(XCell.Text(line, 3))) }
                        if (sec.tableHeaders.isNotEmpty()) {
                            rows.add(sec.tableHeaders.map { XCell.Text(it, 2) })
                            sec.tableRows.forEach { r -> rows.add(r.map { XCell.Text(it, 3) }) }
                        }
                    }
                    "RajTeachers" to rows
                }
            }
        }
    }

    private fun buildArrearSheet(format: FormatItem, data: FormatFormData): Pair<String, List<List<XCell>>> {
        val summary = CalculationEngine.calculateArrear(data)
        val rows = mutableListOf<List<XCell>>()
        rows.add(listOf(XCell.Text("${data.schoolName} — ${format.title}", 1)))
        rows.add(listOf(XCell.Text("Employee: ${data.employeeName} (${data.employeeId}) | ${data.designation} | Block: ${data.block}, ${data.district}", 0)))
        rows.add(listOf(XCell.Text("Drawn: ${data.oldPayLevel} (DA ${data.oldDaPercent}%) | Due: ${data.newPayLevel} (DA ${data.newDaPercent}%) | HRA: ${data.hraPercent}%", 0)))
        rows.add(emptyList())
        rows.add(
            listOf(
                "S.N.", "Month",
                "Drawn Basic", "Drawn DA", "Drawn HRA", "Drawn Total",
                "Due Basic", "Due DA", "Due HRA", "Due Total",
                "Diff (Due-Drawn)", "NPS/GPF 10%", "Net Payable"
            ).map { XCell.Text(it, 2) }
        )

        val startRowIdx = 6 // 1-indexed row in Excel where data starts
        summary.rows.forEachIndexed { idx, r ->
            val rowNum = startRowIdx + idx
            rows.add(
                listOf(
                    XCell.Num(r.serial.toDouble(), 3),
                    XCell.Text(r.monthLabel, 3),
                    XCell.Num(r.drawnBasic.toDouble(), 3),
                    XCell.Num(r.drawnDa.toDouble(), 3),
                    XCell.Num(r.drawnHra.toDouble(), 3),
                    XCell.Formula("SUM(C$rowNum:E$rowNum)", r.drawnGross.toDouble(), 3),
                    XCell.Num(r.dueBasic.toDouble(), 3),
                    XCell.Num(r.dueDa.toDouble(), 3),
                    XCell.Num(r.dueHra.toDouble(), 3),
                    XCell.Formula("SUM(G$rowNum:I$rowNum)", r.dueGross.toDouble(), 3),
                    XCell.Formula("J$rowNum-F$rowNum", r.grossDifference.toDouble(), 3),
                    XCell.Num(r.npsDeduction.toDouble(), 3),
                    XCell.Formula("K$rowNum-L$rowNum", r.netPayable.toDouble(), 3)
                )
            )
        }

        val endRowIdx = startRowIdx + summary.rows.size - 1
        rows.add(
            listOf(
                XCell.Text("", 4),
                XCell.Text("TOTAL", 4),
                XCell.Formula("SUM(C$startRowIdx:C$endRowIdx)", summary.rows.sumOf { it.drawnBasic }.toDouble(), 4),
                XCell.Formula("SUM(D$startRowIdx:D$endRowIdx)", summary.rows.sumOf { it.drawnDa }.toDouble(), 4),
                XCell.Formula("SUM(E$startRowIdx:E$endRowIdx)", summary.rows.sumOf { it.drawnHra }.toDouble(), 4),
                XCell.Formula("SUM(F$startRowIdx:F$endRowIdx)", summary.totalDrawn.toDouble(), 4),
                XCell.Formula("SUM(G$startRowIdx:G$endRowIdx)", summary.rows.sumOf { it.dueBasic }.toDouble(), 4),
                XCell.Formula("SUM(H$startRowIdx:H$endRowIdx)", summary.rows.sumOf { it.dueDa }.toDouble(), 4),
                XCell.Formula("SUM(I$startRowIdx:I$endRowIdx)", summary.rows.sumOf { it.dueHra }.toDouble(), 4),
                XCell.Formula("SUM(J$startRowIdx:J$endRowIdx)", summary.totalDue.toDouble(), 4),
                XCell.Formula("SUM(K$startRowIdx:K$endRowIdx)", summary.totalGrossDifference.toDouble(), 4),
                XCell.Formula("SUM(L$startRowIdx:L$endRowIdx)", summary.totalNpsDeduction.toDouble(), 4),
                XCell.Formula("SUM(M$startRowIdx:M$endRowIdx)", summary.totalNetPayable.toDouble(), 4)
            )
        )
        return "Arrear_Sheet" to rows
    }

    private fun buildIncomeTaxSheet(data: FormatFormData): Pair<String, List<List<XCell>>> {
        val tax = CalculationEngine.calculateIncomeTax(data)
        val rows = mutableListOf<List<XCell>>()
        rows.add(listOf(XCell.Text("Income Tax Calculation Sheet — ${tax.financialYear}", 1)))
        rows.add(listOf(XCell.Text("${data.employeeName} | ${data.designation} | ID: ${data.employeeId} | School: ${data.schoolName}", 0)))
        rows.add(emptyList())
        rows.add(listOf("Particulars", "Old Regime (INR)", "New Regime (INR)").map { XCell.Text(it, 2) })
        rows.add(listOf(XCell.Text("Gross Annual Salary", 3), XCell.Num(tax.grossIncome.toDouble(), 3), XCell.Num(tax.grossIncome.toDouble(), 3)))
        rows.add(listOf(XCell.Text("Standard & Chapter VI-A Deductions", 3), XCell.Num(tax.oldTotalDeductions.toDouble(), 3), XCell.Num(tax.newStandardDeduction.toDouble(), 3)))
        rows.add(listOf(XCell.Text("Taxable Income", 3), XCell.Formula("MAX(0,B5-B6)", tax.oldTaxableIncome.toDouble(), 3), XCell.Formula("MAX(0,C5-C6)", tax.newTaxableIncome.toDouble(), 3)))
        rows.add(listOf(XCell.Text("Income Tax (after 87A rebate)", 3), XCell.Num(tax.oldBaseTax.toDouble(), 3), XCell.Num(tax.newBaseTax.toDouble(), 3)))
        rows.add(listOf(XCell.Text("Health & Education Cess (4%)", 3), XCell.Formula("ROUND(B8*0.04,0)", tax.oldCess.toDouble(), 3), XCell.Formula("ROUND(C8*0.04,0)", tax.newCess.toDouble(), 3)))
        rows.add(listOf(XCell.Text("Total Tax Payable", 4), XCell.Formula("B8+B9", tax.oldTotalTax.toDouble(), 4), XCell.Formula("C8+C9", tax.newTotalTax.toDouble(), 4)))
        rows.add(emptyList())
        rows.add(listOf(XCell.Text("Recommended Regime", 4), XCell.Text(tax.recommendedRegime, 4), XCell.Text("Savings: INR ${tax.taxSavings}", 4)))
        return "Income_Tax" to rows
    }

    private fun buildPlSurrenderSheet(data: FormatFormData): Pair<String, List<List<XCell>>> {
        val basic = data.newBasicPay.toDoubleOrNull() ?: data.oldBasicPay.toDoubleOrNull() ?: 44300.0
        val daPct = data.newDaPercent.toDoubleOrNull() ?: 55.0
        val daAmt = (basic * daPct / 100.0)
        val totalMonthly = basic + daAmt
        val payable = (totalMonthly / 30.0) * 15.0
        val rows = listOf(
            listOf(XCell.Text("15 Days PL Surrender Bill Sheet — ${data.schoolName}", 1)),
            listOf(XCell.Text("Employee: ${data.employeeName} (${data.employeeId}) | ${data.designation}", 0)),
            emptyList(),
            listOf("Employee Name", "Pay Level", "Basic Pay", "DA %", "DA Amount", "Total (Basic+DA)", "Days", "Net Payable (15 Days)").map { XCell.Text(it, 2) },
            listOf(
                XCell.Text(data.employeeName, 3),
                XCell.Text(data.newPayLevel, 3),
                XCell.Num(basic, 3),
                XCell.Num(daPct, 3),
                XCell.Formula("ROUND(C5*D5/100,0)", daAmt, 3),
                XCell.Formula("C5+E5", totalMonthly, 3),
                XCell.Num(15.0, 3),
                XCell.Formula("ROUND(F5/30*G5,0)", payable, 4)
            )
        )
        return "PL_Surrender" to rows
    }

    private fun buildMdmSheet(data: FormatFormData): Pair<String, List<List<XCell>>> {
        val mdm = CalculationEngine.calculateMdm(data)
        val primary = data.mdmPrimaryCount.toDoubleOrNull() ?: 120.0
        val upper = data.mdmUpperPrimaryCount.toDoubleOrNull() ?: 95.0
        val days = data.mdmWorkingDays.toDoubleOrNull() ?: 22.0
        val rows = listOf(
            listOf(XCell.Text("MDM Monthly Sheet — ${data.schoolName} (UDISE: ${data.udiseCode})", 1)),
            listOf(XCell.Text("Month: ${data.mdmMonth} | Working Days: ${data.mdmWorkingDays}", 0)),
            emptyList(),
            listOf("Class Group", "Daily Students", "Working Days", "Total Meals", "Grain Rate (kg)", "Total Grain (kg)", "Cost Rate (INR)", "Cooking Cost (INR)").map { XCell.Text(it, 2) },
            listOf(
                XCell.Text("Primary (1-5)", 3),
                XCell.Num(primary, 3),
                XCell.Num(days, 3),
                XCell.Formula("B5*C5", mdm.primaryStudentMeals.toDouble(), 3),
                XCell.Num(0.10, 3),
                XCell.Formula("D5*E5", mdm.primaryGrainKg, 3),
                XCell.Num(6.19, 3),
                XCell.Formula("D5*G5", mdm.primaryCookingCost, 3)
            ),
            listOf(
                XCell.Text("Upper Primary (6-8)", 3),
                XCell.Num(upper, 3),
                XCell.Num(days, 3),
                XCell.Formula("B6*C6", mdm.upperPrimaryStudentMeals.toDouble(), 3),
                XCell.Num(0.15, 3),
                XCell.Formula("D6*E6", mdm.upperPrimaryGrainKg, 3),
                XCell.Num(9.29, 3),
                XCell.Formula("D6*G6", mdm.upperPrimaryCookingCost, 3)
            ),
            listOf(
                XCell.Text("TOTAL", 4),
                XCell.Formula("SUM(B5:B6)", primary + upper, 4),
                XCell.Num(days, 4),
                XCell.Formula("SUM(D5:D6)", (mdm.primaryStudentMeals + mdm.upperPrimaryStudentMeals).toDouble(), 4),
                XCell.Text("-", 4),
                XCell.Formula("SUM(F5:F6)", mdm.totalGrainKg, 4),
                XCell.Text("-", 4),
                XCell.Formula("SUM(H5:H6)", mdm.totalCookingCost, 4)
            )
        )
        return "MDM_Monthly" to rows
    }

    private fun buildAnnualIncrementSheet(data: FormatFormData): Pair<String, List<List<XCell>>> {
        val oldBasic = data.oldBasicPay.toLongOrNull() ?: 39300L
        val nextPay = CalculationEngine.calculateAcpFixationPay(data.oldPayLevel, oldBasic, data.oldPayLevel)
        val rows = listOf(
            listOf(XCell.Text("Annual Increment Order Sheet (01 July) — ${data.schoolName}", 1)),
            listOf(XCell.Text("Order No: ${data.orderNumber} | Date: ${data.orderDate}", 0)),
            emptyList(),
            listOf("S.N.", "Employee Name", "Employee ID", "Designation", "Pay Level", "Existing Basic", "New Basic (1 July)", "Diff").map { XCell.Text(it, 2) },
            listOf(
                XCell.Num(1.0, 3),
                XCell.Text(data.employeeName, 3),
                XCell.Text(data.employeeId, 3),
                XCell.Text(data.designation, 3),
                XCell.Text(data.oldPayLevel, 3),
                XCell.Num(oldBasic.toDouble(), 3),
                XCell.Num(nextPay.first.toDouble(), 3),
                XCell.Formula("G5-F5", (nextPay.first - oldBasic).toDouble(), 4)
            )
        )
        return "Increment_Sheet" to rows
    }

    private fun buildWorksheetXml(rows: List<List<XCell>>): String {
        val sb = StringBuilder()
        sb.append("""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>""")
        sb.append("""<worksheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">""")
        sb.append("""<cols>""")
        for (c in 1..13) {
            val width = if (c == 1) 18 else 16
            sb.append("""<col min="$c" max="$c" width="$width" customWidth="1"/>""")
        }
        sb.append("""</cols>""")
        sb.append("""<sheetData>""")
        rows.forEachIndexed { rIdx, row ->
            val rNum = rIdx + 1
            sb.append("""<row r="$rNum">""")
            row.forEachIndexed { cIdx, cell ->
                val ref = "${colLetter(cIdx)}$rNum"
                when (cell) {
                    is XCell.Text -> {
                        sb.append("""<c r="$ref" s="${cell.style}" t="inlineStr"><is><t>${escapeXml(cell.value)}</t></is></c>""")
                    }
                    is XCell.Num -> {
                        val formatted = if (cell.value % 1.0 == 0.0) cell.value.toLong().toString() else String.format(java.util.Locale.US, "%.2f", cell.value)
                        sb.append("""<c r="$ref" s="${cell.style}"><v>$formatted</v></c>""")
                    }
                    is XCell.Formula -> {
                        val formatted = if (cell.cachedValue % 1.0 == 0.0) cell.cachedValue.toLong().toString() else String.format(java.util.Locale.US, "%.2f", cell.cachedValue)
                        sb.append("""<c r="$ref" s="${cell.style}"><f>${escapeXml(cell.formula)}</f><v>$formatted</v></c>""")
                    }
                }
            }
            sb.append("""</row>""")
        }
        sb.append("""</sheetData></worksheet>""")
        return sb.toString()
    }

    private fun colLetter(index: Int): String = ('A' + index).toString()

    private fun escapeXml(input: String): String = input
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&apos;")

    private fun writeZipEntry(zip: ZipOutputStream, name: String, content: String) {
        zip.putNextEntry(ZipEntry(name))
        zip.write(content.toByteArray(Charsets.UTF_8))
        zip.closeEntry()
    }

    private fun workbookXml(sheetName: String): String = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">
  <sheets>
    <sheet name="${escapeXml(sheetName.take(30))}" sheetId="1" r:id="rId1"/>
  </sheets>
</workbook>"""

    private const val CONTENT_TYPES_XML = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
  <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
  <Default Extension="xml" ContentType="application/xml"/>
  <Override PartName="/xl/workbook.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"/>
  <Override PartName="/xl/worksheets/sheet1.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml"/>
  <Override PartName="/xl/styles.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml"/>
</Types>"""

    private const val ROOT_RELS_XML = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
  <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="xl/workbook.xml"/>
</Relationships>"""

    private const val WORKBOOK_RELS_XML = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
  <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet" Target="worksheets/sheet1.xml"/>
  <Relationship Id="rId2" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml"/>
</Relationships>"""

    private const val STYLES_XML = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<styleSheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">
  <fonts count="4">
    <font><sz val="11"/><name val="Calibri"/></font>
    <font><b/><sz val="13"/><color rgb="FF0B3B49"/><name val="Calibri"/></font>
    <font><b/><sz val="11"/><color rgb="FFFFFFFF"/><name val="Calibri"/></font>
    <font><b/><sz val="11"/><color rgb="FF0F172A"/><name val="Calibri"/></font>
  </fonts>
  <fills count="4">
    <fill><patternFill patternType="none"/></fill>
    <fill><patternFill patternType="gray125"/></fill>
    <fill><patternFill patternType="solid"><fgColor rgb="FF0B3B49"/></patternFill></fill>
    <fill><patternFill patternType="solid"><fgColor rgb="FFE2E8F0"/></patternFill></fill>
  </fills>
  <borders count="2">
    <border><left/><right/><top/><bottom/><diagonal/></border>
    <border>
      <left style="thin"><color rgb="FF94A3B8"/></left>
      <right style="thin"><color rgb="FF94A3B8"/></right>
      <top style="thin"><color rgb="FF94A3B8"/></top>
      <bottom style="thin"><color rgb="FF94A3B8"/></bottom>
      <diagonal/>
    </border>
  </borders>
  <cellStyleXfs count="1"><xf numFmtId="0" fontId="0" fillId="0" borderId="0"/></cellStyleXfs>
  <cellXfs count="5">
    <xf numFmtId="0" fontId="0" fillId="0" borderId="0" xfId="0"/>
    <xf numFmtId="0" fontId="1" fillId="0" borderId="0" xfId="0" applyFont="1"/>
    <xf numFmtId="0" fontId="2" fillId="2" borderId="1" xfId="0" applyFont="1" applyFill="1" applyBorder="1"/>
    <xf numFmtId="0" fontId="0" fillId="0" borderId="1" xfId="0" applyBorder="1"/>
    <xf numFmtId="0" fontId="3" fillId="3" borderId="1" xfId="0" applyFont="1" applyFill="1" applyBorder="1"/>
  </cellXfs>
</styleSheet>"""
}
