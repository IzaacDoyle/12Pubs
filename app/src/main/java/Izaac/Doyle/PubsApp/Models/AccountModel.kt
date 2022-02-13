package Izaac.Doyle.PubsApp.Models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountModel(var id: String ="",
                        var username: String = "",
                        var email: String = "",
                        var group: String? =""): Parcelable

