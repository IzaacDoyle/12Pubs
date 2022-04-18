package Izaac.Doyle.PubsApp.Helpers

import Izaac.Doyle.PubsApp.Models.AccountModel
import Izaac.Doyle.PubsApp.Models.FBAccountNameModel
import Izaac.Doyle.PubsApp.Models.RulesModel
import Izaac.Doyle.PubsApp.R
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.common.io.Resources

interface  ProfileClickListener{
    fun onProfileClicked(accounts: FBAccountNameModel,itemView: View)
}

class UserSearchRecyclerview(private val accounts: ArrayList<FBAccountNameModel>, private val listener: ProfileClickListener, val context :Context):RecyclerView.Adapter<UserSearchRecyclerview.ViewHolder>(){



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
        fun bindAccounts(accounts:FBAccountNameModel, listener: ProfileClickListener){
            Log.d("RecyclerView",accounts.toString())
            itemView.findViewById<TextView>(R.id.username).text = accounts.Username.substring(0,1).uppercase()+accounts.Username.substring(1)
            itemView.findViewById<TextView>(R.id.email).text = accounts.UserEmail

            itemView.setOnClickListener {
                listener.onProfileClicked(accounts,itemView)
                itemView.isSelected


            }


        }


    }

    override fun onBindViewHolder(holder: UserSearchRecyclerview.ViewHolder, position: Int) {
        holder.bindAccounts(accounts[position],listener)

    }

    override fun getItemCount(): Int {
        return accounts.size
    }

}