package Izaac.Doyle.PubsApp.Models

import androidx.lifecycle.MutableLiveData

interface RulesStore {


    fun ruleLoad()
    fun ruleRoll(ruleNum:ArrayList<Int>,rule: MutableLiveData<MutableList<RulesModel>>)
    fun getRuleNums(ruleNums: MutableLiveData<RuleAmount>)
}