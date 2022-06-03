package Izaac.Doyle.PubsApp.Models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
class FBAccountModel(var UserUUID:String ="", var Username:String?="", var UserEmail:String="", var GroupName: String? ="",
                     var GroupUUID:String? = ""):
    Parcelable {
    constructor() : this("", "", "", "", "")

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "UserUUID" to UserUUID,
            "UserName" to Username?.lowercase(),
            "UserEmail" to UserEmail,
            "GroupName" to GroupName,
            "GroupUUID" to GroupUUID

            )
    }
}
