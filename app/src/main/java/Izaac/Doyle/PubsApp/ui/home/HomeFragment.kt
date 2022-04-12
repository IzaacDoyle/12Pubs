package Izaac.Doyle.PubsApp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.activities.RuleAdd
import Izaac.Doyle.PubsApp.databinding.FragmentHomeBinding
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root



        binding.RuleAdd.setOnClickListener {
            val intent = Intent(requireContext(), RuleAdd::class.java)
            startActivity(intent)
        }


        if (arguments != null){
            if (requireArguments().containsKey("Signout")){
                Toast.makeText(requireContext(),"Signed OUt", Toast.LENGTH_SHORT).show()
            }
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}