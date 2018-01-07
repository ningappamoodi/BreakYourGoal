package com.moodi.breakyourgoal.addgoal

import android.widget.ArrayAdapter

/**
 * Created by ningappamoodi on 14/9/17.
 */
interface AddGoalViewI {

    fun subgoalBtnClickListener()
    fun fromDateClickListener()
    fun toDateClickListener()
    fun saveSubGoalBtnClickListener()
    fun setCategoryAdapter(arrayAdapter: ArrayAdapter<CharSequence>)
    fun setDurationAdapter(arrayAdapter: ArrayAdapter<CharSequence>)
    fun durationBtnClickListener()

}