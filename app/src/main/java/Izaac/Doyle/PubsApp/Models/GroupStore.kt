package Izaac.Doyle.PubsApp.Models

interface GroupStore {
    fun CreateGroup(Group:GroupModel)
    fun LeaveGroup(Group: GroupModel)
    fun UpdateGroup(Group: GroupModel)
}