package com.moodi.breakyourgoal


import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by ningappamoodi on 28/7/17.
 */

class MyListCursorAdapter(context: Context, cursor: Cursor) : CursorRecyclerViewAdapter<MyListCursorAdapter.ViewHolder>(context, cursor) {



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var goalId: TextView
        var goalName: TextView
        var goalCategory: TextView
        var goalDuration: TextView
        var goalDate: TextView

        init {
            goalId = view.findViewById(R.id.list_item_goalId)
            goalName = view.findViewById(R.id.list_item_goal_name)
            goalCategory = view.findViewById(R.id.list_item_goal_category)
            goalDuration = view.findViewById(R.id.list_item_goal_duration)
            goalDate = view.findViewById(R.id.list_item_date)
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
    }
}