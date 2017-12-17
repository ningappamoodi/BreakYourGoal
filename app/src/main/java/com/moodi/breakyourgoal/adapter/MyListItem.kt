package com.moodi.breakyourgoal.adapter

import android.database.Cursor
import android.util.Log
import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * Created by ningappamoodi on 28/7/17.
 */

class MyListItem {

    public var goalId: String? = null
   public var goalName: String? = null
    public var goalCategory: String? = null
    public var goalDuration: String? = null
    public var goalDate: String? = null


    companion object {

        fun fromCursor(cursor: Cursor) : MyListItem {
            val listItem = MyListItem()

            Log.d("GOAL", "value @0 : " + cursor.getString(1))

            listItem.goalId = cursor.getString(0)
            listItem.goalName = cursor.getString(1)
            listItem.goalCategory = cursor.getString(2)
            listItem.goalDuration = cursor.getString(3)

            val df: DateFormat = SimpleDateFormat("dd/MM/yyyy")
            val df1: DateFormat = SimpleDateFormat("dd-MMM-yy")

            val date = df.parse(cursor.getString(4))
            val date2 = df.parse(cursor.getString(5))

            listItem.goalDate = df1.format(date) + " to " + df1.format(date2)

         return listItem
        }

        fun fromCursorForStatus(cursor: Cursor) : Map<String, Int>{

           val data: MutableMap<String, Int> = HashMap<String, Int>()

            cursor.moveToFirst()

            if(cursor.count == 0) return data

            Log.d("GOAL", "fromCursorForStatus value @0 : " + cursor.getString(0))

            while (!cursor.isAfterLast) {

                Log.d("GOAL", "cursor value 0 1 : "
                        + cursor.getString(0) + ":" + cursor.getString(1))

                var i: Int = 0
                if( !(data.get(cursor.getString(0) + ":" + cursor.getString(1)) == null)) {

                    i =  data.get(cursor.getString(0) + ":" + cursor.getString(1))!!.toInt()
                }

                i++
                data.put(cursor.getString(0) + ":" + cursor.getString(1),i)
                cursor.moveToNext()
            }

            Log.d("GOAL", "fromCursorForStatus keys: "
                    +data.keys)

            return data
        }
    }
}