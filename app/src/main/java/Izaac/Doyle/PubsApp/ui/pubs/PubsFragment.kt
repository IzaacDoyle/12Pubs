package Izaac.Doyle.PubsApp.ui.pubs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import Izaac.Doyle.PubsApp.databinding.FragmentPubsBinding

class PubsFragment : Fragment() {

    private lateinit var galleryViewModel: PubsViewModel
    private var _binding: FragmentPubsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
                ViewModelProvider(this).get(PubsViewModel::class.java)

        _binding = FragmentPubsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}