package Izaac.Doyle.PubsApp.ui.BottomSheet

import Izaac.Doyle.PubsApp.databinding.AccountBottomDialogBinding
import Izaac.Doyle.PubsApp.databinding.SettingUpdateInfoBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class settings_update_info: BottomSheetDialogFragment() {

    private var _binding: SettingUpdateInfoBinding? = null




    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SettingUpdateInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root




        return root
    }



}