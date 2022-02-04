package Izaac.Doyle.PubsApp.ui.Settings

import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.activities.MainActivity
import Izaac.Doyle.PubsApp.databinding.FragmentHomeBinding
import Izaac.Doyle.PubsApp.databinding.SettingsActivityBinding
import Izaac.Doyle.PubsApp.ui.home.HomeFragment
import Izaac.Doyle.PubsApp.ui.home.HomeViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

class SettingsFragment: Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: SettingsActivityBinding? = null
    lateinit var app: MainApp

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProvider(this)[SettingsViewModel::class.java]

        _binding = SettingsActivityBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        binding.navviewMenuitems.menu[2].subMenu[0].setOnMenuItemClickListener {
//            when(it.isCheckable){
//                true->{
//                    app.account.SignOut(requireActivity())
//                    true
//                }
//                false->{
//
//                    true
//                }
//            }
//        }

        binding.navviewMenuitems.menu[2].subMenu[0].setOnMenuItemClickListener {
            when (it.itemId){
                R.id.settings_signout ->{
                    Log.d("account","Signout")
                    app.account.SignOut(requireActivity())
                    val homefragment = HomeFragment()
                    //add fragment trasaction to get out of settings
                    val fragmentTrans = childFragmentManager.beginTransaction()
                    fragmentTrans.replace(R.id.fragment_container_view_tag,homefragment)
                    fragmentTrans.addToBackStack(null)
                    homefragment.arguments = bundleOf("SignOut" to "SignedOut")
                    fragmentTrans.commit()

                    true
                }
                else -> {
                    true
                }
            }
        }



        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = requireActivity().application as MainApp
    }




//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId) {
//            R.id.settings_signout -> {
//                Log.d("settingsActivity", "Log out")
//                //   app.account.SignOut(this)
//            }
//        }
//
//
//        return super.onOptionsItemSelected(item)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}