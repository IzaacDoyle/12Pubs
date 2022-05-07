package Izaac.Doyle.PubsApp.Helpers



import Izaac.Doyle.PubsApp.Models.GooglePlacesModel
import Izaac.Doyle.PubsApp.Models.RulesModel
import Izaac.Doyle.PubsApp.R
import Izaac.Doyle.PubsApp.ui.Group.GroupsFragment
import Izaac.Doyle.PubsApp.ui.Group.OnAboutDataReceivedListener
import android.content.Context
import android.content.res.Resources
import android.graphics.Color

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.NotificationCompat.getColor
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI.getApplicationContext
import org.w3c.dom.Text
import java.util.*

interface  PlaceClickListener{
    fun onPlaceClicked(place: GooglePlacesModel,itemView:View,position: Int)
}


class LocationRecycleView(private val rule: MutableList<GooglePlacesModel>?, private val listener: OnAboutDataReceivedListener?,private val ClickListener:PlaceClickListener,var context: Context,var CardType:String): RecyclerView.Adapter<LocationRecycleView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):ViewHolder {
        var v:View? = null
        if (CardType == "SmallView"){
            v  = LayoutInflater.from(parent.context).inflate(R.layout.group_pubscardview,parent,false)
        }else if (CardType == "LargeView"){
             v  = LayoutInflater.from(parent.context).inflate(R.layout.places_cardview,parent,false)
        }


        return ViewHolder(v!!)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindAccounts(place: GooglePlacesModel, listener:OnAboutDataReceivedListener?,ClickListener: PlaceClickListener,context: Context,CardType: String){

            if (CardType == "SmallView"){
                itemView.findViewById<TextView>(R.id.groupVP_PubName).text = place.PubName
                itemView.findViewById<TextView>(R.id.groupVP_PubAddress).text = place.PubAddress

                itemView.setOnClickListener {
                    ClickListener.onPlaceClicked(place,itemView,adapterPosition)
                }

            }else{
                itemView.findViewById<TextView>(R.id.places_name).text = place.PubName
                itemView.findViewById<TextView>(R.id.places_PhoneNumber).text = place.PubPhoneNum?.trim()
                itemView.findViewById<TextView>(R.id.places_address).text = place.PubAddress
                itemView.findViewById<TextView>(R.id.places_hours).text = place.PubOpeningHours?.toString()


                itemView.setOnClickListener {
                    when(itemView.isSelected){
                        true->{
                            println("true")
                            itemView.isSelected = false
                            itemView.setBackgroundColor(context.getColor(R.color.Background_color))
                            ClickListener.onPlaceClicked(place,itemView,adapterPosition)

                        }
                        false->{
                            println("false")
                            itemView.isSelected = true
                            itemView.setBackgroundColor(Color.BLACK)
                            ClickListener.onPlaceClicked(place,itemView,adapterPosition)
                        }
                    }
                }
            }





        }

    }

    override fun onBindViewHolder(holder: LocationRecycleView.ViewHolder, position: Int) {
        holder.bindAccounts(rule?.get(position)!!,listener,ClickListener,context,CardType)
    }

    override fun getItemCount(): Int {
        return rule?.size ?:0
    }




}