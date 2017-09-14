package com.moodi.breakyourgoal


import android.content.Context
import android.content.res.Configuration
import android.database.Cursor
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * Created by ningappamoodi on 28/7/17.
 */

class MyListCursorAdapter : CursorRecyclerViewAdapter<MyListCursorAdapter.ViewHolder> {


    var context: Context? = null
    var cursor1: Cursor? = null

    var statusMap: Map<String, Int>? = HashMap<String, Int>()


    var selectedPosition: Int? = null
    var newDataPosition: Int? = null

    constructor(context: Context, cursor: Cursor) : super(context, cursor)  {


        this.context = context
        this.cursor1 = cursor
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnLongClickListener {

        var goalId: TextView
        var goalName: TextView
        var goalCategory: TextView
        var goalDuration: TextView
        var goalDate: TextView

        var statusInProgressTxt: TextView
        var statusCompletedTxt: TextView
        var statusOpenTxt: TextView

        var percentage: TextView

        var listItemRow: FrameLayout

        init {

            goalId = view.findViewById(R.id.list_item_goalId)
            goalName = view.findViewById(R.id.list_item_goal_name)
            goalCategory = view.findViewById(R.id.list_item_goal_category)
            goalDuration = view.findViewById(R.id.list_item_goal_duration)
            goalDate = view.findViewById(R.id.list_item_date)

            statusInProgressTxt = view.findViewById(R.id.list_item_goal_status_inprogress_txt)
            statusCompletedTxt = view.findViewById(R.id.list_item_goal_status_completed_txt)
            statusOpenTxt = view.findViewById(R.id.list_item_goal_status_open_txt)

            percentage = view.findViewById(R.id.list_item_goal_percentage)
            listItemRow = view.findViewById(R.id.list_item_row)

        }

        override fun onLongClick(p0: View?): Boolean {

            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent!!.getContext())
                .inflate(R.layout.item_list_content, parent, false)
        val vh = ViewHolder(itemView)
        return vh
    }

   override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

       Log.d("GOAL", "onBindViewHolder")

        super.cursor?.moveToPosition(position)
        val myListItem = MyListItem.fromCursor(super.cursor!!)

       Log.d("GOAL", "newDataPosition: " + newDataPosition)
       Log.d("GOAL", "viewHolder.goalId.text.toString() : "
               + viewHolder.goalId.text.toString())



       viewHolder.goalId.setText(myListItem.goalId)
       viewHolder.goalName.setText(myListItem.goalName)
       viewHolder.goalCategory.setText(myListItem.goalCategory)
       viewHolder.goalDuration.setText(myListItem.goalDuration)
       viewHolder.goalDate.setText(myListItem.goalDate)

       if ((context!!.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
               == Configuration.SCREENLAYOUT_SIZE_XLARGE &&
               context!!.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) ||
               (context!!.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
                       == Configuration.SCREENLAYOUT_SIZE_LARGE &&
                       context!!.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)) {

          if (position == selectedPosition) {

             /* (viewHolder.listItemRow.layoutParams as RecyclerView.LayoutParams)
                      .setMargins(8, 8, 0, 8)*/

              val card =  viewHolder.listItemRow as CardView
              card.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPurple_50))

          } else if (!TextUtils.isEmpty(viewHolder.goalId.text.toString()) &&
                  viewHolder.goalId.text.toString().toInt() == newDataPosition) {

              viewHolder.listItemRow.isSelected = true
             /* (viewHolder.listItemRow.layoutParams as RecyclerView.LayoutParams)
                      .setMargins(8, 8, 0, 8)*/

              val card =  viewHolder.listItemRow as CardView
              card.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPurple_50))
              newDataPosition = null
          } else {
             /* (viewHolder.listItemRow.layoutParams as RecyclerView.LayoutParams)
                      .setMargins(8, 8, 8, 8)*/
              viewHolder.listItemRow.isSelected = false

              val card =  viewHolder.listItemRow as CardView
              card.setCardBackgroundColor(Color.WHITE)

          }
      }


       var value = statusMap?.get(myListItem.goalId + ":" + GoalsConstant.IN_PROGRESS)
       if(value == null) value = 0

       viewHolder.statusInProgressTxt.setText(value.toString())


      var  value1 = statusMap?.get(myListItem.goalId + ":" + GoalsConstant.COMPLETED)
       if(value1 == null) value1 = 0

       viewHolder.statusCompletedTxt.setText(value1.toString())


        var value2 = statusMap?.get(myListItem.goalId + ":" + GoalsConstant.OPEN)
       if(value2 == null) value2 = 0

       viewHolder.statusOpenTxt.setText(value2.toString())


       val totalCount: Int = value + value1 + value2

       var valueFloat: Float = 0f
       if(totalCount == 0) {
           value = 0
       } else {
           valueFloat = (((value/2.0f) + (value1 * 1) ) / totalCount) * 100f
       }

     val valueInt = valueFloat.toInt()
       viewHolder.percentage.setText(valueInt.toString() + "%")

       if(valueInt >= 80) {

           val color = ContextCompat.getColor(context, R.color.colorGreen)
           viewHolder.percentage.setTextColor(color)
       }
       else if(valueInt >= 50 && valueInt < 80) {

           val color = ContextCompat.getColor(context, R.color.colorOrange)
           viewHolder.percentage.setTextColor(color)
       }
       else {

           val color = ContextCompat.getColor(context, R.color.colorGrey)
           viewHolder.percentage.setTextColor(color)
       }



    }
}