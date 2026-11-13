package com.example.export

import com.example.model.GeneratedDocumentContent
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object DocxDocumentExporter {

    fun export(content: GeneratedDocumentContent, outFile: File): File {
        val documentXml = buildDocumentXml(content)
        outFile.parentFile?.mkdirs()
        ZipOutputStream(FileOutputStream(outFile)).use { zip ->
            writeZipEntry(zip, "[Content_Types].xml", CONTENT_TYPES_XML)
            writeZipEntry(zip, "_rels/.rels", ROOT_RELS_XML)
            writeZipEntry(zip, "word/document.xml", documentXml)
        }
        return outFile
    }

    private fun buildDocumentXml(content: GeneratedDocumentContent): String {
        val sb = StringBuilder()
        sb.append("""<?xml version="1.0" encoding="UTF-8" standalone="yes"?>""")
        sb.append("""<w:document xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main">""")
        sb.append("""<w:body>""")

        // Centered Department Header
        sb.append(paragraph(content.headerDepartment, bold = true, sizeHalfPts = 28, center = true, colorHex = "0B3B49"))
        sb.append(paragraph(content.headerSubOffice, bold = false, sizeHalfPts = 20, center = true, colorHex = "475569"))
        sb.append(paragraph(content.refNumberLine, bold = true, sizeHalfPts = 20, center = true, colorHex = "1E293B"))
        sb.append(paragraph(content.documentTitle, bold = true, sizeHalfPts = 26, center = true, colorHex = "0F172A"))
        sb.append(paragraph("", bold = false, sizeHalfPts = 12, center = false))

        content.sections.forEach { sec ->
            if (sec.heading.isNotBlank()) {
                sb.append(paragraph(sec.heading, bold = true, sizeHalfPts = 22, center = false, colorHex = "0B3B49"))
            }
            sec.bodyLines.forEach { line ->
                sb.append(paragraph(line, bold = false, sizeHalfPts = 21, center = false, colorHex = "0F172A"))
            }
            if (sec.tableHeaders.isNotEmpty()) {
                sb.append(buildTable(sec.tableHeaders, sec.tableRows))
                sb.append(paragraph("", bold = false, sizeHalfPts = 14, center = false))
            }
        }

        sb.append(paragraph("", bold = false, sizeHalfPts = 20, center = false))
        // Signature two-column borderless table
        sb.append("""<w:tbl><w:tblPr><w:tblW w:w="9500" w:type="dxa"/></w:tblPr>""")
        sb.append("<w:tr>")
        sb.append(tableCell(content.leftFooter, bold = true, shaded = false, alignRight = false))
        sb.append(tableCell(content.rightFooter, bold = true, shaded = false, alignRight = true))
        sb.append("</w:tr></w:tbl>")

        sb.append("""<w:sectPr><w:pgSz w:w="11906" w:h="16838"/><w:pgMar w:top="1000" w:right="1000" w:bottom="1000" w:left="1000"/></w:sectPr>""")
        sb.append("""</w:body></w:document>""")
        return sb.toString()
    }

    private fun buildTable(headers: List<String>, rows: List<List<String>>): String {
        val sb = StringBuilder()
        sb.append("""<w:tbl><w:tblPr><w:tblW w:w="9500" w:type="dxa"/><w:tblBorders>""")
        sb.append("""<w:top w:val="single" w:sz="6" w:space="0" w:color="64748B"/>""")
        sb.append("""<w:bottom w:val="single" w:sz="6" w:space="0" w:color="64748B"/>""")
        sb.append("""<w:left w:val="single" w:sz="6" w:space="0" w:color="64748B"/>""")
        sb.append("""<w:right w:val="single" w:sz="6" w:space="0" w:color="64748B"/>""")
        sb.append("""<w:insideH w:val="single" w:sz="4" w:space="0" w:color="94A3B8"/>""")
        sb.append("""<w:insideV w:val="single" w:sz="4" w:space="0" w:color="94A3B8"/>""")
        sb.append("""</w:tblBorders></w:tblPr>""")

        sb.append("<w:tr>")
        headers.forEach { h ->
            sb.append(tableCell(h, bold = true, shaded = true, alignRight = false))
        }
        sb.append("</w:tr>")

        rows.forEach { row ->
            sb.append("<w:tr>")
            headers.indices.forEach { idx ->
                sb.append(tableCell(row.getOrElse(idx) { "" }, bold = false, shaded = false, alignRight = false))
            }
            sb.append("</w:tr>")
        }
        sb.append("</w:tbl>")
        return sb.toString()
    }

    private fun tableCell(text: String, bold: Boolean, shaded: Boolean, alignRight: Boolean): String {
        val shd = if (shaded) """<w:shd w:val="clear" w:color="auto" w:fill="E2E8F0"/>""" else ""
        val jc = if (alignRight) """<w:jc w:val="right"/>""" else ""
        val bTag = if (bold) "<w:b/>" else ""
        val lines = text.split("\n")
        val paras = lines.joinToString("") { line ->
            """<w:p><w:pPr>$jc<w:spacing w:after="60"/></w:pPr><w:r><w:rPr>$bTag<w:sz w:val="20"/></w:rPr><w:t xml:space="preserve">${escapeXml(line)}</w:t></w:r></w:p>"""
        }
        return """<w:tc><w:tcPr>$shd<w:tcMar><w:top w:w="80" w:type="dxa"/><w:bottom w:w="80" w:type="dxa"/><w:left w:w="100" w:type="dxa"/><w:right w:w="100" w:type="dxa"/></w:tcMar></w:tcPr>$paras</w:tc>"""
    }

    private fun paragraph(
        text: String,
        bold: Boolean,
        sizeHalfPts: Int,
        center: Boolean,
        colorHex: String = "0F172A"
    ): String {
        val jc = if (center) """<w:jc w:val="center"/>""" else ""
        val bTag = if (bold) "<w:b/>" else ""
        return """<w:p><w:pPr>$jc<w:spacing w:after="110" w:line="276" w:lineRule="auto"/></w:pPr><w:r><w:rPr>$bTag<w:color w:val="$colorHex"/><w:sz w:val="$sizeHalfPts"/></w:rPr><w:t xml:space="preserve">${escapeXml(text)}</w:t></w:r></w:p>"""
    }

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

    private const val CONTENT_TYPES_XML = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
  <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
  <Default Extension="xml" ContentType="application/xml"/>
  <Override PartName="/word/document.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"/>
</Types>"""

    private const val ROOT_RELS_XML = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
  <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="word/document.xml"/>
</Relationships>"""
}
