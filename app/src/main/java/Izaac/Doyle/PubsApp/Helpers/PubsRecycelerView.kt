package Izaac.Doyle.PubsApp.Helpers


import Izaac.Doyle.PubsApp.Models.GooglePlacesModel
import Izaac.Doyle.PubsApp.R
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class PubsRecycelerView(private val pubs:ArrayList<GooglePlacesModel>): RecyclerView.Adapter<PubsRecycelerView.ViewHolder>(){



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):ViewHolder {
        val v  = LayoutInflater.from(parent.context).inflate(
            R.layout.cardview_googleplaces,parent,false
        )
        return ViewHolder(v)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun deleteItem(pos: Int){
        pubs.removeAt(pos)
        notifyItemRemoved(pos)
        notifyDataSetChanged()
    }

    fun saveLocation(pos: Int,pubs: GooglePlacesModel){

    }



    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindAccounts(pubs: GooglePlacesModel){
            Log.d("RecyclerView",pubs.toString())
            val position = layoutPosition +1
            itemView.findViewById<TextView>(R.id.places_number_selected).text = position.toString()
            itemView.findViewById<TextView>(R.id.places_location_name).text = pubs.Name
            itemView.findViewById<TextView>(R.id.places_location_address).text = pubs.Address


//            itemView.findViewById<TextView>(R.id.username).text = pubs.Name.toString()
//            itemView.findViewById<TextView>(R.id.email).text = pubs.Address.toString()


//            itemView.findViewById<TextView>(R.id.username).text = accounts.Username.substring(0,1).uppercase()+accounts.Username.substring(1)
//            itemView.findViewById<TextView>(R.id.email).text = accounts.UserEmail
        }

        init {



            itemView.setOnClickListener {
                    v: View ->
                val position = adapterPosition
                //add user to textview to add
<<<<<<< HEAD
                Log.d("recylerView","$position")
            }   
=======
                Log.d("recylerView","$position" + itemView as GooglePlacesModel)

            }
>>>>>>> 12Pubs_CameraScanner



        }

    }

    override fun onBindViewHolder(holder: PubsRecycelerView.ViewHolder, position: Int) {
        holder.bindAccounts(pubs[position])
    }

    override fun getItemCount(): Int {

        return pubs.size
    }

}