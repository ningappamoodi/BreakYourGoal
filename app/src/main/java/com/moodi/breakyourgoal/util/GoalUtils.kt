package com.moodi.breakyourgoal.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by ningappa on 1/1/18.
 */
class GoalUtils {

    companion object {


         fun getDurationDate(days: Int): String? {
            val calendar = Calendar.getInstance()

             calendar.add(Calendar.DATE, days)

            val dateFormat = SimpleDateFormat("dd/MM/yyyy")

            return dateFormat.format(calendar.time)
        }
    }

}