package Izaac.Doyle.PubsApp.Models

import android.app.Activity

interface AccountStore{
    fun SignIn(Account: AccountModel)
    fun SignOut(activity: Activity)
    fun UpdateAccount(Account: AccountModel)
    fun LoginCreate(Email:String,Password: String,Username:String,activity: Activity)
}