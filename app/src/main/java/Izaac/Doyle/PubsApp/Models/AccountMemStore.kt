package Izaac.Doyle.PubsApp.Models

import Izaac.Doyle.PubsApp.Firebase.FBCreateAccount
import Izaac.Doyle.PubsApp.Firebase.FBLogout
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.activities.MainActivity
import android.app.Activity
import android.content.res.Resources
import android.util.Log
import kotlinx.coroutines.*
import java.util.logging.Handler


class AccountMemStore: AccountStore {


    override fun SignIn(Account: AccountModel) {

    }

    override fun SignOut(activity: Activity) {
        FBLogout(activity)
    }

    override fun UpdateAccount(Account: AccountModel) {


        TODO("Not yet implemented")
    }

    override fun LoginCreate(Email: String, Password: String, Username:String, activity: Activity) {
        val account = AccountModel(0,Username,Password,Email)
        if (account.email.isNotEmpty()){
            GlobalScope.launch (Dispatchers.IO){
                val job = launch{FBCreateAccount(account,activity)}

                //inside launch storage creation, no storage to be created unless Account created
                // need to save user Profile pic to begin with
            }
            Thread.sleep(2000)
            Log.d("Create User","See if pause works")
        }
    }

}











