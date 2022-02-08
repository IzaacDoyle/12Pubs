package Izaac.Doyle.PubsApp.Firebase

import Izaac.Doyle.PubsApp.Models.AccountModel
import Izaac.Doyle.PubsApp.activities.MainActivity
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.prefs.Preferences


fun FBcreateDB(userUUID: String,username:String) {
    val db = Firebase.firestore

    val UserDb = hashMapOf(
        "UserUUID" to userUUID,
        "Username" to username
    )

    db.collection("UserProfiles").document(userUUID)
        .set(UserDb)
        .addOnSuccessListener {
            Log.d("FirestoreDB", "DB created")
        }

}


fun FBGetDB(userUUID: String,activity: Activity){
    val db = Firebase.firestore


    db.collection("UserProfiles").document(userUUID)
        .get()
        .addOnSuccessListener { result ->
            if (result != null){
                Log.d("FBGet","SetSaved Pref")
                 val userInfo = result.toObject(AccountModel::class.java)
              //  Update(userInfo!!)
              //  Update(userInfo!!)

                Log.d("FBGet","SetSaved Pref ${userInfo!!.email} + ${result.data!!["Username"].toString()}")
                val datastore = activity.getSharedPreferences(userUUID,Context.MODE_PRIVATE)
                val editor = datastore.edit()
                    //editor.putString("Email", result.data!!["Username"] as String?)
                    editor.putString("Username",result.data!!["Username"].toString())
                    editor.apply()
            }
//            val userInfo = result.data.
            val username = result.data!!["Username"]
            Log.d("FBgetDB","$username and with UID of $userUUID")
        }

}


fun FBUpdateDB(userUUID: String, username: String){
    val db = Firebase.firestore

    db.collection("UserProfiles").document(userUUID)
        .update("Username", username)

}
