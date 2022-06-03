package Izaac.Doyle.PubsApp.ui.Group.viewpager

import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser

import Izaac.Doyle.PubsApp.Firebase.FirebaseLoggedIn

import Izaac.Doyle.PubsApp.Helpers.GroupRulesRecycleView
import Izaac.Doyle.PubsApp.Helpers.RulesClickListener
import Izaac.Doyle.PubsApp.Helpers.onDataPasser
import Izaac.Doyle.PubsApp.Models.RulesModel
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.ui.home.GroupViewModel
import Izaac.Doyle.PubsApp.ui.pubs.BottomPubsInfoFragment
import android.content.Context
import android.util.Log
import androidx.core.os.bundleOf

import androidx.fragment.app.activityViewModels

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



class RulesViewpager : Fragment(), RulesClickListener,onDataPasser{

    private val groupViewModel: GroupViewModel by viewModels()

    private val firebaseloggedin : FirebaseLoggedIn by activityViewModels()

    lateinit var myRuleAdaptor: GroupRulesRecycleView
    lateinit var dataPasser : onDataPasser





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        groupViewModel.QrCodeScanSearch(CheckCurrentUser()!!.uid)


        groupViewModel.qrcodeSearch.observe(viewLifecycleOwner){result->
            if (result != null) {
                if (result.isNotEmpty()) {
                    if (!result[0].GroupUUID.isNullOrEmpty())

                    groupViewModel.getUserGroup(result[0].GroupUUID.toString())



                    println("Test " + result.toString())
                    Log.d("GroupUUID From User", result[0].GroupUUID.toString())
                }
            }
        }



        groupViewModel.gNames.observe(viewLifecycleOwner){result->
            if (result != null){
                if (result.isNotEmpty()){
                    if (!result[0].RuleNumbers.isNullOrEmpty())
                groupViewModel.Rules(result[0].RuleNumbers)
            }}
//            println(result[0].RuleNumbers)
            Log.d("RulesGNAME ",result.toString())
        }

        groupViewModel.groupRule.observe(viewLifecycleOwner) { it ->

            if (it.isNotEmpty()) {
                Log.d("Rules GroupRules", it.toString())
                val RuleRecycleView = view?.findViewById<RecyclerView>(R.id.group_RulesVote)
                myRuleAdaptor = GroupRulesRecycleView(it, this)
                // println("observer Rules ${}")
                RuleRecycleView!!.layoutManager = LinearLayoutManager(requireContext())
                RuleRecycleView.adapter = myRuleAdaptor
            }else{
                it.clear()
                val RuleRecycleView = view?.findViewById<RecyclerView>(R.id.group_RulesVote)
                myRuleAdaptor = GroupRulesRecycleView(it, this)
                // println("observer Rules ${}")
                RuleRecycleView!!.layoutManager = LinearLayoutManager(requireContext())
                RuleRecycleView.adapter = myRuleAdaptor
            }
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

    override fun onResume() {

        firebaseloggedin.getAccount(CheckCurrentUser()!!.uid)
        firebaseloggedin.AccountObservable.observe(viewLifecycleOwner) { account ->
            if (account.isNotEmpty()) {

                if (account[0].GroupUUID.isNullOrBlank()) {

                    val RuleRecycleView = view?.findViewById<RecyclerView>(R.id.group_RulesVote)
                    myRuleAdaptor = GroupRulesRecycleView(null, this)
                    // println("observer Rules ${}")
                    RuleRecycleView!!.layoutManager = LinearLayoutManager(requireContext())
                    RuleRecycleView.adapter = myRuleAdaptor
                } else {
                    groupViewModel.getUserGroup(account[0].GroupUUID.toString())
                }

            }
        }


        super.onResume()

    }


    override fun onAttach(context: Context) {
        dataPasser = context as onDataPasser
        super.onAttach(context)
    }

    override fun changeBottomSheet(sheetActive: String) {
    }

    override fun AccountStatus(info: String, email: String) {
    }

    override fun PassView(view: Boolean) {
    }

    override fun Rules(Rules: MutableList<RulesModel>) {
        if (Rules.isNotEmpty()) {

        }
    }


}