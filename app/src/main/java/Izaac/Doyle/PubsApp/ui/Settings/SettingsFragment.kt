package Izaac.Doyle.PubsApp.ui.Settings

import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.databinding.SettingsActivityBinding
import Izaac.Doyle.PubsApp.ui.BottomSheet.BottomFragmentDelete
import Izaac.Doyle.PubsApp.ui.BottomSheet.BottomFragmentLogin
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation

class SettingsFragment: Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: SettingsActivityBinding? = null
    lateinit var app: MainApp
    lateinit var dataPasser : onDataPasser



    // var reAuth: Boolean = false


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
       // val dialog = Dialog(requireContext())



        binding.navviewMenuitems.menu[2].subMenu[1].setOnMenuItemClickListener {

            //SignOut
            when (it.itemId){
                R.id.settings_signout ->{
                    Log.d("account","Signout")
                    app.account.SignOut(requireActivity())
                    //val homefragment = HomeFragment()
                    //add fragment trasaction to get out of settings
                    val navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main)
                    navController.navigate(R.id.nav_home)
                    true
                }
                else -> {
                    true
                }
            }
        }

        binding.navviewMenuitems.menu[2].subMenu[2].setOnMenuItemClickListener {
            //Delete Account
            when (it.itemId) {
                R.id.settings_delete_account -> {

                   val bottomFragment = BottomFragmentLogin()
                    val bottomDelete = BottomFragmentDelete()

                    bottomDelete.show(childFragmentManager,"Delete account")



                    true
                }
                else -> {true }
            }
        }





        binding.navviewMenuitems.menu[2].subMenu[0].setOnMenuItemClickListener {
            val settings = settings_update_info()
            when(it.itemId){
                R.id.settings_update_account->{

                    if (!settings.isAdded) {
                        settings.arguments = bundleOf("Delete" to "Delete")
                        settings.show(childFragmentManager, "Bottom Create")
                        settings.isHidden
                    }
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as onDataPasser
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

    fun dialogShow() {

        }


}
