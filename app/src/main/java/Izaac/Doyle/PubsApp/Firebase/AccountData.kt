package Izaac.Doyle.PubsApp.Firebase

import Izaac.Doyle.PubsApp.Models.FBAccountNameModel
import Izaac.Doyle.PubsApp.Models.FBAccountStore
import Izaac.Doyle.PubsApp.Models.GooglePlacesModel
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object AccountData:FBAccountStore {
    override fun getAccounnt(uuid: String,account: MutableLiveData<List<FBAccountNameModel>>) {
        val db = Firebase.firestore
        val localList = ArrayList<FBAccountNameModel>()
        db.collection("UserProfiles").document(uuid).get()
            .addOnSuccessListener { accounts->
                Log.d("AccountDataAccount",accounts.toString())
                if (accounts != null){
                    val profile = accounts.toObject(FBAccountNameModel::class.java)
                    if (profile !=null){
                        localList.add(profile)
                        Log.d("AccountData",localList.toString())
                    }
                }
                account.value = localList
            }

    }


}