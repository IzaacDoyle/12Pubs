package Izaac.Doyle.PubsApp.Firebase

import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.Models.AccountModel
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

import com.google.firebase.auth.FirebaseAuthUserCollisionException

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.delay


fun GoogleSignInAccount(){
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()




}

   fun FBCreateAccount(account: AccountModel, activity: Activity) {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val Email = account.email
    val Username = account.username
    val Password = account.password
    //var info: String? = null
       var dataPasser :onDataPasser



    firebaseAuth.createUserWithEmailAndPassword(Email,Password)
        .addOnCompleteListener(activity) { task->
            dataPasser = activity as onDataPasser

            if (task.isSuccessful){
                val user = firebaseAuth.currentUser
                //UpDateUI
                    dataPasser.ErrorCreatingAccount("Task was Successful", Email)

               // info = "Task was Successful"
            }

            if (!task.isSuccessful) {
                 try {
                    throw task.exception!!
                } catch (e: FirebaseAuthUserCollisionException) {
                     dataPasser.ErrorCreatingAccount("Error Email Already in Use",Email)
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

fun FBLogout(){
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    firebaseAuth.signOut()



}

fun CheckCurrentUser(): UserInfo? {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    return firebaseAuth.currentUser

}


  fun firebaseAuthWithGoogle(idToken: String?,activity: Activity) {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    firebaseAuth.signInWithCredential(credential)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("TAG", "signInWithCredential:success")
                val user = firebaseAuth.currentUser

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
