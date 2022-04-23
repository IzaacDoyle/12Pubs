package Izaac.Doyle.PubsApp.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class FBAccountNameModel(var UserUUID:String ="", var Username:String="",var UserEmail:String="",var group: String? ="",
                         var GroupUUID:String? = ""):
    Parcelable{
    constructor():this("","","","","")
    }
