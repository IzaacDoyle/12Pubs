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


interface PubsClickListener{
    fun onPubsClicked(pubs: GooglePlacesModel)
}


class PubsRecycelerView constructor(private val pubs:ArrayList<GooglePlacesModel>, private val listener: PubsClickListener): RecyclerView.Adapter<PubsRecycelerView.ViewHolder>(){





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



    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView.rootView){
        fun bindAccounts(pubs: GooglePlacesModel, listener: PubsClickListener){

            itemView.tag = pubs

            Log.d("RecyclerView",pubs.toString())
            val position = layoutPosition +1
            itemView.findViewById<TextView>(R.id.places_number_selected).text = position.toString()
            itemView.findViewById<TextView>(R.id.places_location_name).text = pubs.Name
            itemView.findViewById<TextView>(R.id.places_location_address).text = pubs.Address




            itemView.setOnClickListener {
                listener.onPubsClicked(pubs)
                itemView.isSelected = true

                //itemView.setBackgroundColor(R.color.white)


            }


//            itemView.findViewById<TextView>(R.id.username).text = pubs.Name.toString()
//            itemView.findViewById<TextView>(R.id.email).text = pubs.Address.toString()


//            itemView.findViewById<TextView>(R.id.username).text = accounts.Username.substring(0,1).uppercase()+accounts.Username.substring(1)
//            itemView.findViewById<TextView>(R.id.email).text = accounts.UserEmail

        }



    }


    override fun onBindViewHolder(holder: PubsRecycelerView.ViewHolder, position: Int) {
        holder.bindAccounts(pubs[position],listener)


    }

    override fun getItemCount(): Int {
        return pubs.size
    }

}