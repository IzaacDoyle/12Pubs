package Izaac.Doyle.PubsApp.Models

import androidx.lifecycle.MutableLiveData

interface FBAccountStore {
    fun getAccounnt(uuid:String,account: MutableLiveData<List<FBAccountNameModel>>)
}