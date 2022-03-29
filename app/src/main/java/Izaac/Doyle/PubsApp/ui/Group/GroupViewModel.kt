package Izaac.Doyle.PubsApp.ui.home

import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser
import Izaac.Doyle.PubsApp.Models.AccountModel
import Izaac.Doyle.PubsApp.Models.FBAccountNameModel

import Izaac.Doyle.PubsApp.Models.GroupModel
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.*
import kotlin.collections.ArrayList
import kotlin.coroutines.coroutineContext

class GroupViewModel : ViewModel() {
    private lateinit var db: FirebaseFirestore
    private lateinit var CUuid:String
    private var GroupNames: MutableLiveData<ArrayList<GroupModel>> = MutableLiveData<ArrayList<GroupModel>>()
    private var Username: MutableLiveData<ArrayList<FBAccountNameModel>> = MutableLiveData<ArrayList<FBAccountNameModel>>()

    init {
        db = FirebaseFirestore.getInstance()
        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        CUuid = CheckCurrentUser()!!.uid
        getUserGroup()
       // getGroupName()

    }
    override fun onCleared() {
        super.onCleared()
    }

 private  fun getUserGroup(){

         db.collection("Groups").document(CUuid)
             .addSnapshotListener { snapshot, error ->
                 Log.d("GVM snapshot", "$snapshot")
                 if (error != null) {
                     Log.d("GVM getGName", "failed", error)
                     return@addSnapshotListener
                 }
                 var usergroup = ArrayList<GroupModel>()
                 if (snapshot != null && snapshot.exists()) {
                     val data = snapshot.toObject(GroupModel::class.java)
                     Log.d("GVM", "$data")
                     // usergroup = data!!
                     usergroup.add(data!!)
                 }
                 //usergroupname = usergroup
                 GroupNames.value = usergroup
                // Log.d("GVM final", "$usergroupname")
             }
     }

    //search someting to get marks



     fun SearchAddusersToGroup(SearchName:String){
         Log.d("SearchUser", SearchName.lowercase())
        db.collection("UserProfiles").orderBy("Username")
            .startAt(SearchName)
            .endAt("$SearchName\uf8ff")
            .limit(10)
            .get()
            .addOnSuccessListener { snapshot ->
                Log.d("Searchuser Document","${snapshot.documents}")
                val username = ArrayList<FBAccountNameModel>()
                if (snapshot != null) {
                    Log.d("SearchuserInside","${snapshot.documents}")
                    val document = snapshot.documents
                    document.forEach {
                        val groupuser = it.toObject(FBAccountNameModel::class.java)
                        if(groupuser != null){
                            Log.d("SearchUsername","$groupuser")
                            username.add(groupuser!!)
                        }

                    }
                    Username.value = username
                }
            }
    }





    internal var gNames: MutableLiveData<ArrayList<GroupModel>>
        get() { return GroupNames }
        set(value) { GroupNames = value }

    internal var UsersGroupname: MutableLiveData<ArrayList<FBAccountNameModel>>
        get(){return Username}
        set(value){Username = value}
}