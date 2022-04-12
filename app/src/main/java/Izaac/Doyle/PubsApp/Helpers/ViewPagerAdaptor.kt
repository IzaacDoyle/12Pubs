package Izaac.Doyle.PubsApp.Helpers

import Izaac.Doyle.PubsApp.ui.Group.viewpager.PubsViewpager
import Izaac.Doyle.PubsApp.ui.Group.viewpager.RulesViewpager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.gms.dynamic.SupportFragmentWrapper

 class ViewPagerAdaptor(supportFragmentManager:FragmentManager,lifecycle:Lifecycle):FragmentStateAdapter(supportFragmentManager,lifecycle) {

     private val mFraglist = ArrayList<Fragment>()


     fun addFragment(fragment: Fragment){
         mFraglist.add(fragment)
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