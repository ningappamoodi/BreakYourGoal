package com.moodi.breakyourgoal

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.*
import android.database.Cursor

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_add_goal.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_add_goal.*
import kotlinx.android.synthetic.main.dialog_subgoal.*
import kotlinx.android.synthetic.main.item_list_content.*
import java.text.SimpleDateFormat
import java.util.*
import android.database.MergeCursor
import android.database.MatrixCursor
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView


class AddGoalActivity : AppCompatActivity() {

    var goalId: TextView? = null
    var subGoalList: RecyclerView? = null
    var subGoalDialogFragment: SubGoalDialogFragment? = null

    var extendedCursor: MergeCursor? = null
    var extras: MatrixCursor? = null

    lateinit var recyclerAdapter : SubGoalCursorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_goal)

        setSupportActionBar(add_goal_toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val catAdapter = ArrayAdapter.createFromResource(this, R.array.array_add_goal_category,
                R.layout.support_simple_spinner_dropdown_item)

        val durAdapter = ArrayAdapter.createFromResource(this, R.array.array_add_goal_duration,
                R.layout.support_simple_spinner_dropdown_item)

        add_goal_category.adapter = catAdapter
        add_goal_duration.adapter = durAdapter

        val cursor = contentResolver.query(GoalsConstant.GOAL_LIST_CONTENT_URI, arrayOf("max(_id)"), null, null, null)

        cursor.moveToFirst()
        goalId = findViewById<TextView>(R.id.goal_id)
        val maxId: Int = cursor.getInt(0) + 1

        Log.i("GOAL", "################ maxId: " + maxId)
        goalId?.text = maxId.toString()

        subGoalList = findViewById<RecyclerView>(R.id.add_goal_subgoal_list)
        subGoalList?.setLayoutManager( LinearLayoutManager(this))



    }

    fun saveGoal(view: View) {

        Log.i("GOAL", "#################### In saveGoal")

        val projection = arrayOf("_id", "SubGoalName", "GoalId", "Status", "TargetDate")

        if (TextUtils.isEmpty(add_goal_goal_name.text.toString()) ||
                TextUtils.isEmpty(add_goal_category.selectedItem.toString()) ||
                TextUtils.isEmpty(add_goal_duration.selectedItem.toString()) ||
                "Select category".contentEquals(add_goal_category.selectedItem.toString()) ||
                "Select duration".contentEquals(add_goal_duration.selectedItem.toString()) ||
                TextUtils.isEmpty(add_goal_from_date.text.toString()) ||
                TextUtils.isEmpty(add_goal_to_date.text.toString())) {

            Toast.makeText(this, "Form fields can't be empty!", Toast.LENGTH_LONG).show()
            return
        }

        val c = contentResolver.query(GoalsConstant.SUB_GOAL_CONTENT_URI, projection, null,
                null, null)

        val from = arrayOf("_id", "SubGoalName")
        val to = intArrayOf(R.id.subgoal_id, R.id.subgoal_item_goal_name)

      //  val adapter = SimpleCursorAdapter(this, R.layout.item_list_subgoal, c, from, to, 0)

        recyclerAdapter = SubGoalCursorAdapter(this, c)

        subGoalList!!.adapter = recyclerAdapter

        val values = ContentValues()
        values.put("GoalName", add_goal_goal_name.text.toString())
        values.put("Category", add_goal_category.selectedItem.toString())
        values.put("Duration", add_goal_duration.selectedItem.toString())
        values.put("FromDate", add_goal_from_date.text.toString())
        values.put("ToDate", add_goal_to_date.text.toString())

        val uri: Uri = contentResolver.insert(GoalsConstant.GOAL_LIST_CONTENT_URI, values)

        Log.i("GOAL", "############## uri.lastPathSegment : " + uri.lastPathSegment)


        extras!!.moveToFirst()

        while (!extras!!.isAfterLast) {

           Log.i("GOAL", "##################### cursor index : "
                   +  extras!!.getString(0))

            extras!!.getString(1)

            val values = ContentValues()

            values.put("SubGoalName", extras!!.getString(1))
            values.put("GoalId", uri.lastPathSegment)
            values.put("Status", extras!!.getString(3))
            values.put("TargetDate", extras!!.getString(4))

            contentResolver.insert(GoalsConstant.SUB_GOAL_CONTENT_URI, values)


            extras!!.moveToNext()
        }

        val intent = Intent(baseContext, ItemListActivity::class.java)
        startActivity(intent)
    }


    fun addSubGoal(view: View) {

        subGoalDialogFragment = SubGoalDialogFragment()


        // findViewById(R.id.subgoal_id) as TextView

        // subGoalDialogFragment.subgoalId = (as TextView).text.toString()
        subGoalDialogFragment!!.show(supportFragmentManager, "subGoalDialogFragment")

    }

    fun subgoalAdd(view: View) {


        Log.i("GOAL", "Inside subgoalAdd!! ")

        Log.i("GOAL", "subgoalName: " + subGoalDialogFragment!!.subgoalName?.text.toString())

        /*  val values = ContentValues()
          values.put("SubGoalName", subGoalDialogFragment!!.subgoalName?.text.toString())
          values.put("Status", "open")
          values.put("GoalId", goalId?.text.toString())
          values.put("TargetDate", subGoalDialogFragment!!.subgoalDate?.text.toString())

          contentResolver.insert(GoalsConstant.SUB_GOAL_CONTENT_URI,values)*/

        subGoalDialogFragment!!.dismiss()


        val projection = arrayOf("_id", "SubGoalName", "GoalId", "Status", "TargetDate")

      /*  val c = contentResolver.query(GoalsConstant.SUB_GOAL_CONTENT_URI, projection, "_id=?",
                arrayOf(goalId!!.text.toString()), null)*/

        if (extras == null) {
             extras = MatrixCursor(arrayOf("_id", "SubGoalName", "GoalId", "Status", "TargetDate"))
        }

        extras?.addRow(arrayOf("-1", subGoalDialogFragment!!.subgoalName?.text.toString(), goalId!!.text.toString(), "open", subGoalDialogFragment!!.subgoalDate?.text.toString()))

        val cursors = arrayOf<Cursor>(extras!!)
        extendedCursor = MergeCursor(cursors)


        val from = arrayOf("_id", "SubGoalName")
        val to = intArrayOf(R.id.subgoal_id, R.id.subgoal_item_goal_name)

       // val adapter = SimpleCursorAdapter(this, R.layout.item_list_subgoal, extendedCursor, from, to, 0)

        Log.i("GOAL", "################# cursor count: " + extendedCursor?.count)
        recyclerAdapter = SubGoalCursorAdapter(this, extendedCursor!!)
        subGoalList?.adapter = recyclerAdapter


    }

    fun subgoalCancel(view: View) {

        subGoalDialogFragment!!.dismiss()
    }

    fun showDatePickerDialog(v: View?) {

        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")

        // View view = this.getCurrentFocus();
        if (v != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    fun showDatePickerDialog2(v: View?) {

        val newFragment = DatePickerFragment2()
        newFragment.show(supportFragmentManager, "datePicker")

        // View view = this.getCurrentFocus();
        if (v != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    fun showDatePickerDialog3(v: View?) {

        val newFragment = DatePickerFragment3()
        newFragment.show(supportFragmentManager, "datePicker")

        // View view = this.getCurrentFocus();
        if (v != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
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

            val act = activity as AddGoalActivity
            act.dateSet(datePicker, i, i1, i2)

        }


    }

    class DatePickerFragment2 : DialogFragment(), DatePickerDialog.OnDateSetListener {


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

            val act = activity as AddGoalActivity
            act.dateSet2(datePicker, i, i1, i2)

        }


    }

    class DatePickerFragment3 : DialogFragment(), DatePickerDialog.OnDateSetListener {


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

            val act = activity as AddGoalActivity
            act.dateSet3(datePicker, i, i1, i2)

        }


    }

    fun dateSet(datePickerquery: DatePicker, i: Int, i1: Int, i2: Int) {

        add_goal_from_date.setText(i2.toString() + "/" + i1.toString() + "/" + i.toString())

    }

    fun dateSet2(datePicker: DatePicker, i: Int, i1: Int, i2: Int) {

        add_goal_to_date.setText(i2.toString() + "/" + i1.toString() + "/" + i.toString())

    }

    fun dateSetForSubgoal(datePicker: DatePicker, i: Int, i1: Int, i2: Int) {

        add_goal_to_date.setText(i2.toString() + "/" + i1.toString() + "/" + i.toString())

    }

    fun dateSet3(datePicker: DatePicker, i: Int, i1: Int, i2: Int) {

        // subgoal_date.setText(i2.toString() + "/" + i1.toString() + "/" + i.toString())
        subGoalDialogFragment!!.subgoalDate?.setText(i2.toString() + "/" + i1.toString() + "/" + i.toString())

    }

}
