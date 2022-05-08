package Izaac.Doyle.PubsApp.Models

import Izaac.Doyle.PubsApp.Firebase.*
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.View
import kotlinx.coroutines.*
import java.util.logging.Handler
import kotlin.math.log


class AccountMemStore: AccountStore {


    override fun SignIn(Account: AccountModel,Password: String,context: Context,activity: Activity) {
        Log.d("signing","${Account.email}")
        FBLogin(Account.email, Password,context,activity)
    }

    override fun SignOut(activity: Activity) {
        FBLogout(activity)
    }

    override fun UpdateAccountDB(Account: AccountModel) {
        FBUpdateDB(Account.id,Account.username)
    }

    override fun LoginCreate(Account: AccountModel, Password: String, activity: Activity) {
        val account = AccountModel("",Account.username,Account.email,"")
        if (Account.email.isNotEmpty()){
//            GlobalScope.launch (Dispatchers.IO){
//                val job = launch{
            Log.d("Create User","Entering here")
                    FBCreateAccount(account,Password,activity)}

                //inside launch storage creation, no storage to be created unless Account created
                // need to save user Profile pic to begin with
//            }
//            Thread.sleep(2000)
            Log.d("Create User","See if pause works")
      //  }
    }

    override fun GoogleSignIn(idToken: String?, activity: Activity,info: String) {
        GoogleSignInAccount(idToken,activity,info)
    }



    override fun ReAuth(email: String, password: String,info:String,activity: Activity) {
        FBReAuth(email, password,info,activity)
    }

    override fun DeleteAccount(activity: Activity,context: Context) {
        FBDeleteAccount(activity, context)
    }

}











