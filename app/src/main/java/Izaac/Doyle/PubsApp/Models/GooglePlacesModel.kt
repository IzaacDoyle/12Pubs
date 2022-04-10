package Izaac.Doyle.PubsApp.Models


import android.os.Parcelable
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Period
import com.google.android.libraries.places.api.model.Place
import com.google.type.LatLng
import kotlinx.parcelize.Parcelize


@Parcelize
data class GooglePlacesModel(
    var ID: String?,
    var Name:String?,
    var LocationLat: Double? ,
    var LocationLng: Double?,
    var Address:String?,
    //Google Places Requires String
    var PhoneNumber: String?,
    var OpeningHours: MutableList<String>?

) : Parcelable

