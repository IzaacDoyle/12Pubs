package Izaac.Doyle.PubsApp.ui.BottomSheet


import Izaac.Doyle.PubsApp.Firebase.AccountActivitysViewModel
import Izaac.Doyle.PubsApp.Firebase.AccountData

import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.activities.MainActivity
import Izaac.Doyle.PubsApp.databinding.CustomDeleteDialogboxBinding
import Izaac.Doyle.PubsApp.ui.home.HomeViewModel
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomFragmentDelete: BottomSheetDialogFragment() {

    private var _binding: CustomDeleteDialogboxBinding? = null
    lateinit var dataPasser : onDataPasser
    lateinit var app: MainApp

    private val homeViewModel : HomeViewModel by activityViewModels()
    private lateinit var loginViewmodel: AccountActivitysViewModel


    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = requireActivity().application as MainApp
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CustomDeleteDialogboxBinding.inflate(inflater, container, false)
        val root: View = binding.root

        loginViewmodel = ViewModelProvider(this)[AccountActivitysViewModel::class.java]

        binding.reAuthDelete.setOnClickListener {
            dismiss()
            dataPasser.changeBottomSheet("reAuth")
        }

        if (this.arguments != null) {
            if (requireArguments().containsKey("reAuth")) {
                binding.reAuthDelete.isEnabled = false
                binding.deleteCheckmark.isVisible = true
                binding.deleteConfirm.isEnabled = true
            }
        }

        if (binding.deleteConfirm.isEnabled){
            binding.deleteConfirm.setOnClickListener {
//                app.account.DeleteAccount(requireActivity(),requireContext())
                homeViewModel.load(loginViewmodel.liveFirebaseUser.value!!.uid)
//                AccountData.deleteAccount(requireActivity(),requireContext(),homeViewModel.observableAccountData.value!![0])
                loginViewmodel.DeleteAccount(requireActivity(),requireContext(),homeViewModel.observableAccountData.value!![0])

                dismiss()
            }
        }

        binding.deleteCancel.setOnClickListener {
            dismiss()
        }



        return root

    }

    override fun onAttach(context: Context) {
        dataPasser = context as onDataPasser
        super.onAttach(context)
    }

}