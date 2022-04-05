package Izaac.Doyle.PubsApp.ui.home

import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser
import Izaac.Doyle.PubsApp.Helpers.UserSearchRecyclerview
import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import Izaac.Doyle.PubsApp.databinding.FragmentGroupBinding
import Izaac.Doyle.PubsApp.ui.BottomSheet.BottomFragmentGroupCreate
import android.app.SearchManager
import android.database.MatrixCursor
import android.provider.BaseColumns
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.datatransport.runtime.scheduling.persistence.SQLiteEventStore_Factory
import com.google.firebase.storage.FirebaseStorage
import java.io.Serializable
import kotlin.math.log

class GroupsFragment : Fragment() {

    private lateinit var groupViewModel: GroupViewModel
    private var _binding: FragmentGroupBinding? = null
    lateinit var app: MainApp
    lateinit var myAdapter:UserSearchRecyclerview

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
            groupViewModel =
                ViewModelProvider(this)[GroupViewModel::class.java]




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

                }else{
                    binding.GroupImage.setImageResource(R.drawable.ic_group)
                }
            }







            binding.groupAddSearch.setOnQueryTextListener(object :SearchView.OnQueryTextListener{

                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (binding.groupAddSearch.isEmpty()) {

                    }else if (query.isNullOrEmpty()){

                    }else{
                        Log.d("Search","$query")
                        groupViewModel.SearchAddusersToGroup(query.lowercase())

                    }
                    return true
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    if (binding.groupAddSearch.isEmpty()){

                    }else if (query.isNullOrBlank()){

                    }else{
                        Log.d("Search","$query")
                        groupViewModel.SearchAddusersToGroup(query.lowercase())

                    }
                    return true
                }
            })

            groupViewModel.UsersGroupname.observe(viewLifecycleOwner){it->

                Log.d("UserGroups", it.toString())


                myAdapter = UserSearchRecyclerview(it)
                binding.useraddRecyclerview.layoutManager = LinearLayoutManager(requireContext())
                binding.useraddRecyclerview.adapter = myAdapter


            }









        }







            app = requireActivity().application as MainApp




//        groupViewModel.gNames.observe(viewLifecycleOwner) { it ->
//            binding.editTextTextPersonName.setText(it.toString())
//
//            //fix retun type
//        }


//        groupViewModel.groupName.observe(viewLifecycleOwner, Observer {
//            binding.groupName.text = it.toString()
//        })


//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//        })

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_to_group,menu)

        groupViewModel.gNames.observe(viewLifecycleOwner) { it ->
            if (it.isNotEmpty()){
                menu.findItem(R.id.Create_group).isVisible = false
            }
        }

        val search = menu.findItem(R.id.GroupAddSearch)
        val searchView = search.actionView as SearchView

        searchView.queryHint = "Search Users"

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{


            override fun onQueryTextSubmit(query: String?): Boolean {
                if (binding.groupAddSearch.isEmpty()) {
                    groupViewModel.SearchAddusersToGroup(null)
                    myAdapter.notifyDataSetChanged()
                }else if (query.isNullOrEmpty()){
                    groupViewModel.SearchAddusersToGroup(null)
                    myAdapter.notifyDataSetChanged()
                }else{
                    Log.d("Search","$query")
                    groupViewModel.SearchAddusersToGroup(query.lowercase())

                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                Log.d("SearchView",query.toString())
                if (binding.groupAddSearch.isEmpty()){
                   myAdapter.notifyDataSetChanged()
                    groupViewModel.SearchAddusersToGroup(null)

                }else if (query.isNullOrBlank()){
                   myAdapter.notifyDataSetChanged()
                    groupViewModel.SearchAddusersToGroup(null)
                }else{
                    Log.d("Search","$query")
                    groupViewModel.SearchAddusersToGroup(query.lowercase())

                }
                return true
            }
        })

        searchView.findViewById<AutoCompleteTextView>(R.id.search_src_text)



        groupViewModel.UsersGroupname.observe(viewLifecycleOwner){it->

//            searchView.suggestionsAdapter = SimpleCursorAdapter(requireContext(),R.layout.support_simple_spinner_dropdown_item,null,
//                arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1),
//                intArrayOf(R.id.name),CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)


            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d("searchView",query.toString())
                 return true
                }


                override fun onQueryTextChange(query: String?): Boolean {
                    Log.d("searchView",query.toString())
                  return true
                }

            })

           // searchView.setSearchableInfo(it)



//            Log.d("UserGroups", it.toString())


//            myAdapter = UserSearchRecyclerview(it)
//            binding.useraddRecyclerview.layoutManager = LinearLayoutManager(requireContext())
//            binding.useraddRecyclerview.adapter = myAdapter


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

            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


