package Izaac.Doyle.PubsApp.Firebase

import Izaac.Doyle.PubsApp.Models.AccountModel
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.activities.MainActivity
import android.view.View
import android.widget.TextView
import com.google.android.material.navigation.NavigationView



// Firebase Update With Content with Account Info,
fun Update(accountModel: AccountModel){

//    val navView = MainActivity().findViewById<NavigationView>(R.id.nav_view)
//    val navHeaderView = navView.getHeaderView(0)

// val    navHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_main)
// val    tvHeaderName = navHeaderView.findViewById(R.id.tvHeaderName) as TextView
//    tvHeaderName.setText("Saly")

   // val navHeaderView = NavigationView.getHea


    // navController.navigate(R.id.nav_home)

    val navigationView: NavigationView = MainActivity().findViewById(R.id.nav_view)
    val navHeaderView = navigationView.getHeaderView(0)

    val  UserEmailTxt = navHeaderView.findViewById<TextView>(R.id.Drawer_Email)
    val UsernameTxt = navHeaderView.findViewById<TextView>(R.id.Drawer_Name)

    UserEmailTxt.text = accountModel.email
    UsernameTxt.text = accountModel.username





}