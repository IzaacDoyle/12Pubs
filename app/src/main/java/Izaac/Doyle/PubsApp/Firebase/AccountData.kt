package Izaac.Doyle.PubsApp.Firebase

import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.Models.FBAccountModel
import Izaac.Doyle.PubsApp.Models.FBAccountStore
import Izaac.Doyle.PubsApp.Models.GooglePlacesModel
import Izaac.Doyle.PubsApp.activities.MainActivity
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
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
                    database.child("accounts").child(uuid).removeEventListener(this)
                    Log.d("FindAllPubs",localList.toString())
                    account.value = localList
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("GetAccount","$error")
                }

            }
        )
    }


        //Create account to FB Real Time
    override fun createAccountDB(account: FBAccountModel,sheet: BottomSheetDialogFragment?,activity: Activity) {
            var dataPasser : onDataPasser
            Log.d("FirebaseRealTimeDB", "Create Account $account")
            val uuid = account.UserUUID
            val key = database.child("accounts").push().key ?: return
            account.AccountID = key
            val accountValue = account.toMap()
            val childAdd: MutableMap<String, Any?> = HashMap()
            childAdd["/accounts/$uuid/$key"] = accountValue
            database.updateChildren(childAdd)
                .addOnSuccessListener {
                    sheet?.dismiss()
                    dataPasser = activity as onDataPasser
                    dataPasser.AccountStatus("Task was Successful", null , account)

                }
        }

    override fun DeleteDB(account: FBAccountModel) {
        TODO("Not yet implemented")
    }


    override fun updateAccountUserName(account: FBAccountModel) {
        val uuid = account.UserUUID
        val key = account.AccountID
        Log.d("RealTimeUpdate",account.AccountID.toString())
//        val key = database.child("Accounts").push().key ?: return
//            account.UserUUID = key
        val accountValue = account.toMap()
        val childAdd : MutableMap<String, Any?> = HashMap()
        childAdd["/accounts/$uuid/$key"] = accountValue
        database.updateChildren(childAdd)



    }

//    override fun deleteAccount(activity: Activity, context: Context, account:FBAccountModel) {
//
//        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
//        val user = firebaseAuth.currentUser!!
//
//
//        val uuid = account.UserUUID
//        val key = account.AccountID
//
//        val AccountDelete : MutableMap<String,Any?> = HashMap()
//        AccountDelete["/accounts/$uuid/$key"] = null
//
//        database.updateChildren(AccountDelete)
//
//        user.delete().addOnCompleteListener  { task->
//            if (task.isSuccessful){
//
//                signOutAccount(account,activity)
//
//                val intent = Intent(context,MainActivity()::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//                activity.startActivity(intent)
//
//            }else{
//                Log.d("Error Delete",task.exception?.message.toString())
//            }
//
//        }
//
//
//
//
//
//    }






//    override fun reAuthAccount(
//        account: MutableLiveData<List<FBAccountModel>>,
//        password: String,
//        info: String,
//        activity: Activity
//    ) {
//        TODO("Not yet implemented")
//
//
//    }





//    override fun signOutAccount(
//        account:FBAccountModel,
//        activity: Activity
//    ) {
//        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
//        val googleSignInClient: GoogleSignInClient
//        val gso = GoogleSignInOptions
//            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(activity.getString(Izaac.Doyle.PubsApp.R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        googleSignInClient = GoogleSignIn.getClient(activity, gso)
//        googleSignInClient.signOut()
//        firebaseAuth.signOut()
////        activity.recreate()
//    }


}