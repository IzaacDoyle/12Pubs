package Izaac.Doyle.PubsApp.Models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountModel(var id: Long = 0,
                        var username: String = "",
                        var password: String = "",
                        var email: String = ""): Parcelable

