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

class SubGoalCursorAdapter(context: Context, cursor: Cursor) : CursorRecyclerViewAdapter<SubGoalCursorAdapter.ViewHolder>(context, cursor) {



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var subGoalName: TextView
       var subGoalId: TextView
        /*   var goalDuration: TextView
          var goalDate: TextView*/

        init {
            subGoalName = view.findViewById(R.id.subgoal_item_goal_name)
            subGoalId = view.findViewById(R.id.subgoal_id)
            /*goalCategory = view.findViewById(R.id.list_item_goal_category)
            goalDuration = view.findViewById(R.id.list_item_goal_duration)
            goalDate = view.findViewById(R.id.list_item_date)*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {

        Log.i("GOAL", "######### SubGoalCursorAdapter : onCreateViewHolder : ")

        val itemView = LayoutInflater.from(parent!!.getContext())
                .inflate(R.layout.item_list_subgoal, parent, false)
        val vh = ViewHolder(itemView)
        return vh
    }

   override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

       Log.i("GOAL", "################# onBindViewHolder")

        super.cursor?.moveToPosition(position)
        val myListItem = SubGoalListItem.fromCursor(super.cursor!!)

        viewHolder.subGoalName.setText(myListItem.subGoalName)
       viewHolder.subGoalId.setText(myListItem.subGoalId)
    /*   viewHolder.goalCategory.setText(myListItem.goalCategory)
       viewHolder.goalDuration.setText(myListItem.goalDuration)
       viewHolder.goalDate.setText(myListItem.goalDate)*/
    }
}