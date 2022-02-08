package Izaac.Doyle.PubsApp.Models

import android.app.Activity
import android.app.Dialog
import android.view.View
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo

interface AccountStore{
    fun SignIn(Account: AccountModel)
    fun SignOut(activity: Activity)
    fun UpdateAccount(Account: AccountModel)
    fun LoginCreate(Account: AccountModel,Password: String,activity: Activity)
    fun GoogleSignIn(idToken: String?,activity: Activity)
    fun ReAuth(email:String,password:String,info: String)
}