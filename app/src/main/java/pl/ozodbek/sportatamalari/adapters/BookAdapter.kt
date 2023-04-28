package pl.ozodbek.sportatamalari.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import pl.ozodbek.sportatamalari.R
import pl.ozodbek.sportatamalari.data.ChaptersNames
import pl.ozodbek.sportatamalari.databinding.ChapterRowLayoutBinding
import pl.ozodbek.sportatamalari.utils.BookDiffCallback


class BookAdapter(
    private var list: List<ChaptersNames>,
    val onItemClick: (ChaptersNames, Int) -> Unit,
) : RecyclerView.Adapter<BookAdapter.MyViewHolder>() {

    inner class MyViewHolder(private val binding: ChapterRowLayoutBinding) :
        ViewHolder(binding.root) {

        fun bind(bookData: ChaptersNames, position: Int){
            binding.apply {
                chapterName.text = bookData.bookName

                if (bookData.isPinned) {
                    pinIcon.setImageResource(R.drawable.ic_star_yellow)
                } else {
                    pinIcon.setImageResource(R.drawable.ic_star_brown)
                }

                pinIcon.setOnClickListener {
                    togglePin(bookData, position)
                }

                itemView.setOnClickListener {
                    onItemClick(bookData, position)
                }
            }
        }

        private fun togglePin(item: ChaptersNames, position: Int) {
            if (item.isPinned) {
                // If item is already pinned, remove pin and move to its last position
                item.isPinned = false

                val newList = list.toMutableList()
                newList.removeAt(position)
                newList.add(item)

                setData(newList)

                binding.pinIcon.setImageResource(R.drawable.ic_star_brown)
            } else {
                // If item is not pinned, pin it and move to the top
                item.isPinned = true

                val newList = list.toMutableList()
                newList.removeAt(position)
                newList.add(0, item)

                setData(newList)

                binding.pinIcon.setImageResource(R.drawable.ic_star_yellow)
            }
        }

    }

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ChapterRowLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    fun setData(newList: List<ChaptersNames>) {
        val diffCallback = BookDiffCallback(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }
}
