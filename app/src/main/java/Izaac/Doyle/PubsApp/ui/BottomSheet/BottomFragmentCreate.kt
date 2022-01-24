package Izaac.Doyle.PubsApp.ui.BottomSheet

import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.R
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

    lateinit var app: MainApp
    lateinit var dataPasser : onDataPasser
    var emailValid:Boolean = false
    var passwordValid:Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = requireActivity().application as MainApp

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.account_create_bottom_dialog, container, false)



        val LoginAccountButton = view?.findViewById<TextView>(R.id.Login_Account)
        val Create_Button_Account = view?.findViewById<Button>(R.id.User_Create_Create)
        val email = view?.findViewById<EditText>(R.id.User_Email_Create2)
        val password = view?.findViewById<EditText>(R.id.User_Password_Create2)
        val username = view?.findViewById<EditText>(R.id.User_Username_Create2)
        val password_retype = view?.findViewById<EditText>(R.id.User_Password_Retype_Create2)
        val UserPasswordCreate = view?.findViewById<TextInputLayout>(R.id.User_Password_Create1)
        val UserPasswordCreate2 = view?.findViewById<TextInputLayout>(R.id.User_Password_Retype_Create1)


        LoginAccountButton!!.setOnClickListener {
            dismiss()
           //dataPasser.changeBottomSheet("Login")
            Log.d("CreateAccount","Login CLicked ")

        }

        Create_Button_Account!!.setOnClickListener {
            password?.restoreDefaultFocus()
            email?.clearFocus()
            password?.clearFocus()
            username?.clearFocus()
            password_retype?.clearFocus()

            if (password?.text.toString().trim() != password_retype?.text.toString().trim()){
                UserPasswordCreate?.helperText = "Passwords do not Match"
                UserPasswordCreate2?.helperText = "Passwords do not Match"
            }else{
                if (passwordValid && emailValid){
                  app.account.LoginCreate(email?.text.toString().trim(),password?.text.toString().trim(),username?.text.toString().trim(),requireActivity())

                    }
                     /* "Noting Returned" -> Log.d("Create Account","Global scope not reached and create account was not Attempted")
                      "Email Already In Use" -> {
                          Log.d("User Create BFC","Email is in User")
                          Toast.makeText(requireContext(),"Email Address Linked with Another Account",Toast.LENGTH_SHORT).show()
                      }

                      */
                  }
                }







        return view

    }



    private fun emailFocusListener() {
        val UserEmailLogin = view?.findViewById<TextInputLayout>(R.id.User_Email_Create1)
        val UserEmailTextInput = view?.findViewById<EditText>(R.id.User_Email_Create2)
        UserEmailTextInput!!.setOnFocusChangeListener { _, focused ->
            if (!focused){
                UserEmailLogin!!.helperText  = validEmail()
            }
        }

    }

    private fun passwordFocusListener(){
        val UserPasswordCreate = view?.findViewById<TextInputLayout>(R.id.User_Password_Create1)
        val UserPassedTextInput = view?.findViewById<EditText>(R.id.User_Password_Create2)
        val UserPasswordCreate2 = view?.findViewById<TextInputLayout>(R.id.User_Password_Retype_Create1)
        val UserPassedTextInput2 = view?.findViewById<EditText>(R.id.User_Password_Retype_Create2)
        UserPassedTextInput!!.setOnFocusChangeListener { _, focused ->
            if (!focused){
                UserPasswordCreate!!.helperText  = validPassWord()
            }
        }
        UserPassedTextInput2!!.setOnFocusChangeListener { _, focused ->
            if (!focused){
                UserPasswordCreate2!!.helperText  = validPassWord()
            }
        }

    }


    private fun validPassWord(): String {
        val password = view?.findViewById<EditText>(R.id.User_Password_Create2)?.text.toString().trim()
        val password2 = view?.findViewById<EditText>(R.id.User_Password_Retype_Create2)?.text.toString().trim()
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

        if (password != password2){
            return "Passwords do not Match"
        }
        passwordValid = true
        return "All Good"
    }

    private fun validEmail(): String {
        val email = view?.findViewById<EditText>(R.id.User_Email_Create2)?.text.toString().trim()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return "Email is Incorrect"
        }
        emailValid = true
        return "All Good"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dataPasser.changeBottomSheet("Login")
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

    override fun ErrorCreatingAccount(info: String, email: String) {
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