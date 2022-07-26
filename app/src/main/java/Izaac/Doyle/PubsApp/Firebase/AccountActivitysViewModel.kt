package Izaac.Doyle.PubsApp.Firebase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import Izaac.Doyle.PubsApp.Firebase.FirebaseManager
import Izaac.Doyle.PubsApp.Models.AccountModel
import Izaac.Doyle.PubsApp.Models.FBAccountModel
import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo

class AccountActivitysViewModel(app:Application): AndroidViewModel(app) {


    var firebaseManager: FirebaseManager = FirebaseManager(app)

    //this is observable to see firebase user
    var liveFirebaseUser : MutableLiveData<FirebaseUser> = firebaseManager.liveFirebaseUser



    fun EPLogin(email:String,password: String,context: Context,activity: Activity){
        firebaseManager.LoginEP(email,password,context,activity)
    }

    fun EPRegister(account: AccountModel, password: String, activity: Activity){
        firebaseManager.EPRegister(account,password,activity)
    }

    fun LogOut(activity:Activity){
        firebaseManager.LogOut(activity)
    }

    fun ReAuth(Email:String,password:String,info:String,activity: Activity){
        firebaseManager.ReAuth(Email,password,info,activity)
    }

    fun DeleteAccount(activity: Activity, context: Context, account: FBAccountModel){
        firebaseManager.DeleteAccount(activity,context,account)
    }

    fun CheckCurrentUser(){
        firebaseManager.CheckCurrentUser()
    }


}