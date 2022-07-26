package Izaac.Doyle.PubsApp.Firebase

import Izaac.Doyle.PubsApp.Models.GroupModel
import Izaac.Doyle.PubsApp.Models.GroupStore
import Izaac.Doyle.PubsApp.ui.home.GroupViewModel
import android.app.Activity
import com.google.firebase.auth.FirebaseUser

object GroupRealTimeDB:GroupStore {
    override fun CreateGroup(Group: GroupModel, UUID: String, UserName: String) {
        TODO("Not yet implemented")
    }

    override fun LeaveGroup(Group: GroupModel, activity: Activity, groupViewModel: GroupViewModel,user: FirebaseUser) {
        TODO("Not yet implemented")
    }

    override fun UpdateGroup(Group: GroupModel) {
        TODO("Not yet implemented")
    }
}