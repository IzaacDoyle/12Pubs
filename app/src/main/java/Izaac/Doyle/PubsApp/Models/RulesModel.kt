package Izaac.Doyle.PubsApp.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RulesModel(var RuleID:String,var RuleName:String,var RuleDescription:String):
    Parcelable {
    constructor():this("","","")
}
