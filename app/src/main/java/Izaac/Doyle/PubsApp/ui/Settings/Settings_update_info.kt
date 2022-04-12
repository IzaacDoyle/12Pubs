package Izaac.Doyle.PubsApp.ui.Settings

import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser
import Izaac.Doyle.PubsApp.Firebase.UploadImage
import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.Models.AccountModel
import Izaac.Doyle.PubsApp.databinding.SettingUpdateInfoBinding
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.storage.FirebaseStorage

class settings_update_info: BottomSheetDialogFragment() {

    private var _binding: SettingUpdateInfoBinding? = null
    private  var ImageUri:Uri? = null

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

        binding.accountImageUpdate.isEnabled = false



        val sharedPrefInfo = requireContext().getSharedPreferences(CheckCurrentUser()!!.uid, Context.MODE_PRIVATE)

       binding.accountUsernameUpdate.setText(sharedPrefInfo.getString("Username", ""))

        binding.accountToggleUpdate.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                binding.accountImageUpdate.isEnabled = true
                binding.accountUsername1.isEnabled = true
                binding.accountInfoUpdate.isEnabled = true
            }
            if (!isChecked){
                binding.accountImageUpdate.isClickable = false
                binding.accountUsername1.isEnabled = false
               binding.accountInfoUpdate.isEnabled = false
            }
        }


        binding.accountInfoUpdate.setOnClickListener {

            app.account.UpdateAccountDB(AccountModel(CheckCurrentUser()!!.uid,binding.accountUsernameUpdate.text.toString().trim(),""))

            if (ImageUri != null) {
                UploadImage(CheckCurrentUser()!!.uid, ImageUri!!, "ProfileImage")
            }
            val datastore = requireActivity().getSharedPreferences(CheckCurrentUser()!!.uid,Context.MODE_PRIVATE)

            val editor = datastore.edit()
            //editor.putString("Email", result.data!!["Username"] as String?)
            editor.putString("Username",binding.accountUsernameUpdate.text.toString().trim())
            editor.apply()
            dismiss()

        }
        val profileImage = binding.accountImageUpdate

        val FBprofileImage = FirebaseStorage.getInstance().reference.child("${CheckCurrentUser()!!.uid}/ProfileImage.jpg")

        FBprofileImage.downloadUrl.addOnSuccessListener { Uri ->
            val imageURL = Uri.toString()
            Glide.with(this).load(imageURL).into(profileImage)
        }

        binding.accountImageUpdate.setOnClickListener {
            ImageLauncher.launch("image/*")
            // ShowImagePicker(requireActivity(),IMAGE_REQUEST,"Group")
        }






        return root
    }


    private val ImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){ uri: Uri? ->
        uri?.let {
            binding.accountImageUpdate.setImageURI(uri)
            ImageUri = uri
        }
    }

}