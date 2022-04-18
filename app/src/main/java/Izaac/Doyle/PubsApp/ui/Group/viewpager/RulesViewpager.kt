package Izaac.Doyle.PubsApp.ui.Group.viewpager

import Izaac.Doyle.PubsApp.Helpers.GroupRulesRecycleView
import Izaac.Doyle.PubsApp.Helpers.RulesClickListener
import Izaac.Doyle.PubsApp.Models.GooglePlacesModel
import Izaac.Doyle.PubsApp.Models.RulesModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.ui.home.GroupViewModel
import Izaac.Doyle.PubsApp.ui.pubs.BottomPubsInfoFragment
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RulesViewpager : Fragment(), RulesClickListener {

    private val groupViewModel: GroupViewModel by viewModels()
    lateinit var myRuleAdaptor: GroupRulesRecycleView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        groupViewModel.gNames.observe(viewLifecycleOwner){it->


            if (it.isNotEmpty()){
                groupViewModel.Rules(it[0].RuleNumbers)

            }
        }

        groupViewModel.groupRule.observe(viewLifecycleOwner){it->

            val RuleRecycleView = view?.findViewById<RecyclerView>(R.id.group_RulesVote)

            myRuleAdaptor = GroupRulesRecycleView(it,this)
           // println("observer Rules ${}")
            RuleRecycleView!!.layoutManager = LinearLayoutManager(requireContext())
            RuleRecycleView.adapter = myRuleAdaptor
        }




        return inflater.inflate(R.layout.fragment_rules_viewpager, container, false)
    }

    override fun onRulesClicked(rules: RulesModel) {
        Log.d("RuleClicked",rules.toString())
        val bottomFragment = BottomPubsInfoFragment()
        bottomFragment.arguments = bundleOf(
            "2" to rules.RuleName,
            "3" to rules.RuleDescription
        )
        bottomFragment.show(childFragmentManager, "Rules Info")
    }


}