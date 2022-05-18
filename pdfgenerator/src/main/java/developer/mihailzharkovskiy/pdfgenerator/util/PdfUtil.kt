package developer.mihailzharkovskiy.pdfgenerator.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class PdfUtil {

    companion object {
        internal fun editPdfName(pdfName: String): String {
            val date = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
            val name = pdfName.lowercase().replace(" ", "_")
            return "$name($date).pdf"
        }

        /**
         * переданный в функцию код будет выполняться только если версия sdk >= 29
         * иначе функиция вернет null
         * **/
        internal fun <T> sdk29AndUp(onSdk29: () -> T): T? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) onSdk29() else null
        }

        fun openPdfDocumentInAnotherApp(path: Uri, context: Context) {
            val pdfIntent = Intent(Intent.ACTION_VIEW)
            pdfIntent.setDataAndType(path, "application/pdf")
            pdfIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            try {
                context.startActivity(pdfIntent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                Toast.makeText(context, "у вас нет приложения для работы с pdf", Toast.LENGTH_LONG)
                    .show()
            }
        }

    }
}