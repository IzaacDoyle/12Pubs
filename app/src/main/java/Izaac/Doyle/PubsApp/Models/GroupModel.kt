package Izaac.Doyle.PubsApp.Models

import android.location.Location
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupModel(
    val GroupOwner:String = "",
    val GroupId:Int = 0,
                      val GroupName:String ="",
                      val GroupLocation:String = ""): Parcelable