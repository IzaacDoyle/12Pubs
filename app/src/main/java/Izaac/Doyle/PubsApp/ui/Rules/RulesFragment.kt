package Izaac.Doyle.PubsApp.ui.Rules


import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.databinding.FragmentRulesBinding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import java.util.*
import kotlin.collections.ArrayList


class RulesFragment:Fragment() {

    private var _binding: FragmentRulesBinding? = null

    private val  ruleViewmodel: RulesViewModel by viewModels()

    val rulearray = ArrayList<Int>()



    private val binding get() = _binding!!







    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRulesBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        binding.spinnerInclude.spinnerframeLayout.getTransition(R.id.spintop).setEnable(false)






        Toast.makeText(requireContext(), "${ruleViewmodel.ruleAmountnum.value}", Toast.LENGTH_SHORT).show()

//        Log.d("RuleNumAmount", ruleViewmodel.ruleAmountnum.value.toString())


        fun randomRule(randomNumber :Int) {

            rulearray.clear()

            var number:Int = 0

            Log.d("RuleRandomNumber",randomNumber.toString())

            repeat(randomNumber){
                number = (1..ruleViewmodel.ruleAmountnum.value!!.RuleAmount.toInt()).random()
                if (rulearray.contains(number)) {
                    number = (1..ruleViewmodel.ruleAmountnum.value!!.RuleAmount.toInt()).random()

                    while (!rulearray.contains(number)) {
                        rulearray.add(number)
                    }
                }else{
                    rulearray.add(number)
                }

            }
//            Log.d("RuleRandomNum2", rulearray.toString())

//            repeat(randomNumber) { index->
//                Log.d("Ruleindex", index.toString())
//                number = (1..ruleViewmodel.ruleAmountnum.value!!.RuleAmount.toInt()).random()
//                if (rulearray.contains(number) && number == 0) {
//                    number = (1..ruleViewmodel.ruleAmountnum.value!!.RuleAmount.toInt()).random()
//                }
//                while (!rulearray.contains(number)) {
//                    Log.d("RuleNumberadd", number.toString())
//                    rulearray.add(number)
//
//                }
//            }
        }











        binding.spinnerTrigger.setOnClickListener {
            val randomNumber: Int = (6..10).random()







            Log.d("RuleRandomNum", rulearray.toString())

            randomRule(randomNumber)



            if (rulearray.size == randomNumber){



//                Log.d("RuleArraySize",rulearray.size.toString())


            ruleViewmodel.ruleRoll(rulearray)

//            if (randomRule() != 0) {

                if (ruleViewmodel.observableRuleRoll.value != null) {

                    Log.d("RuleNumAmount", ruleViewmodel.ruleAmountnum.value!!.RuleAmount)

                    // Check if current random rule is already taken/used

//                    binding.spinnerInclude.textView.text = ruleViewmodel.observableRuleRoll.value!!.RuleName.toString()


                    ruleViewmodel.observableRuleRoll.observe(viewLifecycleOwner){
                        if (it != null) {

                            if (it.size == randomNumber) {


                                var spinCount: Int = 0

                                Log.d(
                                    "RuleArraySize",
                                    "${rulearray.size}   $rulearray            $it"
                                )


//                                binding.spinnerInclude.textView.text = it[spinCount].RuleName


                                binding.spinnerInclude.spinnerframeLayout.setTransition(
                                    R.id.start,
                                    R.id.spintop
                                )
                                binding.spinnerInclude.spinnerframeLayout.setTransitionDuration(600)
                                binding.spinnerInclude.spinnerframeLayout.transitionToEnd()
//            binding.spinnerInclude.spinnerframeLayout.


                                binding.spinnerInclude.spinnerframeLayout.setTransitionListener(
                                    object :
                                        MotionLayout.TransitionListener {

                                        override fun onTransitionStarted(
                                            motionLayout: MotionLayout?,
                                            startId: Int,
                                            endId: Int
                                        ) {
                                            Log.d(
                                                "SpinnerTriggerStarted",
                                                "$startId  $endId $spinCount $randomNumber"
                                            )

                                            if (spinCount == randomNumber -1) {
                                                motionLayout!!.isEnabled = false
                                            }
//                    randomNumber =
                                        }


                                        override fun onTransitionChange(
                                            motionLayout: MotionLayout?,
                                            startId: Int,
                                            endId: Int,
                                            progress: Float
                                        ) {
//                    Log.d("SpinnerProgress","$startId $endId $progress")
                                        }


                                        override fun onTransitionCompleted(
                                            motionLayout: MotionLayout?,
                                            currentId: Int
                                        ) {


                                            if (spinCount == randomNumber) {


                                                Log.d("Spinner", "$spinCount")
//                            spinCount = 0
//                            binding.spinnerInclude.spinnerframeLayout.endState


                                                if (currentId == R.id.spinbottom) {
                                                    Log.d("SpinnerTriggerEnd", "$currentId")
                                                    binding.spinnerInclude.spinnerframeLayout.setTransition(
                                                        R.id.spinbottom,
                                                        R.id.finshSpin
                                                    )
                                                    binding.spinnerInclude.spinnerframeLayout.setTransitionDuration(
                                                        500
                                                    )
                                                    binding.spinnerInclude.spinnerframeLayout.transitionToEnd()

//                                binding.spinnerInclude.spinnerframeLayout.removeTransitionListener(this)
                                                }


                                                if (currentId == R.id.finshSpin) {
                                                    Log.d("SpinnerFinish", "$currentId")

                                                    binding.spinnerInclude.spinnerframeLayout.removeTransitionListener(
                                                        this
                                                    )
                                                }


                                            } else {

                                                if (currentId == R.id.spinbottom) {

                                                    Log.d("SpinnerTriggerBottom", "$currentId")
                                                    binding.spinnerInclude.spinnerframeLayout.setTransition(
                                                        R.id.spinbottom,
                                                        R.id.spintop
                                                    )
                                                    binding.spinnerInclude.spinnerframeLayout.setTransitionDuration(
                                                        1000
                                                    )
                                                    binding.spinnerInclude.spinnerframeLayout.transitionToEnd()

                                                }

                                                if (currentId == R.id.spintop) {
                                                    spinCount++
                                                    if (it.size > spinCount){
                                                        binding.spinnerInclude.textView.text = it[spinCount].RuleName
                                                    }
                                                    Log.d("SpinnerTriggerTop", "$currentId")
                                                    binding.spinnerInclude.spinnerframeLayout.setTransition(
                                                        R.id.spintop,
                                                        R.id.spinbottom
                                                    )
                                                    binding.spinnerInclude.spinnerframeLayout.setTransitionDuration(
                                                        300
                                                    )
                                                    binding.spinnerInclude.spinnerframeLayout.transitionToEnd()
                                                }

//                            if (currentId == R.id.start) {
//                                spinCount++
//                                Log.d("SpinnerTriggerStart", "$currentId")
//                                binding.spinnerInclude.spinnerframeLayout.setTransition(
//                                    R.id.start,
//                                    R.id.spintop
//                                )
//                                binding.spinnerInclude.spinnerframeLayout.transitionToEnd()
//                            }
                                            }


                                        }

                                        override fun onTransitionTrigger(
                                            motionLayout: MotionLayout?,
                                            triggerId: Int,
                                            positive: Boolean,
                                            progress: Float
                                        ) {
                                            Log.d("SpinnerTrigger", "$triggerId")
                                        }

                                    })

                            }
                        }

            }
        }
            }
        }













        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onStart() {
        ruleViewmodel.getRuleAmount()
        super.onStart()

    }
}