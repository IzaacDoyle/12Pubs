package Izaac.Doyle.PubsApp.activities

import Izaac.Doyle.PubsApp.Firebase.AccountData
import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser
import Izaac.Doyle.PubsApp.Models.FBAccountModel
import Izaac.Doyle.PubsApp.R
import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import kotlin.system.exitProcess

class StartUpActivity: AppCompatActivity() {
    private val PERMISSIONS_GRANTED = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        requestPermissions()

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
            if (CheckCurrentUser() != null){

                 val account = MutableLiveData<List<FBAccountModel>>()
                AccountData.getAccount(CheckCurrentUser()!!.uid, account)

                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    if (account.value?.get(0) != null) {
                        intent.putExtra("account", account.value?.get(0)!!.UserEmail)
                        Log.d("StartUp","Account Available " + account.value?.get(0)!!.UserEmail)
                    }
                    startActivity(intent)
                    finish()
                }, 3000)

            }else {
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 3000)
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
                    }, 2000)
                }else{
                    exitProcess(-1)
                }
            }
        }else{
            exitProcess(-1)
        }

    }


}