package developer.mihailzharkovskiy.pdfgenerator.state

import androidx.documentfile.provider.DocumentFile

internal sealed class FolderState {
    class Exist(val dir: DocumentFile) : FolderState()
    object NeedSelectFolder : FolderState()
}