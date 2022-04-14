package Izaac.Doyle.PubsApp.Helpers

import Izaac.Doyle.PubsApp.Models.AccountModel
import Izaac.Doyle.PubsApp.Models.FBAccountNameModel
import Izaac.Doyle.PubsApp.R
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class UserSearchRecyclerview(private val accounts: ArrayList<FBAccountNameModel>):RecyclerView.Adapter<UserSearchRecyclerview.ViewHolder>(){



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):ViewHolder {
       val v  = LayoutInflater.from(parent.context).inflate(
           R.layout.account_serach_cardview,parent,false
       )
        return ViewHolder(v)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindAccounts(accounts:FBAccountNameModel){
            Log.d("RecyclerView",accounts.toString())
            itemView.findViewById<TextView>(R.id.username).text = accounts.Username.substring(0,1).uppercase()+accounts.Username.substring(1)
            itemView.findViewById<TextView>(R.id.email).text = accounts.UserEmail
        }

init {
    itemView.setOnClickListener {
        v:View ->
        val position = adapterPosition
        //add user to textview to add
        Log.d("recylerView","$position")
    }
}

    }

    override fun onBindViewHolder(holder: UserSearchRecyclerview.ViewHolder, position: Int) {
        holder.bindAccounts(accounts[position])
    }

    override fun getItemCount(): Int {
        return accounts.size
    }

}