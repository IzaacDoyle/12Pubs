package Izaac.Doyle.PubsApp.ui.Maps




import Izaac.Doyle.PubsApp.Firebase.AccountActivitysViewModel
import Izaac.Doyle.PubsApp.Firebase.FirebaseLoggedIn
import Izaac.Doyle.PubsApp.Models.GooglePlacesModel
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.databinding.FragmentMapsBinding
import Izaac.Doyle.PubsApp.ui.home.GroupViewModel
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions


class MapsFragment : Fragment() {


    private var _binding: FragmentMapsBinding? = null

    private lateinit var lastlocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    lateinit var loader: AlertDialog

    private val mapsViewModel: MapsViewModel by activityViewModels()
    private val groupsViewModel: GroupViewModel by viewModels()
    private val firebaseloggedin : FirebaseLoggedIn by activityViewModels()
    private lateinit var loginViewmodel : AccountActivitysViewModel


    lateinit var toggle: SwitchCompat
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val menuHost: MenuHost = requireActivity()

        loginViewmodel = ViewModelProvider(this)[AccountActivitysViewModel::class.java]

//        if (loginViewmodel.liveFirebaseUser.value != null){
//            setHasOptionsMenu(true)
//        }



        menuHost.addMenuProvider(object :MenuProvider{


            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//                inflater.inflate(R.menu.maps_toggle,menu)


                val item = menu.findItem(R.id.toggle_group) as MenuItem
                item.setActionView(R.layout.togglelayout)
                val toggle: SwitchCompat = item.actionView.findViewById(R.id.toggle_groups)
                toggle.isChecked = false



                    if (loginViewmodel.liveFirebaseUser.value != null) {

                        firebaseloggedin.AccountObservable.observe(viewLifecycleOwner) { profile ->
                            if (profile.isNotEmpty()) {
                                if (profile[0].GroupUUID.isNullOrBlank()) {
                                    toggle.isChecked = true
                                }
                            }
                        }
                    }





                toggle.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        groupsViewModel.Places.observe(viewLifecycleOwner) { places ->
                            displayMarker(places as ArrayList<GooglePlacesModel>,"Users")
                            Toast.makeText(requireContext(), "Displaying Your Favourite Pubs", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        firebaseloggedin.AccountObservable.observe(viewLifecycleOwner){profile->
                            if (!profile.isEmpty()){
                                Log.d("MapsData",profile.toString())
                                mapsViewModel.load(profile[0].GroupUUID.toString())
                            }
                            Toast.makeText(requireContext(), "Displaying Your Groups Pubs", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                menuInflater.inflate(R.menu.maps_toggle,menu)
            }


            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }

        },viewLifecycleOwner, Lifecycle.State.RESUMED)








        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(callback)






    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mapsViewModel.mMap = googleMap
        mapsViewModel.mMap.isMyLocationEnabled = true
        mapsViewModel.mMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.google_maps_custom_pub)
        )
        mapsViewModel.currentLocation.observe(viewLifecycleOwner) {
            val loc = LatLng(
                mapsViewModel.currentLocation.value!!.latitude,
                mapsViewModel.currentLocation.value!!.longitude
            )
            mapsViewModel.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 10f))
            mapsViewModel.mMap.uiSettings.isZoomControlsEnabled = true
            mapsViewModel.mMap.uiSettings.isMyLocationButtonEnabled = true


            mapsViewModel.observableGooglePlacesPub.observe(viewLifecycleOwner, Observer { pubs ->

                pubs?.let {
                    displayMarker(pubs as ArrayList<GooglePlacesModel>,null)
                }

            })
        }



    }




//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.maps_toggle,menu)
//
//
//        val item = menu.findItem(R.id.toggle_group) as MenuItem
//        item.setActionView(R.layout.togglelayout)
//        val toggle: SwitchCompat = item.actionView.findViewById(R.id.toggle_groups)
//        toggle.isChecked = false
//        firebaseloggedin.AccountObservable.observe(viewLifecycleOwner) { profile ->
//            if (profile.isNotEmpty()){
//                if (profile[0].GroupUUID.isNullOrBlank()){
//                    toggle.isChecked = true
//                }
//            }
//        }
//
//
//        toggle.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                        groupsViewModel.Places.observe(viewLifecycleOwner) { places ->
//                                    displayMarker(places as ArrayList<GooglePlacesModel>,"Users")
//                            Toast.makeText(requireContext(), "Displaying Your Favourite Pubs", Toast.LENGTH_SHORT).show()
//                        }
//            }else{
//                firebaseloggedin.AccountObservable.observe(viewLifecycleOwner){profile->
//                    if (!profile.isEmpty()){
//                        Log.d("MapsData",profile.toString())
//                        mapsViewModel.load(profile[0].GroupUUID.toString())
//                    }
//                    Toast.makeText(requireContext(), "Displaying Your Groups Pubs", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//
//
//
//    }

    private fun displayMarker(pubs: ArrayList<GooglePlacesModel>,details:String?) {
        var markerColor:Float =BitmapDescriptorFactory.HUE_RED
        if (!pubs.isEmpty()) {
            mapsViewModel.mMap.clear()
            pubs.forEach {
                if (details == "Users"){
                    markerColor =  BitmapDescriptorFactory.HUE_AZURE + 5
                    mapsViewModel.mMap.addMarker(
                        MarkerOptions().position(LatLng(it.PubLat!!, it.PubLng!!))
                            .title(it.PubName)
                            .icon( BitmapDescriptorFactory.fromResource(R.drawable.ic_pubs))
                            .snippet("Your Favourite Pubs")
                    )




                }else{
                    mapsViewModel.mMap.addMarker(
                        MarkerOptions().position(LatLng(it.PubLat!!, it.PubLng!!))
                            .title(it.PubName)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pubs))
                            .snippet("Group Pubs")
                    )

                }

            }


        }

    }

    override fun onResume() {
        super.onResume()


        if (loginViewmodel.liveFirebaseUser.value != null){

            firebaseloggedin.getAccount(loginViewmodel.liveFirebaseUser.value!!.uid)
            firebaseloggedin.AccountObservable.observe(viewLifecycleOwner){profile->
                if (!profile.isEmpty()){
                    Log.d("MapsData",profile.toString())
                    mapsViewModel.load(profile[0].GroupUUID.toString())
                    groupsViewModel.getUsersPubsList(loginViewmodel.liveFirebaseUser.value!!.uid)
                }
            }
            groupsViewModel.gNames.observe(viewLifecycleOwner) { it ->
                if (it !=null) {
                    if (it.isNotEmpty()) {
                        groupsViewModel.getGroupPlaces(it[0].OwnerUUID)
                    }
                }
            }
        }
    }


}




