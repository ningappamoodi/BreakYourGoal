package com.moodi.breakyourgoal

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.database.Cursor
import android.database.MatrixCursor
import android.database.MergeCursor
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.support.v7.widget.Toolbar
import android.view.View
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.ActionBar
import android.support.v4.app.NavUtils
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import java.util.*

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [ItemListActivity].
 */


class ItemDetailActivity : AppCompatActivity() {

    var subGoalDialogFragment: SubGoalDialogFragment? = null
   public var goalId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)




        val toolbar = findViewById<View>(R.id.detail_toolbar) as Toolbar
        setSupportActionBar(toolbar)

      /*  val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/

        // Show the Up button in the action bar.
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val arguments = Bundle()

            val fragment = ItemDetailFragment()
            fragment.arguments = arguments
            supportFragmentManager.beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit()
        }

        val appBarLayout = findViewById<View>(R.id.toolbar_layout) as CollapsingToolbarLayout
        if (appBarLayout != null) {
            // appBarLayout.title = mItem!!.get("list_item_goal_date")
            appBarLayout.title = "Goal details"
            // appBarLayout.setExpandedTitleColor(resources.getColor(R.color.colorAccent, null))
        }

        if ((resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
                == Configuration.SCREENLAYOUT_SIZE_XLARGE &&
                resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) ||
                (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
                        == Configuration.SCREENLAYOUT_SIZE_LARGE &&
                        resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)) {

            val sharedPref = getSharedPreferences("GOALS", MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("ItemDetailFragment", "ItemDetail")
            editor.commit()

            onBackPressed()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, Intent(this, ItemListActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    public  fun itemAddSubGoal(view: View) {

        subGoalDialogFragment = SubGoalDialogFragment()
        subGoalDialogFragment!!.show(supportFragmentManager, "subGoalDialogFragment")

    }

}
