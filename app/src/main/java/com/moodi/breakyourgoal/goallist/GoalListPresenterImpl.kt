package com.moodi.breakyourgoal.goallist

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.moodi.breakyourgoal.*
import com.moodi.breakyourgoal.adapter.MyListCursorAdapter
import com.moodi.breakyourgoal.addgoal.AddGoalActivity
import com.moodi.breakyourgoal.addgoal.AddGoalFragment
import com.moodi.breakyourgoal.common.GoalsConstant
import com.moodi.breakyourgoal.goaldetail.ItemDetailActivity
import com.moodi.breakyourgoal.goaldetail.ItemDetailFragment
import com.moodi.breakyourgoal.util.GoalActivityUtil
import com.moodi.breakyourgoal.util.VerticalDividerItemDecoration
import kotlinx.android.synthetic.main.item_list_content.view.*

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
            recyclerAdapter?.newDataPosition = sharedPref.getString("GoalId", null)?.toInt()
        }

        val editor = sharedPref.edit()
        editor.remove("GoalId")
        editor.commit()

        recyclerView.adapter = recyclerAdapter

        /*recyclerView.addOnItemTouchListener(RecyclerItemClickListner(activity!!, object :RecyclerItemClickListner.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Log.d("GOAL", "Inside addOnItemTouchListener")

                // view.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorGrey))
                view.isSelected = true
                recyclerAdapter!!.selectedPosition = position

                recyclerView.adapter.notifyDataSetChanged()


                Log.d("GOAL", "$$$$$$$$$$$$$ GoalId: " + view.list_item_goalId.text.toString())

                if (GoalActivityUtil.isLargeScreenAndLandscape(  activity!!.resources)) {
                    // Create the detail fragment and add it to the activity
                    // using a fragment transaction.
                    val arguments = Bundle()

                    arguments.putString("GoalId", view.list_item_goalId.text.toString())
                    arguments.putString("goalName",  view.list_item_goal_name.text.toString())

                    val fragment = ItemDetailFragment()
                    fragment.arguments = arguments
                    activity!!.supportFragmentManager.beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit()

                } else {
                    val intent = Intent(  activity?.baseContext, ItemDetailActivity::class.java)
                    intent.putExtra("GoalId", view.list_item_goalId.text.toString())
                    intent.putExtra("goalName",  view.list_item_goal_name.text.toString())
                    activity?.startActivity(intent)
                }
            }

          override  fun onItemLongClick(view: View, position: Int) {

              Log.d("GOAL", "$$$$$$$$$ onItemLongClick!! ")

            }

        }))*/

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

    private fun resetOpionMenu() {

        isLongClick(false)
        activity?.invalidateOptionsMenu()

    }


    override fun deleteGoals() {

        val cursor = getRecyclerAdapter().cursor!!
        val selectedIds: ArrayList<String>  = ArrayList<String>()
       for(item in  getRecyclerAdapter()
               .selectedPositions) {


           cursor.moveToPosition(item)

           val _id = cursor.getInt(cursor.getColumnIndex("_id"))

           selectedIds.add(_id.toString())
       }
        activity?.contentResolver?.delete(GoalsConstant.GOAL_LIST_CONTENT_URI,
                "_ID=?", selectedIds.toTypedArray())

        resetOpionMenu()
        restartLoader()
    }

}