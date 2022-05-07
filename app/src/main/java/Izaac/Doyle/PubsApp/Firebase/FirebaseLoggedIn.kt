package Izaac.Doyle.PubsApp.Firebase

import Izaac.Doyle.PubsApp.Models.FBAccountNameModel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FirebaseLoggedIn(application: Application): AndroidViewModel(application) {

    private val userProfile = MutableLiveData<List<FBAccountNameModel>>()


    val AccountObservable: LiveData<List<FBAccountNameModel>>
        get() = userProfile

    fun getAccount(uuid:String){
        try {
            AccountData.getAccounnt(uuid,userProfile)
        }catch (e:Exception){

        }
    }




}