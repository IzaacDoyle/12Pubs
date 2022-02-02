package Izaac.Doyle.PubsApp.activities

import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser
import Izaac.Doyle.PubsApp.Helpers.onDataPasser


import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.Models.AccountModel
import Izaac.Doyle.PubsApp.R
import android.os.Bundle
import android.view.Menu

import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import Izaac.Doyle.PubsApp.databinding.ActivityMainBinding
import Izaac.Doyle.PubsApp.ui.BottomSheet.BottomFragmentCreate
import Izaac.Doyle.PubsApp.ui.BottomSheet.BottomFragmentLogin
import android.util.Log
import android.view.MenuItem
import android.widget.*

import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.view.menu.MenuView
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class MainActivity : AppCompatActivity(), onDataPasser {


    lateinit var app: MainApp
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        app = application as MainApp
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        setSupportActionBar(binding.appBarMain.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_pubs, R.id.nav_maps, R.id.nav_settings
            ), drawerLayout
        )






        if (CheckCurrentUser() != null) {
           drawerLayout.findViewById<LinearLayout>(R.id.Drawer_Login_Create).isGone = true
        }


//        val settingsBtn =
//            navView.findViewById<androidx.appcompat.widget.LinearLayoutCompat>(R.id.drawer_setting)
        val loginBtn = navView.findViewById<TextView>(R.id.Drawer_Login)
        val createAccountBtn = navView.findViewById<TextView>(R.id.Drawer_CreateA)
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

            val googleSignInClient: GoogleSignInClient

            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)

            googleSignInClient.signOut()
            app.account.SignOut()

            Toast.makeText(applicationContext, "Create Works", Toast.LENGTH_SHORT).show()
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val accountdrawer =  navView.menu[1].subMenu[1]
        accountdrawer.setActionView(R.layout.menu_account_dropdown)
//        accountdrawer.setOnMenuItemClickListener {
//            when(it.itemId){
//              R.id.Menu_Account ->{
//                    Log.d("drawerAction","Account Button pressed")
//                    navView.menu[1].subMenu[2].expandActionView()
//                   true
//                }
//
//                else -> {
//                    Log.d("drawerAction","ELSE ENTERED Account Button pressed")
//                    false}
//            }
//
//        }



        //getItem(R.id.Menu_Account).setActionView(R.layout.menu_account_dropdown)
            //.setActionView(R.layout.menu_account_dropdown)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.Menu_Account -> {
                binding.navView.menu[1].subMenu[2].isVisible

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        CheckCurrentUser()
        Log.d("Google Account", "${CheckCurrentUser()?.email}")


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

       //menu[1].subMenu[1].setActionView(R.layout.menu_account_dropdown)


        return true
    }

    override fun onResume() {

        super.onResume()
    }

    override fun onPause() {

        super.onPause()
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        val email = findViewById<TextView>(R.id.Drawer_Email)
        val name = findViewById<TextView>(R.id.Drawer_Name)
        //val profileimage = findViewById<ImageView>(R.id.Drawer_Account_Image)


        //an UI so that once user loged in Email and Name and Image change
        //get google Image save to firestore


        email.text = CheckCurrentUser()?.email.toString()
        name.text = CheckCurrentUser()?.displayName.toString()

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun changeBottomSheet(sheetActive: String) {
        val bottomFragment = BottomFragmentLogin()
        val bottomFragmentCreate = BottomFragmentCreate()
        when (sheetActive) {


            "Create" -> {

                if (!bottomFragmentCreate.isAdded) {
                    bottomFragmentCreate.show(supportFragmentManager, "Bottom Create")
                    bottomFragment.isHidden
                }

            }
            "Login" -> {

                if (!bottomFragment.isAdded && !bottomFragment.isVisible) {
                    bottomFragment.show(supportFragmentManager, "Bottom Login")
                    bottomFragmentCreate.isHidden
                }

            }


        }
    }


    override fun CreatingAccount(info: String, email: String) {
        when (info) {
            "Task was Successful" -> {
                //restart UI
                // Update()

                //Account Was Made
                Toast.makeText(this, "Task was Successful", Toast.LENGTH_SHORT).show()
            }
            "Error Email Already in Use" -> {
                val bottomFragment = BottomFragmentLogin()
                if (!bottomFragment.isAdded && !bottomFragment.isVisible) {
                    Toast.makeText(this, "Error Email Already in Use Try logging In", Toast.LENGTH_SHORT).show()
                    bottomFragment.arguments = bundleOf("Email" to email)
                    bottomFragment.show(supportFragmentManager, "Bottom Login")
                }
            }
        }
    }
}

