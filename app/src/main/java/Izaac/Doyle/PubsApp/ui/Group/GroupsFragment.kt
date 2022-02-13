package Izaac.Doyle.PubsApp.ui.home

import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser
import Izaac.Doyle.PubsApp.Helpers.UserSerachRecyclerview
import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.Models.GroupModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import Izaac.Doyle.PubsApp.databinding.FragmentGroupBinding
import android.util.Log
import android.widget.SearchView
import androidx.core.view.isEmpty
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query

class GroupsFragment : Fragment() {

    private lateinit var groupViewModel: GroupViewModel
    private var _binding: FragmentGroupBinding? = null
    lateinit var app: MainApp

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
                    binding.groupName.text = it[0].GroupName
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


                val myAdapter = UserSerachRecyclerview(it)
                binding.useraddRecyclerview.layoutManager = LinearLayoutManager(requireContext())
                binding.useraddRecyclerview.adapter = myAdapter




            }









        }







            app = requireActivity().application as MainApp

            binding.button3.setOnClickListener {
                if (!binding.editTextTextPersonName.text.isEmpty()) {
                    app.group.CreateGroup(
                        GroupModel(
                            CheckCurrentUser()!!.uid,
                            0,
                            binding.editTextTextPersonName.text.toString(),
                            ""
                        )
                    )
                    //make to bottom sheet
                }
            }


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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


