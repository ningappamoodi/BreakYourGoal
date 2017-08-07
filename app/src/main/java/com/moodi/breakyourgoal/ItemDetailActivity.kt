package com.moodi.breakyourgoal

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.database.MergeCursor
import android.graphics.Color
import android.os.Bundle
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

        goalId = intent.getStringExtra("GoalId")

        Log.i("GOAL", "############## onCreate goalId: " + goalId)

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

    fun showDatePickerDialog3(v: View?) {

        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")

        // View view = this.getCurrentFocus();
        if (v != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    fun subgoalAdd(view: View) {


        Log.i("GOAL", "Inside subgoalAdd!! ")

        Log.i("GOAL", "subgoalName: " + subGoalDialogFragment!!.subgoalName?.text.toString())
        Log.i("GOAL", "subgoalName length: "
                + subGoalDialogFragment!!.subgoalName?.text.toString().length)

          val values = ContentValues()
          values.put("SubGoalName", subGoalDialogFragment!!.subgoalName?.text.toString())
          values.put("Status", "open")
          values.put("GoalId", goalId)
          values.put("TargetDate", subGoalDialogFragment!!.subgoalDate?.text.toString())

          contentResolver.insert(GoalsConstant.SUB_GOAL_CONTENT_URI,values)

        if(!subGoalDialogFragment!!.validate()) {

            return
        }

        val fragmentItemDetail = supportFragmentManager.findFragmentById(R.id.item_detail_container)
        as ItemDetailFragment

        fragmentItemDetail.loaderManager.restartLoader(GoalsConstant.SUBGOAL, null, fragmentItemDetail)
        subGoalDialogFragment!!.dismiss()


    }

    fun subgoalCancel(view: View) {

        subGoalDialogFragment!!.dismiss()
    }

    class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {


        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current date as the default date in the picker
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // Create a new instance of DatePickerDialog and return it
            return DatePickerDialog(activity, this, year, month, day)
        }

        override fun onDateSet(datePicker: DatePicker, i: Int, i1: Int, i2: Int) {

            val act = activity as ItemDetailActivity
            act.dateSet3(datePicker, i, i1, i2)

        }

    }

    public fun dateSet3(datePicker: DatePicker, i: Int, i1: Int, i2: Int) {

        // subgoal_date.setText(i2.toString() + "/" + i1.toString() + "/" + i.toString())
        subGoalDialogFragment!!.subgoalDate?.setText(i2.toString() + "/" + i1.toString() + "/" + i.toString())

    }

}
