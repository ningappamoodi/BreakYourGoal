package com.moodi.breakyourgoal

import android.database.Cursor
import android.util.Log

/**
 * Created by ningappamoodi on 28/7/17.
 */

class SubGoalListItem {

   public var subGoalName: String? = null
    public var subGoalId: String? = null
    /*public var goalCategory: String? = null
    public var goalDuration: String? = null
    public var goalDate: String? = null
*/

    companion object {

        fun fromCursor(cursor: Cursor) : SubGoalListItem{
            val listItem = SubGoalListItem()

            Log.i("GOAL", "################ value @0 : " + cursor.getString(1))

            listItem.subGoalId = cursor.getString(0)
            listItem.subGoalName = cursor.getString(1)
            /*listItem.goalCategory = cursor.getString(2)
            listItem.goalDuration = cursor.getString(3)
            listItem.goalDate = cursor.getString(4) + " to " + cursor.getString(5)*/

         return listItem
        }
    }
}