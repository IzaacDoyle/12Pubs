package Izaac.Doyle.PubsApp.ui.BottomSheet




import Izaac.Doyle.PubsApp.Firebase.AccountActivitysViewModel
import Izaac.Doyle.PubsApp.Firebase.AccountData


import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.Models.AccountModel
import Izaac.Doyle.PubsApp.Models.FBAccountModel
import Izaac.Doyle.PubsApp.Models.RulesModel
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.databinding.AccountBottomDialogBinding
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth


class BottomFragmentLogin: BottomSheetDialogFragment(),onDataPasser{

    private var _binding: AccountBottomDialogBinding? = null

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth : FirebaseAuth



    private lateinit var loginViewmodel: AccountActivitysViewModel

    private lateinit var providers:List<AuthUI.IdpConfig>

    lateinit var dataPasser : onDataPasser
    lateinit var app: MainApp
    var Account = AccountModel()
    var emailValid:Boolean = false
    var passwordValid:Boolean = false
     var extraData: String? = null






    /*
    Tasks to do with this Page


    2) Add Username verification
    3) Fix Google Button in Layout, Needs a better look, its very spaced out
     */


    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = requireActivity().application as MainApp


    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = AccountBottomDialogBinding.inflate(inflater, container, false)
        val root: View = binding.root


        loginViewmodel = ViewModelProvider(this)[AccountActivitysViewModel::class.java]

      // val view = inflater.inflate(R.layout.account_bottom_dialog, container , false)

        firebaseAuth = FirebaseAuth.getInstance()

        if (this.arguments != null) {
            if (arguments?.containsKey("Email") == true) {
                val email = this.arguments?.get("Email").toString()
                binding.UserEmailLogin2.setText(email)
            }else if (arguments?.containsKey("relogin") == true){
                binding.LoginAccount.isEnabled = false
                binding.LoginAccount.isVisible = false
                binding.loginTopText.text = "Login to Re-Authenticate "
                //binding.LoginAccount.gravity = 6
                binding.LoginAccount.text = "Account"
                binding.UserLoginLogin.text = "Re-Authenticate"
                //Log.d("dialogArg",arguments!!.get("relogin").toString())
                 //dialogSettings = arguments?.get("dialog") as Dialog
            }
        }


        binding.LoginAccount.setOnClickListener {
            dismiss()
            dataPasser.changeBottomSheet("Create")
            Log.d("CreateAccount","CreateAccount CLicked  ${binding.LoginAccount.text}")

        }

        providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
        )




        binding.UserLoginLogin.setOnClickListener {
            binding.UserEmailLogin2.clearFocus()
            binding.UserPasswordLogin2.clearFocus()
            if (emailValid && passwordValid) {
                if (arguments?.containsKey("relogin") == true) {


                    loginViewmodel.ReAuth(
                        binding.UserEmailLogin2.text.toString(),
                        binding.UserPasswordLogin2.text.toString(),
                        "Delete",
                        requireActivity()
                    )




                    dismiss()
                    Log.d("ReAuth Login","Google ReAuth")

                } else {
                    Log.d(
                        "User",
                        "User can be logged in or if account not there create account prompt"
                    )
//                    val accountModel = AccountModel("","",binding.UserEmailLogin2.text.toString(),"")
//                    app.account.SignIn(accountModel,binding.UserPasswordLogin2.text.toString(),requireContext(),requireActivity())

                    loginViewmodel.EPLogin(binding.UserEmailLogin2.text.toString(),binding.UserPasswordLogin2.text.toString(),requireContext(),requireActivity())

                    dismiss()
                }
            }

//        }
        }

        binding.GoogleSignIn.setOnClickListener {
        val signinIntent  = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.LoginTheme)
            //.setAuthMethodPickerLayout(customLayout)
            .build()
        signInLauncher.launch(signinIntent)
            }
        return root
    }




    private fun emailFocusListener() {
//        val UserEmailLogin = view?.findViewById<TextInputLayout>(R.id.User_Email_Login1)
//        val UserEmailTextInput = view?.findViewById<EditText>(R.id.User_Email_Login2)
         binding.UserEmailLogin2.setOnFocusChangeListener { _, focused ->
             if (!focused){
                binding.UserEmailLogin1.helperText  = validEmail()
           }
          }

    }

    private fun passwordFocusListener(){
//        val UserPasswordLogin = view?.findViewById<TextInputLayout>(R.id.User_Password_Login1)
//        val UserPassedTextInput = view?.findViewById<EditText>(R.id.User_Password_Login2)

        binding.UserPasswordLogin2.setOnFocusChangeListener { _, focused ->
            if (!focused){
               binding.UserPasswordLogin1.helperText  = validPassWord()
            }
        }
    }



    private fun validPassWord(): String? {
        val password = binding.UserPasswordLogin2.text.toString().trim()

        if (password.isEmpty()){
            return null
        }
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
       // return "All Good"
        return null
    }

    private fun validEmail(): String? {
        val email = binding.UserEmailLogin2.text.toString().trim()
        if (email.isEmpty()){
            return null
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return "Email is Incorrect"
        }
        emailValid = true
        //return "All Good"
        return null
    }


    // needs to be changed to on Activity Call back
    //Google sign in method
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode ==  GoogleSignIn_R_Code){
//            val task  = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                val account = task.getResult(ApiException::class.java)
//                firebaseAuthWithGoogle(account.idToken,requireActivity())
//                dismiss()
//            }catch (e:Exception){
//                Toast.makeText(requireContext() ,    "${e.message} $e", Toast.LENGTH_SHORT).show()
//                Log.d("Google SignIn","${e.message} $e")
//            }
//        }
//    }

    //Firebase UI SignIn
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        if (arguments?.containsKey("relogin")==true){
            Log.d("ReAuth","FirebaseUI ReAuth")
            dataPasser.changeBottomSheet("Delete")
        }else if (FirebaseAuth.getInstance().currentUser !=null){
            Log.d("Info",res.toString())
            //set that its gets username from firebase
            if (res.idpResponse!!.isNewUser){
                val accounts = FBAccountModel(loginViewmodel.liveFirebaseUser.value!!.uid,loginViewmodel.liveFirebaseUser.value!!.displayName.toString(), res.idpResponse!!.email!!,"","")
                Log.d("FirebaseRealTimeDBTest", "Create Account $accounts")
                AccountData.createAccountDB(accounts,this,requireActivity())
                Log.d("Info","FBUI new user")
            }
            //Just Call Recreate
//            dataPasser.AccountStatus("Task was Successful", res.idpResponse?.email!! )
            //

//            dismiss()


         }
        //this.onSignInResult(res)
    }

    override fun onStart() {
        super.onStart()



    }



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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun changeBottomSheet(sheetActive: String) {
        TODO("Not yet implemented")
    }

    override fun AccountStatus(info: String, email: String?, account: FBAccountModel?) {

        when (info) {
            "Task was Successful" -> {
                //restart UI
            }
            "Error Email Already in Use" -> {
//                val UserEmailLogin = view?.findViewById<EditText>(R.id.User_Email_Login2)
//                (UserEmailLogin as TextView).text = email
            }
        }


    }

    override fun PassView(view:Boolean) {

    }

    override fun Rules(Rules: MutableList<RulesModel>) {

    }


}

