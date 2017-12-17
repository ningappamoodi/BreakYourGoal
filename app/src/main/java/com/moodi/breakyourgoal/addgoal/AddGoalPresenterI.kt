package com.moodi.breakyourgoal.addgoal

import android.support.v4.app.DialogFragment
import android.view.View

/**
 * Created by ningappamoodi on 14/9/17.
 */
interface AddGoalPresenterI {

    fun removeDialogFragment()
    fun onActivityCreated()
    fun subgoalAdd(view: View)
    fun saveGoal(view: View)

    fun getSubGoalDialogFragment() : DialogFragment
}