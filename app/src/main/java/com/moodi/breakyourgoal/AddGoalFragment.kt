package com.moodi.breakyourgoal



import android.app.LoaderManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.database.MergeCursor
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import kotlinx.android.synthetic.main.fragment_add_goal.*

/**
 * Created by ningappamoodi on 14/8/17.
 */
class AddGoalFragment : Fragment() {


    var goalId: TextView? = null
    var subGoalList: RecyclerView? = null
    val subGoalDialogFragment  = SubGoalDialogFragment()

    var extendedCursor: MergeCursor? = null
    //var extras: MatrixCursor? = null

    var recyclerAdapter : SubGoalCursorAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       //setContentView(R.layout.fragment_add_goal)



    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        val catAdapter = ArrayAdapter.createFromResource(activity, R.array.array_add_goal_category,
                R.layout.support_simple_spinner_dropdown_item)

        val durAdapter = ArrayAdapter.createFromResource(activity, R.array.array_add_goal_duration,
                R.layout.support_simple_spinner_dropdown_item)

       // val add_goal_category = activity.findViewById<Spinner>(R.id.add_goal_category)
      //  val add_goal_duration = activity.findViewById<Spinner>(R.id.add_goal_duration)


        add_goal_category.adapter = catAdapter
        add_goal_duration.adapter = durAdapter

        val cursor = activity.contentResolver.query(GoalsConstant.GOAL_LIST_CONTENT_URI, arrayOf("max(_id)"), null, null, null)

        cursor.moveToFirst()
        goalId = activity.findViewById<TextView>(R.id.goal_id)
        val maxId: Int = cursor.getInt(0) + 1

        Log.d("GOAL", "maxId: " + maxId)
        goalId?.text = maxId.toString()

        subGoalList = activity.findViewById<RecyclerView>(R.id.add_goal_subgoal_list)
        subGoalList?.setLayoutManager( LinearLayoutManager(activity))

       // val addSubGoalBtn = activity.findViewById<Button>(R.id.add_goal_add_subgoal_btn)
        //val saveSubGoalBtn = activity.findViewById<Button>(R.id.add_goal_save_subgoal_btn)
      //  val fromDateBtn = activity.findViewById<Button>(R.id.add_goal_save_subgoal_btn)


        add_goal_add_subgoal_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

               // val subGoalDialogFragment = SubGoalDialogFragment()
                subGoalDialogFragment.show(fragmentManager, "subGoalDialogFragment")

            }
        })

       // val datePicker = this as DatePickerI
        add_goal_from_date.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

                val newFragment = DatePickerFragment()

                newFragment.editText = add_goal_from_date
                newFragment.show(fragmentManager, "datePicker")

                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(p0?.windowToken, 0)
            }
        })

        add_goal_to_date.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

                val newFragment = DatePickerFragment()

                newFragment.editText = add_goal_to_date
                newFragment.show(fragmentManager, "datePicker")

                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(p0?.windowToken, 0)
            }
        })

        add_goal_save_subgoal_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

                saveGoal(p0!!)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_add_goal, container, false)

        return rootView
    }

    fun saveGoal(view: View) {

        Log.d("GOAL", "In saveGoal")

        val projection = arrayOf("_id", "SubGoalName", "GoalId", "Status", "TargetDate")

        if (TextUtils.isEmpty(add_goal_goal_name.text.toString()) ||
                TextUtils.isEmpty(add_goal_category.selectedItem.toString()) ||
                TextUtils.isEmpty(add_goal_duration.selectedItem.toString()) ||
                "Select category".contentEquals(add_goal_category.selectedItem.toString()) ||
                "Select duration".contentEquals(add_goal_duration.selectedItem.toString()) ||
                TextUtils.isEmpty(add_goal_from_date.text.toString()) ||
                TextUtils.isEmpty(add_goal_to_date.text.toString())) {

            Toast.makeText(activity, "Form fields can't be empty!", Toast.LENGTH_LONG).show()
            return
        }

        val c = activity.contentResolver.query(GoalsConstant.SUB_GOAL_CONTENT_URI, projection, null,
                null, null)

        val from = arrayOf("_id", "SubGoalName")
        val to = intArrayOf(R.id.subgoal_id, R.id.subgoal_item_goal_name)

        val values = ContentValues()
        values.put("GoalName", add_goal_goal_name.text.toString())
        values.put("Category", add_goal_category.selectedItem.toString())
        values.put("Duration", add_goal_duration.selectedItem.toString())
        values.put("FromDate", add_goal_from_date.text.toString())
        values.put("ToDate", add_goal_to_date.text.toString())

        val uri: Uri = activity.contentResolver.insert(GoalsConstant.GOAL_LIST_CONTENT_URI, values)

        Log.d("GOAL", "uri.lastPathSegment : " + uri.lastPathSegment)

        val sharedPref = activity.getSharedPreferences("GOALS", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("GoalId", uri.lastPathSegment)
        editor.commit()


        if(recyclerAdapter?.extras != null) {
            recyclerAdapter?.extras!!.moveToFirst()

            while (!recyclerAdapter?.extras!!.isAfterLast) {

                Log.d("GOAL", "cursor index : "
                        + recyclerAdapter?.extras!!.getString(0))

                val values = ContentValues()

                values.put("SubGoalName", recyclerAdapter?.extras!!.getString(1))
                values.put("GoalId",      uri.lastPathSegment)
                values.put("Status",      recyclerAdapter?.extras!!.getString(3))
                values.put("TargetDate",  recyclerAdapter?.extras!!.getString(4))

                 activity.contentResolver.insert(GoalsConstant.SUB_GOAL_CONTENT_URI, values)


                recyclerAdapter?.extras!!.moveToNext()
            }
        }

        if(activity is ItemListActivity) {

            (activity as ItemListActivity).recyclerAdapter.newDataPosition = uri.lastPathSegment.toInt()
            activity.loaderManager.restartLoader(GoalsConstant.GOAL, null,
                    activity as LoaderManager.LoaderCallbacks<Cursor>)


            Log.d("GOAL", "Add Goal Fragment : inserted row : " + uri.lastPathSegment)
            val arguments = Bundle()
            arguments.putString("GoalId", uri.lastPathSegment)
            val fragment = ItemDetailFragment()
            fragment.arguments = arguments
            fragmentManager.beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()

        } else {
            val intent = Intent(activity, ItemListActivity::class.java)
            startActivity(intent)
        }
    }

    fun subgoalAdd(view: View) {

        Log.d("GOAL", "Inside subgoalAdd!! ")

        Log.d("GOAL", "subgoalName: " + subGoalDialogFragment!!.subgoalName?.text.toString())

        if(!subGoalDialogFragment!!.validate()) {

            return
        }

        var matrixCursor: MatrixCursor? = null

        if(recyclerAdapter == null || (recyclerAdapter != null && recyclerAdapter?.extras == null)) {

            matrixCursor = MatrixCursor(arrayOf("_id", "SubGoalName", "GoalId", "Status", "TargetDate"))

        }
        else if((recyclerAdapter != null && recyclerAdapter?.extras != null)) {

            matrixCursor = recyclerAdapter?.extras
        }

        Log.d("GOAL", "recyclerAdapter : " + matrixCursor)


        val count = matrixCursor!!.count + 1
        matrixCursor.addRow(arrayOf("-" + count.toString(),
                subGoalDialogFragment!!.subgoalName?.text.toString(), goalId!!.text.toString(),
                GoalsConstant.OPEN, subGoalDialogFragment!!.subgoalDate?.text.toString()))

        val cursors = arrayOf<Cursor>(matrixCursor)
        extendedCursor = MergeCursor(cursors)

        Log.d("GOAL", "cursor count: " + matrixCursor.count)

        if(recyclerAdapter == null) {

            recyclerAdapter = SubGoalCursorAdapter(activity, extendedCursor!!, null)
            subGoalList?.adapter = recyclerAdapter
        } else {
            recyclerAdapter?.swapCursor(extendedCursor!!)
        }

        recyclerAdapter?.extras = matrixCursor

        Log.d("GOAL", "InAddGoalFragment: is item list acivity? : " + (activity is ItemListActivity))

        if(activity is ItemListActivity)
        activity.loaderManager.restartLoader(GoalsConstant.GOAL, null, activity as ItemListActivity)

        subGoalDialogFragment!!.dismiss()
    }

}