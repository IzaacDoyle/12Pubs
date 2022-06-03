package Izaac.Doyle.PubsApp.Models

import android.app.Activity
import androidx.lifecycle.MutableLiveData

interface FBAccountStore {
    fun getAccount(uuid:String,account: MutableLiveData<List<FBAccountModel>>)
    fun createAccount(account: FBAccountModel)
    fun updateAccountUserName(account:FBAccountModel)
    fun deleteAccount(account: MutableLiveData<List<FBAccountModel>>)
    fun reAuthAccount(account: MutableLiveData<List<FBAccountModel>>,password:String, info: String, activity: Activity)
    fun signOutAccount(account: MutableLiveData<List<FBAccountModel>>, activity: Activity)
}