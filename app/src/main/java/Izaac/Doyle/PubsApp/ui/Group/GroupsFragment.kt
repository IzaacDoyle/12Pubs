package Izaac.Doyle.PubsApp.ui.Group

import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser
import Izaac.Doyle.PubsApp.Helpers.UserSearchRecyclerview
import Izaac.Doyle.PubsApp.Helpers.ViewPagerAdaptor
import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.Models.GooglePlacesModel
import Izaac.Doyle.PubsApp.Models.GroupModel
import Izaac.Doyle.PubsApp.Models.RulesModel
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.databinding.FragmentGroupBinding
import Izaac.Doyle.PubsApp.ui.BottomSheet.ViewSavedLocations

import Izaac.Doyle.PubsApp.ui.home.GroupViewModel
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.storage.FirebaseStorage
import com.nex3z.notificationbadge.NotificationBadge


private var mAboutDataListener: OnAboutDataReceivedListener? = null

interface OnAboutDataReceivedListener {
    fun onDataReceived(model: MutableList<GooglePlacesModel>?)
}

fun setAboutDataListener(listener: OnAboutDataReceivedListener?) {
    mAboutDataListener = listener
}

class GroupsFragment : Fragment(), onDataPasser {

    private val groupViewModel: GroupViewModel by viewModels()
    private var _binding: FragmentGroupBinding? = null
    lateinit var app: MainApp
    lateinit var myAdapter:UserSearchRecyclerview
    var notificationBadge: NotificationBadge? = null
    lateinit var groupdata: GroupModel

    lateinit var dataPasser : onDataPasser
      var RulesList: MutableList<RulesModel>? = null





    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (CheckCurrentUser() !=null){
//            groupViewModel =
//                ViewModelProvider(this)[GroupViewModel::class.java]

            // check if user has invitations on run when the screen is drawn and now always checking. Save app load wont check always but Firebase Functions could help with this
            groupViewModel.CheckInvitations(CheckCurrentUser()!!.uid)
            groupViewModel.QrCodeScanSearch(CheckCurrentUser()!!.uid)
            groupViewModel.getUsersPubsList()



            groupViewModel.qrcodeSearch.observe(viewLifecycleOwner){result->
                if (result != null) {
                    if (result.isNotEmpty()) {

                        if (!result[0].Group.isNullOrBlank()) {
                            println(result[0].Group.toString())
                        groupViewModel.getUserGroup(result[0].Group.toString())
                            println("Test " + result.toString())
                            Log.d("GroupUUID From User", result[0].Group.toString())
                        }
                    }
                }
            }

            groupViewModel.Places.observe(viewLifecycleOwner){places->
                if (places !=null) {
                    if (places.isNotEmpty()) {
//                        mAboutDataListener!!.onDataReceived(places)



                        binding.GroupAddPub.setOnClickListener {
                            val groupPubAdd = ViewSavedLocations()
                            groupPubAdd.arguments = bundleOf("Add" to "Add",
                            "Places" to places,
                                "GroupOwner" to groupdata.OwnerUUID
                                )
                            groupPubAdd.show(childFragmentManager,"SavedLocations")
                        }
                    }
                }
            }


            groupViewModel.groupPlaces.observe(viewLifecycleOwner) { places ->

                if (places != null) {
                    if (places.isNotEmpty()) {
                        mAboutDataListener!!.onDataReceived(places)

                        binding.GroupRemovePub.setOnClickListener {
                            //remove from Pubs Group List
                            val groupPubAdd = ViewSavedLocations()
                            groupPubAdd.arguments = bundleOf(
                                "Remove" to "Remove",
                                "Places" to places,
                                "GroupOwner" to groupdata.OwnerUUID

                            )
                            groupPubAdd.show(childFragmentManager, "SavedLocations")
                        }

                    }
                }
            }



//





            groupViewModel.gNames.observe(viewLifecycleOwner) { it ->
                Log.d("Observe group", "$it")
                if (it.isNotEmpty()) {
                    groupViewModel.Rules(it[0].RuleNumbers)
                    groupViewModel.CheckGroupAdmin(it[0].OwnerUUID)
                    groupViewModel.getGroupPlaces(it[0].OwnerUUID)

                   // binding.groupName.text = it[0].GroupName
                       groupdata = it[0]
                       Log.d("Group",it.toString())
                    val firebaseImageReg = FirebaseStorage.getInstance().reference.child("${it[0].OwnerUUID}/GroupImage.jpg")
                    firebaseImageReg.downloadUrl.addOnSuccessListener { Uri ->
                        val imageURL = Uri.toString()
                        Glide.with(this).load(imageURL).into(binding.GroupImage)
                    }.addOnFailureListener { error->
                        Log.d("ImageGlide",error.message.toString() )
                        binding.GroupImage.setImageResource(R.drawable.ic_group)
                       // Toast.makeText(requireContext(), "Error Loading Image, Get Group Admin to Re-Upload Image", Toast.LENGTH_SHORT).show()
                    }
                    Log.d("GroupImage", "$firebaseImageReg  $it")

                    binding.groupGroupName.text = it[0].GroupName

                }else{
                    binding.GroupImage.setImageResource(R.drawable.ic_group)
                }
                groupViewModel.Invites.observe(viewLifecycleOwner) { result ->

                    println("Invite $result")
                    if (!result.isNullOrEmpty()){
                        setHasOptionsMenu(true)
                    }
                }
                //get the numbers and get the Rules from db and push to recyclerview

            }

            groupViewModel.GUsers.observe(viewLifecycleOwner){users->
                if (users.isNotEmpty()){
                    Log.d("UsersGroup","Is Not Empty $users")
                    binding.GroupAddPub.isVisible = true
                    binding.GroupRemovePub.isVisible = true
                }
            }





            setUpTabs()




            }else{

        }


            app = requireActivity().application as MainApp



        return root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {


    }

    private fun setUpTabs() {

            val adaptor = ViewPagerAdaptor(childFragmentManager, lifecycle)
            binding.viewPager.adapter = adaptor


        groupViewModel.groupRule.observe(viewLifecycleOwner) { resultRules ->
            println("ResultRules = $resultRules")
            if (resultRules.isNotEmpty()){
                println("Rules From Group = $resultRules")
                adaptor.AddRules(resultRules)
            }
        }
            TabLayoutMediator(binding.viewpagerTabL, binding.viewPager) { tab, position ->

                when (position) {
                    0 -> {
                        tab.text = "Pubs"
                    }
                    1 -> {
                        tab.text = "Rules"


                    }
                }


            }.attach()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }






    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_to_group,menu)




        groupViewModel.gNames.observe(viewLifecycleOwner) { it ->

            val bell = menu.findItem(R.id.Group_join)
            val bellAction = bell.actionView


            bellAction.setOnClickListener {
                groupViewModel.CheckInvitations(CheckCurrentUser()!!.uid)
            }

            if (!it.isNullOrEmpty()) {
                menu.findItem(R.id.Create_group).isVisible = !it.isNotEmpty()
                menu.findItem(R.id.AddToGroup).isVisible = it.isNotEmpty()
                menu.findItem(R.id.Leave_Group).isVisible =it.isNotEmpty()
                bell.isVisible = it.isEmpty()
                bellAction.isVisible = it.isEmpty()
            }
            if (it.isNullOrEmpty()){
                println(it)

                groupViewModel.Invites.observe(viewLifecycleOwner){result->
                    println(result)
                    if (!result.isNullOrEmpty()){
                        bell.isVisible = true
                        notificationBadge = bellAction.findViewById(R.id.badge) as NotificationBadge
                        bellAction!!.setOnClickListener {

//                            Toast.makeText(requireContext(), "Notification USer Group UUID  ${result[0].GroupUUID}", Toast.LENGTH_SHORT).show()
                            val bottomjoin = BottomJoinGroupFragment()
                            bottomjoin.arguments = bundleOf("GroupUUID" to result[0].GroupUUID)
                            bottomjoin.show(childFragmentManager,"JoinGroup")
                        }
                        notificationBadge!!.isVisible = true
                        notificationBadge!!.setText("1")
                    }
                }
            }




        }


        groupViewModel.UsersGroupname.observe(viewLifecycleOwner){it->
//            println("Test Group "+ it[0].GroupUUID)




        }



        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.Create_group ->{
                val bottomFragmentGroupCreate = BottomFragmentGroupCreate()
                bottomFragmentGroupCreate.show(childFragmentManager,"Create Group")

                true
            }
            R.id.AddToGroup->{
                val bottomFragmentJoinAddBinding = BottomJoinAddGroupFragment()
                bottomFragmentJoinAddBinding.show(childFragmentManager,"JoinAdd Group")

                true
            }
            R.id.Leave_Group->{

                val view = View.inflate(context, R.layout.group_leave_group_confirm,null)
                val builder = AlertDialog.Builder(context)
                builder.setView(view)
                builder.setMessage("Confirm if you would like to leave this group")
                val dialog = builder.create()

                view.findViewById<Button>(R.id.group_leave_cancel).setOnClickListener {
                    dialog.dismiss()
                }
                view.findViewById<Button>(R.id.group_leave_leave).setOnClickListener {
//                    groupViewModel.UsersGroupname.value!!.clear()
//                    groupViewModel.gNames.value!!.clear()
//                    groupViewModel.groupRule.value!!.clear()
//                    groupViewModel.GroupNames.value!!.clear()
//                    groupViewModel.Rules.value!!.clear()
//                    groupViewModel.Update()

                    dialog.dismiss()
                    app.group.LeaveGroup(groupdata,requireActivity(),groupViewModel)
                }

                dialog.show()

                true
            }


            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        dataPasser = context as onDataPasser
        super.onAttach(context)
    }

    override fun changeBottomSheet(sheetActive: String) {

    }

    override fun AccountStatus(info: String, email: String) {
    }

    override fun PassView(view: Boolean) {
        println(view.toString())


    }

    override fun Rules(Rules: MutableList<RulesModel>) {

    }




}




