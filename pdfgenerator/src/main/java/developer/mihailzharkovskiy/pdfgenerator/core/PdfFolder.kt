package developer.mihailzharkovskiy.pdfgenerator.core

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import developer.mihailzharkovskiy.pdfgenerator.state.FolderState

internal class PdfFolder(
    private val context: Context,
    private val contentResolver: ContentResolver,
) {

    private val pdfSharedPref = "developer.mihailzharkovskiy.pdfgenerator.core.pdfSharedPref"
    private val keyFolderUri = "pdf_keyFolderUri"

    @SuppressLint("ApplySharedPref")
    fun saveSelectedFolderUri(folderUri: Uri, context: Context) {
        val editor = context.getSharedPreferences(pdfSharedPref, 0)!!.edit()
        editor.putString(keyFolderUri, folderUri.toString())
        editor.commit()
    }

    fun getSelectedFolder(context: Context): FolderState {
        val uriStr = context.getSharedPreferences(pdfSharedPref, 0).getString(keyFolderUri, null)
        val uri = if (uriStr != null) Uri.parse(uriStr) else return FolderState.NeedSelectFolder
        return checkFolder(uri)
    }

    private fun checkFolder(uri: Uri): FolderState {
        val dir = DocumentFile.fromTreeUri(context, uri)
        val access = checkFolderAccessGranted(uri)

        dir?.let { if (dir.exists() && access) return FolderState.Exist(dir) }
        return FolderState.NeedSelectFolder
    }

    private fun checkFolderAccessGranted(uri: Uri): Boolean {
        val list = contentResolver.persistedUriPermissions
        list.forEachIndexed { index, uriPermissions ->
            if (uriPermissions.uri == uri
                && list[index].isWritePermission
                && list[index].isReadPermission
            ) return true
        }
        return false
    }
}

