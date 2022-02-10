package Izaac.Doyle.PubsApp.ui.home

import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser
import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.Models.GroupModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.databinding.FragmentGroupBinding
import Izaac.Doyle.PubsApp.databinding.FragmentHomeBinding
import androidx.lifecycle.LiveData

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
        groupViewModel =
            ViewModelProvider(this)[GroupViewModel::class.java]

        _binding = FragmentGroupBinding.inflate(inflater, container, false)
        val root: View = binding.root

        app = requireActivity().application as MainApp

        binding.button3.setOnClickListener {
            if (!binding.editTextTextPersonName.text.isEmpty()){
                app.group.CreateGroup(GroupModel(CheckCurrentUser()!!.uid,0,binding.editTextTextPersonName.text.toString(),""))
            }

        }


        groupViewModel.groupname.observe(viewLifecycleOwner) { it ->
            binding.editTextTextPersonName.setText(it.toString())

            //fix retun type
        }

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