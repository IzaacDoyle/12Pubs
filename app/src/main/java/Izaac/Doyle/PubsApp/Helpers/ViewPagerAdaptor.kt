package Izaac.Doyle.PubsApp.Helpers

import Izaac.Doyle.PubsApp.Models.RulesModel
import Izaac.Doyle.PubsApp.ui.Group.viewpager.PubsViewpager
import Izaac.Doyle.PubsApp.ui.Group.viewpager.RulesViewpager
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.gms.dynamic.SupportFragmentWrapper

 class ViewPagerAdaptor(supportFragmentManager:FragmentManager,lifecycle:Lifecycle):FragmentStateAdapter(supportFragmentManager,lifecycle) {

     private val mFraglist = ArrayList<Fragment>()
     private  var RulesList:MutableList<RulesModel>? =null



     fun addFragment(fragment: Fragment){
         mFraglist.add(fragment)
     }
     fun AddRules(Rules:MutableList<RulesModel>){
         Log.d("RulesAddViewPager",Rules.toString())
         RulesList = Rules
     }

     override fun getItemCount(): Int {
        return 2
     }

     override fun createFragment(position: Int): Fragment {
         return when(position) {
             0 -> {
                    PubsViewpager()
             }
             1->{

                 RulesViewpager()
             }
             else -> {
                 Fragment()
             }
         }

     }
 }