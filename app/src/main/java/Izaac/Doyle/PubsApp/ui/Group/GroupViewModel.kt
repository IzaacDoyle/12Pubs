package Izaac.Doyle.PubsApp.ui.home

import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser

import Izaac.Doyle.PubsApp.Main.MainApp
import Izaac.Doyle.PubsApp.Models.GroupModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class GroupViewModel : ViewModel() {
    private lateinit var db: FirebaseFirestore
    private var GroupName: MutableLiveData<ArrayList<GroupModel>> =
        MutableLiveData<ArrayList<GroupModel>>()


    init {
        db = FirebaseFirestore.getInstance()
        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        getGroupName()
    }

    override fun onCleared() {
        super.onCleared()
    }

    private fun getGroupName() {
        db.collection("Group")
            .addSnapshotListener { value, error ->


                if (error != null) {
                    Log.d("GVM getGName", "failed", error)
                    return@addSnapshotListener
                }

                val groupNames = ArrayList<GroupModel>()

                if (value != null) {

                    val documents = value.documents
                    documents.forEach{
                        val groupN = it.toObject(GroupModel::class.java)
                        if (groupN != null){
                            if (groupN.GroupOwner == CheckCurrentUser()!!.uid){
                                groupNames.add(groupN)
                                Log.d("groupNames",groupN.toString())
                            }
                        }
                    }




//                    val document = value.toObject(GroupModel::class.java)
//                    groupNames.add(document!!)
//                val document = value
//                document.forEach{
//                    val groupName = it.toObject(GroupModel::class.java)
//                    if (groupName != null){
//                        groupNames.add(groupName)
//                    }
                }
                GroupName.value = groupNames
            }

    }


    internal var groupname: MutableLiveData<ArrayList<GroupModel>>
        get() {
            return GroupName
        }
        set(value) {
            GroupName = value


        }
}