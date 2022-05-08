package Izaac.Doyle.PubsApp.ui.Group

import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser
import Izaac.Doyle.PubsApp.Firebase.FirebaseLoggedIn
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
import Izaac.Doyle.PubsApp.ui.Maps.MapsViewModel
import Izaac.Doyle.PubsApp.ui.Settings.settings_update_info

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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.storage.FirebaseStorage
import com.nex3z.notificationbadge.NotificationBadge
import java.lang.Exception


private var mAboutDataListener: OnAboutDataReceivedListener? = null

interface OnAboutDataReceivedListener {
    fun onDataReceived(model: MutableList<GooglePlacesModel>?)
}

fun setAboutDataListener(listener: OnAboutDataReceivedListener?) {
    mAboutDataListener = listener
}

class GroupsFragment : Fragment(), onDataPasser {

    private val groupViewModel: GroupViewModel by viewModels()
    private val firebaseloggedin : FirebaseLoggedIn by activityViewModels()
    private val mapsViewModel: MapsViewModel by activityViewModels()
    private var _binding: FragmentGroupBinding? = null
    lateinit var app: MainApp
    lateinit var myAdapter:UserSearchRecyclerview
    var notificationBadge: NotificationBadge? = null
<<<<<<< HEAD
    lateinit var groupdata: GroupModel
=======
     var groupdata: GroupModel? = null
>>>>>>> 30099089273251f14653cab44040b5f9b5c3b90e

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




<<<<<<< HEAD
=======




>>>>>>> 30099089273251f14653cab44040b5f9b5c3b90e
            groupViewModel.Places.observe(viewLifecycleOwner){places->
                if (places !=null) {
                    if (places.isNotEmpty()) {
//                        mAboutDataListener!!.onDataReceived(places)
                        binding.GroupAddPub.setOnClickListener {
                            val groupPubAdd = ViewSavedLocations()
                            groupPubAdd.arguments = bundleOf("Add" to "Add",
                                "Places" to places,
<<<<<<< HEAD
                                "GroupOwner" to groupdata.OwnerUUID
=======
                                "GroupOwner" to groupdata!!.OwnerUUID
>>>>>>> 30099089273251f14653cab44040b5f9b5c3b90e
                                )
                            groupPubAdd.show(childFragmentManager,"SavedLocations")
                        }
                    }
                }
            }

            mapsViewModel.observableGooglePlacesPub.observe(viewLifecycleOwner, Observer { pubs ->
                binding.GroupRemovePub.setOnClickListener {
                    println("Remove")
                    //remove from Pubs Group List
                    val groupPubAdd = ViewSavedLocations()
                    groupPubAdd.arguments = bundleOf(
                        "Remove" to "Remove",
                        "Places" to pubs as MutableList<GooglePlacesModel>,
<<<<<<< HEAD
                        "GroupOwner" to groupdata.OwnerUUID
=======
                        "GroupOwner" to groupdata!!.OwnerUUID
>>>>>>> 30099089273251f14653cab44040b5f9b5c3b90e
                    )
                    groupPubAdd.show(childFragmentManager, "SavedLocations")
                }
//                mAboutDataListener!!.onDataReceived(pubs as MutableList<GooglePlacesModel>?)
            })



<<<<<<< HEAD


//
=======
            firebaseloggedin.AccountObservable.observe(viewLifecycleOwner) { account ->
                if (account.isNotEmpty()) {
                    if (account[0].Group.isNullOrEmpty()) {
                        binding.groupGroupName.text = ""
                        binding.GroupImage.setImageResource(R.drawable.ic_group)










                    }
                }
            }
>>>>>>> 30099089273251f14653cab44040b5f9b5c3b90e





            groupViewModel.gNames.observe(viewLifecycleOwner) { it ->
                Log.d("Observe group", "$it")
                if (it.isNotEmpty()) {
                    groupViewModel.Rules(it[0].RuleNumbers)
                    groupViewModel.CheckGroupAdmin(it[0].OwnerUUID)
                    groupViewModel.getGroupPlaces(it[0].OwnerUUID)

                   // binding.groupName.text = it[0].GroupName
                       groupdata = it[0]
                       Log.d("Group",it.toString())

                    if (it != null) {
                        if (binding.GroupImage.isAttachedToWindow) {
                            try {
                                val firebaseImageReg =
                                    FirebaseStorage.getInstance().reference.child("${it[0].OwnerUUID}/GroupImage.jpg")
                                try {
                                    firebaseImageReg.downloadUrl.addOnSuccessListener { Uri ->
                                        val imageURL = Uri.toString()
                                        Glide.with(this).load(imageURL).into(binding.GroupImage)
                                    }.addOnFailureListener { error ->
                                        Log.d("ImageGlide", error.message.toString())
<<<<<<< HEAD
                                        binding.GroupImage.setImageResource(R.drawable.ic_group)
=======
//                                        binding.GroupImage.setImageResource(R.drawable.ic_group)
>>>>>>> 30099089273251f14653cab44040b5f9b5c3b90e
                                        // Toast.makeText(requireContext(), "Error Loading Image, Get Group Admin to Re-Upload Image", Toast.LENGTH_SHORT).show()
                                    }
                                }catch (e:Exception){

                                }

                                Log.d("GroupImage", "$firebaseImageReg  $it")
                            }catch (e:Exception){
                                Log.d("GroupImage", "Was Not attached Yet ")
                            }



                        }
                        mAboutDataListener!!.onDataReceived(null)
                        binding.groupGroupName.text = it[0].GroupName
                    }
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




        groupViewModel.gNames.observe(viewLifecycleOwner) { result ->
<<<<<<< HEAD

            val bell = menu.findItem(R.id.Group_join)
            val bellAction = bell.actionView
=======
            Log.d("Gnames",result.toString())


>>>>>>> 30099089273251f14653cab44040b5f9b5c3b90e


            menu.findItem(R.id.group_update).setOnMenuItemClickListener {

                val update = settings_update_info()
                update.arguments = bundleOf("GroupUpdate" to "GroupUpdate",
                    "GroupUUID" to result[0].OwnerUUID,
                    "GroupName" to result[0].GroupName
                    )
                update.show(childFragmentManager,"Group Update")


                true
            }


<<<<<<< HEAD
            bellAction.setOnClickListener {
                groupViewModel.CheckInvitations(CheckCurrentUser()!!.uid)
            }
=======

>>>>>>> 30099089273251f14653cab44040b5f9b5c3b90e

            if (!result.isNullOrEmpty()) {
                menu.findItem(R.id.Create_group).isVisible = !result.isNotEmpty()
                menu.findItem(R.id.AddToGroup).isVisible = result.isNotEmpty()
                menu.findItem(R.id.Leave_Group).isVisible =result.isNotEmpty()
                menu.findItem(R.id.group_update).isVisible = result.isNotEmpty()
<<<<<<< HEAD
                bell.isVisible = result.isEmpty()
                bellAction.isVisible = result.isEmpty()
            }
            if (result.isNullOrEmpty()){
                println(result)

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
//
//                            if (bottomjoin.isAdded){
//                            if (bottomjoin.requireActivity().isDestroyed){
//                                requireActivity().recreate()
//                            }}
                        }
                        notificationBadge!!.isVisible = true
                        notificationBadge!!.setText("1")
                    }
                }
=======

            }
            if (result.isNullOrEmpty()){

>>>>>>> 30099089273251f14653cab44040b5f9b5c3b90e
            }




        }


<<<<<<< HEAD
        groupViewModel.UsersGroupname.observe(viewLifecycleOwner){it->
//            println("Test Group "+ it[0].GroupUUID)




=======
        firebaseloggedin.AccountObservable.observe(viewLifecycleOwner) { account ->
            val bell = menu.findItem(R.id.Group_join)
            val bellAction = bell.actionView
            if (account.isNotEmpty()) {
                if (!account.isNullOrEmpty()){
                    bell.isVisible = account.isEmpty()
                    bellAction.isVisible = account.isEmpty()
                }
                if (account[0].Group.isNullOrEmpty()) {

                    bellAction.setOnClickListener {
                        groupViewModel.CheckInvitations(CheckCurrentUser()!!.uid)
                    }

                    println(account)

                    groupViewModel.Invites.observe(viewLifecycleOwner){result->
                        println(result)
                        if (!result.isNullOrEmpty()){
                            bell.isVisible =true
                            println("Bell $result")
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
>>>>>>> 30099089273251f14653cab44040b5f9b5c3b90e
        }



<<<<<<< HEAD
=======


>>>>>>> 30099089273251f14653cab44040b5f9b5c3b90e
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
//                    requireActivity().recreate()
                    dialog.dismiss()
<<<<<<< HEAD
                    app.group.LeaveGroup(groupdata,requireActivity(),groupViewModel)
=======
                    firebaseloggedin.getAccount(CheckCurrentUser()!!.uid)

                    app.group.LeaveGroup(groupdata!!,requireActivity(),groupViewModel)
                    groupdata = null
>>>>>>> 30099089273251f14653cab44040b5f9b5c3b90e
                }

                dialog.show()

                true
            }


            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onResume() {
        super.onResume()
<<<<<<< HEAD

        if (CheckCurrentUser() != null){
            firebaseloggedin.getAccount(CheckCurrentUser()!!.uid)
            firebaseloggedin.AccountObservable.observe(viewLifecycleOwner){profile->
                if (!profile.isEmpty()){
                    Log.d("MapsData",profile.toString())
                    if (profile[0].Group!!.isNotBlank()){
                        groupViewModel.getUserGroup(profile[0].Group.toString())
                    }
                    mapsViewModel.load(profile[0].Group.toString())

=======
        groupViewModel.CheckInvitations(CheckCurrentUser()!!.uid)
        groupViewModel.QrCodeScanSearch(CheckCurrentUser()!!.uid)
        groupViewModel.getUsersPubsList()

        if (CheckCurrentUser() != null){
            firebaseloggedin.getAccount(CheckCurrentUser()!!.uid)
            firebaseloggedin.AccountObservable.observe(viewLifecycleOwner) { profile ->
                if (profile.isNotEmpty()) {

                    if (!profile.isEmpty()) {
                        Log.d("MapsData", profile.toString())
                        if (profile[0].Group!!.isNotBlank()) {
                            groupViewModel.getUserGroup(profile[0].Group.toString())
                        }
                        mapsViewModel.load(profile[0].Group.toString())

                    }
>>>>>>> 30099089273251f14653cab44040b5f9b5c3b90e
                }
            }

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




