package Izaac.Doyle.PubsApp.Firebase

import Izaac.Doyle.PubsApp.Models.AccountModel
import Izaac.Doyle.PubsApp.Models.GroupModel
import Izaac.Doyle.PubsApp.activities.MainActivity
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.core.content.edit
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*
import java.util.prefs.Preferences


fun FBcreateDB(userUUID: String,username:String,userEmail:String) {
    val db = Firebase.firestore
    Log.d("UserEmail",userEmail)

    val UserDb = hashMapOf(
        "UserUUID" to userUUID,
        "Username" to username.lowercase(),
        "UserEmail" to userEmail

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
            if (result != null && result.exists()){
                Log.d("FBGet","SetSaved Pref")
                 val userInfo = result.toObject(AccountModel::class.java)
              //  Update(userInfo!!)
              //  Update(userInfo!!)

            //    Log.d("FBGet","SetSaved Pref ${userInfo!!.email} + ${result.data!!["Username"].toString()}")
                val datastore = activity.getSharedPreferences(userUUID,Context.MODE_PRIVATE)
                val editor = datastore.edit()
                    //editor.putString("Email", result.data!!["Username"] as String?)


                    editor.putString("Username",result.data!!["Username"].toString())

                //if group is empty wont local save
                    if (result.data!!["Group"].toString().isNotEmpty()) {
                        editor.putString("GroupName", result.data!!["Group"].toString())
                    }
                    editor.apply()
            }
//            val userInfo = result.data.
          //  val username = result.data!!["Username"]
        //    Log.d("FBgetDB","$username and with UID of $userUUID")
        }

}


fun FBUpdateDB(userUUID: String, username: String){
    val db = Firebase.firestore

    db.collection("UserProfiles").document(userUUID)
        .update("Username", username)

}

fun FBUserAddGroup(group:String){
    val db = Firebase.firestore

    db.collection("UserProfiles").document(CheckCurrentUser()!!.uid)
        .update("Group", group)

}

fun FbGetGroupImage(UUid:String,ImageType: String){
    val storageRef =   FirebaseStorage.getInstance().reference
    val task = storageRef.child("$UUid/$ImageType.jpg")






}



fun FBCreateGroup(groupModel: GroupModel){
    val db = Firebase.firestore

    val GroupDB = hashMapOf(
        "OwnerUUID" to groupModel.OwnerUUID,
        "GroupName" to groupModel.GroupName,

    )

    db.collection("Groups").document( groupModel.OwnerUUID)
        .set(GroupDB)
        .addOnSuccessListener {
            Log.d("FirestoreDB", "DB created for groups")
        }

    FBUserAddGroup(groupModel.GroupName)


}

fun UploadImage(UUid:String,uri:Uri,ImageType:String){
    val storageRef =   FirebaseStorage.getInstance().reference
    Log.d("URI",uri.toString())

    val task = storageRef.child("$UUid/$ImageType.jpg").putFile(uri)
    task.addOnSuccessListener {
        Log.d("UploadImage","Task Is Successful")
    }.addOnFailureListener {
        Log.d("UploadImageFail","Image Upload Failed ${it.printStackTrace()}")
    }

}


//fun FBGetGroupName(userUUID: String): Flow<List<GroupModel>> {
//    val db = Firebase.firestore
//
//
//
//
////    return callbackFlow {
////       val groupName = db.collection("Groups").document(userUUID).collection("Username").addSnapshotListener{
////            querySnapShot:QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
////            if (firebaseFirestoreException != null) {
////                cancel(message = "Error fetching posts",
////                    cause = firebaseFirestoreException)
////                return@addSnapshotListener
////            }
////
////            val groupname = querySnapShot!!.documents.mapNotNull { it.toObject(GroupModel::class.java) }
////            trySend(groupname)
////        }
////        awaitClose {
////            groupName.remove()
////        }
////
////    }
//
//}
