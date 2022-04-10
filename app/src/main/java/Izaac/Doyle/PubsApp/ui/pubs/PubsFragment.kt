package Izaac.Doyle.PubsApp.ui.pubs

import Izaac.Doyle.PubsApp.BuildConfig
import Izaac.Doyle.PubsApp.Helpers.PlacesSwipeLeft
import Izaac.Doyle.PubsApp.Helpers.PlacesSwiperight
import Izaac.Doyle.PubsApp.Helpers.PubsRecycelerView
import Izaac.Doyle.PubsApp.Models.GooglePlacesModel
import Izaac.Doyle.PubsApp.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import Izaac.Doyle.PubsApp.databinding.FragmentPubsBinding
import android.annotation.SuppressLint
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class PubsFragment : Fragment() {

    private lateinit var pubsViewModel: PubsViewModel
    private var _binding: FragmentPubsBinding? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var myAdapter: PubsRecycelerView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("MissingPermission")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        pubsViewModel =
            ViewModelProvider(this)[PubsViewModel::class.java]

        _binding = FragmentPubsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        Places.initialize(root.context,BuildConfig.google_api_key)

        val placesClient = Places.createClient(root.context)





        if (Places.isInitialized()) {
            val autocompleteFragment = childFragmentManager.findFragmentById(R.id.placesSearch)as AutocompleteSupportFragment
            val array2 = arrayListOf<GooglePlacesModel>()






            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


            autocompleteFragment.setCountry("IE")
            autocompleteFragment.setHint("Pubs")
            autocompleteFragment.setTypeFilter(TypeFilter.CITIES)
            fusedLocationClient.lastLocation.addOnSuccessListener { Location ->

                autocompleteFragment.setLocationBias(RectangularBounds.newInstance(LatLngBounds(com.google.android.gms.maps.model.LatLng(Location.longitude+0.01,Location.longitude+0.01),com.google.android.gms.maps.model.LatLng(Location.latitude-.01,Location.longitude-.01))))
            }


            autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT)
            autocompleteFragment.setPlaceFields(
                listOf(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.LAT_LNG,
                    Place.Field.ADDRESS,
                    Place.Field.PHONE_NUMBER,
                    Place.Field.OPENING_HOURS


                )
            )




            autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
                override fun onPlaceSelected(place: Place) {


                    Log.d("Place", "${place.name}, ${place.id}, ${place.latLng}, ${place.address}, ${place.openingHours} ${place.phoneNumber}")
                    val pubsModel = GooglePlacesModel(place.id,
                        place.name,
                        place.latLng?.latitude,
                        place.latLng?.longitude, place.address, place.phoneNumber,place.openingHours?.weekdayText)

//                    pubsViewModel.test.value?.add(pubsModel)
                    pubsViewModel.test.value!!.add(pubsModel)



//                    val array = arrayListOf<GooglePlacesModel>(pubsModel)
//                    array2.add(pubsModel)











                }

                override fun onError(p0: com.google.android.gms.common.api.Status) {
                    Log.d("PlacesError", "$p0")
                }
            })
            pubsViewModel.test2.observe(viewLifecycleOwner){result->
                println(result)
                myAdapter = PubsRecycelerView(result as ArrayList<GooglePlacesModel>)
                binding.pubsPlacesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.pubsPlacesRecyclerView.adapter = myAdapter



                val itemTouchHelperLeft = ItemTouchHelper(PlacesSwipeLeft(myAdapter,requireContext()))
                itemTouchHelperLeft.attachToRecyclerView(binding.pubsPlacesRecyclerView)
                val itemTouchHelperRight = ItemTouchHelper(PlacesSwiperight(myAdapter,requireContext()))
                itemTouchHelperRight.attachToRecyclerView(binding.pubsPlacesRecyclerView)


            }

//        println(test+ "======================D")


        }

          else {
            Log.d("PlacesNotWorking", "Not Initalized")
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}