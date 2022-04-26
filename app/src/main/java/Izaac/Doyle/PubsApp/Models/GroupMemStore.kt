package Izaac.Doyle.PubsApp.Models

import Izaac.Doyle.PubsApp.Firebase.FBCreateGroup

class GroupMemStore: GroupStore {
    override fun CreateGroup(Group: GroupModel,UUID:String,UserName:String) {
        FBCreateGroup(Group,UserName,UUID)
    }

    override fun LeaveGroup(Group: GroupModel) {
    }

    override fun UpdateGroup(Group: GroupModel) {
    }
}