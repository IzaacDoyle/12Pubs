package Izaac.Doyle.PubsApp.Models

import Izaac.Doyle.PubsApp.Firebase.*
import android.app.Activity
import android.app.Dialog
import android.util.Log
import android.view.View
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

    override fun GoogleSignIn(idToken: String?, activity: Activity) {
        GoogleSignInAccount(idToken,activity)
    }



    override fun ReAuth(email: String, password: String,info:String) {
        FBReAuth(email, password,info)
    }

}











