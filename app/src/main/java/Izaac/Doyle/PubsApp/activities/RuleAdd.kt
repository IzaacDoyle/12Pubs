package Izaac.Doyle.PubsApp.activities


import Izaac.Doyle.PubsApp.databinding.RulesAddBinding
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RuleAdd: AppCompatActivity() {

    lateinit var binding:RulesAddBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = RulesAddBinding.inflate(layoutInflater)
        setContentView(binding.root)





        binding.RuleAdd.setOnClickListener {


            val RuleNum = binding.Rulenum.text.toString()
            val ruleName = binding.RuleName.text.toString()
            val RuleDesc = binding.RuleDescription.text.toString()


            saveRule(RuleNum,ruleName,RuleDesc)

            binding.RuleName.setText("")
            binding.RuleDescription.setText("")
            binding.Rulenum.setText("")


        }











    }

    private fun saveRule(RuleNum:String,RuleName:String,RuleDesc:String) {
        val db = Firebase.firestore

        val Rule = mapOf(
            "RuleID" to RuleNum,
            "RuleName" to RuleName,
            "RuleDescription" to RuleDesc
        )

        db.collection("PubRules").document(RuleNum.toString())
            .set(Rule)
    }
}