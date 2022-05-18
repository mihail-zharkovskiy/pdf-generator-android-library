package developer.mihailzharkovskiy.pdfgenerator

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.documentfile.provider.DocumentFile
import developer.mihailzharkovskiy.pdfgenerator.core.PdfFolder
import developer.mihailzharkovskiy.pdfgenerator.core.PdfFromRecycler
import developer.mihailzharkovskiy.pdfgenerator.core.PdfFromView
import developer.mihailzharkovskiy.pdfgenerator.core.PdfSave
import developer.mihailzharkovskiy.pdfgenerator.settings.PdfSetting
import developer.mihailzharkovskiy.pdfgenerator.state.FolderState
import developer.mihailzharkovskiy.pdfgenerator.state.PdfSaveState
import developer.mihailzharkovskiy.pdfgenerator.util.PdfUtil

class PdfCreator(app: Application) : Activity() {

    private val resolver = app.contentResolver
    private val context = app.applicationContext

    private val pdfSave = PdfSave(resolver)
    private val pdfFolder = PdfFolder(context, resolver)

    private val pdfFromView = PdfFromView()
    private val pdfFromRecycler = PdfFromRecycler()

    fun createPdf(data: PdfSetting): PdfResult {
        return when (val folderState = getSelectedFolderForSave()) {
            is FolderState.Exist -> {
                when (val result = save(data, folderState.dir)) {
                    is PdfSaveState.Success -> PdfResult.Success(result.uriSaveDocument,
                        result.message)
                    is PdfSaveState.Error -> PdfResult.Error(result.message)
                }
            }
            is FolderState.NeedSelectFolder -> PdfResult.NeedSelectFolder(::selectFolderForSave)
        }
    }

    fun saveUriSelectedFolder(result: ActivityResult?) {
        if (result?.resultCode == RESULT_OK) {
            result.data?.dataString?.let { uriStr ->
                val uri = Uri.parse(uriStr)
                val takeFlags: Int =
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                resolver.takePersistableUriPermission(uri, takeFlags)
                pdfFolder.saveSelectedFolderUri(uri, context)
            }
        }
    }

    fun getNameFolder(): String {
        return when (val state = getSelectedFolderForSave()) {
            is FolderState.Exist -> state.dir.name ?: "..."
            is FolderState.NeedSelectFolder -> "you need to select a folder"
        }
    }

    fun selectFolderForSave(pdfLauncher: ActivityResultLauncher<Intent>) {
        val intent =
            Intent(Intent.ACTION_OPEN_DOCUMENT_TREE) // отправляем интент чтобы выбрать папку для наших данных
        pdfLauncher.launch(intent)
    }

    private fun getSelectedFolderForSave(): FolderState {
        return pdfFolder.getSelectedFolder(context)
    }

    private fun save(data: PdfSetting, folder: DocumentFile): PdfSaveState {
        return when (data) {
            is PdfSetting.FromRecyclerOnOnePage -> {
                val pdf = pdfFromRecycler.createOnOnePage(data.recycler)
                val name = PdfUtil.editPdfName(data.pdfName)
                pdfSave.savePdfDocument(folder, pdf, name)
            }
            is PdfSetting.FromRecyclerSplitIntoPages -> {
                val pdf =
                    pdfFromRecycler.createSplitIntoPages(data.recycler, data.pageSize, context)
                val name = PdfUtil.editPdfName(data.pdfName)
                pdfSave.savePdfDocument(folder, pdf, name)
            }
            is PdfSetting.FromAnyView -> {
                val pdf = pdfFromView.create(data.view, data.pageSize)
                val name = PdfUtil.editPdfName(data.pdfName)
                pdfSave.savePdfDocument(folder, pdf, name)
            }
        }
    }
}