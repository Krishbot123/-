package com.example

import com.example.data.CalculationEngine
import com.example.data.FormatsCatalog
import com.example.export.DocumentContentBuilder
import com.example.export.DocxDocumentExporter
import com.example.export.XlsxDocumentExporter
import com.example.model.DocumentFileType
import com.example.model.FormatCategory
import com.example.model.FormatFormData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.util.zip.ZipFile

class RajTeachersExportTest {

    @Test
    fun catalogHasAll15TeacherFormatsAndAllScreenshotCategories() {
        val screenshotFormats = FormatsCatalog.allFormats.filter { it.isFromScreenshot }
        assertEquals(15, screenshotFormats.size)
        assertEquals((1..15).toList(), screenshotFormats.map { it.serialNumber })

        assertEquals(3, FormatsCatalog.itemsByCategory(FormatCategory.GPF).size)
        assertEquals(3, FormatsCatalog.itemsByCategory(FormatCategory.MDM_RELATED).size)
        assertEquals(1, FormatsCatalog.itemsByCategory(FormatCategory.ALL_UC_FORMAT).size)
        assertEquals(7, FormatsCatalog.itemsByCategory(FormatCategory.GPA).size)
        assertEquals(6, FormatsCatalog.itemsByCategory(FormatCategory.SI_FORMS).size)
        assertEquals(7, FormatsCatalog.itemsByCategory(FormatCategory.RSR_RULES).size)
        assertEquals(7, FormatsCatalog.itemsByCategory(FormatCategory.SCHOOL_RELATED).size)
        assertEquals(7, FormatsCatalog.itemsByCategory(FormatCategory.NPS_OPS).size)
        assertEquals(6, FormatsCatalog.itemsByCategory(FormatCategory.COMPUTER_SOFTWARES).size)
        assertEquals(10, FormatsCatalog.itemsByCategory(FormatCategory.HITKARI_NIDHI).size)
        assertEquals(4, FormatsCatalog.itemsByCategory(FormatCategory.FINANCE_ORDERS).size)
        assertEquals(12, FormatsCatalog.itemsByCategory(FormatCategory.SDMC_SMC).size)
        assertEquals(17, FormatsCatalog.itemsByCategory(FormatCategory.LEAVE_ORDERS).size)
        assertEquals(13, FormatsCatalog.itemsByCategory(FormatCategory.OFFICE_FORMS).size)
        assertEquals(25, FormatsCatalog.itemsByCategory(FormatCategory.SHIKSHA_ADESH).size)
        assertEquals(10, FormatsCatalog.itemsByCategory(FormatCategory.LATEST_UPDATES).size)
        assertEquals(10, FormatsCatalog.itemsByCategory(FormatCategory.LATEST_FORMATS).size)
        assertEquals(10, FormatsCatalog.itemsByCategory(FormatCategory.ONLINE_SERVICES).size)
    }

    @Test
    fun arrearTaxPensionBonusAndTaCalculationsAreConsistent() {
        val data = FormatFormData(
            oldPayLevel = "L-10",
            oldBasicPay = "39300",
            newBasicPay = "60000",
            oldDaPercent = "60",
            newDaPercent = "63",
            hraPercent = "9",
            arrearMonths = "4",
            includeNpsDeduction = true,
            grossAnnualSalary = "950000",
            totalServiceYears = "30",
            commutationPercent = "40",
            bonusMonthsWorked = "12",
            taFareAmount = "1200",
            taDaDays = "3",
            taDailyRate = "450"
        )
        val arrear = CalculationEngine.calculateArrear(data)
        assertEquals(4, arrear.rows.size)
        assertTrue(arrear.totalNetPayable > 0L)
        assertEquals(arrear.totalDue - arrear.totalDrawn, arrear.totalGrossDifference)

        val tax = CalculationEngine.calculateIncomeTax(data)
        assertEquals(0L, tax.newTotalTax)

        val pension = CalculationEngine.calculatePension(data)
        assertEquals(30000L, pension.fullBasicPension)
        assertEquals(12000L, pension.commutedMonthlyPension)
        assertEquals(18000L, pension.residualMonthlyPension)
        assertTrue(pension.commutationLumpSum > 1000000L)
        assertTrue(pension.dcrgGratuity > 1000000L)

        val bonus = CalculationEngine.calculateBonus2025_26(data)
        assertTrue(bonus.isEligible)
        assertEquals(6774L, bonus.grossBonus)
        assertEquals(bonus.grossBonus, bonus.cashAmount75 + bonus.gpfAmount25)

        val ta = CalculationEngine.calculateTaBill(data)
        assertEquals(1350L, ta.totalDaAmount)
        assertEquals(2550L, ta.totalClaimAmount)
    }

    @Test
    fun xlsxAndDocxExportersGenerateValidOpenXmlArchivesForNewFormats() {
        val tempDir = File(System.getProperty("java.io.tmpdir"), "rajteachers_test_${System.currentTimeMillis()}")
        tempDir.mkdirs()
        try {
            val data = FormatFormData(employeeName = "Test Teacher", employeeId = "RJTEST01", oldBasicPay = "44300")
            val daFormat = FormatsCatalog.findById("lu_01_da_arrear_60_63")!!
            assertEquals(DocumentFileType.EXCEL, daFormat.fileType)
            val xlsxFile = XlsxDocumentExporter.export(daFormat, data, File(tempDir, "da_60_63.xlsx"))
            assertTrue(xlsxFile.exists() && xlsxFile.length() > 500)
            ZipFile(xlsxFile).use { zip ->
                val sheetXml = zip.getInputStream(zip.getEntry("xl/worksheets/sheet1.xml")).bufferedReader().readText()
                assertTrue(sheetXml.contains("Test Teacher"))
                assertTrue(sheetXml.contains("SUM("))
            }

            val bonusFormat = FormatsCatalog.findById("lu_02_adhoc_bonus_2025_26")!!
            val bonusXlsx = XlsxDocumentExporter.export(bonusFormat, data, File(tempDir, "bonus.xlsx"))
            assertTrue(bonusXlsx.exists() && bonusXlsx.length() > 500)

            val sdmcWord = FormatsCatalog.findById("sdmc_03_reg_affidavit_word")!!
            assertEquals(DocumentFileType.WORD, sdmcWord.fileType)
            val content = DocumentContentBuilder.build(sdmcWord, data)
            val docxFile = DocxDocumentExporter.export(content, File(tempDir, "sdmc.docx"))
            assertTrue(docxFile.exists() && docxFile.length() > 400)
            ZipFile(docxFile).use { zip ->
                val docXml = zip.getInputStream(zip.getEntry("word/document.xml")).bufferedReader().readText()
                assertTrue(docXml.contains("SDMC"))
            }
        } finally {
            tempDir.deleteRecursively()
        }
    }
}
