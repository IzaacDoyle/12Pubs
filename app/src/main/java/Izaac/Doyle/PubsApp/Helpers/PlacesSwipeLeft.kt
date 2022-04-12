package Izaac.Doyle.PubsApp.Helpers

import Izaac.Doyle.PubsApp.R
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class PlacesSwipeLeft(var adapter: PubsRecycelerView, context:Context): ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT ){


    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete)

    private val LintrinsicWidth = deleteIcon?.intrinsicWidth
    private val LintrinsicHeight = deleteIcon?.intrinsicHeight
    private val Lbackground = ColorDrawable()
    private val LbackgroundColor = Color.parseColor("#f44336")




    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

//    private val intrinsicWidth = editIcon?.intrinsicWidth
//    private val intrinsicHeight = editIcon?.intrinsicHeight
//    private val background = ColorDrawable()
//    private val backgroundColor = Color.parseColor("#2196F3")
//    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        val pos = viewHolder.adapterPosition
        when(direction){
            ItemTouchHelper.LEFT->{
                adapter.deleteItem(pos)
            }
//            ItemTouchHelper.RIGHT->{
//                adapter.deleteItem(pos)
//            }
        }


    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceld  = dX == 0f && !isCurrentlyActive


        if (isCanceld){
            when(itemView.layoutDirection){
                View.LAYOUT_DIRECTION_LTR -> {
                    clearCanvas(c, itemView.left + dX, itemView.top.toFloat(), itemView.left.toFloat(), itemView.bottom.toFloat())
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    return
                }
//                View.LAYOUT_DIRECTION_RTL -> {
//                    clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
//                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//                    return
//                }
            }
        }



        when(itemView.layoutDirection){
            itemView.left->{
                //Delete
                Lbackground.color = LbackgroundColor
                Lbackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                Lbackground.draw(c)

                val deleteIconTop = itemView.top + (itemHeight - LintrinsicHeight!!) / 2
                val deleteIconMargin = (itemHeight - LintrinsicHeight) / 2
                val deleteIconLeft = itemView.right - deleteIconMargin - LintrinsicWidth!!
                val deleteIconRight = itemView.right - deleteIconMargin
                val deleteIconBottom = deleteIconTop + LintrinsicHeight


                // Draw the delete icon
                deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
                deleteIcon?.draw(c)
            }
//            itemView.right->{
//
//                Lbackground.color = LbackgroundColor
//                Lbackground.setBounds(itemView.left + dX.toInt(), itemView.top, itemView.left, itemView.bottom)
//                Lbackground.draw(c)
//
//                val deleteIconTop = itemView.top + (itemHeight - LintrinsicHeight!!) / 2
//                val deleteIconMargin = (itemHeight - LintrinsicHeight) / 2
//                val deleteIconLeft = itemView.left - deleteIconMargin - LintrinsicWidth!!
//                val deleteIconRight = itemView.left - deleteIconMargin
//                val deleteIconBottom = deleteIconTop + LintrinsicHeight
//
//
//                // Draw the delete icon
//                deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
//                deleteIcon?.draw(c)
//            }


            View.LAYOUT_DIRECTION_LTR -> {



            }

            View.LAYOUT_DIRECTION_RTL -> {


            }
        }



        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }




    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }

}