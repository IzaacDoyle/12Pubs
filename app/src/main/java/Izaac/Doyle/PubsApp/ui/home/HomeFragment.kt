package Izaac.Doyle.PubsApp.ui.home


import Izaac.Doyle.PubsApp.Firebase.AccountActivitysViewModel
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.activities.MainActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import Izaac.Doyle.PubsApp.databinding.FragmentHomeBinding
import android.content.ContentValues.TAG
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bumptech.glide.util.LogTime

class HomeFragment : Fragment() {

    private val homeViewModel : HomeViewModel by activityViewModels()
    private lateinit var loginViewmodel : AccountActivitysViewModel
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

        loginViewmodel = ViewModelProvider(this)[AccountActivitysViewModel::class.java]


        if (loginViewmodel.liveFirebaseUser.value != null){
            homeViewModel.load(loginViewmodel.liveFirebaseUser.value!!.uid)

            homeViewModel.observableAccountData.observe(viewLifecycleOwner, Observer { account ->
//            Log.d("AccountHome",account[0].UserEmail.toString())

//            Toast.makeText(requireContext(), "$account", Toast.LENGTH_SHORT).show()

            })
        }



        binding.homeMapLarger.setOnClickListener {
            requireView().findNavController().navigate(R.id.action_nav_home_to_nav_maps)
        }







        if (arguments != null){
            if (requireArguments().containsKey("Signout")){
                Toast.makeText(requireContext(),"Signed OUt", Toast.LENGTH_SHORT).show()
            }
        }



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()


        menuHost.addMenuProvider(object :MenuProvider{

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.maps_toggle,menu)
                menu.findItem(R.id.toggle_group).isVisible = false
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }

        })
    }
//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.maps_toggle,menu)
//
//
//    }







    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}