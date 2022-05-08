package Izaac.Doyle.PubsApp.ui.pubs

import Izaac.Doyle.PubsApp.BuildConfig
import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser
import Izaac.Doyle.PubsApp.Helpers.*
import Izaac.Doyle.PubsApp.Models.GooglePlacesModel
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.databinding.FragmentPubsBinding
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class PubsFragment : Fragment(), PubsClickListener {

    private lateinit var pubsViewModel: PubsViewModel
    private var _binding: FragmentPubsBinding? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var myAdapter: PubsRecycelerView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("MissingPermission", "NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pubsViewModel =
            ViewModelProvider(this)[PubsViewModel::class.java]

        _binding = FragmentPubsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        Places.initialize(root.context, BuildConfig.google_api_key)

        val placesClient = Places.createClient(root.context)





        if (Places.isInitialized()) {
            val autocompleteFragment = childFragmentManager.findFragmentById(R.id.placesSearch) as AutocompleteSupportFragment


            val array2 = arrayListOf<GooglePlacesModel>()



            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


            autocompleteFragment.setCountry("IE")
            autocompleteFragment.setHint("Pubs")


            fusedLocationClient.lastLocation.addOnSuccessListener { Location ->
                autocompleteFragment.setLocationBias(
                    RectangularBounds.newInstance(
                        LatLng(Location.latitude-0.02,Location.longitude-0.02),
                        LatLng(Location.latitude+0.02,Location.longitude+0.02)
                    //0.01 is 1 km ish

                    )
                )

                Log.d("LocationBias",
                    LatLng(Location.latitude-0.02,Location.longitude-0.02).toString() + "  " +LatLng(Location.latitude+0.02,Location.longitude+0.04).toString()
                )

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

                    Log.d(
                        "Place",
                        "${place.name}, ${place.id}, ${place.latLng}, ${place.address}, ${place.openingHours} ${place.phoneNumber}"
                    )
                    val pubsModel = GooglePlacesModel(
                        "",
                        place.id,
                        place.name,
                        place.latLng?.latitude,
                        place.latLng?.longitude,
                        place.address,
                        place.phoneNumber,
                        place.openingHours?.weekdayText
                    )
//                    pubsViewModel.test.value?.add(pubsModel)
                    pubsViewModel.PlacesObv.value!!.add(pubsModel)


                }

                override fun onError(p0: com.google.android.gms.common.api.Status) {
                    Log.d("PlacesError", "$p0")
                }
            })
            pubsViewModel.PlacesObv.observe(viewLifecycleOwner) { result ->

                println(result)
                myAdapter = PubsRecycelerView(result as ArrayList<GooglePlacesModel>, this)
                binding.pubsPlacesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.pubsPlacesRecyclerView.adapter = myAdapter

                myAdapter.notifyDataSetChanged()

                if (CheckCurrentUser() !=null) {
                    val itemTouchHelperLeft =
                        ItemTouchHelper(PlacesSwipeLeft(myAdapter, requireContext()))
                    itemTouchHelperLeft.attachToRecyclerView(binding.pubsPlacesRecyclerView)
                    val itemTouchHelperRight =
                        ItemTouchHelper(PlacesSwiperight(myAdapter, requireContext()))
                    itemTouchHelperRight.attachToRecyclerView(binding.pubsPlacesRecyclerView)
                    val itemTouchHelperMove =
                        ItemTouchHelper(PlacesDragtoRearage(myAdapter, requireContext(), result))
                    itemTouchHelperMove.attachToRecyclerView(binding.pubsPlacesRecyclerView)
                }else{
                    myAdapter = PubsRecycelerView(arrayListOf(GooglePlacesModel("","0","Your Local Pub",0.0,0.0,"made Easy to Find","0",null)), this)
                    binding.pubsPlacesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                    binding.pubsPlacesRecyclerView.adapter = myAdapter
                }
            }


        } else {
            Log.d("PlacesNotWorking", "Not Initalized")
        }





        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPubsClicked(pubs: GooglePlacesModel) {

        Log.d("Pubs Click", pubs.toString())
        if (pubs.PubsID == "0"){
            Toast.makeText(requireContext(), "Please Log in to Gain full range of Features", Toast.LENGTH_LONG).show()
        }else{
            val bottomFragment = BottomPubsInfoFragment()
            bottomFragment.arguments = bundleOf(
                "1" to pubs.PubName,
                "2" to pubs.PubPhoneNum,
                "3" to pubs.PubAddress,
                "4" to pubs.PubOpeningHours
                        )
//            bottomFragment.arguments = bundleOf("PubNumber" to pubs.PhoneNumber)
//            bottomFragment.arguments = bundleOf("PubAddress" to pubs.Address)
//           bottomFragment.arguments = bundleOf("PubHours" to pubs.OpeningHours)
            bottomFragment.show(childFragmentManager, "Pubs Info")
        }

            }
}