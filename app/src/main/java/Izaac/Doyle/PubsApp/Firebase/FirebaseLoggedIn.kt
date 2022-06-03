package Izaac.Doyle.PubsApp.Firebase

import Izaac.Doyle.PubsApp.Models.FBAccountModel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FirebaseLoggedIn(application: Application): AndroidViewModel(application) {

    private val userProfile = MutableLiveData<List<FBAccountModel>>()


    val AccountObservable: LiveData<List<FBAccountModel>>
        get() = userProfile

    fun getAccount(uuid:String){
        try {
            AccountData.getAccount(uuid,userProfile)
        }catch (e:Exception){

        }
    }




}