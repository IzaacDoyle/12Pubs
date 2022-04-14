package Izaac.Doyle.PubsApp.ui.home

import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser
import Izaac.Doyle.PubsApp.Models.*

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.data.model.User
import com.google.firebase.firestore.*
import kotlin.collections.ArrayList
import kotlin.coroutines.coroutineContext

class GroupViewModel : ViewModel() {
    private lateinit var db: FirebaseFirestore
    private lateinit var CUuid:String
    private var GroupNames: MutableLiveData<ArrayList<GroupModel>> = MutableLiveData<ArrayList<GroupModel>>()
    private var Username: MutableLiveData<ArrayList<FBAccountNameModel>> = MutableLiveData<ArrayList<FBAccountNameModel>>()
    private var QrcodeSearch:  MutableLiveData<MutableList<FBAccountNameModel>> = MutableLiveData(ArrayList<FBAccountNameModel>())
    private var GooglePlaces: MutableLiveData<MutableList<GooglePlacesModel>> = MutableLiveData(ArrayList<GooglePlacesModel>())
    private var Rules:MutableLiveData<MutableList<RulesModel>> = MutableLiveData(ArrayList<RulesModel>())

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



     public fun SearchAddusersToGroup(SearchName: String?){
         if (SearchName.isNullOrEmpty()){
             Username.value!!.clear()
             Log.d("SearchName","Search Is Empty")
             UsersGroupname.value!!.clear()
         }else{


         Log.d("SearchUser", SearchName.lowercase())
             val username = ArrayList<FBAccountNameModel>()
        db.collection("UserProfiles").orderBy("Username")
            .startAt(SearchName)
            .endAt("$SearchName\uf8ff")
            .limit(5)
            .get()
            .addOnSuccessListener { snapshot ->
                Log.d("Searchuser Document", "${snapshot.documents}")

                if (snapshot != null) {
                    Log.d("SearchuserInside", "${snapshot.documents}")
                    val document = snapshot.documents
                    document.forEach {
                        val groupuser = it.toObject(FBAccountNameModel::class.java)
                        if (groupuser != null) {
                            Log.d("SearchUsername", "${groupuser.UserEmail + groupuser.Username}")
                            username.add(groupuser)
                        }

                    }
                    Log.d("Search", username.toString())
                    Username.value = username
                }
            }
            }
    }

    fun QrCodeScanSearch(QRCode: String) {
        val profile = mutableListOf<FBAccountNameModel>()
        db.collection("UserProfiles")
            .orderBy("UserUUID")
            .startAt(QRCode)
            .endAt("$QRCode\uf8ff")
            .limit(5)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    Log.d("QRSearchProfileAll", "${snapshot.documents}")
                    val document = snapshot.documents
                    document.forEach {
                        val groupUser = it.toObject(FBAccountNameModel::class.java)
                        Log.d("QrUser", groupUser.toString())
                        if (groupUser != null) {
                            Log.d(
                                "QrSearchProfile",
                                groupUser.UserEmail + " " + groupUser.Username + " " + groupUser.UserUUID
                            )
//                            profile.value!!.add(groupUser)
                            profile.add(groupUser)

//                            profile.value!!.add(groupUser)
//                            groupUser = profile.toList()
                        }

                    }
                    QrcodeSearch.value = profile
                    qrcodeSearch.notifyObserver()
//                    QrcodeSearch.value?.add(profile)
//                    QrcodeSearch.notifyObserver()
//                    qrcodeSearch.notifyObserver()
                }
            }
    }   

    fun PubsOverseve(GroupCode:GroupViewModel){
        val pubs = mutableListOf<GooglePlacesModel>()
      //  db.collection("Groups")

    }

     fun Rules(rule: ArrayList<Int>){
        val rules = ArrayList<RulesModel>()
         Log.d("RulesList",rule.toString())
        for (i in rule) {
            println("Rules $i")
            db.collection("PubRules").document(i.toString())
                .get()
                .addOnSuccessListener { it ->
                    println("Rules On Success $it")
                    if (it.exists()) {
                        val grouprule = it.toObject(RulesModel::class.java)
                        println(grouprule)
//                        rules.add(grouprule!!)
                        if (grouprule != null) {
                            rules.add(grouprule)
                        }
                    }
                    if (rules.size >= 14){
                        Rules.value = rules
                    }

                }

        }

    }




    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }

//    Log.d("QR Searchuser Document", "${snapshot.documents}")
//    Log.d("QR Search", profile.toString())


    internal var groupRule:MutableLiveData<MutableList<RulesModel>>
        get() {return Rules}
        set(value) {Rules = value}



    internal  var qrcodeSearch:MutableLiveData<MutableList<FBAccountNameModel>>
        get() { return QrcodeSearch}
        set(value) {QrcodeSearch =  value}


    internal var gNames: MutableLiveData<ArrayList<GroupModel>>
        get() { return GroupNames }
        set(value) { GroupNames = value }

    internal var UsersGroupname: MutableLiveData<ArrayList<FBAccountNameModel>>
        get(){ return Username
        } set(value){ Username = value}
}