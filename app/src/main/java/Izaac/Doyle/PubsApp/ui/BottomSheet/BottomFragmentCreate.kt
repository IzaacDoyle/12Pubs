package Izaac.Doyle.PubsApp.ui.BottomSheet


import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.Models.AccountModel
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.activities.MainActivity
import Izaac.Doyle.PubsApp.databinding.AccountCreateBottomDialogBinding
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth

class BottomFragmentCreate: BottomSheetDialogFragment(),onDataPasser {

    private lateinit var googleSignInClient: GoogleSignInClient
    private var _binding: AccountCreateBottomDialogBinding? = null
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var providers:List<AuthUI.IdpConfig>
    lateinit var app: MainApp
    lateinit var dataPasser : onDataPasser
    var emailValid:Boolean = false
    var passwordValid:Boolean = false
    var usernameValid:Boolean = false

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


        //val view = inflater.inflate(R.layout.account_create_bottom_dialog, container, false)
        _binding = AccountCreateBottomDialogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        firebaseAuth = FirebaseAuth.getInstance()

        providers = arrayListOf(
           // AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
            )


            binding.LoginAccount.setOnClickListener {
                dismiss()
                dataPasser.changeBottomSheet("Login")

           //dataPasser.changeBottomSheet("Login")
            Log.d("CreateAccount","Login CLicked ")

        }


            binding.UserCreateCreate.setOnClickListener {
                binding.UserEmailCreate2.clearFocus()
                binding.UserPasswordCreate2.clearFocus()
                binding.UserUsernameCreate2.clearFocus()
                binding.UserPasswordRetypeCreate2.clearFocus()



            if (binding.UserEmailCreate2.text.toString().isBlank()) {
                binding.UserEmailCreate1.helperText = "Required"
            } else if (binding.UserUsernameCreate2.text.toString().isBlank()) {
               binding.UserUsernameCreate1.helperText = "Required"
            } else if (binding.UserPasswordCreate2.text.toString().isBlank()) {
                binding.UserPasswordCreate1.helperText = "Required"
            }else if (binding.UserPasswordRetypeCreate2.text.toString().isBlank()) {
                binding.UserPasswordRetypeCreate1.helperText = "Required"
            } else {

                if (binding.UserPasswordCreate2.text.toString().trim() != binding.UserPasswordRetypeCreate2.text.toString().trim()) {
                    binding.UserPasswordCreate1.helperText = "Passwords do not Match"
                    binding.UserPasswordRetypeCreate1.helperText = "Passwords do not Match"
                } else {
                    if (passwordValid && emailValid) {
                        app.account.LoginCreate(
                            AccountModel("",  binding.UserUsernameCreate2.text.toString().trim(), binding.UserEmailCreate2.text.toString().trim(),""),
                            binding.UserPasswordCreate2.text.toString().trim(),
                            requireActivity()
                        )
                        dismiss()


                    }
                    /* "Noting Returned" -> Log.d("Create Account","Global scope not reached and create account was not Attempted")
                      "Email Already In Use" -> {
                          Log.d("User Create BFC","Email is in User")
                          Toast.makeText(requireContext(),"Email Address Linked with Another Account",Toast.LENGTH_SHORT).show()
                      }

                      */
                }
            }
        }

        binding.GoogleSignIn.setOnClickListener {

            val signinIntent  = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                //.setAuthMethodPickerLayout(customLayout)
                .build()
            signInLauncher.launch(signinIntent)

//            val gso = GoogleSignInOptions
//                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build()
//
//            googleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(requireActivity(), gso)
//            // Google Sign in
//            Log.d("Google Sign In","Google Sign in Attempt")
//            val intent = googleSignInClient.signInIntent
//            // both methods are here, the launch did not work but check again
//            activityResultLauncher.launch(intent)
            //startActivityForResult Is depercated( do not use any more)
            //  startActivityForResult(intent,GoogleSignIn_R_Code)
        }


        return root

    }



    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        if (FirebaseAuth.getInstance().currentUser !=null){
            Log.d("Info",res.toString())
            dataPasser.AccountStatus("Task was Successful", res.idpResponse?.email!!)
            dismiss()
        }
    }


    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {

        if(it.resultCode == Activity.RESULT_OK){
            Log.d("Activity Result","Activity Result has been started")
            val task  = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                app.account.GoogleSignIn(account.idToken,requireActivity(),"SignIn")
                dismiss()
            }catch (e:Exception){
              //  Toast.makeText(requireContext() ,    "${e.message} $e", Toast.LENGTH_SHORT).show()
                Log.d("Google SignIn","${e.message} $e")
            }
        }


    })



    private fun emailFocusListener() {

        binding.UserEmailCreate2.setOnFocusChangeListener { _, focused ->
            if (!focused){
                binding.UserEmailCreate1.helperText  = validEmail()
            }
        }

    }

    private fun passwordFocusListener(){

       binding.UserPasswordCreate2.setOnFocusChangeListener { _, focused ->
            if (!focused){
                binding.UserPasswordCreate1.helperText  = validPassWord()
            }
        }
        binding.UserPasswordRetypeCreate2.setOnFocusChangeListener { _, focused ->
            if (!focused){
                binding.UserPasswordRetypeCreate1.helperText  = validPassWord()
            }
        }

    }


    private fun validPassWord(): String? {
        val password = binding.UserPasswordCreate2.text.toString().trim()
        val password2 = binding.UserPasswordRetypeCreate2.text.toString().trim()

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

        if (!password2.isBlank()) {
            if (password != password2) {
                return "Passwords do not Match"
            }
        }
        passwordValid = true
       // return "All Good"
        return null
    }

    private fun validEmail(): String? {
        val email = binding.UserEmailCreate2.text.toString().trim()

        if (email.isEmpty())return null
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return "Email is Incorrect"
        }
        emailValid = true
        //return "All Good"
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
      // dataPasser.changeBottomSheet("Login")
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.isFitToContents
//        behavior.peekHeight = 1200
//        behavior.maxHeight = 1400

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        dataPasser = context as onDataPasser

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailFocusListener()
        passwordFocusListener()

    }

    override fun changeBottomSheet(sheetActive: String) {
        TODO("Not yet implemented")
    }

    override fun AccountStatus(info: String, email: String) {
        when (info) {
            "Task was Successful" -> {
                //restart UI
            }
            "Error Email Already in Use" -> {
                dismiss()
            }
        }
    }

    override fun PassView(view: Boolean) {

    }


}