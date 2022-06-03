package Izaac.Doyle.PubsApp.ui.Maps

import Izaac.Doyle.PubsApp.Firebase.GoogleRealTimeDB
import Izaac.Doyle.PubsApp.Models.FBAccountModel
import Izaac.Doyle.PubsApp.Models.GooglePlacesModel
import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("MissingPermission")
class MapsViewModel(application: Application):AndroidViewModel(application) {

    private lateinit var db: FirebaseFirestore
    private lateinit var CUuid:String
    lateinit var mMap: GoogleMap
    private var PubLocations: MutableLiveData<MutableList<GooglePlacesModel>> = MutableLiveData(mutableListOf<GooglePlacesModel>())

    private val PubsList = MutableLiveData<List<GooglePlacesModel>>()
    private val userProfile = MutableLiveData<List<FBAccountModel>>()
    var currentLocation = MutableLiveData<Location>()
    var fusedLocationClient:FusedLocationProviderClient

    val locationResult = LocationRequest.create().apply {
//        interval = 10000
//        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    val locationResultCheck = LocationRequest.create().apply {
//        interval = 10000
//        fastestInterval = 5000
//        smallestDisplacement = 0.1F
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }


    val locationCallback = object :LocationCallback(){
        override fun onLocationResult(p0: LocationResult?) {
            currentLocation.value = p0!!.locations.last()
        }
    }



    init {
//        db = FirebaseFirestore.getInstance()
//        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
//        CUuid = CheckCurrentUser()!!.uid

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
        if (locationResult != locationResultCheck){
            fusedLocationClient.requestLocationUpdates(locationResultCheck, locationCallback, Looper.getMainLooper())
        }else{
            fusedLocationClient.requestLocationUpdates(locationResult, locationCallback, Looper.getMainLooper())
        }




    }

    val observableGooglePlacesPub: LiveData<List<GooglePlacesModel>>
        get() = PubsList

    fun load(GroupUUID:String){
        try {
            GoogleRealTimeDB.FindAll(GroupUUID,PubsList)
        }catch (e:Exception){
        }
    }







}