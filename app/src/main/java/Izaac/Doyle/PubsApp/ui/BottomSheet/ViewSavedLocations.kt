package Izaac.Doyle.PubsApp.ui.BottomSheet

import Izaac.Doyle.PubsApp.Firebase.AccountActivitysViewModel
import Izaac.Doyle.PubsApp.Firebase.GoogleRealTimeDB
import Izaac.Doyle.PubsApp.Firebase.GoogleRealTimeDB.removeLocation
import Izaac.Doyle.PubsApp.Helpers.LocationRecycleView
import Izaac.Doyle.PubsApp.Helpers.PlaceClickListener
import Izaac.Doyle.PubsApp.Models.GooglePlacesModel
import Izaac.Doyle.PubsApp.databinding.ViewSavedLocationsBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ViewSavedLocations:BottomSheetDialogFragment(),PlaceClickListener {

    private var _binding: ViewSavedLocationsBinding? = null
    lateinit var myRuleAdaptor: LocationRecycleView

    private lateinit var loginViewmodel : AccountActivitysViewModel

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ViewSavedLocationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        loginViewmodel = ViewModelProvider(this)[AccountActivitysViewModel::class.java]

        if (requireArguments().containsKey("Places")){
            val RuleRecycleView = binding.viewSavedLocationRecyclerView
            myRuleAdaptor = LocationRecycleView(requireArguments().getParcelableArrayList("Places"), null,this,requireContext(),"LargeView")
            RuleRecycleView.layoutManager = LinearLayoutManager(requireContext())
            RuleRecycleView.adapter = myRuleAdaptor
        }

        if (requireArguments().containsKey("Add")){
            binding.actionButton.text = requireArguments().getString("Add")
        }
        if (requireArguments().containsKey("Remove")){
            binding.textView7.text = "Remove Pubs From Group"
            binding.actionButton.text = requireArguments().getString("Remove")
        }

        binding.CancelButton.setOnClickListener {
            dismiss()
        }




        return root

    }

    override fun onPlaceClicked(place: GooglePlacesModel,itemView:View,position:Int) {
//            myRuleAdaptor.notifyItemChanged(position)
        val placesList:ArrayList<GooglePlacesModel> = ArrayList<GooglePlacesModel>()
        myRuleAdaptor.notifyDataSetChanged()
        placesList.add(place)
        if (requireArguments().containsKey("Add")) {
            binding.actionButton.setOnClickListener {
//add
//                AddPlacesToGroup(placesList, requireArguments().getString("GroupOwner").toString(),myRuleAdaptor)
                GoogleRealTimeDB.AddLocation(requireArguments().getString("GroupOwner").toString(),placesList,
                    loginViewmodel.liveFirebaseUser.value!!
                )
                myRuleAdaptor.notifyDataSetChanged()
                dismiss()
            }
        }
        if (requireArguments().containsKey("Remove")) {
            binding.actionButton.setOnClickListener {
//remove
//                RemovePlacesFromGroup(placesList,requireArguments().getString("GroupOwner").toString(),myRuleAdaptor)
                removeLocation(requireArguments().getString("GroupOwner").toString(),placesList)
                myRuleAdaptor.notifyDataSetChanged()
                dismiss()

            }
        }




    }

}