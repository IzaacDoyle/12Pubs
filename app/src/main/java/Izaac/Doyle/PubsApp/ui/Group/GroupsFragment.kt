package Izaac.Doyle.PubsApp.ui.Group

import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser
import Izaac.Doyle.PubsApp.Helpers.UserSearchRecyclerview
import Izaac.Doyle.PubsApp.Helpers.ViewPagerAdaptor
import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.databinding.FragmentGroupBinding
import Izaac.Doyle.PubsApp.ui.home.GroupViewModel
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.storage.FirebaseStorage
import com.nex3z.notificationbadge.NotificationBadge


class GroupsFragment : Fragment(), onDataPasser {

    private val groupViewModel: GroupViewModel by viewModels()
    private var _binding: FragmentGroupBinding? = null
    lateinit var app: MainApp
    lateinit var myAdapter:UserSearchRecyclerview
    var notificationBadge: NotificationBadge? = null



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

            groupViewModel.CheckInvitations(CheckCurrentUser()!!.uid)


            groupViewModel.gNames.observe(viewLifecycleOwner) { it ->
                Log.d("Observe group", "$it")
                if (it.isNotEmpty()) {

                   // binding.groupName.text = it[0].GroupName
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
                //get the numbers and get the Rules from db and push to recyclerview

            }




            setUpTabs()




            }else{

        }









            app = requireActivity().application as MainApp




//        groupViewModel.gNames.observe(viewLifecycleOwner) { it ->
//            binding.editTextTextPersonName.setText(it.toString())
//
//            //fix retun type
//        }





//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//        })

        return root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {









    }

    private fun setUpTabs() {

        val adaptor = ViewPagerAdaptor(childFragmentManager,lifecycle)
        binding.viewPager.adapter = adaptor

        TabLayoutMediator(binding.viewpagerTabL,binding.viewPager){tab, position->

            when(position){
                0->{
                    tab.text = "Pubs"
                }
                1->{
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
            menu.findItem(R.id.Create_group).isVisible = !it.isNotEmpty()
            menu.findItem(R.id.AddToGroup).isVisible = it.isNotEmpty()
        }



        groupViewModel.Invites.observe(viewLifecycleOwner){it->
            println(it)
            if (it.isNotEmpty()){
                val bell = menu.findItem(R.id.Group_join).actionView
                notificationBadge = bell.findViewById(R.id.badge) as NotificationBadge

                bell!!.setOnClickListener {
                    Toast.makeText(requireContext(), "Notification $it", Toast.LENGTH_SHORT).show()
                }
                notificationBadge!!.isVisible = true
                notificationBadge!!.setText("1")

            }
        }


        groupViewModel.UsersGroupname.observe(viewLifecycleOwner){it->



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
//            R.id.GroupAddSearch ->{
//
//
//
//                true
//            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun changeBottomSheet(sheetActive: String) {

    }

    override fun AccountStatus(info: String, email: String) {
    }

    override fun PassView(view: Activity) {
        println(view.toString())


    }
}




