package Izaac.Doyle.PubsApp.Models

import Izaac.Doyle.PubsApp.Firebase.FBCreateGroup

class GroupMemStore: GroupStore {
    override fun CreateGroup(Group: GroupModel) {
        FBCreateGroup(Group)
    }

    override fun LeaveGroup(Group: GroupModel) {
    }

    override fun UpdateGroup(Group: GroupModel) {
    }
}