package Izaac.Doyle.PubsApp.ui.Group.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.ui.home.GroupViewModel
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator

class PubsViewpager : Fragment() {


    private val groupViewModel: GroupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pubs_viewpager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {





    }


}