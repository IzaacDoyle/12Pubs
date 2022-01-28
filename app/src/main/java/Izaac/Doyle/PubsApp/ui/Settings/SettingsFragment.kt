package Izaac.Doyle.PubsApp.ui.Settings

import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.databinding.FragmentHomeBinding
import Izaac.Doyle.PubsApp.databinding.SettingsActivityBinding
import Izaac.Doyle.PubsApp.ui.home.HomeViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class SettingsFragment: Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: SettingsActivityBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProvider(this)[SettingsViewModel::class.java]

        _binding = SettingsActivityBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}