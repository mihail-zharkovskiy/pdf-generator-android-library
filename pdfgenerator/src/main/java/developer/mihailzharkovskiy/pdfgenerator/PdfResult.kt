package developer.mihailzharkovskiy.pdfgenerator

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher

sealed class PdfResult {
    class NeedSelectFolder(val selectFolder: (launcher: ActivityResultLauncher<Intent>) -> Unit) :
        PdfResult()

    class Success(val pdfUri: Uri, val message: String) : PdfResult()
    class Error(val message: String) : PdfResult()
}
