package developer.mihailzharkovskiy.pdfgenerator.core

import android.content.ContentResolver
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import developer.mihailzharkovskiy.pdfgenerator.state.PdfSaveState
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

internal class PdfSave(private val contentResolver: ContentResolver) {

    fun savePdfDocument(dir: DocumentFile, pdfDocument: PdfDocument, name: String): PdfSaveState {
        val file = dir.createFile("*/pdf", name)
        return if (file != null && file.canWrite()) saveDocument(file.uri, pdfDocument)
        else PdfSaveState.Error("Write error! no file or cannot write")
    }

    //перенеси на фоновый поток
    private fun saveDocument(uri: Uri, pdfDocument: PdfDocument): PdfSaveState {
        try {
            contentResolver.openFileDescriptor(uri, "w")?.use { parcelFileDescriptor ->
                pdfDocument.writeTo(FileOutputStream(parcelFileDescriptor.fileDescriptor))
                pdfDocument.close()
                return PdfSaveState.Success("File Write OK!", uri)
            }
        } catch (e: FileNotFoundException) {
            pdfDocument.close()
            e.printStackTrace()
        } catch (e: IOException) {
            pdfDocument.close()
            e.printStackTrace()
        }
        return PdfSaveState.Error("Write error!")
    }
}