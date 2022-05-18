package developer.mihailzharkovskiy.pdfgenerator.settings

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import developer.mihailzharkovskiy.pdfgenerator.settings.page.PageSize
import developer.mihailzharkovskiy.pdfgenerator.settings.storage.StorageType

sealed class PdfSetting {
    class FromRecyclerOnOnePage(
        val recycler: RecyclerView,
        val pdfName: String,
        val storageForSave: StorageType = StorageType.Shared,
    ) : PdfSetting()

    class FromRecyclerSplitIntoPages(
        val recycler: RecyclerView,
        val pageSize: PageSize,
        val pdfName: String,
        val storageForSave: StorageType = StorageType.Shared,
    ) : PdfSetting()

    class FromAnyView(
        val view: View,
        val pageSize: PageSize,
        val pdfName: String,
        val storageForSave: StorageType = StorageType.Shared,
    ) : PdfSetting()
}