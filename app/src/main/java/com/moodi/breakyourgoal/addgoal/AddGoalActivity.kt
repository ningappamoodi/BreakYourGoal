package com.moodi.breakyourgoal.addgoal

import android.content.res.Configuration

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.moodi.breakyourgoal.R
import kotlinx.android.synthetic.main.activity_add_goal.*
import com.moodi.breakyourgoal.util.GoalActivityUtil


class AddGoalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_goal)

        setSupportActionBar(add_goal_toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        Log.i("GOAL", "########## AddGoalActivity: onCreate");

        if (GoalActivityUtil.isLargeScreenAndLandscape(this)) {

            val sharedPref = getSharedPreferences("GOALS", MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("AddGoalFragment", "AddGoal")
            editor.commit()

            onBackPressed()
        }
        if (savedInstanceState == null) {
            // Create the add fragment and add it to the activity
            // using a fragment transaction.
            val arguments = Bundle()

            val fragment = AddGoalFragment()
            fragment.arguments = arguments
            supportFragmentManager.beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit()
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        Log.d("GOAL", "$$$$$$$$$$$$$$$$ AddGoalActivity onConfigurationChanged: ")
        Log.d("GOAL", "newConfig?.orientation:  " + newConfig?.orientation)

        if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            val preferences = getPreferences(0)
            val editor = preferences.edit()

            editor.putString("fromActivity", "AddGoal")
        }
    }

}
