package Izaac.Doyle.PubsApp.Models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface GooglePlacesStore {

    fun AddLocation(GroupUUID:String, placesModel: ArrayList<GooglePlacesModel>,user: FirebaseUser)
    fun FindAll(GroupUUID: String,placesModel: MutableLiveData<List<GooglePlacesModel>>)
    fun removeLocation(GroupUUID: String,placesModel: ArrayList<GooglePlacesModel>)
}