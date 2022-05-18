package developer.mihailzharkovskiy.pdfgenerator.core

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal class Screenshot {
    /**
     * ДЕЛАЕТ ПОЛНОРАЗМЕРНЫЙ СКРИНШОТ RECYCLER VIEW.
     * принцип работы примерно тотже,что и у метода [takeScreenshotScreen]
     * только данный метод преднозначен для создания полномоштабного скриншота recycler view
     * Чтобы понять сделай в активити или фраменте примерно это:
     * 1) val bitmap = takeScreenshotRecyclerView(recyclerView) //recyclerView - элементы которого ухдят за экрын
     * 2) val image = ImageView(context)
     *    image.setImageBitmap(bitmap)
     *    AlertDialog.Builder(context).setView(image).show()
     * **/
    fun takeScreenshotRecyclerView(recycler: RecyclerView): Bitmap {
        recycler.measure(
            View.MeasureSpec.makeMeasureSpec(recycler.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        val bitmap = Bitmap.createBitmap(recycler.measuredWidth,
            recycler.measuredHeight,
            Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        recycler.draw(canvas)
        return bitmap
    }

    /**
     * ДЕЛАЕТ СКРИНШОТ ПЕРЕДАННОЙ VIEW
     * в качестве view можно передать любую вьюху
     * 1) если передать root view, то будет сделан "скриншот" всего видимого экрана
     * 2) если ередать например text view, то на "скриншотк" будет видно только эта view и
     * и размер этого "скриншота" будет равет размеру view
     * 3) необязательно передавать view из layout-а который виден в данный момент на экране,
     * можно например создать layout-шаблон для pdf и сделать вот так ->
     *
     *    val binding = PdfLayoutBinding.inflate(layoutInflater)
     *    binding.textView.text = "hello word"
     *    val bitmap = takeScreenshotScreen(binding.root)
     *
     * Чтобы лучше понять, просто побалуйся передавая в метод разные view,
     * а затем чтобы посмотреть получившийся сриншот сделай это ->
     *
     *    val bitmap = takeScreenshotScreen(...какаято вьюха...)
     *    val image = ImageView(context)
     *    image.setImageBitmap(bitmap)
     *    AlertDialog.Builder(context).setView(image).show()
     *
     * NB!!! если передать recycler view, то скриншот будет содержать только теле items
     * которые видны в данный момент, для полноразмерного сриншота recyclerview используй
     * [takeScreenshotRecyclerView]
     * **/
    fun takeScreenshotScreen(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}