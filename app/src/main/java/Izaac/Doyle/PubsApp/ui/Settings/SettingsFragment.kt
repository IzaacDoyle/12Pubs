package Izaac.Doyle.PubsApp.ui.Settings

import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.databinding.SettingsActivityBinding
import Izaac.Doyle.PubsApp.ui.BottomSheet.BottomFragmentDelete
import Izaac.Doyle.PubsApp.ui.BottomSheet.BottomFragmentLogin
import Izaac.Doyle.PubsApp.ui.BottomSheet.settings_update_info
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
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



//                    bottomFragment.arguments = bundleOf("relogin" to "deleteLogin")
//                    bottomFragment.show(childFragmentManager, "Bottom Login")



                   // dataPasser.dialogBoxShare(dialog)
//                    var dialog:Dialog = Dialog(requireContext())
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//                    dialog.setCancelable(false)
//                    dialog.setContentView(R.layout.custom_delete_dialogbox)
//                    val textbox = dialog.findViewById<EditText>(R.id.dialog_password_box)
//                    //textbox.hint = CheckCurrentUser()!!.email
//                    val cancelBtn = dialog.findViewById<Button>(R.id.dialog_cancel)
//                    val confirmbtn = dialog.findViewById<Button>(R.id.dialog_confirm)
//
//                    //Need to login again
//                    cancelBtn.setOnClickListener {
//                        dialog.dismiss()
//                    }
//                    confirmbtn.setOnClickListener {
//                        textbox.clearFocus()
//                        if (!textbox.text.toString().isEmpty()){
//                            textbox.error = null
//                            val navController = Navigation.findNavController(requireActivity(),
//                                R.id.nav_host_fragment_content_main)
//                            navController.navigate(R.id.nav_home)
//                            app.account.ReAuth(CheckCurrentUser()!!.email.toString(),textbox.toString(),"Delete",requireActivity())
////                            app.account.AccountDelete(requireActivity(),dialog)
//                            dialog.dismiss()
//                        }else{
//                            textbox.restoreDefaultFocus()
//                            textbox.error
//                            // textbox.error = "Email Does not match"
//                        }
//                    }

                    //start up the login again to verify, and then after display dialog box to confirm


                    true
                }
                else -> {true }
            }
        }












//        Log.d("reAuthSettings","${MainActivity().reAuth}")
//        when(MainActivity().reAuth){
//            "true"->{
//                Log.d("reAuthSettings","${MainActivity().reAuth}")
//                //  dialog.show()
//            }
//        }

        binding.navviewMenuitems.menu[2].subMenu[0].setOnMenuItemClickListener {
            val settings = settings_update_info()
            when(it.itemId){
                R.id.settings_update_account->{

                    if (!settings.isAdded) {
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

    public fun dialogShow() {

        }


}
