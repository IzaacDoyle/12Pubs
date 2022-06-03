package Izaac.Doyle.PubsApp.ui.home

import Izaac.Doyle.PubsApp.Firebase.AccountData
import Izaac.Doyle.PubsApp.Firebase.GoogleRealTimeDB
import Izaac.Doyle.PubsApp.Models.FBAccountModel
import Izaac.Doyle.PubsApp.Models.GooglePlacesModel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel(application: Application): AndroidViewModel(application) {

    private val account = MutableLiveData<List<FBAccountModel>>()




    val observableAccountData: LiveData<List<FBAccountModel>>
        get() = account

    fun load(uuid:String){
        try {
            AccountData.getAccount(uuid,account)
        }catch (e:Exception){
        }
    }



}