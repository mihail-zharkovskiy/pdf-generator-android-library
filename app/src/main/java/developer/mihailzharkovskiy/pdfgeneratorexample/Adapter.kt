package developer.mihailzharkovskiy.pdfgeneratorexample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import developer.mihailzharkovskiy.pdfgeneratorexample.databinding.ItemExampleBinding

class Adapter : RecyclerView.Adapter<Adapter.SatItemHolder>() {

    private val list = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SatItemHolder.from(parent)

    override fun onBindViewHolder(holder: SatItemHolder, position: Int) =
        holder.update(list[position])

    override fun getItemCount(): Int = list.size


    class SatItemHolder private constructor(private val binding: ItemExampleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun update(value: String) {
            binding.textView.text = value
        }

        companion object {
            fun from(parent: ViewGroup): SatItemHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemExampleBinding.inflate(inflater, parent, false)
                return SatItemHolder(binding)
            }
        }
    }
}
