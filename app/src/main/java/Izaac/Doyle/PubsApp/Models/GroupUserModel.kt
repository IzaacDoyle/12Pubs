package Izaac.Doyle.PubsApp.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupUserModel(var IsAdmin:Boolean, var User:String,var UserName:String,var UserPending:Boolean) :
    Parcelable {
        constructor():this(false,"","",false)
}