package Izaac.Doyle.PubsApp.Firebase

import Izaac.Doyle.PubsApp.Models.RuleAmount
import Izaac.Doyle.PubsApp.Models.RulesModel
import Izaac.Doyle.PubsApp.Models.RulesStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.toObject

object RulesData: RulesStore {

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {

        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()


    }




    override fun ruleLoad() {
    }

    override fun ruleRoll(ruleNum: ArrayList<Int>, rule: MutableLiveData<MutableList<RulesModel>>) {
        val rulesList = mutableListOf<RulesModel>()



        for(i in ruleNum) {


            db.collection("PubRules").document(i.toString())
                .get()
                .addOnSuccessListener { result ->
                    if (result.exists()) {
                        val rules = result.toObject(RulesModel::class.java)
                        if (rules != null) {
                            rulesList.add(rules)
                        }
                    }

                    Log.d("RuleRoll", rulesList.toString())

                    if (rulesList.size == ruleNum.size) {

                        val hash = ruleNum.withIndex().associateTo(HashMap()){it.value.toString() to it.index}
                        rulesList.sortBy { hash[it.RuleID] }

                        rule.value = rulesList

                    }
                }
        }
    }

    override fun getRuleNums(ruleNums: MutableLiveData<RuleAmount>) {
         db.collection("PubRules").document("0")
             .get()
             .addOnSuccessListener { result ->
                 if (result.exists()){

                     val rulesAmount = result.toObject(RuleAmount::class.java)
                     Log.d("RuleNumAmountGet","$rulesAmount")
                    ruleNums.value = rulesAmount
                 }
             }
    }
}