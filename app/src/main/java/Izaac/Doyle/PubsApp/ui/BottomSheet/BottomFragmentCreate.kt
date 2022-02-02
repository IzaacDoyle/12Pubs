package Izaac.Doyle.PubsApp.ui.BottomSheet

import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.databinding.AccountBottomDialogBinding
import Izaac.Doyle.PubsApp.databinding.AccountCreateBottomDialogBinding
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import org.w3c.dom.Text

class BottomFragmentCreate: BottomSheetDialogFragment(),onDataPasser {

    private var _binding: AccountCreateBottomDialogBinding? = null
    lateinit var app: MainApp
    lateinit var dataPasser : onDataPasser
    var emailValid:Boolean = false
    var passwordValid:Boolean = false

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


//        val LoginAccountButton = view?.findViewById<TextView>(R.id.Login_Account)
//        val Create_Button_Account = view?.findViewById<Button>(R.id.User_Create_Create)
//        val email = view?.findViewById<EditText>(R.id.UserEmailCreate2)
//        val password = view?.findViewById<EditText>(R.id.UserPasswordCreate2)
//        val username = view?.findViewById<EditText>(R.id.UserUsernameCreate2)
//        val password_retype = view?.findViewById<EditText>(R.id.UserPasswordRetypeCreate2)
//        val UserPasswordCreate = view?.findViewById<TextInputLayout>(R.id.UserPasswordCreate1)
//        val UserPasswordCreate2 = view?.findViewById<TextInputLayout>(R.id.UserPasswordRetypeCreate1)
//        val UserEmailLogin = view?.findViewById<TextInputLayout>(R.id.User_Email_Create1)
//        val UsernameTextInput = view?.findViewById<TextInputLayout>(R.id.UserUsernameCreate1)




            binding.LoginAccount.setOnClickListener {
            dismiss()
           //dataPasser.changeBottomSheet("Login")
            Log.d("CreateAccount","Login CLicked ")

        }


            binding.UserCreateCreate.setOnClickListener {
                //binding.UserPasswordCreate2.restoreDefaultFocus()
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
                            binding.UserEmailCreate2.text.toString().trim(),
                            binding.UserPasswordCreate2.text.toString().trim(),
                           binding.UserUsernameCreate2.text.toString().trim(),
                            requireActivity()
                        )

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


        return root

    }



    private fun emailFocusListener() {
//        val UserEmailLogin = view?.findViewById<TextInputLayout>(R.id.UserEmailCreate1)
//        val UserEmailTextInput = view?.findViewById<EditText>(R.id.UserEmailCreate2)
        binding.UserEmailCreate2.setOnFocusChangeListener { _, focused ->
            if (!focused){
                binding.UserEmailCreate1.helperText  = validEmail()
            }
        }

    }

    private fun passwordFocusListener(){
//        val UserPasswordCreate = view?.findViewById<TextInputLayout>(R.id.UserPasswordCreate1)
//        val UserPassedTextInput = view?.findViewById<EditText>(R.id.UserPasswordCreate2)
//        val UserPasswordCreate2 = view?.findViewById<TextInputLayout>(R.id.UserPasswordRetypeCreate1)
//        val UserPassedTextInput2 = view?.findViewById<EditText>(R.id.UserPasswordRetypeCreate2)
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
        return "All Good"
    }

    private fun validEmail(): String? {
        val email = binding.UserEmailCreate2.text.toString().trim()

        if (email.isEmpty())return null
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return "Email is Incorrect"
        }
        emailValid = true
        return "All Good"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dataPasser.changeBottomSheet("Login")
        _binding = null
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

    override fun CreatingAccount(info: String, email: String) {
        when (info) {
            "Task was Successful" -> {
                //restart UI
            }
            "Error Email Already in Use" -> {
                dismiss()
            }
        }
    }

}