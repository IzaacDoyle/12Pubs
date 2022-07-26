package Izaac.Doyle.PubsApp.ui.Settings

import Izaac.Doyle.PubsApp.Firebase.AccountActivitysViewModel
import Izaac.Doyle.PubsApp.Firebase.AccountData

import Izaac.Doyle.PubsApp.Firebase.FBUpdateGroup
import Izaac.Doyle.PubsApp.Firebase.UploadImage
import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.Models.AccountModel
import Izaac.Doyle.PubsApp.Models.FBAccountModel
import Izaac.Doyle.PubsApp.databinding.SettingUpdateInfoBinding
import Izaac.Doyle.PubsApp.ui.home.HomeViewModel
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.storage.FirebaseStorage


class settings_update_info: BottomSheetDialogFragment() {

    private var _binding: SettingUpdateInfoBinding? = null
    private  var ImageUri:Uri? = null
//    private val args by navArgs<settings_update_infoArgs>()

    lateinit var app: MainApp

    private val homeViewModel : HomeViewModel by activityViewModels()
    private lateinit var loginViewmodel : AccountActivitysViewModel




    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SettingUpdateInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        loginViewmodel = ViewModelProvider(this)[AccountActivitysViewModel::class.java]
        app = requireActivity().application as MainApp

        binding.accountImageUpdate.isEnabled = false



        val sharedPrefInfo = requireContext().getSharedPreferences(loginViewmodel.liveFirebaseUser.value!!.uid, Context.MODE_PRIVATE)

        homeViewModel.load(loginViewmodel.liveFirebaseUser.value!!.uid)

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
        if (!requireArguments().isEmpty) {
            if (!requireArguments().isEmpty) {
                if (requireArguments().containsKey("GroupUpdate")) {
                    //Update - Text On Group
                    binding.accountUsername1.helperText = "Update Group Name"
                    binding.textView5.text = "Click to Change Group Image"

                }

                binding.accountUsernameUpdate.setText(requireArguments().getString("GroupName"))
            }else{
                //Update - Text On Profile
                binding.accountUsernameUpdate.setText(sharedPrefInfo.getString("Username", ""))
            }
        }

        homeViewModel.observableAccountData.observe(viewLifecycleOwner, Observer {
            println(it[0].AccountID)
        })


        binding.accountInfoUpdate.setOnClickListener {
            if (!requireArguments().isEmpty) {
                if (requireArguments().containsKey("GroupUpdate")) {
                    //Update Group
                    FBUpdateGroup(
                        requireArguments().getString("GroupUUID").toString(),
                        binding.accountUsernameUpdate.text.toString().trim()
                    )
                    if (ImageUri != null) {
                        UploadImage(requireArguments().getString("GroupUUID").toString(), ImageUri!!, "GroupImage"
                        )
                    }
                } else {
                    // Update Profile
//                    app.account.UpdateAccountDB(AccountModel(CheckCurrentUser()!!.uid, binding.accountUsernameUpdate.text.toString().trim(), ""))
                    AccountData.updateAccountUserName(FBAccountModel(loginViewmodel.liveFirebaseUser.value!!.uid,binding.accountUsernameUpdate.text.toString().trim(),
                        loginViewmodel.liveFirebaseUser.value!!.email.toString(),
                        homeViewModel.observableAccountData.value!![0].AccountID))
                    if (ImageUri != null) {
                        UploadImage(loginViewmodel.liveFirebaseUser.value!!.uid, ImageUri!!, "ProfileImage")
                    }
                    val datastore = requireActivity().getSharedPreferences(loginViewmodel.liveFirebaseUser.value!!.uid, Context.MODE_PRIVATE)
                    val editor = datastore.edit()
                    editor.putString("Username", binding.accountUsernameUpdate.text.toString().trim())
                    editor.apply()
                    dismiss()
                }

            }
        }
        val profileImage = binding.accountImageUpdate
        if (!requireArguments().isEmpty) {
            if (requireArguments().containsKey("GroupUpdate")) {
                val FBgroupImage = FirebaseStorage.getInstance().reference.child("${requireArguments().getString("GroupUUID")}/GroupImage.jpg")

                FBgroupImage.downloadUrl.addOnSuccessListener { Uri ->
                    val imageURL = Uri.toString()
                    Glide.with(this).load(imageURL).into(profileImage)
                }
                binding.accountImageUpdate.setOnClickListener {
                    ImageLauncher.launch("image/*")
                }

            } else {
                val FBprofileImage =
                    FirebaseStorage.getInstance().reference.child("${loginViewmodel.liveFirebaseUser.value!!.uid}/ProfileImage.jpg")

                FBprofileImage.downloadUrl.addOnSuccessListener { Uri ->
                    val imageURL = Uri.toString()
                    Glide.with(this).load(imageURL).into(profileImage)
                }
            }

            binding.accountImageUpdate.setOnClickListener {
                ImageLauncher.launch("image/*")
                // ShowImagePicker(requireActivity(),IMAGE_REQUEST,"Group")
            }

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