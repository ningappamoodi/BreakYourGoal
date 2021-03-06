package com.moodi.breakyourgoal.adapter

import android.database.Cursor

/**
 * Created by ningappamoodi on 28/7/17.
 */

class SubGoalListItem {

    public  var  subGoalName: String? = null
    public  var  subGoalId: String? = null
    public  var  targetDate: String? = null
    public  var  statusTxt: String? = null

    companion object {

        fun fromCursor(cursor: Cursor) : SubGoalListItem {
            val listItem = SubGoalListItem()

            listItem.subGoalId = cursor.getString(0)
            listItem.subGoalName = cursor.getString(1)
            listItem.statusTxt = cursor.getString(3)
            listItem.targetDate = cursor.getString(4)

         return listItem
        }
    }
}