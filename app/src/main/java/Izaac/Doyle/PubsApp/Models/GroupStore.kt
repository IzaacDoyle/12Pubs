package Izaac.Doyle.PubsApp.Models

import Izaac.Doyle.PubsApp.ui.home.GroupViewModel
import android.app.Activity
import com.google.firebase.auth.FirebaseUser

interface GroupStore {
    fun CreateGroup(Group:GroupModel,UUID:String,UserName:String)
    fun LeaveGroup(Group: GroupModel,activity: Activity,groupViewModel: GroupViewModel,user: FirebaseUser)
    fun UpdateGroup(Group: GroupModel)
}