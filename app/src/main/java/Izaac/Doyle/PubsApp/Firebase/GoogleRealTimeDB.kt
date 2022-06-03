package Izaac.Doyle.PubsApp.Firebase

import Izaac.Doyle.PubsApp.Models.GooglePlacesModel
import Izaac.Doyle.PubsApp.Models.GooglePlacesStore
import Izaac.Doyle.PubsApp.Models.GroupModel
import Izaac.Doyle.PubsApp.Models.GroupStore
import Izaac.Doyle.PubsApp.ui.home.GroupViewModel
import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*

object GoogleRealTimeDB :GooglePlacesStore{
    var database: DatabaseReference = FirebaseDatabase.getInstance().reference


    override fun AddLocation(GroupUUID: String, placesModel: ArrayList<GooglePlacesModel>) {
        Log.d("RealTime",placesModel.toString())
        val uid = CheckCurrentUser()!!.uid
        val key = database.child("Pubs").key

        for (i in placesModel){
            placesModel[i.describeContents()].PubsUID = key
            val placesValue = placesModel[i.describeContents()].toMap()

            val pubAdd = HashMap<String,Any?>()
            pubAdd["/Pubs/$GroupUUID/${placesModel[i.describeContents()].PubName}"] = placesValue

            database.updateChildren(pubAdd)
        }
    }

    override fun FindAll(GroupUUID: String, placesModel: MutableLiveData<List<GooglePlacesModel>>) {
        database.child("Pubs").child(GroupUUID).addValueEventListener(
            object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                  val localList = ArrayList<GooglePlacesModel>()
                    val children = snapshot.children
                    children.forEach{
                        val pub  = it.getValue(GooglePlacesModel::class.java)
                        localList.add(pub!!)
                    }
                    database.child("Pubs").child(GroupUUID).removeEventListener(this)
                    Log.d("FindAllPubs",localList.toString())

                    placesModel.value = localList
                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
        )
    }

    override fun removeLocation(
        GroupUUID: String,
        placesModel: ArrayList<GooglePlacesModel>
    ) {
        val Locationdelete: MutableMap<String, Any?> = HashMap()
        for (i in placesModel) {
            Locationdelete["/Pubs/$GroupUUID/${placesModel[i.describeContents()].PubName}"] =null

            database.updateChildren(Locationdelete)
        }
    }


}