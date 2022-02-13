package Izaac.Doyle.PubsApp.Firebase

import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.Models.AccountModel
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.activities.MainActivity
import Izaac.Doyle.PubsApp.ui.BottomSheet.BottomFragmentDelete
import Izaac.Doyle.PubsApp.ui.BottomSheet.BottomFragmentLogin
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*


fun FBReAuth(Email:String,password:String,info:String,activity: Activity){
var dataPasser:onDataPasser
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    firebaseAuth.currentUser!!.reauthenticate(EmailAuthProvider.getCredential(Email,password)).addOnCompleteListener {task ->
        if (task.isSuccessful){
            dataPasser = activity as onDataPasser
            Log.d("ReAuth Delete","Auth Entered")
            if (info == "Delete"){
           // dataPasser = activity as onDataPasser
                dataPasser.changeBottomSheet("Delete")
            //    MainActivity().reAuth = "true"
           // SettingsFragment().dialog()
            //SettingsFragment().reAuth = true
                //FBDeleteAccount()
            }
        }
        if (!task.isSuccessful){
            Log.d("ReAuth Failed", task.exception?.message.toString())
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
                    dataPasser.AccountStatus("Task was Successful", Email)

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
                     dataPasser.AccountStatus("Error Email Already in Use",Email)
                    Log.d("User Create" ,"Error Email Already in Use ${e.message}  FBCr")
                  //   "Email Already In Use"
                } catch (e: Exception) {
                  //  "Error with Firebase ${e.message}"
                }
                }


            }

}

fun FBLogin(email:String,password: String,context: Context,activity: Activity){
    var dataPasser :onDataPasser
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    firebaseAuth.signInWithEmailAndPassword(email,password)
        .addOnCompleteListener { task->
            if (task.isSuccessful){

                Log.d("SignIn","${task.result}  Logged In ")
                activity.recreate()


            }
//            else
//                Log.d("SignInfailed","${task.result} with Email of $email")
            if (!task.isSuccessful){
                dataPasser = activity as onDataPasser
                try {
                    throw task.exception!!

                }catch (e: FirebaseAuthInvalidUserException){
                    Log.d("SignInFailed","Error account not valid")
                    Toast.makeText(context, "Account Details Incorrect", Toast.LENGTH_SHORT).show()
                    dataPasser.AccountStatus("Login Failed",email)


                }catch (e:Exception){
                    Log.d("SignINError","${e.message} with email $email")
                    Toast.makeText(context, "Login Failed Please Try Again", Toast.LENGTH_SHORT).show()
                }

            }
    }

}

fun FBDeleteAccount(activity: Activity,context: Context){

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
   val user = firebaseAuth.currentUser!!
    user.delete().addOnCompleteListener  { task->
        if (task.isSuccessful){

           // val navController = Navigation.findNavController(activity, R.id.nav_host_fragment_content_main)
           // navController.navigate(R.id.nav_home)
            val intent = Intent(context,MainActivity()::class.java)
            activity.startActivity(intent)


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


  fun GoogleSignInAccount(idToken: String?,activity: Activity,info: String) {
     var dataPasser :onDataPasser
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    firebaseAuth.signInWithCredential(credential)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                dataPasser = activity as onDataPasser
               // reAuth = settings_update_info() as reAuth
                // Sign in success, update UI with the signed-in user's information
                Log.d("TAG", "signInWithCredential:success")
                val user = firebaseAuth.currentUser
                if (task.result.additionalUserInfo?.isNewUser == true) {
                    FBcreateDB(user!!.uid, user.displayName.toString())
                }
                if (info == "Delete"){
                //call ondatapasser
                    dataPasser.changeBottomSheet("Delete")

                }else{
                    activity.recreate()
                }
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
