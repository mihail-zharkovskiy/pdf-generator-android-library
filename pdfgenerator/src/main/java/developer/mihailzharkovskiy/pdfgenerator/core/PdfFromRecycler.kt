package developer.mihailzharkovskiy.pdfgenerator.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import developer.mihailzharkovskiy.pdfgenerator.settings.page.PageSize
import kotlin.math.ceil

internal class PdfFromRecycler {

    private val screenshotCreator = Screenshot()

    fun createOnOnePage(recycler: RecyclerView): PdfDocument {
        val bitmap = screenshotCreator.takeScreenshotRecyclerView(recycler)
        return generatePdfOnOnePage(bitmap)
    }

    fun createSplitIntoPages(
        recycler: RecyclerView,
        pageSize: PageSize,
        context: Context,
    ): PdfDocument {
        val bitmap = screenshotCreator.takeScreenshotRecyclerView(recycler)
        return generatePdfSplitIntoPages(bitmap, pageSize)
    }

    private fun generatePdfSplitIntoPages(bitmap: Bitmap, pageSize: PageSize): PdfDocument {
        val document = PdfDocument()
        val bitmapPages = splitBitmap(bitmap, pageSize)
        var pageNumber = 1
        bitmapPages.forEach { bitmapPage ->
            document.generatePdfPage(bitmapPage, pageNumber)
            pageNumber++
        }
        return document
    }

    private fun generatePdfOnOnePage(bitmap: Bitmap): PdfDocument {
        val document = PdfDocument()
        document.generatePdfPage(bitmap, 1)
        return document
    }

    private fun PdfDocument.generatePdfPage(bm: Bitmap, pageNumber: Int) {
        val pageInfo = PdfDocument.PageInfo.Builder(bm.width, bm.height, pageNumber).create()
        val page = this.startPage(pageInfo)
        page.canvas.drawBitmap(bm, 0f, 0f, null)
        this.finishPage(page)
    }

    private fun scaleBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    /**разделяет bitmap на части равные размерам одной pdf страницы**/
    private fun splitBitmap(bitmap: Bitmap, pageSize: PageSize): List<Bitmap> {
        val startX = 0
        val startY = 0
        var startYForSplit = 0
        val widthPage = pageSize.width
        val heightPage = pageSize.height

        val procentSzhatiya = when {
            bitmap.width > pageSize.width -> (pageSize.width * 100) / bitmap.width
            pageSize.width > bitmap.width -> (bitmap.width * 100) / pageSize.width
            else -> 100 //слуяай когда стороны равны
        }
        val heightBitmapForSplit = (bitmap.height * procentSzhatiya) / 100
        val widthBitmapForSplit = widthPage

        val bitmapForSplit = scaleBitmap(bitmap, widthBitmapForSplit, heightBitmapForSplit)

        var pageNumber = calculateNumberOfPages(bitmapForSplit, pageSize)
        val pages = mutableListOf<Bitmap>()

        Log.d("pages",
            "число страниц = $pageNumber \n высота сриншота = ${bitmapForSplit.height}\n высота станицы = $heightPage")

        while (pageNumber > 1) { // нужно гонять цыкл пока > 0 для этого надо додумать как нормално сжать высоту стандартной bitmap
            val pixelsArray = IntArray(widthPage * heightPage)
            val bitmapForPage = Bitmap.createBitmap(widthPage, heightPage, Bitmap.Config.ARGB_8888)

            bitmapForSplit.getPixels(pixelsArray,
                0,
                widthPage,
                startX,
                startYForSplit,
                widthPage,
                heightPage)
            bitmapForPage.setPixels(pixelsArray,
                0,
                widthPage,
                startX,
                startY,
                widthPage,
                heightPage)

            pages.add(bitmapForPage)
            startYForSplit += heightPage
            pageNumber--
        }
        return pages
    }

    /**
     * Основываясь на [pageSize] (pageSize - это выбранный размер одной страницы в создоваемом pdf),
     * расчитывает сколько страниц понадобиться чтобы отоброзить bitmap
     * @return число страниц
     * **/
    private fun calculateNumberOfPages(bitmap: Bitmap, pageSize: PageSize): Int {
        return when {
            bitmap.height > pageSize.height -> {
                val pages = bitmap.height.toDouble() / pageSize.height.toDouble()
                ceil(pages).toInt()
            }
            bitmap.height < pageSize.height -> {
                val pages = pageSize.height.toDouble() / bitmap.width.toDouble()
                ceil(pages).toInt()
            }
            else -> 1
        }
    }
}