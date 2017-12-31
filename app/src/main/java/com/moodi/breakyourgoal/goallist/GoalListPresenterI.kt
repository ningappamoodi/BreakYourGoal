package com.moodi.breakyourgoal.goallist

import android.content.res.Configuration
import android.support.v7.widget.RecyclerView
import com.moodi.breakyourgoal.adapter.MyListCursorAdapter

/**
 * Created by ningappamoodi on 14/9/17.
 */
interface GoalListPresenterI {

    fun addFragment()
    fun addGoal()
    fun setupRecyclerView(recyclerView: RecyclerView)
    fun getRecyclerAdapter() : MyListCursorAdapter
    fun loaddata()
    fun restartLoader()
    fun removeDetailedFragment(newConfig: Configuration?)
    fun removeDetailedFragment()
    fun getLoader(): GoalListLoader
    fun isLongClick(isLongClick: Boolean)
    fun getIsLongClick() : Boolean
    fun  deleteGoals()
}