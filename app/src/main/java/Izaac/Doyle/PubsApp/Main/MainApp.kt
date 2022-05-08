package Izaac.Doyle.PubsApp.Main

import Izaac.Doyle.PubsApp.Models.AccountMemStore
import Izaac.Doyle.PubsApp.Models.AccountStore
import Izaac.Doyle.PubsApp.Models.GroupMemStore
import Izaac.Doyle.PubsApp.Models.GroupStore
import android.app.Application

class MainApp: Application() {

    lateinit var account: AccountStore
     lateinit var group: GroupStore

    override fun onCreate() {
        super.onCreate()

        account = AccountMemStore()
        group = GroupMemStore()


    }
}