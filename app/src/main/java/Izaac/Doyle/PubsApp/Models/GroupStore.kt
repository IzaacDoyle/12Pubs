package Izaac.Doyle.PubsApp.Models

import Izaac.Doyle.PubsApp.ui.home.GroupViewModel
import android.app.Activity

interface GroupStore {
    fun CreateGroup(Group:GroupModel,UUID:String,UserName:String)
    fun LeaveGroup(Group: GroupModel,activity: Activity,groupViewModel: GroupViewModel)
    fun UpdateGroup(Group: GroupModel)
}