package Izaac.Doyle.PubsApp.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RuleAmount(val RuleAmount:String): Parcelable {
    constructor():this("")
}
