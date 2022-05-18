package developer.mihailzharkovskiy.pdfgenerator.settings.storage

sealed class StorageType {
    object Internal : StorageType()
    object External : StorageType()
    object Shared : StorageType()
}