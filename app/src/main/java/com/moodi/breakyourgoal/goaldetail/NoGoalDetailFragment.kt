package com.moodi.breakyourgoal.goaldetail


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moodi.breakyourgoal.R

/**
 * Created by ningappamoodi on 14/01/18.
 */
class NoGoalDetailFragment : Fragment() {


override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                          savedInstanceState: Bundle?): View? {
    val rootView = inflater!!.inflate(R.layout.no_goal_detail, container, false)
    return rootView
}
}