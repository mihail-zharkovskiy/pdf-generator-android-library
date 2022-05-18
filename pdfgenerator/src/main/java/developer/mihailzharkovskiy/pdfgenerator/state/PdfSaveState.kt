package developer.mihailzharkovskiy.pdfgenerator.state

import android.net.Uri

internal sealed class PdfSaveState() {
    class Error(val message: String) : PdfSaveState()
    class Success(val message: String, val uriSaveDocument: Uri) : PdfSaveState()
}