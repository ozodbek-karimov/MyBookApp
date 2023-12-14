package pl.ozodbek.sportatamalari.utils

import androidx.recyclerview.widget.DiffUtil
import pl.ozodbek.sportatamalari.data.ChaptersNames

class BookDiffCallback(
    private val oldList: List<ChaptersNames>,
    private val newList: List<ChaptersNames>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size


    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].bookName == newList[newItemPosition].bookName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]

    }
}
