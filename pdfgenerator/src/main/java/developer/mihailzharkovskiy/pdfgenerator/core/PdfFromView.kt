package developer.mihailzharkovskiy.pdfgenerator.core

import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.view.View
import developer.mihailzharkovskiy.pdfgenerator.settings.page.PageSize

internal class PdfFromView {
    private val screenshotCreator = Screenshot()

    fun create(view: View, pageSize: PageSize): PdfDocument {
        val bitmap = screenshotCreator.takeScreenshotScreen(view)
        return generatePdfOnOnePage(bitmap, pageSize)
    }

    private fun generatePdfOnOnePage(bitmap: Bitmap, pageSize: PageSize): PdfDocument {
        val scaleBitmap = scaleBitmap(bitmap, pageSize.width, pageSize.height)
        val document = PdfDocument()
        document.generatePdfPage(scaleBitmap)
        return document
    }

    private fun PdfDocument.generatePdfPage(bitmap: Bitmap) {
        val pageNumber = 1
        val pageInfo =
            PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, pageNumber).create()
        val page = this.startPage(pageInfo)
        page.canvas.drawBitmap(bitmap, 0f, 0f, null)
        this.finishPage(page)
    }

    private fun scaleBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }
}