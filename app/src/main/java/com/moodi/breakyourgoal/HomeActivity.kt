package com.moodi.breakyourgoal

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
       // setSupportActionBar(toolbar)

        fab1.setOnClickListener { view ->
           /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()*/

          /*  Log.i("GOAL", " In Home Activity fab.setOnClickListener  : ")

            if (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
                    == Configuration.SCREENLAYOUT_SIZE_XLARGE ||
                    (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
                            == Configuration.SCREENLAYOUT_SIZE_LARGE &&
                            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)) {
                // Create the detail fragment and add it to the activity
                // using a fragment transaction.
                val arguments = Bundle()

                Log.i("GOAL", " In Home Activity fragment: ")
              *//*  arguments.putString("GoalId", goalId.text.toString())
                arguments.putString("goalName", goalName.text.toString())*//*

                val fragment = AddGoalFragment()
                fragment.arguments = arguments
                supportFragmentManager.beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit()

            } else {

                Log.i("GOAL", " In Home Activity activity: ")
                 val intent =  Intent(baseContext, AddGoalActivity::class.java)
            startActivity(intent)
            }*/
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
