package Izaac.Doyle.PubsApp.ui.Group

import Izaac.Doyle.PubsApp.Firebase.AddUserToGroup
import Izaac.Doyle.PubsApp.Helpers.ProfileClickListener
import Izaac.Doyle.PubsApp.Helpers.UserSearchRecyclerview
import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.Models.FBAccountNameModel
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.databinding.CameraViewBinding
import Izaac.Doyle.PubsApp.databinding.FragmentJoinAddBinding
import Izaac.Doyle.PubsApp.ui.home.GroupViewModel
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isEmpty
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomJoinAddGroupFragment:BottomSheetDialogFragment(), ProfileClickListener {



    private var _binding: FragmentJoinAddBinding? = null
    private var camera_binding: CameraViewBinding? = null
    lateinit var dataPasser : onDataPasser
    lateinit var myAdapter:UserSearchRecyclerview
    var account:FBAccountNameModel? = null

    private var camera = false
    private lateinit var groupViewModel: GroupViewModel

    private val binding get() = _binding!!
    private val camerabinding get() = camera_binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentJoinAddBinding.inflate(inflater, container, false)
        val root: View = binding.root

        groupViewModel =
            ViewModelProvider(this)[GroupViewModel::class.java]


       binding.profileSearchview.isIconified = false

        root.requestFocus()




        if (this.arguments != null) {
            if (arguments?.containsKey("QRCode") == true) {
                Log.d("QRCode",this.requireArguments().get("QRCode").toString())
                Log.d("QRCodeReturn ",groupViewModel.UsersGroupname.value.toString() + " Test")
                groupViewModel.QrCodeScanSearch(this.requireArguments().get("QRCode").toString())
//                println(GroupViewModel().QrCodeScanSearch(this.requireArguments().get("QRCode").toString()))
//                val args = this.requireArguments().get("QRCode").toString()
//                groupViewModel.QrCodeScanSearch(args)

            }
            }

        groupViewModel.qrcodeSearch.observe(viewLifecycleOwner){it ->
            Log.d("QRCodeSearch Observed Data",it.toString())
            myAdapter = UserSearchRecyclerview(it as ArrayList<FBAccountNameModel>,this,requireContext())
            binding.userSearchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.userSearchRecyclerView.adapter = myAdapter

        }




//                    myAdapter = UserSearchRecyclerview(it)
//                    binding.userSearchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//                    binding.userSearchRecyclerView.adapter = myAdapter

//        groupViewModel.UsersGroupname.observe(viewLifecycleOwner) { it ->
//            //binding.searchView.queryHint = it[0].Username
//            // binding.searchView.setQuery(it[0].Username,true)
//            Log.d("SearchView QR Code",it.toString())
//
//
//
//            myAdapter = UserSearchRecyclerview(it)
//            binding.userSearchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//            binding.userSearchRecyclerView.adapter = myAdapter
//
//        }

//        groupViewModel.UsersGroupname.observe(requireActivity(), Observer {
//

//        })
        binding.button6add.setOnClickListener {
            if (account == null){
                Toast.makeText(requireContext(), "Please Select A User to Add", Toast.LENGTH_SHORT).show()
            }else{
                AddUserToGroup(account!!,
                    groupViewModel.gNames.value?.get(0)?.OwnerUUID.toString(),requireContext(),requireDialog(),
                    Extra = true,
                    IsAdmin = false
                    )

            }
        }






        binding.ScanButton.setOnClickListener {
            dataPasser.AccountStatus("Camera", "")
            dismiss()
        }

        binding.profileSearchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (binding.profileSearchview.isEmpty()) {

                }else if (query.isNullOrEmpty()){

                }else{
                    Log.d("Search","$query")
                    groupViewModel.SearchAddusersToGroup(query.lowercase())

                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (binding.profileSearchview.isEmpty()){

                }else if (query.isNullOrBlank()){

                }else{
                    Log.d("Search","$query")
                    groupViewModel.SearchAddusersToGroup(query.lowercase())

                }
                return true
            }
        })


        groupViewModel.UsersGroupname.observe(viewLifecycleOwner) { it ->
            Log.d("QR SearchUser",it.toString())

            myAdapter = UserSearchRecyclerview(it as ArrayList<FBAccountNameModel>,this,requireContext())
            binding.userSearchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.userSearchRecyclerView.adapter = myAdapter

        }







        return root
    }

    //
//
    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
        behavior.maxWidth = Resources.getSystem().displayMetrics.widthPixels
        behavior.isDraggable = false

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as onDataPasser
    }

    override fun onProfileClicked(accounts: FBAccountNameModel,itemView:View) {
        Log.d("AccountClicked",accounts.toString())


        if (!itemView.isSelected){
            itemView.isSelected = true
            itemView.setBackgroundColor(Color.BLACK)
            itemView.defaultFocusHighlightEnabled = true
            account = accounts

        }
        if (itemView.isSelected && account == accounts){

//            itemView.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.Background_color,null))

        }



    }


}



