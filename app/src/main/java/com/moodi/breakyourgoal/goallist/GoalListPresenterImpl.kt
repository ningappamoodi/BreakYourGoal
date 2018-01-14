package com.moodi.breakyourgoal.goallist

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.database.Cursor
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.moodi.breakyourgoal.*
import com.moodi.breakyourgoal.adapter.MyListCursorAdapter
import com.moodi.breakyourgoal.addgoal.AddGoalActivity
import com.moodi.breakyourgoal.addgoal.AddGoalFragment
import com.moodi.breakyourgoal.common.GoalsConstant
import com.moodi.breakyourgoal.goaldetail.ItemDetailFragment
import com.moodi.breakyourgoal.goaldetail.NoGoalDetailFragment
import com.moodi.breakyourgoal.util.GoalActivityUtil
import com.moodi.breakyourgoal.util.VerticalDividerItemDecoration

/**
 * Created by ningappamoodi on 14/9/17.
 */
class GoalListPresenterImpl : GoalListPresenterI {


    var listView: GoalListViewI? = null
    var activity: AppCompatActivity? = null
    private var loader: GoalListLoader? = null

    private  var recyclerAdapter : MyListCursorAdapter? = null

    var isLongClicked = false

    val context: Context

    constructor(listView: GoalListViewI) {
        this.listView = listView
        activity = listView as AppCompatActivity
        loader = GoalListLoader(this, activity!!)

        context = activity as Context
    }

    override fun addFragment() {

        val sharedPref = activity!!.getSharedPreferences("GOALS",
                AppCompatActivity.MODE_PRIVATE)


        Log.d("GOAL", "AddGoalFragment: "
                +  sharedPref.contains("AddGoalFragment"))
        Log.d("GOAL", "ItemDetailFragment: "
                +  sharedPref.contains("ItemDetailFragment"))

        Log.d("GOAL", "AddGoalFragment: "
                +  sharedPref.getString("AddGoalFragment", null))

        if( sharedPref.contains("AddGoalFragment")) {

            val arguments = Bundle()

            Log.d("GOAL", " In Home Activity fragment: ")
            // arguments.putString("GoalId", goalId.text.toString())
            //arguments.putString("goalName", goalName.text.toString())

            val fragment = AddGoalFragment()
            fragment.arguments = arguments
            activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()

            val editor = sharedPref.edit()
            editor.remove("AddGoalFragment")
            editor.commit()
        }
        else  if( sharedPref.contains("ItemDetailFragment")) {

            val arguments = Bundle()

            Log.d("GOAL", " In List Activity fragment ItemDetailFragment : ")
            // arguments.putString("GoalId", goalId.text.toString())
            //arguments.putString("goalName", goalName.text.toString())

            arguments.putString("GoalId", sharedPref.getString("GoalId", null))
            val fragment = ItemDetailFragment()
            fragment.arguments = arguments
            activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()

            val editor = sharedPref.edit()
            editor.remove("ItemDetailFragment")
            // editor.remove("GoalId")
            editor.commit()

        }

    }

    override fun addGoal() {

        Log.d("GOAL", " In Home Activity fab.setOnClickListener  : ")

        if (GoalActivityUtil.isLargeScreenAndLandscape(context)) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val arguments = Bundle()

            Log.d("GOAL", " In Home Activity fragment: ")
            // arguments.putString("GoalId", goalId.text.toString())
            //arguments.putString("goalName", goalName.text.toString())

            val fragment = AddGoalFragment()
            fragment.arguments = arguments
            activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()

            recyclerAdapter?.selectedPosition = null
            recyclerAdapter?.notifyDataSetChanged()

        } else {

            Log.d("GOAL", " In Home Activity activity: ")
            val intent =  Intent(activity?.baseContext, AddGoalActivity::class.java)
            activity?.startActivity(intent)
        }
    }

    override fun setupRecyclerView(recyclerView: RecyclerView) {

        val projection = arrayOf("_id", "GoalName", "Category", "Duration", "FromDate", "ToDate")
        val selection = null
        val selectionArgs = null
        val sortOrder = null

        val c =   activity!!.contentResolver.query(GoalsConstant.GOAL_LIST_CONTENT_URI, projection, selection,
                selectionArgs, sortOrder)

        recyclerView.addItemDecoration(VerticalDividerItemDecoration(20, true))

        recyclerAdapter = MyListCursorAdapter(activity!!, c)

        val sharedPref =   activity!!.getSharedPreferences("GOALS", AppCompatActivity.MODE_PRIVATE)

        if(  activity!!.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerAdapter?.newDataPosition = sharedPref.getString("GoalId", "0")?.toInt()
        }

        val editor = sharedPref.edit()
        editor.remove("GoalId")
        editor.commit()

        recyclerView.adapter = recyclerAdapter

    }

    override  fun getRecyclerAdapter() : MyListCursorAdapter {

        return recyclerAdapter!!
    }

    override fun loaddata() {

        activity!!.loaderManager.initLoader(GoalsConstant.SUBGOAL, null, loader)
        activity!!.loaderManager.initLoader(GoalsConstant.GOAL, null, loader)


    }

    override fun restartLoader() {

        activity!!.loaderManager.restartLoader(GoalsConstant.SUBGOAL, null, loader)
        activity!!.loaderManager.restartLoader(GoalsConstant.GOAL, null, loader)


    }

    override fun removeDetailedFragment(newConfig: Configuration?) {

        if (newConfig?.orientation == Configuration.ORIENTATION_PORTRAIT) {

            val fragment = activity!!.supportFragmentManager.findFragmentById(R.id.item_detail_container)

            if (fragment != null) {
                activity!!.supportFragmentManager.beginTransaction().remove(fragment).commit()
            }
        }
    }
    override fun removeDetailedFragment() {

            val fragment = activity!!.supportFragmentManager.findFragmentById(R.id.item_detail_container)

            if (fragment != null) {
                activity!!.supportFragmentManager.beginTransaction().remove(fragment).commit()
            }
    }

    override fun getLoader(): GoalListLoader {
        return loader!!
    }

    override fun isLongClick(isLongCLick: Boolean) {

      isLongClicked = isLongCLick
    }
    override fun getIsLongClick(): Boolean {
        return isLongClicked
    }

    private fun resetOptionMenu() {

        isLongClick(false)
        activity?.invalidateOptionsMenu()

    }


    override fun deleteGoals() {

        val cursor = getRecyclerAdapter().cursor!!

       for(item in  getRecyclerAdapter()
               .selectedPositions) {


           if (cursor.count == 0) break
           if(cursor.isAfterLast) break

           cursor.moveToPosition(item)

           val id = cursor.getInt(cursor.getColumnIndex("_id"))

           activity?.contentResolver?.delete(GoalsConstant.GOAL_LIST_CONTENT_URI,
                   "_ID = ?", arrayOf(id.toString()))

           activity?.contentResolver?.delete(GoalsConstant.SUB_GOAL_CONTENT_URI,
                   "GoalId = ?", arrayOf(id.toString()))

       }

        replaceWithNoGoalFragment(cursor)


        resetOptionMenu()
        restartLoader()
    }

    private fun replaceWithNoGoalFragment(cursor: Cursor) {
        if (GoalActivityUtil.isLargeScreenAndLandscape(context)
               /* && cursor.count
                == getRecyclerAdapter().selectedPositions.size*/
                ) {

            val fragment2 = NoGoalDetailFragment()

            activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.item_detail_container, fragment2)
                    .commit()
        }
    }

}