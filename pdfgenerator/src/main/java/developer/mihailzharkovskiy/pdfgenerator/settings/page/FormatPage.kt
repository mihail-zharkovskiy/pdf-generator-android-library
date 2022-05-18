package developer.mihailzharkovskiy.pdfgenerator.settings.page

enum class FormatPage(val width: Int, val height: Int) {
    A5(437, 620),
    A4(620, 877),
    A3(877, 1240),
    A2(1240, 1754),
    A1(1754, 2483);
}