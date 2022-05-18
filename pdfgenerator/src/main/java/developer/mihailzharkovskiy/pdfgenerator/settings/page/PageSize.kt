package developer.mihailzharkovskiy.pdfgenerator.settings.page

import android.view.View

/**
 * Да выглядит криво, но перебрав кучу вариантов,данный вариант оказался наиболее предпочтительным
 * + позволяет не проверять коректность ввода
 **/
class PageSize private constructor(
    val width: Int,
    val height: Int,
) {

//    enum class Format(val width: Int, val height: Int) {
//        A5(437, 620),
//        A4(620, 877),
//        A3(877, 1240),
//        A2(1240, 1754),
//        A1(1754, 2483);
//    }

    companion object {
        fun set(format: FormatPage) = PageSize(format.width, format.height)

        /**используй чтобы размеры страницы pdf были равны размерам "скриншота"**/
        fun set(view: View) = PageSize(view.width, view.height)
    }
}








