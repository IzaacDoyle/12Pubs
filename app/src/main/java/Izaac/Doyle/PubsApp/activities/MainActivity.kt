package Izaac.Doyle.PubsApp.activities


import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser
import Izaac.Doyle.PubsApp.Firebase.FBGetDB
import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.databinding.ActivityMainBinding
import Izaac.Doyle.PubsApp.ui.BottomSheet.BottomFragmentCreate
import Izaac.Doyle.PubsApp.ui.BottomSheet.BottomFragmentDelete
import Izaac.Doyle.PubsApp.ui.BottomSheet.BottomFragmentGroupCreate
import Izaac.Doyle.PubsApp.ui.BottomSheet.BottomFragmentLogin
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.view.menu.ActionMenuItem
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.navArgument
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), onDataPasser {


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    lateinit var dataPasser : onDataPasser
    lateinit var app: MainApp
    var SearchActive:Boolean = false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        app = application as MainApp
        auth = Firebase.auth
        dataPasser =this as onDataPasser

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_pubs, R.id.nav_maps, R.id.nav_settings, R.id.nav_group
            ), drawerLayout
        )




//<<<<<<< HEAD
        //test


        // if (CheckCurrentUser() != null) {
        //   drawerLayout.findViewById<>()
//=======
        if (CheckCurrentUser() == null) {
            binding.drawerLayout.findViewById<LinearLayout>(R.id.Drawer_Login_Create).isGone =
                false
            binding.navView.menu[2].isVisible = false
            binding.navView.menu[2].isEnabled = false
            binding.navView.menu[1].subMenu[1].isVisible = false
        } else {
            binding.drawerLayout.findViewById<LinearLayout>(R.id.Drawer_Login_Create).isGone = true
            binding.navView.menu[2].isVisible = true
            binding.navView.menu[2].isEnabled = true
            binding.navView.menu[1].subMenu[1].isVisible = true
            FBGetDB(CheckCurrentUser()!!.uid, this)
//>>>>>>> 4e3c07637217810eb2b9107dc18be93947d77770
        }


//        val settingsBtn =
//            navView.findViewById<androidx.appcompat.widget.LinearLayoutCompat>(R.id.drawer_setting)
        val loginBtn = navView.findViewById<LinearLayout>(R.id.Drawer_Login)
        val createAccountBtn = navView.findViewById<LinearLayout>(R.id.Drawer_CreateA)
//        settingsBtn.setOnClickListener {
//
//            //if account avalable show Settings if not make it Gone
//            // settings.visibility = View.GONE
//
//
//            // val intent = Intent(this,SettingsActivity::class.java)
//            //  startActivity(intent)
//
//            //Toast.makeText(applicationContext, "This Works", Toast.LENGTH_SHORT).show()
//
//            //Change Drawer from here
//        }

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




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val searchBox = findViewById<ConstraintLayout>(R.id.searchBoxConstraint)

//        if (!SearchActive){
//            searchBox.visibility = View.GONE
//        }

//        if (item.isActionViewExpanded){
//            searchBox.visibility = View.VISIBLE
//        }
//       item.collapseActionView()




        when (item.itemId) {
            R.id.settings_signout -> {
                Log.d("settingsActivity", "Log out")
                app.account.SignOut(this)
            }
//            R.id.GroupAddSearch->{

//
//                searchBox.visibility = View.VISIBLE

//                searchBox.isVisible = !searchBox.isVisible



//                searchBox.setOnFocusChangeListener { v, hasFocus ->
//                    if (!hasFocus){
//                        println("Test")
////                        searchBox.visibility = View.GONE
//                    }
//                    if (hasFocus){
//                        println("Test2")
////                        searchBox.visibility = View.VISIBLE
//                    }
//                }


//                searchBox.visibility = View.VISIBLE
//
//                if (!SearchActive){
//                    Log.d("SearchActive", SearchActive.toString())
//                    searchBox.visibility = View.VISIBLE
//                }




//                searchBox.visibility = View.VISIBLE
//
//                val search = findViewById<ActionMenuItemView>(R.id.GroupAddSearch)


//                search.setOnFocusChangeListener { v, hasFocus ->
//                    if (!hasFocus){
//                        Log.d("SearchBoc","No Focus")
//                        searchBox.visibility = View.GONE
//                    }else{
//                        Log.d("SearchBoc"," Focus")
//                    }
//                }
//                .setOnFocusChangeListener { v, hasFocus ->
//
//                }

               // Log.d("searchBox", searchBox.toString())

//            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        CheckCurrentUser()
        Log.d("Google Account", "${CheckCurrentUser()?.email}")


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        CheckCurrentUser()
        val email = findViewById<TextView>(R.id.Drawer_Email)
        val name = findViewById<TextView>(R.id.Drawer_Name)

        //val profileimage = findViewById<ImageView>(R.id.Drawer_Account_Image)

        val drawerDropDown = findViewById<LinearLayout>(R.id.drawer_header_button)

        var buttonclicks = 0

        if (CheckCurrentUser() == null) {
            drawerDropDown.isVisible = false
            drawerDropDown.isEnabled = false
        } else {

            val sharedPrefInfo =
                getSharedPreferences(CheckCurrentUser()!!.uid, Context.MODE_PRIVATE)

            if (sharedPrefInfo.getString("Username", "")!!.isEmpty()){
                val username = CheckCurrentUser()!!.displayName
                name.text = username
                email.text = CheckCurrentUser()!!.email

                val datastore = getSharedPreferences(CheckCurrentUser()!!.uid,Context.MODE_PRIVATE)
                val editor = datastore.edit()
                editor.putString("Username",username)
                editor.apply()
            }else{
                val username = sharedPrefInfo.getString("Username", "")
                //val userEmail = sharedPrefInfo.getString("Email", "")
                name.text = username
                email.text = CheckCurrentUser()!!.email
            }


            //Log.d("shardpref", "$username to $userEmail")


            //  email.text = userinfo.email






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
                        app.account.SignOut(this)
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
        if (CheckCurrentUser() != null) {
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


    override fun AccountStatus(info: String, email: String) {
        when (info) {
            "Task was Successful" -> {
                //restart UI
                // Update()
                recreate()

                //Account Was Made
//                Toast.makeText(this, "Task was Successful", Toast.LENGTH_SHORT).show()
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
        }

    }

    override fun PassView(view: Activity) {
    }
}








