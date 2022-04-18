package Izaac.Doyle.PubsApp.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InvitationsModel(var GroupUUID:String,var NewUser:String) : Parcelable{
    constructor():this("","" )
}