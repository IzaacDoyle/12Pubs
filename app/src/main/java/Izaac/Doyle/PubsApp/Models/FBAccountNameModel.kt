package Izaac.Doyle.PubsApp.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class FBAccountNameModel(var UserUUID:String ="", var Username:String="",var UserEmail:String=""):
    Parcelable{
    constructor():this("","","")
    }
