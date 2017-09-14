package com.moodi.breakyourgoal

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.*
import android.content.res.Configuration
import android.database.Cursor

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_add_goal.*
import kotlinx.android.synthetic.main.fragment_add_goal.*
import java.util.*
import android.database.MergeCursor
import android.database.MatrixCursor
import android.preference.Preference
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import java.util.prefs.Preferences


class AddGoalActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_goal)

        setSupportActionBar(add_goal_toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        Log.i("GOAL", "########## AddGoalActivity: onCreate");

        if ((resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
                == Configuration.SCREENLAYOUT_SIZE_XLARGE &&
                resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) ||
                (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
                        == Configuration.SCREENLAYOUT_SIZE_LARGE &&
                        resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)) {

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

    override fun onDestroy() {
        super.onDestroy()
        //subgoalFr
    }
}
