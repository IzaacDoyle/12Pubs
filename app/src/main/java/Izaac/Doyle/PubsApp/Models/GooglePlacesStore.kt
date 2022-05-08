package Izaac.Doyle.PubsApp.Models

import androidx.lifecycle.MutableLiveData

interface GooglePlacesStore {

    fun AddLocation(GroupUUID:String, placesModel: ArrayList<GooglePlacesModel>)
    fun FindAll(GroupUUID: String,placesModel: MutableLiveData<List<GooglePlacesModel>>)
    fun removeLocation(GroupUUID: String,placesModel: ArrayList<GooglePlacesModel>)
}