package pl.ozodbek.sportatamalari.data

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChapterNameData(val bookName: String) : Parcelable