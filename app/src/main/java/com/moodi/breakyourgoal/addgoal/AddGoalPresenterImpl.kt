package com.moodi.breakyourgoal.addgoal

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.MatrixCursor
import android.database.MergeCursor
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.moodi.breakyourgoal.GoalApplication
import com.moodi.breakyourgoal.R
import com.moodi.breakyourgoal.adapter.SubGoalCursorAdapter
import com.moodi.breakyourgoal.common.GoalsConstant
import com.moodi.breakyourgoal.dialogfragment.SubGoalDialogFragment
import com.moodi.breakyourgoal.goaldetail.ItemDetailFragment
import com.moodi.breakyourgoal.goallist.ItemListActivity
import kotlinx.android.synthetic.main.fragment_add_goal.*
import javax.inject.Inject

/**
 * Created by ningappamoodi on 14/9/17.
 */
class AddGoalPresenterImpl: AddGoalPresenterI {

    private var subGoalDialogFragment: SubGoalDialogFragment?  = null

    private var extendedCursor: MergeCursor? = null

    private var recyclerAdapter : SubGoalCursorAdapter? = null

    private var addGoalViewI: AddGoalViewI? = null
    private var fragment: Fragment? = null

    @Inject
    lateinit var addGoal: AddGoalImpl

    constructor(addGoalViewI: AddGoalViewI) {

        this.addGoalViewI = addGoalViewI
        this.fragment = addGoalViewI as Fragment

        (addGoalViewI.activity.applicationContext as GoalApplication)
                .component.inject(this)

        Log.i("GOAL", "######## addGoal: " + addGoal)
    }
    override fun saveGoal(view: View) {

        Log.d("GOAL", "In saveGoal")

        if (validateForm()) return

        val uri: Uri = saveGoal()
        updatePref(uri)
        updateGoals(uri)
        updateScreen(uri)
    }

    private fun updateScreen(uri: Uri) {
        if (fragment!!.activity is ItemListActivity) {

            restartLoader(uri)
            replaceFragment(uri)

        } else {
            startItemListActivity()
        }
    }

    private fun startItemListActivity() {
        val intent = Intent(fragment!!.activity, ItemListActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        fragment!!.startActivity(intent)
    }

    private fun replaceFragment(uri: Uri) {
        Log.d("GOAL", "Add Goal Fragment : inserted row : " + uri.lastPathSegment)
        val arguments = Bundle()
        arguments.putString("GoalId", uri.lastPathSegment)
        val fragment = ItemDetailFragment()
        fragment.arguments = arguments

        (addGoalViewI as AddGoalFragment).activity.supportFragmentManager
        //fragment!!.fragmentManager.beginTransaction()
                .beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit()
    }

    private fun restartLoader(uri: Uri) {
        (fragment!!.activity as ItemListActivity).presenter!!.getRecyclerAdapter().newDataPosition = uri.lastPathSegment.toInt()
        val loader = (fragment!!.activity as ItemListActivity).presenter?.getLoader()
        fragment!!.activity.loaderManager.restartLoader(
                GoalsConstant.GOAL,
                null,
                loader
        )
    }

    private fun validateForm(): Boolean {
        if (!validateFormFileds()) {

            Toast.makeText(fragment!!.activity, "Form fields can't be empty!", Toast.LENGTH_LONG).show()
            return true
        }
        return false
    }

    private fun updateGoals(uri: Uri) {
        if (recyclerAdapter?.extras != null) {
            recyclerAdapter?.extras!!.moveToFirst()

            while (!recyclerAdapter?.extras!!.isAfterLast) {

                Log.d("GOAL", "cursor index : "
                        + recyclerAdapter?.extras!!.getString(0))

                val values = ContentValues()

                values.put("SubGoalName", recyclerAdapter?.extras!!.getString(1))
                values.put("GoalId", uri.lastPathSegment)
                values.put("Status", recyclerAdapter?.extras!!.getString(3))
                values.put("TargetDate", recyclerAdapter?.extras!!.getString(4))

                fragment!!.activity.contentResolver.insert(GoalsConstant.SUB_GOAL_CONTENT_URI, values)


                recyclerAdapter?.extras!!.moveToNext()
            }
        }
    }

    private fun updatePref(uri: Uri) {
        val sharedPref = fragment!!.activity.getSharedPreferences("GOALS", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("GoalId", uri.lastPathSegment)
        editor.commit()
    }

    private fun saveGoal(): Uri {
        /*fragment!!.activity.contentResolver.query(GoalsConstant.SUB_GOAL_CONTENT_URI, projection, null,
                null, null)*/

        /* arrayOf("_id", "SubGoalName")
        intArrayOf(R.id.subgoal_id, R.id.subgoal_item_goal_name)*/

        val values = ContentValues()
        values.put("GoalName", fragment!!.add_goal_goal_name.text.toString())
        values.put("Category", fragment!!.add_goal_category.selectedItem.toString())
        values.put("Duration", fragment!!.add_goal_duration.selectedItem.toString())
        values.put("FromDate", fragment!!.add_goal_from_date.text.toString())
        values.put("ToDate", fragment!!.add_goal_to_date.text.toString())

        val uri: Uri = fragment!!.activity.contentResolver.insert(GoalsConstant.GOAL_LIST_CONTENT_URI, values)

        Log.d("GOAL", "uri.lastPathSegment : " + uri.lastPathSegment)
        return uri
    }

    override fun subgoalAdd(view: View) {

        Log.d("GOAL", "Inside subgoalAdd!! ")

        Log.d("GOAL", "subgoalName: " + subGoalDialogFragment!!.subgoalName?.text.toString())

        if(!subGoalDialogFragment!!.validate()) return

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
                subGoalDialogFragment!!.subgoalName?.text.toString(), fragment!!.goal_id!!.text.toString(),
                GoalsConstant.OPEN, subGoalDialogFragment!!.subgoalDate?.text.toString()))

        val cursors = arrayOf<Cursor>(matrixCursor)
        extendedCursor = MergeCursor(cursors)

        Log.d("GOAL", "cursor count: " + matrixCursor.count)

        if(recyclerAdapter == null) {

            recyclerAdapter = SubGoalCursorAdapter(fragment!!.activity, extendedCursor!!, null)
            fragment!!.add_goal_subgoal_list?.adapter = recyclerAdapter
        } else {
            recyclerAdapter?.swapCursor(extendedCursor!!)
        }

        recyclerAdapter?.extras = matrixCursor

        Log.d("GOAL", "InAddGoalFragment: is item list acivity? : " + (fragment!!.activity is ItemListActivity))

        if(fragment!!.activity is ItemListActivity)
            fragment!!.activity.loaderManager.restartLoader(GoalsConstant.GOAL, null,
                    (fragment!!.activity as ItemListActivity).presenter!!.getLoader())

        if(subGoalDialogFragment != null)
            subGoalDialogFragment?.dismiss()
    }


    private fun  validateFormFileds(): Boolean {

        if(TextUtils.isEmpty(fragment!!.add_goal_goal_name.text.toString()) ||
                TextUtils.isEmpty(fragment!!.add_goal_category.selectedItem.toString()) ||
                TextUtils.isEmpty(fragment!!.add_goal_duration.selectedItem.toString()) ||
                "Select category".contentEquals(fragment!!.add_goal_category.selectedItem.toString()) ||
                "Select duration".contentEquals(fragment!!.add_goal_duration.selectedItem.toString()) ||
                TextUtils.isEmpty(fragment!!.add_goal_from_date.text.toString()) ||
                TextUtils.isEmpty(fragment!!.add_goal_to_date.text.toString())) {
            return false;
        }
        return true
    }

    override fun removeDialogFragment() {

        val ft = fragment!!.fragmentManager.beginTransaction()
        val prev = fragment!!.fragmentManager.findFragmentByTag("subGoalDialogFragment")
        if (prev != null) {
            (prev as DialogFragment).dismiss()
            ft.remove(prev)
            ft.addToBackStack(null)
            subGoalDialogFragment = SubGoalDialogFragment.newInstance() as SubGoalDialogFragment
            subGoalDialogFragment?.show(fragment!!.fragmentManager, "subGoalDialogFragment")
        } else {
            subGoalDialogFragment = SubGoalDialogFragment.newInstance() as SubGoalDialogFragment
        }
        ft.commit();
    }

   override fun onActivityCreated() {

      //  Log.i("GOAL", "########## AddGoalFragment: onActivityCreated");
        val catAdapter = ArrayAdapter.createFromResource(fragment!!.activity, R.array.array_add_goal_category,
                R.layout.support_simple_spinner_dropdown_item)

        val durAdapter = ArrayAdapter.createFromResource(fragment!!.activity, R.array.array_add_goal_duration,
                R.layout.support_simple_spinner_dropdown_item)

       addGoalViewI!!.setCategoryAdapter(catAdapter)
       addGoalViewI!!.setDurationAdapter(durAdapter)



        val cursor = fragment!!.activity.contentResolver.query(GoalsConstant.GOAL_LIST_CONTENT_URI,
                arrayOf("max(_id)"), null, null, null)
        cursor.moveToFirst()
        val maxId: Int = cursor.getInt(0) + 1

        Log.d("GOAL", "maxId: " + maxId)
        fragment!!.goal_id?.text = maxId.toString()

        fragment!!.add_goal_subgoal_list.layoutManager = LinearLayoutManager(fragment!!.activity)

    }

    override fun getSubGoalDialogFragment(): DialogFragment {
        return subGoalDialogFragment!!
    }

}