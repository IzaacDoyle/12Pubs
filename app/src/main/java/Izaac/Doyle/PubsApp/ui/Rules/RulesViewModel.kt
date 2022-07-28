package Izaac.Doyle.PubsApp.ui.Rules

import Izaac.Doyle.PubsApp.Firebase.AccountData
import Izaac.Doyle.PubsApp.Firebase.RulesData
import Izaac.Doyle.PubsApp.Models.FBAccountModel
import Izaac.Doyle.PubsApp.Models.RuleAmount
import Izaac.Doyle.PubsApp.Models.RulesModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.log

class RulesViewModel: ViewModel() {

//    var Rule: MutableLiveData<MutableList<RulesModel>> = MutableLiveData(ArrayList<RulesModel>())
    var ruleroll : MutableLiveData<MutableList<RulesModel>> = MutableLiveData(ArrayList<RulesModel>())

    var ruleAmount: MutableLiveData<RuleAmount> = MutableLiveData()


    val observableRuleRoll: LiveData<MutableList<RulesModel>>
        get() = ruleroll

    fun ruleRoll(ruleNum:ArrayList<Int>){
        try {
            RulesData.ruleRoll(ruleNum,ruleroll)
        }catch (e:Exception){
            Log.d("rulerollError","${e.message}")
        }
    }


    val ruleAmountnum: LiveData<RuleAmount>
        get() = ruleAmount

    fun getRuleAmount(){
        try {
            RulesData.getRuleNums(ruleAmount)
        }catch (e:Exception){

        }
    }


}