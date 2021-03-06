package Izaac.Doyle.PubsApp.Models

import Izaac.Doyle.PubsApp.Firebase.FBCreateGroup
import Izaac.Doyle.PubsApp.Firebase.Leavegroup
import Izaac.Doyle.PubsApp.ui.home.GroupViewModel
import android.app.Activity

class GroupMemStore: GroupStore {
    override fun CreateGroup(Group: GroupModel,UUID:String,UserName:String) {
        FBCreateGroup(Group,UserName,UUID)
    }

    override fun LeaveGroup(Group: GroupModel,activity: Activity,groupViewModel: GroupViewModel) {
//        println("Group Leave Button Clicked ${Group.OwnerUUID}")
        Leavegroup(Group,activity,groupViewModel)
    }

    override fun UpdateGroup(Group: GroupModel) {
    }
}