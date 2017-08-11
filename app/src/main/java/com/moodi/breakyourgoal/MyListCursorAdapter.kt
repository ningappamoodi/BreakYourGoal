package com.moodi.breakyourgoal


import android.content.Context
import android.database.Cursor
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


    constructor(context: Context, cursor: Cursor) : super(context, cursor)  {


        this.context = context
        this.cursor1 = cursor
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var goalId: TextView
        var goalName: TextView
        var goalCategory: TextView
        var goalDuration: TextView
        var goalDate: TextView

        var statusInProgressTxt: TextView
        var statusCompletedTxt: TextView
        var statusOpenTxt: TextView

        var percentage: TextView

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

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent!!.getContext())
                .inflate(R.layout.item_list_content, parent, false)
        val vh = ViewHolder(itemView)
        return vh
    }

   override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

       Log.i("GOAL", "################# onBindViewHolder")

        super.cursor?.moveToPosition(position)
        val myListItem = MyListItem.fromCursor(super.cursor!!)

       viewHolder.goalId.setText(myListItem.goalId)
       viewHolder.goalName.setText(myListItem.goalName)
       viewHolder.goalCategory.setText(myListItem.goalCategory)
       viewHolder.goalDuration.setText(myListItem.goalDuration)
       viewHolder.goalDate.setText(myListItem.goalDate)

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