package Izaac.Doyle.PubsApp.Helpers

import android.app.Activity
import android.app.Dialog
import android.view.View

interface onDataPasser {
  //  fun dataPassed(Email :String, Password : String)
   // fun dataInvalid(Email_Incorrect:Boolean,Password: Boolean)
   // fun passView(view:View)

    fun changeBottomSheet(sheetActive:String)
    fun CreatingAccount(info:String,email:String)
   // fun dialogBoxShare(dialog: Dialog)
}