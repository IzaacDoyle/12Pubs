package Izaac.Doyle.PubsApp.ui.pubs

import Izaac.Doyle.PubsApp.Models.FBAccountNameModel
import Izaac.Doyle.PubsApp.Models.GooglePlacesModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PubsViewModel : ViewModel() {

    var test : MutableLiveData<MutableList<GooglePlacesModel>> = MutableLiveData(ArrayList<GooglePlacesModel>())


    fun test(googleplaces: GooglePlacesModel){

         test.value!!.add(googleplaces)

    }








    internal  var test2: MutableLiveData<MutableList<GooglePlacesModel>>
        get() { return test}
        set(value) {test =  value}


}