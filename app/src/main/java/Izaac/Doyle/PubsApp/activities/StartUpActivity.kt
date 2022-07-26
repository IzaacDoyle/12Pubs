package Izaac.Doyle.PubsApp.activities

import Izaac.Doyle.PubsApp.Firebase.AccountActivitysViewModel
import Izaac.Doyle.PubsApp.Firebase.AccountData

import Izaac.Doyle.PubsApp.Models.FBAccountModel
import Izaac.Doyle.PubsApp.R
import android.Manifest
import android.app.Application
import android.content.Intent
import androidx.lifecycle.Observer
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.system.exitProcess

class StartUpActivity: AppCompatActivity() {
    private val PERMISSIONS_GRANTED = 101

    private lateinit var loggedViewModel:AccountActivitysViewModel
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)



//        loggedViewModel.CheckCurrentUser()






        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)


    }

    private fun requestPermissions(){
       // Toast.makeText(this, "Entered Permission Check", Toast.LENGTH_SHORT).show()
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=  PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) !=  PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=  PackageManager.PERMISSION_GRANTED
        ){
         //   Toast.makeText(this, "Entered Permission Check 2", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this, arrayOf(
                //Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA ), PERMISSIONS_GRANTED)


        }else{
         //   Toast.makeText(this, "Else Entered", Toast.LENGTH_SHORT).show()
            if (firebaseAuth.currentUser != null){
                val profile =  MutableLiveData<List<FBAccountModel>>()
                AccountData.getAccount(firebaseAuth.currentUser!!.uid, profile)
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    if (profile.value != null && profile.value!!.isNotEmpty()) {
                        intent.putExtra("account", profile.value!![0].Username!!)

                        Log.d("StartUp","Account Available " + profile.value?.get(0)!!.Username!!)
                    }
                    startActivity(intent)
                    finish()
                }, 4000)

            }else {
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 4000)
            }
        }
        return
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        if (requestCode == PERMISSIONS_GRANTED && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permissions", "${permissions[i]} Granted")

                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 4000)
                }else{
                    exitProcess(-1)
                }
            }
        }else{
            exitProcess(-1)
        }

    }

    override fun onStart() {
        super.onStart()
        loggedViewModel = ViewModelProvider(this)[AccountActivitysViewModel::class.java]
        loggedViewModel.liveFirebaseUser.observe(this, Observer
        { firebaseUser -> if (firebaseUser != null){
            Log.d("StartUp","${firebaseUser.uid}")
            requestPermissions()
        }else{
            requestPermissions()
        }

        })
    }



}