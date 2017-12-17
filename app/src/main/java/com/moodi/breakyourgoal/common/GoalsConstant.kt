package com.moodi.breakyourgoal.common

import android.net.Uri

/**
 * Created by ningappamoodi on 28/7/17.
 */
class  GoalsConstant {

    companion object {

        public  final val GOAL:Int = 1
        public  final val GOAL_ID:Int = 2
        public  final val SUBGOAL = 3

       val GOAL_LIST_CONTENT_URI = Uri.parse("content://com.moodi.breakyourgoal.provider/GoalList") as Uri
        val SUB_GOAL_CONTENT_URI = Uri.parse("content://com.moodi.breakyourgoal.provider/SubGoal") as Uri

        final val IN_PROGRESS = "In Progress"
        final val COMPLETED = "Completed"
        final val OPEN = "Open"
        final val POSTPONED = "Postponed"
    }

}