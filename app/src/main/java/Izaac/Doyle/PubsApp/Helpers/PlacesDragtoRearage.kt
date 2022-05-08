package Izaac.Doyle.PubsApp.Helpers

import Izaac.Doyle.PubsApp.Models.GooglePlacesModel
import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class PlacesDragtoRearage(var adapter: PubsRecycelerView,var context: Context,var pubs:ArrayList<GooglePlacesModel>):ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN,0) {



        @SuppressLint("NotifyDataSetChanged")
        override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {

        val startPosition = viewHolder.adapterPosition
        val endPosition = target.adapterPosition

        Collections.swap(pubs,startPosition,endPosition)
            adapter.notifyItemMoved(startPosition,endPosition)
            adapter.notifyDataSetChanged()
            return false
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }
}