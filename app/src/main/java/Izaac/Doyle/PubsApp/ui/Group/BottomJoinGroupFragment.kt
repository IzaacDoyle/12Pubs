package Izaac.Doyle.PubsApp.ui.Group



import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser
import Izaac.Doyle.PubsApp.Firebase.InviteResponce
import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.activities.MainActivity
import Izaac.Doyle.PubsApp.databinding.GroupJoiningGroupBinding
import Izaac.Doyle.PubsApp.ui.home.GroupViewModel
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.storage.FirebaseStorage

class BottomJoinGroupFragment:BottomSheetDialogFragment() {

    private var _binding: GroupJoiningGroupBinding? = null
    private val groupViewModel: GroupViewModel by viewModels()
    lateinit var dataPasser : onDataPasser

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = GroupJoiningGroupBinding.inflate(inflater, container, false)
        val root: View = binding.root

        println("Join Group")
        if (this.arguments != null) {
            if (arguments?.containsKey("GroupUUID") == true) {
            println("Test " + arguments?.getString("GroupUUID").toString())
                groupViewModel.getUserGroup(arguments?.getString("GroupUUID").toString())
                groupViewModel.QrCodeScanSearch(arguments?.getString("GroupUUID").toString())
                val firebaseImageReg = FirebaseStorage.getInstance().reference.child("${arguments?.getString("GroupUUID").toString()}/GroupImage.jpg")

                groupViewModel.gNames.observe(viewLifecycleOwner) { it ->
                    println(it)
                    if (it != null && it.isNotEmpty()) {
                        binding.joinGroupName.text = it[0].GroupName
                    }
                }
                groupViewModel.qrcodeSearch.observe(viewLifecycleOwner){result->
                    if (result !=null && result.isNotEmpty()){
                        binding.joinGroupOwner.text = result[0].Username
                    }
                }


                firebaseImageReg.downloadUrl.addOnSuccessListener { Uri ->
                    val imageURL = Uri.toString()
                    Glide.with(this).load(imageURL).into(binding.joinGroupImage)
                }.addOnFailureListener { error->
                    Log.d("ImageGlide",error.message.toString() )
                    binding.joinGroupImage.setImageResource(R.drawable.ic_group)
                    // Toast.makeText(requireContext(), "Error Loading Image, Get Group Admin to Re-Upload Image", Toast.LENGTH_SHORT).show()
                }

            }
            binding.joinJoinGroup.setOnClickListener {
                InviteResponce(arguments?.getString("GroupUUID").toString(),requireActivity().getSharedPreferences(
                    CheckCurrentUser()!!.uid, Context.MODE_PRIVATE).getString("Username", "")
                    .toString(), CheckCurrentUser()!!.uid,true)
                groupViewModel.getUserGroup(arguments?.getString("GroupUUID").toString())
                arguments?.clear()
                groupViewModel.Invitations.value!!.clear()
                groupViewModel.Invites.value!!.clear()
                dataPasser.PassView(true)
<<<<<<< HEAD
                groupViewModel.Update()
=======

>>>>>>> 30099089273251f14653cab44040b5f9b5c3b90e
//                requireActivity().recreate()
                dismiss()

            }

            binding.joinDeclineGroup.setOnClickListener {
                InviteResponce(arguments?.getString("GroupUUID").toString(),requireActivity().getSharedPreferences(
                    CheckCurrentUser()!!.uid, Context.MODE_PRIVATE).getString("Username", "")
                    .toString(), CheckCurrentUser()!!.uid,false)

                arguments?.clear()
                groupViewModel.Invitations.value!!.clear()
                groupViewModel.Invites.value!!.clear()
                dataPasser.PassView(true)

                dismiss()

            }


        }else{
            binding.joinJoinGroup.setOnClickListener {
                Toast.makeText(requireContext(), "Error Joining Group", Toast.LENGTH_SHORT).show()
                dismiss()
            }

            binding.joinDeclineGroup.setOnClickListener {
                Toast.makeText(requireContext(), "Error Declining Group", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }









        return  root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as onDataPasser
    }




}