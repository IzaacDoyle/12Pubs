package Izaac.Doyle.PubsApp.Helpers

import Izaac.Doyle.PubsApp.Models.GooglePlacesModel
import Izaac.Doyle.PubsApp.Models.RulesModel
import Izaac.Doyle.PubsApp.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import java.util.*


interface  RulesClickListener{
    fun onRulesClicked(rules: RulesModel)
}

class GroupRulesRecycleView(private val rule: MutableList<RulesModel>?, private val listener: RulesClickListener): RecyclerView.Adapter<GroupRulesRecycleView.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):ViewHolder {
        val v  = LayoutInflater.from(parent.context).inflate(
            R.layout.rule_cardview,parent,false
        )

        return ViewHolder(v)
    }



        class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            fun bindAccounts(grourule: RulesModel, listener: RulesClickListener){
//                for (i in 0..14){
                    itemView.findViewById<TextView>(R.id.group_rulename).text = grourule.RuleName
                    itemView.findViewById<TextView>(R.id.group_number).text = grourule.RuleID

                    itemView.setOnClickListener {
                        listener.onRulesClicked(grourule)
                        itemView.isSelected = true

                        //itemView.setBackgroundColor(R.color.white)

                    }
//                }




            }

    }


    override fun onBindViewHolder(holder: GroupRulesRecycleView.ViewHolder, position: Int) {
            holder.bindAccounts(rule?.get(position)!!,listener)
    }

    override fun getItemCount(): Int {
        return rule?.size ?:0
    }


}