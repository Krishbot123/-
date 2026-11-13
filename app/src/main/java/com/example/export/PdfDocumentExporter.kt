package com.example.export

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.example.model.GeneratedDocumentContent
import java.io.File
import java.io.FileOutputStream

object PdfDocumentExporter {

    private const val PAGE_WIDTH = 595   // A4 portrait points
    private const val PAGE_HEIGHT = 842
    private const val MARGIN_X = 38f
    private const val MARGIN_TOP = 38f
    private const val MARGIN_BOTTOM = 44f
    private const val CONTENT_WIDTH = PAGE_WIDTH - (MARGIN_X * 2)

    fun export(content: GeneratedDocumentContent, outFile: File): File {
        val pdf = PdfDocument()
        var pageNumber = 1
        var page = pdf.startPage(PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create())
        var canvas = page.canvas
        var y = MARGIN_TOP

        val titlePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#0B3B49")
            textSize = 14f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val subPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#334155")
            textSize = 9.5f
        }
        val docTitlePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#0F172A")
            textSize = 12.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val sectionHeaderPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#0B3B49")
            textSize = 10.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val bodyPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#1E293B")
            textSize = 10f
        }
        val tableHeaderPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 8.5f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val tableCellPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#0F172A")
            textSize = 8.5f
        }
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#94A3B8")
            style = Paint.Style.STROKE
            strokeWidth = 0.8f
        }
        val fillTealPaint = Paint().apply {
            color = Color.parseColor("#0B3B49")
            style = Paint.Style.FILL
        }
        val fillTintPaint = Paint().apply {
            color = Color.parseColor("#F1F5F9")
            style = Paint.Style.FILL
        }

        fun ensureSpace(neededHeight: Float) {
            if (y + neededHeight > PAGE_HEIGHT - MARGIN_BOTTOM) {
                pdf.finishPage(page)
                pageNumber++
                page = pdf.startPage(PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create())
                canvas = page.canvas
                y = MARGIN_TOP
            }
        }

        fun drawCenteredText(text: String, paint: TextPaint, extraSpaceAfter: Float = 4f) {
            val layout = StaticLayout.Builder.obtain(text, 0, text.length, paint, CONTENT_WIDTH.toInt())
                .setAlignment(Layout.Alignment.ALIGN_CENTER)
                .setLineSpacing(2f, 1.08f)
                .build()
            ensureSpace(layout.height.toFloat() + extraSpaceAfter)
            canvas.save()
            canvas.translate(MARGIN_X, y)
            layout.draw(canvas)
            canvas.restore()
            y += layout.height + extraSpaceAfter
        }

        fun drawLeftText(text: String, paint: TextPaint, width: Float = CONTENT_WIDTH, offsetX: Float = MARGIN_X, extraSpaceAfter: Float = 5f) {
            val layout = StaticLayout.Builder.obtain(text, 0, text.length, paint, width.toInt())
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(2f, 1.15f)
                .build()
            ensureSpace(layout.height.toFloat() + extraSpaceAfter)
            canvas.save()
            canvas.translate(offsetX, y)
            layout.draw(canvas)
            canvas.restore()
            y += layout.height + extraSpaceAfter
        }

        // Official Header Box
        drawCenteredText(content.headerDepartment, titlePaint, 3f)
        drawCenteredText(content.headerSubOffice, subPaint, 6f)
        canvas.drawLine(MARGIN_X, y, PAGE_WIDTH - MARGIN_X, y, borderPaint)
        y += 8f

        drawCenteredText(content.refNumberLine, subPaint, 8f)
        drawCenteredText(content.documentTitle, docTitlePaint, 10f)

        // Sections
        content.sections.forEach { section ->
            if (section.heading.isNotBlank()) {
                ensureSpace(22f)
                canvas.drawRect(MARGIN_X, y, PAGE_WIDTH - MARGIN_X, y + 18f, fillTintPaint)
                canvas.drawText(section.heading, MARGIN_X + 6f, y + 13f, sectionHeaderPaint)
                y += 24f
            }

            section.bodyLines.forEach { line ->
                drawLeftText(line, bodyPaint, CONTENT_WIDTH, MARGIN_X, 5f)
            }

            if (section.tableHeaders.isNotEmpty()) {
                val cols = section.tableHeaders.size
                val colW = CONTENT_WIDTH / cols
                val headerLayouts = section.tableHeaders.map { h ->
                    StaticLayout.Builder.obtain(h, 0, h.length, tableHeaderPaint, (colW - 8).toInt().coerceAtLeast(24))
                        .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                        .build()
                }
                val headerH = (headerLayouts.maxOfOrNull { it.height } ?: 14) + 10f
                ensureSpace(headerH)
                canvas.drawRect(MARGIN_X, y, PAGE_WIDTH - MARGIN_X, y + headerH, fillTealPaint)
                headerLayouts.forEachIndexed { idx, layout ->
                    canvas.save()
                    canvas.translate(MARGIN_X + idx * colW + 4f, y + 5f)
                    layout.draw(canvas)
                    canvas.restore()
                }
                y += headerH

                section.tableRows.forEachIndexed { rIdx, row ->
                    val cellLayouts = (0 until cols).map { cIdx ->
                        val cellText = row.getOrElse(cIdx) { "" }
                        StaticLayout.Builder.obtain(cellText, 0, cellText.length, tableCellPaint, (colW - 8).toInt().coerceAtLeast(24))
                            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                            .build()
                    }
                    val rowH = (cellLayouts.maxOfOrNull { it.height } ?: 14) + 8f
                    ensureSpace(rowH)
                    if (rIdx % 2 == 1) {
                        canvas.drawRect(MARGIN_X, y, PAGE_WIDTH - MARGIN_X, y + rowH, fillTintPaint)
                    }
                    canvas.drawRect(MARGIN_X, y, PAGE_WIDTH - MARGIN_X, y + rowH, borderPaint)
                    for (c in 1 until cols) {
                        val lx = MARGIN_X + c * colW
                        canvas.drawLine(lx, y, lx, y + rowH, borderPaint)
                    }
                    cellLayouts.forEachIndexed { cIdx, layout ->
                        canvas.save()
                        canvas.translate(MARGIN_X + cIdx * colW + 4f, y + 4f)
                        layout.draw(canvas)
                        canvas.restore()
                    }
                    y += rowH
                }
                y += 10f
            }
        }

        // Signature Footer
        y += 18f
        ensureSpace(56f)
        val halfW = (CONTENT_WIDTH / 2f) - 10f
        val leftLayout = StaticLayout.Builder.obtain(content.leftFooter, 0, content.leftFooter.length, bodyPaint, halfW.toInt())
            .setAlignment(Layout.Alignment.ALIGN_NORMAL).build()
        val rightLayout = StaticLayout.Builder.obtain(content.rightFooter, 0, content.rightFooter.length, bodyPaint, halfW.toInt())
            .setAlignment(Layout.Alignment.ALIGN_OPPOSITE).build()

        canvas.save()
        canvas.translate(MARGIN_X, y)
        leftLayout.draw(canvas)
        canvas.restore()

        canvas.save()
        canvas.translate(MARGIN_X + halfW + 20f, y)
        rightLayout.draw(canvas)
        canvas.restore()

        pdf.finishPage(page)

        outFile.parentFile?.mkdirs()
        FileOutputStream(outFile).use { fos ->
            pdf.writeTo(fos)
        }
        pdf.close()
        return outFile
    }
}
