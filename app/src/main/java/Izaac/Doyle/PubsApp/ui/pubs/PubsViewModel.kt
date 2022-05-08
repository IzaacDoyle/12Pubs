package Izaac.Doyle.PubsApp.ui.pubs

import Izaac.Doyle.PubsApp.Models.FBAccountNameModel
import Izaac.Doyle.PubsApp.Models.GooglePlacesModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PubsViewModel : ViewModel() {

    var Places : MutableLiveData<MutableList<GooglePlacesModel>> = MutableLiveData(ArrayList<GooglePlacesModel>())


    fun test(googleplaces: GooglePlacesModel){

        Places.value!!.add(googleplaces)

    }








    internal  var PlacesObv: MutableLiveData<MutableList<GooglePlacesModel>>
        get() { return Places}
        set(value) {Places =  value}
}