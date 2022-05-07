package Izaac.Doyle.PubsApp.Models


import android.os.Parcelable
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Period
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.google.type.LatLng
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class GooglePlacesModel(
    var PubsUID:String?,
    var PubsID: String?,
    var PubName:String?,
    var PubLat: Double? ,
    var PubLng: Double?,
    var PubAddress:String?,
    //Google Places Requires String
    var PubPhoneNum: String?,
    var PubOpeningHours: MutableList<String>?

) : Parcelable{
    constructor():this("","","",0.0,0.0,"","", mutableListOf())

    @Exclude
    fun toMap():Map<String,Any?>{
        return mapOf(
            "PubsUID" to PubsUID,
            "PubName" to PubName,
            "PubID" to PubsID,
            "PubPhoneNum" to PubPhoneNum,
            "PubAddress" to PubAddress,
            "PubLat" to PubLat,
            "PubLng" to PubLng,
            "PubOpeningHours" to PubOpeningHours
        )
    }
}

