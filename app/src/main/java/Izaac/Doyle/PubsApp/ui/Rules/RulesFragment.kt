package Izaac.Doyle.PubsApp.ui.Rules


import Izaac.Doyle.PubsApp.Firebase.random
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.databinding.FragmentRulesBinding
import Izaac.Doyle.PubsApp.databinding.RulesSpinnerBinding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import kotlin.random.Random


class RulesFragment:Fragment() {

    private var _binding: FragmentRulesBinding? = null



    private val binding get() = _binding!!







    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRulesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val randomNumber :Int = (6..10).random()












        binding.spinnerTrigger.setOnClickListener {
            var spinCount:Int = 0
            binding.spinnerInclude.spinnerframeLayout.setTransition(R.id.start, R.id.spintop)
            binding.spinnerInclude.spinnerframeLayout.setTransitionDuration(500)
            binding.spinnerInclude.spinnerframeLayout.transitionToEnd()
//            binding.spinnerInclude.spinnerframeLayout.


                binding.spinnerInclude.spinnerframeLayout.setTransitionListener(object :
                    MotionLayout.TransitionListener {

                    override fun onTransitionStarted(
                        motionLayout: MotionLayout?,
                        startId: Int,
                        endId: Int
                    ) {
                        Log.d("SpinnerTriggerStarted", "$startId  $endId $spinCount $randomNumber")
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





                        if (spinCount == randomNumber){

                            Log.d("Spinner","$spinCount")
//                            spinCount = 0
//                            binding.spinnerInclude.spinnerframeLayout.endState

                            if (currentId == R.id.spinbottom) {
                                Log.d("SpinnerTriggerEnd", "$currentId")
                                binding.spinnerInclude.spinnerframeLayout.setTransition(
                                    R.id.spinbottom,
                                    R.id.end
                                )
                                binding.spinnerInclude.spinnerframeLayout.setTransitionDuration(500)
                                binding.spinnerInclude.spinnerframeLayout.transitionToEnd()
                            }

                            binding.spinnerInclude.spinnerframeLayout.removeTransitionListener(this)


                        }else {

                            if (currentId == R.id.spinbottom) {
                                Log.d("SpinnerTriggerBottom", "$currentId")
                                binding.spinnerInclude.spinnerframeLayout.setTransition(
                                    R.id.spinbottom,
                                    R.id.spintop
                                )
                                binding.spinnerInclude.spinnerframeLayout.setTransitionDuration(500)
                                binding.spinnerInclude.spinnerframeLayout.transitionToEnd()

                            }

                            if (currentId == R.id.spintop) {
                                spinCount++
                                Log.d("SpinnerTriggerTop", "$currentId")
                                binding.spinnerInclude.spinnerframeLayout.setTransition(
                                    R.id.spintop,
                                    R.id.spinbottom
                                )
                                binding.spinnerInclude.spinnerframeLayout.setTransitionDuration(500)
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













        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}