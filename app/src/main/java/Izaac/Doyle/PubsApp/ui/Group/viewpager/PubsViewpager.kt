package Izaac.Doyle.PubsApp.ui.Group.viewpager

import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser
import Izaac.Doyle.PubsApp.Firebase.FirebaseLoggedIn
import Izaac.Doyle.PubsApp.Helpers.LocationRecycleView
import Izaac.Doyle.PubsApp.Helpers.PlaceClickListener
import Izaac.Doyle.PubsApp.Models.GooglePlacesModel
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.ui.Group.OnAboutDataReceivedListener
import Izaac.Doyle.PubsApp.ui.Group.setAboutDataListener
import Izaac.Doyle.PubsApp.ui.Maps.MapsViewModel
import Izaac.Doyle.PubsApp.ui.home.GroupViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PubsViewpager : Fragment() , OnAboutDataReceivedListener, PlaceClickListener {


    private val groupViewModel: GroupViewModel by viewModels()
    private val mapsViewModel: MapsViewModel by activityViewModels()
    private val firebaseloggedin : FirebaseLoggedIn by activityViewModels()
    lateinit var myRuleAdaptor: LocationRecycleView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        setAboutDataListener(this)

        mapsViewModel.observableGooglePlacesPub.observe(viewLifecycleOwner, Observer {

            val RuleRecycleView = view?.findViewById<RecyclerView>(R.id.group_RulesVote)
            myRuleAdaptor = LocationRecycleView(it as MutableList<GooglePlacesModel>?, this, this, requireContext(), "SmallView")
            // println("observer Rules ${}")
            RuleRecycleView!!.layoutManager = LinearLayoutManager(requireContext())
            RuleRecycleView.adapter = myRuleAdaptor

        })




        return inflater.inflate(R.layout.fragment_pubs_viewpager, container, false)
    }

    override fun onResume() {
        super.onResume()
        firebaseloggedin.getAccount(CheckCurrentUser()!!.uid)
        firebaseloggedin.AccountObservable.observe(viewLifecycleOwner){profile->
            if (!profile.isEmpty()){
                Log.d("MapsData",profile.toString())
                mapsViewModel.load(profile[0].GroupUUID.toString())
            }
        }
    }


    override fun onDataReceived(model: MutableList<GooglePlacesModel>?) {
//        if (model != null) {
//            Log.d("OnDataReceived", model.toString())
//            val RuleRecycleView = view?.findViewById<RecyclerView>(R.id.group_RulesVote)
//            myRuleAdaptor = LocationRecycleView(model, this, this, requireContext(), "SmallView")
//            // println("observer Rules ${}")
//            RuleRecycleView!!.layoutManager = LinearLayoutManager(requireContext())
//            RuleRecycleView.adapter = myRuleAdaptor
//        }else{
//
//        }
    }

    override fun onPlaceClicked(place: GooglePlacesModel, itemView: View, position: Int) {
    }


}