package Izaac.Doyle.PubsApp.Firebase

import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.Models.AccountModel
import Izaac.Doyle.PubsApp.Models.FBAccountModel
import Izaac.Doyle.PubsApp.activities.MainActivity
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*


class FirebaseManager(application: Application) {

    private var application: Application? = null

    var firebaseAuth: FirebaseAuth? = null
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()


    init {

        this.application = application
        firebaseAuth = FirebaseAuth.getInstance()
        CheckCurrentUser()


        if (firebaseAuth!!.currentUser != null){
            liveFirebaseUser.postValue(firebaseAuth!!.currentUser)
            Log.d("CurrentUser",liveFirebaseUser.value.toString() )
        }

    }

    fun CheckCurrentUser() {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        Log.d("CurrentUser2", firebaseAuth.currentUser?.uid.toString() )
        liveFirebaseUser.value = firebaseAuth.currentUser
    }



    fun LoginEP(email:String,password: String,context: Context,activity: Activity){
        var dataPasser :onDataPasser
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task->
                if (task.isSuccessful){

                    Log.d("SignIn","${task.result}  Logged In ")
//                    liveFirebaseUser.postValue(firebaseAuth.currentUser)
                    liveFirebaseUser.value = firebaseAuth.currentUser
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
                        dataPasser.AccountStatus("Login Failed",email,null)


                    }catch (e:Exception){
                        Log.d("SignINError","${e.message} with email $email")
                        Toast.makeText(context, "Login Failed Please Try Again", Toast.LENGTH_SHORT).show()
                    }

                }
            }
    }

    fun EPRegister(account: AccountModel, password: String, activity: Activity){
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val Email = account.email
        val Username = account.username
        var dataPasser :onDataPasser

        firebaseAuth.createUserWithEmailAndPassword(Email,password)
            .addOnCompleteListener(activity) { task->
                dataPasser = activity as onDataPasser
                if (task.isSuccessful){
                    val user = firebaseAuth.currentUser

//                    liveFirebaseUser.postValue(user)
                    liveFirebaseUser.value = firebaseAuth.currentUser
                    //UpDateUI
                    //May need to change to FBAccountModel
                    dataPasser.AccountStatus("Task was Successful", Email,null)


//                FBcreateDB(user!!.uid, Username,Email)
                    val accounts = FBAccountModel(user!!.uid,Username,Email,null,null,null)
                    Log.d("FirebaseRealTimeDBTest", "Create Account $account")
                    AccountData.createAccountDB(accounts,null,activity)
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

                        //May need to change to FBAccountModel
                        dataPasser.AccountStatus("Error Email Already in Use",Email,null)

                        Log.d("User Create" ,"Error Email Already in Use ${e.message}  FBCr")
                        //   "Email Already In Use"
                    } catch (e: Exception) {
                        //  "Error with Firebase ${e.message}"
                    }
                }
            }

    }



    fun ReAuth(Email:String,password:String,info:String,activity: Activity){
        var dataPasser: onDataPasser
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.currentUser!!.reauthenticate(EmailAuthProvider.getCredential(Email,password)).addOnCompleteListener { task ->
            if (task.isSuccessful){
                dataPasser = activity as onDataPasser
                Log.d("ReAuth Delete","Auth Entered")
                if (info == "Delete"){
                    // dataPasser = activity as onDataPasser
//                    liveFirebaseUser.postValue(firebaseAuth!!.currentUser)
                    liveFirebaseUser.value = firebaseAuth.currentUser
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



    fun LogOut(activity: Activity){
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
    }



    fun DeleteAccount(activity: Activity, context: Context, account: FBAccountModel){
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser!!


        val uuid = account.UserUUID
        val key = account.AccountID

        val AccountDelete : MutableMap<String,Any?> = HashMap()
        AccountDelete["/accounts/$uuid/$key"] = null

        AccountData.database.updateChildren(AccountDelete)

        user.delete().addOnCompleteListener  { task->
            if (task.isSuccessful){

//                AccountData.signOutAccount(account, activity)
                LogOut(activity)

                val intent = Intent(context, MainActivity()::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.startActivity(intent)

            }else{
                Log.d("Error Delete",task.exception?.message.toString())
            }

        }
    }




}