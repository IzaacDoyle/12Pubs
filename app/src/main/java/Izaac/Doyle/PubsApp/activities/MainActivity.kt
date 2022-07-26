package Izaac.Doyle.PubsApp.activities


import Izaac.Doyle.PubsApp.Firebase.AccountActivitysViewModel
import Izaac.Doyle.PubsApp.Helpers.QrCodeDispay
import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.Helpers.setTheme
import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.Models.FBAccountModel
import Izaac.Doyle.PubsApp.Models.RulesModel
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.databinding.ActivityMainBinding
import Izaac.Doyle.PubsApp.databinding.NavHeaderMainBinding
import Izaac.Doyle.PubsApp.ui.BottomSheet.*
import Izaac.Doyle.PubsApp.ui.Group.BottomCameraFragment
import Izaac.Doyle.PubsApp.ui.Group.BottomFragmentGroupCreate
import Izaac.Doyle.PubsApp.ui.Group.BottomJoinAddGroupFragment
import Izaac.Doyle.PubsApp.ui.home.HomeViewModel
import android.app.AlertDialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class MainActivity : AppCompatActivity(), onDataPasser {


    lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navbinding : NavHeaderMainBinding
    private lateinit var auth: FirebaseAuth
    private val homeViewModel : HomeViewModel by viewModels()

    private lateinit var loggedViewModel: AccountActivitysViewModel

    lateinit var dataPasser : onDataPasser
    lateinit var app: MainApp


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setSupportActionBar(binding.appBarMain.toolbar)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        loggedViewModel = ViewModelProvider(this)[AccountActivitysViewModel::class.java]


        app = application as MainApp
        auth = Firebase.auth
        dataPasser = this

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_pubs, R.id.nav_maps, R.id.nav_settings, R.id.nav_group,R.id.nav_rules
            ), drawerLayout
        )

        binding.navView.menu[3].setOnMenuItemClickListener {
            val view = View.inflate(this, R.layout.daynightmode_toggle,null)
            val builder = AlertDialog.Builder(this)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
//            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val day = view.findViewById<CheckBox>(R.id.DayMode_toggle)
            val night = view.findViewById<CheckBox>(R.id.NightMode_toggle)
            val default = view.findViewById<CheckBox>(R.id.SystemDefaultMode_toggle)

            when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
                Configuration.UI_MODE_NIGHT_YES -> { night.isChecked = true}
                Configuration.UI_MODE_NIGHT_NO -> { day.isChecked = true}
                Configuration.UI_MODE_NIGHT_UNDEFINED -> {default.isChecked = true}
            }


            day.setOnCheckedChangeListener{button, b->
                if (b){
                    setTheme("day")
                    dialog.dismiss()
                }
            }
            night.setOnCheckedChangeListener{button, b->
                if (b){
                    setTheme("night")
                    dialog.dismiss()
                }
            }
            default.setOnCheckedChangeListener{button, b->
                if (b){
                    setTheme("default")
                    dialog.dismiss()
                }
            }
            true
        }

        if (FirebaseAuth.getInstance().currentUser == null) {
            binding.drawerLayout.findViewById<LinearLayout>(R.id.Drawer_Login_Create).isGone =
                false
            binding.navView.menu[2].isVisible = false
            binding.navView.menu[2].isEnabled = false
            binding.navView.menu[1].subMenu[1].isVisible = false
            Log.d("StartUpAccount","No Firebase User")
        } else {
            binding.drawerLayout.findViewById<LinearLayout>(R.id.Drawer_Login_Create).isGone = true
            binding.navView.menu[2].isVisible = true
            binding.navView.menu[2].isEnabled = true
            binding.navView.menu[1].subMenu[1].isVisible = true

            Log.d("StartUpAccount","has Firebase User")
            if (intent.hasExtra("account")) {
                val getString = intent.getStringExtra("account")
                Log.d("AccountStartUp",getString.toString())
                Toast.makeText(this, "$getString", Toast.LENGTH_SHORT).show()
            }






//            FBGetDB(CheckCurrentUser()!!.uid, this)

        }
        val loginBtn = navView.findViewById<LinearLayout>(R.id.Drawer_Login)
        val createAccountBtn = navView.findViewById<LinearLayout>(R.id.Drawer_CreateA)
        val QRView = navView.menu[2].subMenu[1]
            QRView.setOnMenuItemClickListener {
                QrCodeDispay(this,this,loggedViewModel)

                true
            }

        loginBtn.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            val bottomFragment = BottomFragmentLogin()
            bottomFragment.show(supportFragmentManager, "Bottom Login")
        }



        createAccountBtn.setOnClickListener {
            // change off and rebrand the Create account button with the drawer.
            drawerLayout.closeDrawer(GravityCompat.START)
            val bottomFragment = BottomFragmentCreate()
            bottomFragment.show(supportFragmentManager, "Bottom Login")

            // Toast.makeText(applicationContext, "Create Works", Toast.LENGTH_SHORT).show()
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.my_nav_host)
//        return navController.navigateUp(myAppBarConfiguration) || super.onSupportNavigateUp()
//    }






    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.settings_signout -> {
                Log.d("settingsActivity", "Log out")
//                app.account.SignOut(this)
                loggedViewModel.LogOut(this)
            }


        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {

        loggedViewModel.CheckCurrentUser()
        Log.d("GoogleAccount", "${loggedViewModel.liveFirebaseUser.value?.email}")

        homeViewModel.observableAccountData.observe(this, Observer {
            if (it != null && it.isNotEmpty()){
                Log.d("HomeAccount",it[0].Username.toString())
                UpDatNavHeader(it[0])
            }
        })

        super.onStart()


    }

    private fun UpDatNavHeader(currentUser: FBAccountModel){
        val headerView = binding.navView.getHeaderView(0)
        navbinding = NavHeaderMainBinding.bind(headerView)
        navbinding.DrawerName.text = currentUser.Username
        navbinding.DrawerEmail.text = currentUser.UserEmail

        try {
            val FBprofileImage =
                FirebaseStorage.getInstance().reference.child("${loggedViewModel.liveFirebaseUser.value!!.uid}/ProfileImage.jpg")
            FBprofileImage.downloadUrl.addOnSuccessListener { Uri ->
                val imageURL = Uri.toString()
                Glide.with(this).load(imageURL).into(navbinding.DrawerAccountImage)
            }.addOnFailureListener {
                navbinding.DrawerAccountImage.setImageResource(R.drawable.ic_pubs)
            }
        }catch (e:Exception){
            navbinding.DrawerAccountImage.setImageResource(R.drawable.ic_pubs)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        val drawerDropDown = findViewById<LinearLayout>(R.id.drawer_header_button)

        var buttonclicks = 0

        if (loggedViewModel.liveFirebaseUser.value == null) {
            drawerDropDown.isVisible = false
            drawerDropDown.isEnabled = false
        } else {

            drawerDropDown.setOnClickListener {

                val icon = findViewById<ImageView>(R.id.button_tabup)

                if (buttonclicks == 0) {
                    icon.setImageResource(R.drawable.ic_log_out_tabdown)
                    binding.navView.menu[0].isVisible = true

                    val signoutButton = binding.navView.menu[0]
                    signoutButton.setOnMenuItemClickListener {
                        Log.d("SignOut", "Signout Clicked")
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                        binding.navView.menu[0].isVisible = false
                        navController.navigate(R.id.nav_home)
//                        app.account.SignOut(this)
                        loggedViewModel.LogOut(this)
                        buttonclicks = 0
                        true
                    }
                    buttonclicks = 1
                } else if (buttonclicks == 1) {
                    icon.setImageResource(R.drawable.ic_log_out_tabup)
                    binding.navView.menu[0].isVisible = false
                    buttonclicks = 0
                }
            }
        }
        if (loggedViewModel.liveFirebaseUser.value != null) {
            drawerDropDown.isVisible = true
            drawerDropDown.isEnabled = true
            binding.drawerLayout.findViewById<LinearLayout>(R.id.Drawer_Login_Create).isGone =
                true
        }
        //an UI so that once user loged in Email and Name and Image change
        //get google Image save to firestore

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun changeBottomSheet(sheetActive: String) {
        val bottomFragment = BottomFragmentLogin()
        val bottomFragmentCreate = BottomFragmentCreate()
        val bottomFragmentDelete = BottomFragmentDelete()
        val bottomFragmentGroupCreate = BottomFragmentGroupCreate()

        when (sheetActive) {
            "Create" -> {
                if (!bottomFragmentCreate.isAdded && !bottomFragment.isVisible) {
                    bottomFragment.isHidden
                    bottomFragmentCreate.show(supportFragmentManager, "Bottom Create")

                }
            }
            "Login" -> {
                if (!bottomFragment.isAdded && !bottomFragment.isVisible) {
                    bottomFragmentCreate.isHidden
                    bottomFragment.show(supportFragmentManager, "Bottom Login")
                }
            }
            "reAuth" -> {
                if (!bottomFragment.isAdded && !bottomFragment.isVisible) {
                    bottomFragmentDelete.isHidden
                    bottomFragment.arguments = bundleOf("relogin" to "reAuth")
                    bottomFragment.show(supportFragmentManager, "Bottom Login")
                }
            }
            "Delete" -> {
                if (!bottomFragmentDelete.isAdded && !bottomFragmentDelete.isVisible) {
                    bottomFragment.isHidden
                    bottomFragmentDelete.arguments = bundleOf("reAuth" to "confirm")
                    bottomFragmentDelete.show(supportFragmentManager, "Delete Account")
                }
            }




        }
    }


    override fun AccountStatus(info: String, email: String?, account: FBAccountModel?) {
        val bottomJoinAddGroupFragment = BottomJoinAddGroupFragment()
        val bottomCameraFragment = BottomCameraFragment()
        when (info) {
            "Task was Successful" -> {
                //restart UI

//                homeViewModel.load(loggedViewModel.liveFirebaseUser.value!!.uid)
                recreate()
                if (account != null){

                }

            }
            "Error Email Already in Use" -> {
                val bottomFragment = BottomFragmentLogin()
                if (!bottomFragment.isAdded && !bottomFragment.isVisible) {
                    Toast.makeText(
                        this,
                        "Error Email Already in Use Try logging In",
                        Toast.LENGTH_SHORT
                    ).show()
                    bottomFragment.arguments = bundleOf("Email" to email)
                    bottomFragment.show(supportFragmentManager, "Bottom Login")
                }
            }
            "Login Failed" -> {
                val bottomFragment = BottomFragmentLogin()
                if (!bottomFragment.isAdded && !bottomFragment.isVisible) {
                    bottomFragment.arguments = bundleOf("Email" to email)
                    bottomFragment.show(supportFragmentManager, "Bottom Login")

                }
            }

            "Camera" ->{
                if (!bottomCameraFragment.isAdded && !bottomCameraFragment.isVisible){
                    bottomJoinAddGroupFragment.isHidden
                    bottomCameraFragment.isCancelable = false
                    bottomCameraFragment.show(supportFragmentManager,"Camera Fragment")
                }
            }
            "Group"->{
                if(!bottomJoinAddGroupFragment.isAdded && !bottomJoinAddGroupFragment.isVisible){
                    bottomCameraFragment.isHidden
                    if (email != null) {
                        if (email.isNotBlank()){
                            bottomJoinAddGroupFragment.arguments = bundleOf("QRCode" to email)
                        }
                    }
                    bottomJoinAddGroupFragment.show(supportFragmentManager,"Group Join")
                }
            }
        }


    }

    override fun PassView(view: Boolean) {

        if (view){
            recreate()
        }


    }

    override fun Rules(Rules: MutableList<RulesModel>) {

    }
}








