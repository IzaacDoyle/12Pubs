package Izaac.Doyle.PubsApp.Models

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

interface FBAccountStore {
    fun getAccount(uuid:String,account: MutableLiveData<List<FBAccountModel>>)

    fun updateAccountUserName(account:FBAccountModel)
    fun createAccountDB(account: FBAccountModel, sheet: BottomSheetDialogFragment?,activity: Activity)
    fun DeleteDB(account: FBAccountModel)



//    fun reAuthAccount(account: MutableLiveData<List<FBAccountModel>>,password:String, info: String, activity: Activity)
//    fun deleteAccount(activity: Activity, context: Context, account: FBAccountModel)
//    fun signOutAccount( account:FBAccountModel, activity: Activity)
}