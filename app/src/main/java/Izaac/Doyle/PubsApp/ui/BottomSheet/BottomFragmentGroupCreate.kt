package Izaac.Doyle.PubsApp.ui.BottomSheet

import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser
import Izaac.Doyle.PubsApp.Firebase.UploadImage
import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.Models.GroupModel
import Izaac.Doyle.PubsApp.databinding.FragmentGroupCreateBinding
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomFragmentGroupCreate:BottomSheetDialogFragment() {

    private var _binding: FragmentGroupCreateBinding? = null


    private val binding get() = _binding!!
    lateinit var app: MainApp
    private lateinit var ImageUri:Uri




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGroupCreateBinding.inflate(inflater, container, false)
        val root: View = binding.root



        binding.GroupImageAdd.setOnClickListener {
            ImageLauncher.launch("image/*")
           // ShowImagePicker(requireActivity(),IMAGE_REQUEST,"Group")
       }



        binding.CreateGroupButton.setOnClickListener {


            if (!binding.GroupNameAdd.text?.isEmpty()!!) {
                app.group.CreateGroup(
                    GroupModel(
                        CheckCurrentUser()!!.uid,
                        0,
                        binding.GroupNameAdd.text.toString(),
                        ""
                    )
                )

                UploadImage(CheckCurrentUser()!!.uid,ImageUri,"GroupImage")
                //make to bottom sheet
            }
        }




        return root
    }

   private val ImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){ uri: Uri? ->
       uri?.let { binding.GroupImageAdd.setImageURI(uri)
       ImageUri = uri
       }
    }

   // private val ImagePicker = registerForActivityResult(ActivityResultContracts.GetContent())

    override fun onCreate(savedInstanceState: Bundle?) {
        app = requireActivity().application as MainApp
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
//        val behavior = BottomSheetBehavior.from(requireView().parent as View)
//        behavior.peekHeight = 700
//        behavior.maxHeight = 700

    }


}