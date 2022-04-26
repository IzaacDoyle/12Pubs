package Izaac.Doyle.PubsApp.Models

interface GroupStore {
    fun CreateGroup(Group:GroupModel,UUID:String,UserName:String)
    fun LeaveGroup(Group: GroupModel)
    fun UpdateGroup(Group: GroupModel)
}