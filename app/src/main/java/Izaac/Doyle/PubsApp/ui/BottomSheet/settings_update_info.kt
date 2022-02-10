package Izaac.Doyle.PubsApp.ui.BottomSheet

import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser
import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.Models.AccountModel
import Izaac.Doyle.PubsApp.databinding.AccountBottomDialogBinding
import Izaac.Doyle.PubsApp.databinding.SettingUpdateInfoBinding
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class settings_update_info: BottomSheetDialogFragment() {

    private var _binding: SettingUpdateInfoBinding? = null

    lateinit var app: MainApp




    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SettingUpdateInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        app = requireActivity().application as MainApp



        val sharedPrefInfo = requireContext().getSharedPreferences(CheckCurrentUser()!!.uid, Context.MODE_PRIVATE)

       binding.accountUsernameUpdate.setText(sharedPrefInfo.getString("Username", ""))

        binding.accountToggleUpdate.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                binding.accountImageUpdate.isClickable = true
                binding.accountUsername1.isEnabled = true
            }
            if (!isChecked){
                binding.accountImageUpdate.isClickable = false
                binding.accountUsername1.isEnabled = false
            }
        }


        binding.accountInfoUpdate.setOnClickListener {

            app.account.UpdateAccountDB(AccountModel(CheckCurrentUser()!!.uid,binding.accountUsernameUpdate.text.toString().trim(),""))
            val datastore = requireActivity().getSharedPreferences(CheckCurrentUser()!!.uid,Context.MODE_PRIVATE)
            val editor = datastore.edit()
            //editor.putString("Email", result.data!!["Username"] as String?)
            editor.putString("Username",binding.accountUsernameUpdate.text.toString().trim())
            editor.apply()

        }


        return root
    }



}