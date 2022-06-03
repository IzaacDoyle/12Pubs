package Izaac.Doyle.PubsApp.ui.home

import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser
import Izaac.Doyle.PubsApp.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import Izaac.Doyle.PubsApp.databinding.FragmentHomeBinding
import android.content.ContentValues.TAG
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.util.LogTime

class HomeFragment : Fragment() {

    private val homeViewModel : HomeViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
//        homeViewModel =
//            ViewModelProvider(this)[HomeViewModel::class.java]
        setHasOptionsMenu(true)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        if (CheckCurrentUser() != null){
            homeViewModel.load(CheckCurrentUser()!!.uid)
        }


        homeViewModel.observableAccountData.observe(viewLifecycleOwner, Observer { account ->
            Log.d("Account",account.toString())

//            Toast.makeText(requireContext(), "$account", Toast.LENGTH_SHORT).show()

        })






        if (arguments != null){
            if (requireArguments().containsKey("Signout")){
                Toast.makeText(requireContext(),"Signed OUt", Toast.LENGTH_SHORT).show()
            }
        }



        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.maps_toggle,menu)

        menu.findItem(R.id.toggle_group).isVisible = false
    }







    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}