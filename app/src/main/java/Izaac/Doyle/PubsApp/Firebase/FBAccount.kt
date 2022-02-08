package Izaac.Doyle.PubsApp.Firebase

import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.Models.AccountModel
import Izaac.Doyle.PubsApp.activities.MainActivity
import Izaac.Doyle.PubsApp.ui.BottomSheet.BottomFragmentCreate
import Izaac.Doyle.PubsApp.ui.Settings.SettingsFragment
import android.accounts.Account
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.*
import java.lang.Exception
import android.R
import android.app.Dialog
import android.view.View
import androidx.core.view.get
import androidx.navigation.Navigation

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

import com.google.firebase.auth.FirebaseAuthUserCollisionException

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun FBReAuth(Email:String,password:String,info:String){

    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    firebaseAuth.currentUser!!.reauthenticate(EmailAuthProvider.getCredential(Email,password)).addOnCompleteListener {task ->
        if (task.isSuccessful){
            Log.d("ReAuth","Auth Entered")
            if (info == "Delete"){
            SettingsFragment().dialog()
            //SettingsFragment().reAuth = true
                //FBDeleteAccount()
            }
        }
    }

}

   fun FBCreateAccount(account: AccountModel,password: String, activity: Activity) {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val Email = account.email
    val Username = account.username
   // val Password = account.password
    //var info: String? = null
       var dataPasser :onDataPasser

    firebaseAuth.createUserWithEmailAndPassword(Email,password)
        .addOnCompleteListener(activity) { task->
            dataPasser = activity as onDataPasser

            if (task.isSuccessful){
                val user = firebaseAuth.currentUser
                //UpDateUI
                    dataPasser.CreatingAccount("Task was Successful", Email)

                FBcreateDB(user!!.uid, Username)
               // info = "Task was Successful"

                val navController = Navigation.findNavController(activity,
                    Izaac.Doyle.PubsApp.R.id.nav_host_fragment_content_main)
                navController.navigate(Izaac.Doyle.PubsApp.R.id.nav_home)
                activity.recreate()

            }else{
                Log.d("UserCreate", task.exception!!.message.toString()+ account.email)
            }

            if (!task.isSuccessful) {
                 try {
                    throw task.exception!!
                } catch (e: FirebaseAuthUserCollisionException) {
                     dataPasser.CreatingAccount("Error Email Already in Use",Email)
                    Log.d("User Create" ,"Error Email Already in Use ${e.message}  FBCr")
                  //   "Email Already In Use"
                } catch (e: Exception) {
                  //  "Error with Firebase ${e.message}"
                }
                }


            }

}

fun FBLogin(){
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
}

fun FBDeleteAccount(){

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
   val user = firebaseAuth.currentUser!!
    user.delete().addOnCompleteListener  { task->
        if (task.isSuccessful){


        }else{
            Log.d("Error Delete",task.exception?.message.toString())
        }

    }


}

fun FBLogout(activity: Activity){
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    val googleSignInClient: GoogleSignInClient

    val gso = GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(activity.getString(Izaac.Doyle.PubsApp.R.string.default_web_client_id))
        .requestEmail()
        .build()

    googleSignInClient = GoogleSignIn.getClient(activity, gso)




    googleSignInClient.signOut()


    firebaseAuth.signOut()

    activity.recreate()






}

fun CheckCurrentUser(): UserInfo? {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    return firebaseAuth.currentUser

}


  fun GoogleSignInAccount(idToken: String?,activity: Activity) {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    firebaseAuth.signInWithCredential(credential)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("TAG", "signInWithCredential:success")
                val user = firebaseAuth.currentUser
                if (task.result.additionalUserInfo?.isNewUser == true) {
                    FBcreateDB(user!!.uid, user.displayName.toString())
                }
                activity.recreate()
                //need to change my motion of updating the users view with a screen refresh,
                // to update Users name and email and profile and to add the content
                //eg.Group Info, Rules info, Account Prefrances.
                //updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w("TAG", "signInWithCredential:failure", task.exception)
                //updateUI(null)
            }
        }
}
