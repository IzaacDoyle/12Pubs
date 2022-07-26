package Izaac.Doyle.PubsApp.Helpers

import Izaac.Doyle.PubsApp.Models.FBAccountModel
import Izaac.Doyle.PubsApp.Models.RulesModel
import android.app.Activity
import android.app.Dialog
import android.view.View

interface onDataPasser {
  //  fun dataPassed(Email :String, Password : String)
   // fun dataInvalid(Email_Incorrect:Boolean,Password: Boolean)
   // fun passView(view:View)
   fun changeBottomSheet(sheetActive:String)
   fun AccountStatus(info:String,email:String?,account: FBAccountModel?)
   fun PassView(view: Boolean)
   fun Rules(Rules: MutableList<RulesModel>)
   // fun dialogBoxShare(dialog: Dialog)
}