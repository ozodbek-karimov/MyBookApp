package pl.ozodbek.sportatamalari.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChaptersNames(val bookName: String, var isPinned: Boolean = false) : Parcelable
