package Izaac.Doyle.PubsApp.ui.Maps

import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.databinding.FragmentMapsBinding
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.engine.Resource
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.MapsInitializer.Renderer
import com.google.android.gms.maps.OnMapsSdkInitializedCallback


class MapsFragment : Fragment(), OnMapReadyCallback, OnMapsSdkInitializedCallback {

    private lateinit var mapsViewModel: MapsViewModel
    private var _binding: FragmentMapsBinding? = null
    private lateinit var mMap: GoogleMap


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        mapsViewModel =
            ViewModelProvider(this)[MapsViewModel::class.java]

        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        return root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapsInitializer.initialize(requireActivity(), MapsInitializer.Renderer.LATEST, this)

    }


    override fun onMapsSdkInitialized(renderer: MapsInitializer.Renderer) {

        when (renderer) {
            Renderer.LATEST -> {
                Log.d("MapsDemo", "The latest version of the renderer is used.")

                GoogleMapOptions().apply {
                   // mapId(resources.getString(R.string.map_id))
                }
            }
            Renderer.LEGACY -> {

               // mMap.setMapStyle( MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.google_maps_custom_pub))


                Log.d("MapsDemo", "The legacy version of the renderer is used.")
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
       mMap.setMapStyle(
           MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.google_maps_custom_pub)
       )
           // mapId(resources.getString(R.string.map_id))
    }
}


