package Izaac.Doyle.PubsApp.Helpers

import android.app.Activity
import android.view.View

interface onDataPasser {
  //  fun dataPassed(Email :String, Password : String)
   // fun dataInvalid(Email_Incorrect:Boolean,Password: Boolean)
   // fun passView(view:View)

    fun changeBottomSheet(sheetActive:String)
    fun ErrorCreatingAccount(info:String,email:String)
}