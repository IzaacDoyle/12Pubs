package Izaac.Doyle.PubsApp.Firebase

import Izaac.Doyle.PubsApp.Helpers.LocationRecycleView
import Izaac.Doyle.PubsApp.Models.AccountModel
import Izaac.Doyle.PubsApp.Models.FBAccountNameModel
import Izaac.Doyle.PubsApp.Models.GooglePlacesModel
import Izaac.Doyle.PubsApp.Models.GroupModel
import Izaac.Doyle.PubsApp.activities.MainActivity
import Izaac.Doyle.PubsApp.ui.Group.viewpager.RulesViewpager
import Izaac.Doyle.PubsApp.ui.home.GroupViewModel
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.edit
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.net.CacheResponse
import java.util.*
import java.util.prefs.Preferences
import javax.security.auth.callback.Callback
import kotlin.collections.ArrayList
import kotlin.math.log
import kotlin.random.Random


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

fun FBUpdateGroup(groupUUID:String, GroupName:String){
    val db = Firebase.firestore

    db.collection("Groups").document(groupUUID)
        .update("GroupName", GroupName)


}

fun FBUserAddGroup(groupUUID:String,ownerUuid :String){
    val db = Firebase.firestore

    db.collection("UserProfiles").document(ownerUuid)
        .update("Group", groupUUID)


}



 fun RandomRules(UUid: String){
    val db = Firebase.firestore
    db.collection("PubRules").document("0").get()

        .addOnSuccessListener { result->

            if (result.exists()){
                val RuleNum = result.getString("RuleAmount")
                RandomNumberRule(RuleNum,UUid)
                Log.d("RuleNum","Rule Number is : $RuleNum")


            }
        }
}

fun RandomNumberRule(ruleNum: String?,UUid: String) {
    val db = Firebase.firestore
    val IntArray = ArrayList<Int>()
    var randomNumber: Int = 0
    for (i in 1..20) {
        Log.d("RuleNum", i.toString())
//        randomNumber = (1..ruleNum!!.toInt()).random()
        randomNumber = random(ruleNum)
        if (IntArray.contains(randomNumber)) {
//            randomNumber = (1..ruleNum.toInt()).random()
            randomNumber = random(ruleNum)
            println(randomNumber)
            while (!IntArray.contains(randomNumber)) {
                IntArray.add(randomNumber)
                break
            }
        } else {
            IntArray.add(randomNumber)
            println("$randomNumber  $UUid")
        }
        println("Before db Call "+IntArray.size)
    }


     if (IntArray.size == 15){
        println(" Size of IntArray " + IntArray.size)
        val groupRules = hashMapOf(
            "RuleNumbers" to IntArray
        )
        db.collection("Groups").document(UUid)
            .set(groupRules, SetOptions.merge())
            .addOnFailureListener {
                Log.d("RulesAdd",it.message.toString())
            }
    }
}
fun random(ruleNum: String?):Int{
    val random =(1..ruleNum!!.toInt()).random()
    println(random)
    return random


}

fun AddUserToGroup(accountModel: FBAccountNameModel,UUid: String,context: Context?,dialog: Dialog?,Extra:Boolean,IsAdmin:Boolean){
    val db = Firebase.firestore

    val UserAdd = hashMapOf(
        "User" to accountModel.UserUUID,
        "UserName" to accountModel.Username,
        "UserPending" to Extra,
        "IsAdmin" to IsAdmin
    )
    //do if Exists
    db.collection("Groups").document(UUid).collection("Users").document(accountModel.UserUUID)
        .set(UserAdd, SetOptions.merge())
        .addOnSuccessListener {

           if (Extra) {
               CreatePendingAdd(UUid,accountModel.UserUUID)
               Toast.makeText(
                   context,
                   "${accountModel.Username} has been added to the group",
                   Toast.LENGTH_SHORT
               )
                   .show()
               dialog?.dismiss()
           }
        }

}
fun InviteResponce(GroupUUID: String,Username: String,userUUID: String,response: Boolean) {
    val db = Firebase.firestore
println("TestResponce " + response)
    if (response) {
        println(Username)
        db.collection("Groups").document(GroupUUID).collection("Users")
            .document(userUUID)
            .update("UserPending", false)
            .addOnSuccessListener {
                FBUserAddGroup(GroupUUID,userUUID)
                db.collection("PendingInvitation").document(userUUID).delete()
                    .addOnSuccessListener { Log.d("Delete Invitation", "DocumentSnapshot successfully deleted!") }
            }
            .addOnFailureListener { it->
                Log.d("ResponceFailed",it.message.toString())
            }
    }else if (!response){
        db.collection("Groups").document(GroupUUID).collection("Users").document(Username).delete()
            .addOnSuccessListener {
                db.collection("PendingInvitation").document(userUUID).delete()
                    .addOnSuccessListener { Log.d("Delete Invitation", "DocumentSnapshot successfully deleted!")

                    }
            }
    }



}

fun CreatePendingAdd(GroupUUID:String,NewUserUUID:String){

    val db = Firebase.firestore

    val invitation = hashMapOf(
        "GroupUUID" to GroupUUID,
        "NewUser" to NewUserUUID
    )

    db.collection("PendingInvitation").document(NewUserUUID)
        .set(invitation)


}



    fun FBCreateGroup(groupModel: GroupModel,Username:String?,UUid: String?) {
        val db = Firebase.firestore

        val GroupDB = hashMapOf(
            "OwnerUUID" to groupModel.OwnerUUID,
            "GroupName" to groupModel.GroupName,
            )

        db.collection("Groups").document(groupModel.OwnerUUID)
            .set(GroupDB)
            .addOnSuccessListener {
                if (!Username.isNullOrBlank()) {
                    AddUserToGroup(FBAccountNameModel(UUid!!, Username, "", "", ""), UUid, null, null, Extra = false, IsAdmin = true)
                }
                RandomRules(groupModel.OwnerUUID)
                Log.d("FirestoreDB", "DB created for groups")
            }

        FBUserAddGroup(groupModel.OwnerUUID,groupModel.OwnerUUID)


    }

    fun UploadImage(UUid: String, uri: Uri, ImageType: String) {
        val storageRef = FirebaseStorage.getInstance().reference
        Log.d("URI", uri.toString())

        val task = storageRef.child("$UUid/$ImageType.jpg").putFile(uri)
        task.addOnSuccessListener {
            Log.d("UploadImage", "Task Is Successful")
        }.addOnFailureListener {
            Log.d("UploadImageFail", "Image Upload Failed ${it.printStackTrace()}")
        }

    }


    fun savePlaceAsFav(context: Context, UUid: String, Pub: GooglePlacesModel) {
        val db = Firebase.firestore

        val places = hashMapOf(
            "PubName" to Pub.PubName,
            "PubID" to Pub.PubsID,
            "PubPhoneNum" to Pub.PubPhoneNum,
            "PubAddress" to Pub.PubAddress,
            "PubLat" to Pub.PubLat,
            "PubLng" to Pub.PubLng,
            "PubOpeningHours" to Pub.PubOpeningHours
        )
        db.collection("UserProfiles").document(UUid).collection("Places").document(Pub.PubName!!)
            .set(places, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(context, "Pub Added to Favourite ${Pub.PubName}", Toast.LENGTH_SHORT)
                    .show()
            }

    }


fun Leavegroup(groupModel: GroupModel,activity: Activity,groupViewModel: GroupViewModel){
    val db = Firebase.firestore

   val job = db.collection("Groups").document(groupModel.OwnerUUID).collection("Users").document(CheckCurrentUser()!!.uid).delete()
        .addOnSuccessListener {
            Log.d("LeaveGroup","User Removed from group")
            val deleteGroup = hashMapOf<String,Any>(
                "Group" to FieldValue.delete()
            )
           db.collection("UserProfiles").document(CheckCurrentUser()!!.uid).update(deleteGroup)
                .addOnSuccessListener {
                    Log.d("LeaveGroup","Users Profile Update to not in group")

                }
                .addOnCompleteListener {
//                    groupViewModel.UsersGroupname.value!!.clear()
                    groupViewModel.gNames.value!!.clear()
                    groupViewModel.groupRule.value!!.clear()
                    groupViewModel.GroupNames.value!!.clear()
                    groupViewModel.Rules.value!!.clear()

                    activity.recreate()
                }
//            if (job2.isComplete){
//                activity.recreate()
//            }
        }


}

fun AddPlacesToGroup(placesModel: ArrayList<GooglePlacesModel>,GroupUUID: String,myRuleAdaptor: LocationRecycleView){
    val db = Firebase.firestore
    for (i in placesModel){
        val places = hashMapOf(
            "PubName" to i.PubName,
            "PubID" to i.PubsID,
            "PubPhoneNum" to i.PubPhoneNum,
            "PubAddress" to i.PubAddress,
            "PubLat" to i.PubLat,
            "PubLng" to i.PubLng,
            "PubOpeningHours" to i.PubOpeningHours
        )
        db.collection("Groups").document(GroupUUID).collection("Pubs").document(i.PubName.toString())
            .set(places, SetOptions.merge())
            .addOnSuccessListener {
                myRuleAdaptor.notifyDataSetChanged()
            }
    }
}

fun RemovePlacesFromGroup(placesModel: ArrayList<GooglePlacesModel>,GroupUUID: String,myRuleAdaptor: LocationRecycleView){
    val db = Firebase.firestore
    for (i in placesModel){
        db.collection("Groups").document(GroupUUID).collection("Pubs").document(i.PubName.toString()).delete()
            .addOnSuccessListener {
                Log.d("PubFromGroup","Pub Is removed from group ${i.PubName}")
                myRuleAdaptor.notifyDataSetChanged()
            }


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
