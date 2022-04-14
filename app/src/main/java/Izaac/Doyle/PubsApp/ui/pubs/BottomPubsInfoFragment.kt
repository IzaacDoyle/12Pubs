package Izaac.Doyle.PubsApp.ui.pubs

import Izaac.Doyle.PubsApp.Models.GooglePlacesModel
import Izaac.Doyle.PubsApp.databinding.FragmentGroupCreateBinding
import Izaac.Doyle.PubsApp.databinding.PlacesInfoBottomDialogBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomPubsInfoFragment: BottomSheetDialogFragment() {

    var _binding :PlacesInfoBottomDialogBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PlacesInfoBottomDialogBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (this.arguments != null){

            if (this.requireArguments().get("1") != null){
                binding.PubName.text = this.requireArguments().get("1").toString()
            }
            if (this.requireArguments().get( "3") != null){
                binding.PubAddress.text = this.requireArguments().get( "3").toString()
            }
            if (this.requireArguments().get("4") != null){
                binding.PubHours.text = this.requireArguments().get("4").toString()
            }
            if (this.requireArguments().get( "2") != null){
                binding.PubPhoneNumber.text = this.requireArguments().get( "2").toString()
            }



//            val pubs = this.requireArguments().getStringArray("Pubs")


        }










        return root

    }
}