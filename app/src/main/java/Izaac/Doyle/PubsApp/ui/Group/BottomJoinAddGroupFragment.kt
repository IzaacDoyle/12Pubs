package Izaac.Doyle.PubsApp.ui.Group

import Izaac.Doyle.PubsApp.Helpers.UserSearchRecyclerview
import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.Models.FBAccountNameModel
import Izaac.Doyle.PubsApp.databinding.CameraViewBinding
import Izaac.Doyle.PubsApp.databinding.FragmentJoinAddBinding
import Izaac.Doyle.PubsApp.ui.home.GroupViewModel
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isEmpty
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomJoinAddGroupFragment:BottomSheetDialogFragment() {



    private var _binding: FragmentJoinAddBinding? = null
    private var camera_binding: CameraViewBinding? = null
    lateinit var dataPasser : onDataPasser
    lateinit var myAdapter:UserSearchRecyclerview

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
            myAdapter = UserSearchRecyclerview(it as ArrayList<FBAccountNameModel>)
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
            myAdapter = UserSearchRecyclerview(it as ArrayList<FBAccountNameModel>)
            binding.userSearchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.userSearchRecyclerView.adapter = myAdapter

        }



//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//
//
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                if (binding.groupAddSearch.isEmpty()) {
//                    groupViewModel.SearchAddusersToGroup(null)
//                    myAdapter.notifyDataSetChanged()
//                }else if (query.isNullOrEmpty()){
//                    groupViewModel.SearchAddusersToGroup(null)
//                    myAdapter.notifyDataSetChanged()
//                }else{
//                    Log.d("Search","$query")
//                    groupViewModel.SearchAddusersToGroup(query.lowercase())
//
//                }
//                return true
//            }
//
//            override fun onQueryTextChange(query: String?): Boolean {
//                Log.d("SearchView",query.toString())
//                if (binding.groupAddSearch.isEmpty()){
//                   myAdapter.notifyDataSetChanged()
//                    groupViewModel.SearchAddusersToGroup(null)
//
//                }else if (query.isNullOrBlank()){
//                   myAdapter.notifyDataSetChanged()
//                    groupViewModel.SearchAddusersToGroup(null)
//                }else{
//                    Log.d("Search","$query")
//                    groupViewModel.SearchAddusersToGroup(query.lowercase())
//
//                }
//                return true
//            }
//        })




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


}



