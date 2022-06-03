package Izaac.Doyle.PubsApp.Firebase

import Izaac.Doyle.PubsApp.Models.FBAccountModel
import Izaac.Doyle.PubsApp.Models.FBAccountStore
import Izaac.Doyle.PubsApp.Models.GooglePlacesModel
import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object AccountData:FBAccountStore {
    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    //Get account to FB Real Time
    override fun getAccount(uuid: String,account: MutableLiveData<List<FBAccountModel>>) {
        database.child("accounts").child(uuid).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<FBAccountModel>()
                    val children = snapshot.children
                    children.forEach{
                        val profile  = it.getValue(FBAccountModel::class.java)
                        localList.add(profile!!)
                    }
                    GoogleRealTimeDB.database.child("Pubs").child(uuid).removeEventListener(this)
                    Log.d("FindAllPubs",localList.toString())

                    account.value = localList
                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
        )
    }
        //Create account to FB Real Time
    override fun createAccount(account: FBAccountModel) {
            Log.d("FirebaseRealTimeDB", "Create Account $account")
            val uuid = account.UserUUID
            val key = database.child("Accounts").push().key ?: return
//            account.UserUUID = key
            val accountValue = account.toMap()
            val childAdd = HashMap<String, Any?>()
            childAdd["/accounts/$uuid/$key"] = accountValue
            database.updateChildren(childAdd)
    }


    override fun updateAccountUserName(account: FBAccountModel) {
        val uuid = account.UserUUID
        val key = database.child("Accounts").push().key ?: return
//            account.UserUUID = key
        val accountValue = account.toMap()
        val childAdd = HashMap<String, Any?>()
        childAdd["/accounts/$uuid/$key"] = accountValue
        database.updateChildren(childAdd)
    }

    override fun deleteAccount(account: MutableLiveData<List<FBAccountModel>>) {

    }

    override fun reAuthAccount(
        account: MutableLiveData<List<FBAccountModel>>,
        password: String,
        info: String,
        activity: Activity
    ) {
        TODO("Not yet implemented")
    }

    override fun signOutAccount(
        account: MutableLiveData<List<FBAccountModel>>,
        activity: Activity
    ) {
        TODO("Not yet implemented")
    }


}