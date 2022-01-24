package Izaac.Doyle.PubsApp.ui.BottomSheet



import Izaac.Doyle.PubsApp.Firebase.firebaseAuthWithGoogle
import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.Models.AccountModel
import Izaac.Doyle.PubsApp.R
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.Exception


import android.util.Patterns
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout


class BottomFragmentLogin: BottomSheetDialogFragment(),onDataPasser{

    private lateinit var googleSignInClient: GoogleSignInClient
    private val GoogleSignIn_R_Code = 100
    lateinit var dataPasser : onDataPasser
    lateinit var app: MainApp
    var Account = AccountModel()
    var emailValid:Boolean = false
    var passwordValid:Boolean = false
     var extraData: String? = null

    /*
    Tasks to do with this Page

    1) Fix deprecated onActivityResult
    2) Add Username verification
    3) Fix Google Button in Layout, Needs a better look, its very spaced out
     */




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = requireActivity().application as MainApp

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.account_bottom_dialog, container , false)


        val CreateAccount = view?.findViewById<TextView>(R.id.Login_Account)
        val UserLoginLogin = view?.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.User_Login_Login)
        val UserEmailLogin = view?.findViewById<EditText>(R.id.User_Email_Login2)
       // val UserPasswordLogin = view?.findViewById<TextInputLayout>(R.id.User_Password_Login1)
        val GoogleSignIn = view?.findViewById<com.google.android.gms.common.SignInButton>(R.id.Google_SignIn)
        val UserTextInput = view?.findViewById<EditText>(R.id.User_Password_Login2)

        CreateAccount!!.setOnClickListener {
            dismiss()
            dataPasser.changeBottomSheet("Create")
            Log.d("CreateAccount","CreateAccount CLicked  ${CreateAccount.text}")

        }


        UserLoginLogin!!.setOnClickListener {
            UserEmailLogin?.clearFocus()
            UserTextInput?.clearFocus()
            if (emailValid && passwordValid){
                Log.d("User","User can be logged in or if account not there create account prompt")

            }

        }


       GoogleSignIn!!.setOnClickListener {

           val gso = GoogleSignInOptions
               .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
               .requestIdToken(getString(R.string.default_web_client_id))
               .requestEmail()
               .build()

           googleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(requireActivity(), gso)
            // Google Sign in
            Log.d("Google Sign In","Google Sign in Attempt")
                val intent = googleSignInClient.signInIntent
                // both methods are here, the launch did not work but check again
          // ActivityResultLauncher.launch(intent)
           //startActivityForResult Is depercated( do not use any more)
                startActivityForResult(intent,GoogleSignIn_R_Code)
            }


        return view
    }

    private fun emailFocusListener() {
        val UserEmailLogin = view?.findViewById<TextInputLayout>(R.id.User_Email_Login1)
        val UserEmailTextInput = view?.findViewById<EditText>(R.id.User_Email_Login2)
         UserEmailTextInput!!.setOnFocusChangeListener { _, focused ->
             if (!focused){
                UserEmailLogin!!.helperText  = validEmail()
           }
          }

    }

    private fun passwordFocusListener(){
        val UserPasswordLogin = view?.findViewById<TextInputLayout>(R.id.User_Password_Login1)
        val UserPassedTextInput = view?.findViewById<EditText>(R.id.User_Password_Login2)

        UserPassedTextInput!!.setOnFocusChangeListener { _, focused ->
            if (!focused){
                UserPasswordLogin!!.helperText  = validPassWord()
            }
        }
    }



    private fun validPassWord(): String {
        val password = view?.findViewById<EditText>(R.id.User_Password_Login2)?.text.toString().trim()
        if (password.length < 6) {
            return "Password must be at least 6 Characters Long"
        }
        if (!password.matches(".*[A-Z].*".toRegex())) {
            return "Password must contain at least 1 Upper Case"
        }
        if (!password.matches(".*[a-z].*".toRegex())) {
            return "Password must contain at least 1 Lower Case"
        }
        if (!password.matches(".*[0-9].*".toRegex())){
            return "Password must contain at least 1 Digit"
        }
        passwordValid = true
        return "All Good"
    }

    private fun validEmail(): String {
        val email = view?.findViewById<EditText>(R.id.User_Email_Login2)?.text.toString().trim()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return "Email is Incorrect"
        }
        emailValid = true
        return "All Good"
    }


    // needs to be changed to on Activity Call back
    //Google sign in method
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==  GoogleSignIn_R_Code){
            val task  = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken,requireActivity())
                dismiss()
            }catch (e:Exception){
                Toast.makeText(requireContext() ,    "${e.message} $e", Toast.LENGTH_SHORT).show()
                Log.d("Google SignIn","${e.message} $e")
            }
        }
    }

    private val ActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {

        if(it.resultCode == RESULT_OK){
            Log.d("Activity Result","Activity Result has been started")

            val task  = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken,requireActivity())
                dismiss()
            }catch (e:Exception){
                Toast.makeText(requireContext() ,    "${e.message} $e", Toast.LENGTH_SHORT).show()
                Log.d("Google SignIn","${e.message} $e")
            }
        }


    })

    override fun onAttach(context: Context) {
        super.onAttach(context)

        //helps to pass data from bottom sheet to main activity.
        // TO keep parent in control for most part
        dataPasser = context as onDataPasser

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailFocusListener()
        passwordFocusListener()

    }

    override fun onDestroy() {
        dismiss()
        super.onDestroy()

    }

    override fun changeBottomSheet(sheetActive: String) {
        TODO("Not yet implemented")
    }

    override fun ErrorCreatingAccount(info: String, email: String) {

        when (info) {
            "Task was Successful" -> {
                //restart UI
            }
            "Error Email Already in Use" -> {
                val UserEmailLogin = view?.findViewById<EditText>(R.id.User_Email_Login2)
                (UserEmailLogin as TextView).text = email
            }
        }


    }


}

