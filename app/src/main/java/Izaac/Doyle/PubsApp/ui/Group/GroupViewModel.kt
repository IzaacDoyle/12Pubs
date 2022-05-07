package Izaac.Doyle.PubsApp.ui.home

import Izaac.Doyle.PubsApp.Firebase.CheckCurrentUser
import Izaac.Doyle.PubsApp.Models.*
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GroupViewModel : ViewModel() {
    private lateinit var db: FirebaseFirestore
    private lateinit var CUuid:String
    var GroupNames: MutableLiveData<ArrayList<GroupModel>> = MutableLiveData<ArrayList<GroupModel>>()
    private var Username: MutableLiveData<ArrayList<FBAccountNameModel>> = MutableLiveData<ArrayList<FBAccountNameModel>>()
    private var QrcodeSearch:  MutableLiveData<MutableList<FBAccountNameModel>> = MutableLiveData(ArrayList<FBAccountNameModel>())
    private var GooglePlaces: MutableLiveData<MutableList<GooglePlacesModel>> = MutableLiveData(ArrayList<GooglePlacesModel>())
    var Rules:MutableLiveData<MutableList<RulesModel>> = MutableLiveData(ArrayList<RulesModel>())
    var Invitations:MutableLiveData<MutableList<InvitationsModel>> = MutableLiveData(ArrayList<InvitationsModel>())
    private var GroupUsersList:MutableLiveData<MutableList<GroupUserModel>> = MutableLiveData(ArrayList<GroupUserModel>())
    private var placesList: MutableLiveData<MutableList<GooglePlacesModel>> = MutableLiveData(ArrayList<GooglePlacesModel>())
    private var GroupsPlaces: MutableLiveData<MutableList<GooglePlacesModel>> = MutableLiveData(ArrayList<GooglePlacesModel>())

    init {
        db = FirebaseFirestore.getInstance()
        db.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        CUuid = CheckCurrentUser()!!.uid
        getUserGroup(CUuid)
        CheckInvitations(CheckCurrentUser()!!.uid)
        QrCodeScanSearch(CheckCurrentUser()!!.uid)

       // getGroupName()

    }
    override fun onCleared() {
        super.onCleared()
    }

    fun Update(){
        getUserGroup(CUuid)
        CheckInvitations(CUuid)
        QrCodeScanSearch(CUuid)
    }

   fun getUserGroup(UUID: String){
       println(UUID)
         db.collection("Groups").document(UUID)
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
                 Log.d("GVM final", "${GroupNames.value}")
             }

     }

    fun CheckGroupAdmin(UUID: String){
        val db = Firebase.firestore
        val GroupUsers = ArrayList<GroupUserModel>()

        db.collection("Groups").document(UUID).collection("Users").document(CheckCurrentUser()!!.uid).get()
            .addOnSuccessListener {it->
                if (it != null){
                    val groupusers = it.toObject(GroupUserModel::class.java)
                    if (groupusers != null){
                        Log.d("CheckAdmin","$UUID Is Admin $it")
                        GroupUsers.add(groupusers)
                    }
                }
                GroupUsersList.value = GroupUsers
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
                            Log.d("QrSearchProfile", groupUser.UserEmail + " " + groupUser.Username + " " + groupUser.UserUUID + " " + groupUser.Group)
                            profile.add(groupUser)

                        }
                    }
                    QrcodeSearch.value = profile
                }
            }

    }   

    fun PubsOverseve(GroupCode:GroupViewModel){
        val pubs = mutableListOf<GooglePlacesModel>()
//      db.collection("Groups")
    }


    fun getUsersPubsList(){
        val db = Firebase.firestore
        val placeList= ArrayList<GooglePlacesModel>()

        db.collection("UserProfiles").document(CUuid).collection("Places").get()
            .addOnSuccessListener {places->
                if (places != null){
                    if (!places.isEmpty){
                        val document = places.documents
                        document.forEach {
                            Log.d("Places", it.toString())
                            val place = it.toObject(GooglePlacesModel::class.java)
                            if (place != null) {
                                placeList.add(place)
                            }
                        }
                    }
                }
                placesList.value = placeList
            }

    }

    fun getGroupPlaces(GroupOwner:String){
        val db = Firebase.firestore
        val placeList= ArrayList<GooglePlacesModel>()

        db.collection("Groups").document(GroupOwner).collection("Pubs").get()
            .addOnSuccessListener {places->
                if (places != null){
                    if (!places.isEmpty){
                        val document = places.documents
                        document.forEach {
                            Log.d("Places", it.toString())
                            val place = it.toObject(GooglePlacesModel::class.java)
                            if (place != null) {
                                placeList.add(place)
                            }
                        }
                    }
                }
                GroupsPlaces.value = placeList
            }

    }


     fun Rules(rule: ArrayList<Int>){
         Log.d("rulesList","TestRules" + rule.toString())
        val rules = mutableListOf<RulesModel>()
        for (i in rule) {
            println("Rules $i")
            db.collection("PubRules").document(i.toString())
                .get()
                .addOnSuccessListener { result ->
                    println("Rules On Success $result")
                    if (result.exists()) {
                        val grouprule = result.toObject(RulesModel::class.java)
                        println(grouprule)
//                        rules.add(grouprule!!)
                        if (grouprule != null) {
                            rules.add(grouprule)
                        }
                    }
                    if (rules.size == 14) {
                        //need to filter list to be in order of rule<Int>
                        Rules.value = rules

                    }
                }
        }
    }

    fun CheckInvitations(UUID:String){
        val db = Firebase.firestore

        val invos = mutableListOf<InvitationsModel>()
        db.collection("PendingInvitation").document(UUID).get()
            .addOnSuccessListener { it->
                if (it.exists() && it != null){
                    val invo = it.toObject(InvitationsModel::class.java)
                    println(invo)
                    invos.add(invo!!)
                }
                Invitations.value = invos
            }

    }

    internal var groupPlaces :MutableLiveData<MutableList<GooglePlacesModel>>
        get(){return GroupsPlaces}
        set(value){GroupsPlaces = value}

    internal var Places :MutableLiveData<MutableList<GooglePlacesModel>>
        get(){return placesList}
        set(value){placesList = value}


    internal var GUsers:MutableLiveData<MutableList<GroupUserModel>>
        get(){return GroupUsersList}
        set(value){GroupUsersList = value}



    internal var Invites:MutableLiveData<MutableList<InvitationsModel>>
        get() {return Invitations}
        set(value) {Invitations = value}


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