package developer.mihailzharkovskiy.pdfgeneratorexample

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import developer.mihailzharkovskiy.pdfgenerator.PdfCreator
import developer.mihailzharkovskiy.pdfgenerator.PdfResult
import developer.mihailzharkovskiy.pdfgenerator.settings.PdfSetting
import developer.mihailzharkovskiy.pdfgenerator.settings.page.FormatPage
import developer.mihailzharkovskiy.pdfgenerator.settings.page.PageSize
import developer.mihailzharkovskiy.pdfgenerator.util.PdfUtil
import developer.mihailzharkovskiy.pdfgeneratorexample.databinding.ActivityMainBinding

/**ЭТО ПРОСТО ПРИМЕР ИСПОЛЬЗОВАНИЯ БИБЛИОТЕКИ, ПОЭТОМУ АКТИВИТИ ОТВЕЧАЕТ ЗА ВСЕ СРАЗУ**/

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val pdf: PdfCreator by lazy { PdfCreator(application) }

    private val pdfLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        pdf.saveUriSelectedFolder(result)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**у recycler view недолжно быть background-a**/
        setupRecyclerView()

        with(binding) {
            btRvOnOnePage.setOnClickListener {
                val name = "rv_on_one_page"
                val pdf = PdfSetting.FromRecyclerOnOnePage(recycler, name)
                createPdf(pdf)
            }
            btRvSplitIntoPages.setOnClickListener {
                val name = "rv_split"
                val pdf = PdfSetting.FromRecyclerSplitIntoPages(recycler,
                    PageSize.set(FormatPage.A4),
                    name)
                createPdf(pdf)
            }
            btFromAnyView.setOnClickListener {
                val name = "screenshot"
                val pdf = PdfSetting.FromAnyView(root, PageSize.set(root), name)
                createPdf(pdf)
            }
            btSelectFolder.setOnClickListener {
                pdf.selectFolderForSave(pdfLauncher)
            }
            btGetName.setOnClickListener {
                binding.tvFolderName.text = "name folder:${pdf.getNameFolder()}"
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recycler.apply {
            setHasFixedSize(true)
            adapter = Adapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun createPdf(pdf: PdfSetting) {
        val result = this.pdf.createPdf(pdf)
        applyPdfResult(result)
    }

    private fun applyPdfResult(result: PdfResult) {
        when (result) {
            is PdfResult.Success -> PdfUtil.openPdfDocumentInAnotherApp(result.pdfUri, this)
            is PdfResult.NeedSelectFolder -> result.selectFolder(pdfLauncher)
            is PdfResult.Error -> Toast.makeText(this, "error create pdf", Toast.LENGTH_LONG).show()
        }
    }
}