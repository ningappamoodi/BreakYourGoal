package com.moodi.breakyourgoal

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.database.MergeCursor
import android.net.Uri

import android.support.design.widget.CollapsingToolbarLayout
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import android.net.Uri.withAppendedPath
import android.support.v4.app.DialogFragment
import android.support.v4.content.CursorLoader
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.SimpleCursorAdapter
import java.util.*


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class ItemDetailFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {


    var subGoalDialogFragment: SubGoalDialogFragment? = null

    var adapter: SubGoalCursorAdapter? = null

    var goalId: String? = null

    var subGoalList: RecyclerView? = null


    //var subGoalList: RecyclerView? = null

    var extendedCursor: MergeCursor? = null
    var extras: MatrixCursor? = null

    var itemDetailDate: TextView? = null;

    lateinit var recyclerAdapter : SubGoalCursorAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments.containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
           // mItem = DummyContent.ITEM_MAP[arguments.getString(ARG_ITEM_ID)]



           // subGoalList = activity.findViewById<RecyclerView>(R.id.item_detail_list)
            //subGoalList?.setLayoutManager( LinearLayoutManager(activity))

            val activity = this.activity
            val appBarLayout = activity.findViewById<View>(R.id.toolbar_layout) as CollapsingToolbarLayout
            if (appBarLayout != null) {
               // appBarLayout.title = mItem!!.get("list_item_goal_date")
                appBarLayout.title = "GOALS TITLE"
            }
        }


       // subGoalList = activity.findViewById<RecyclerView>(R.id.item_detail_list)
      //  subGoalList?.setLayoutManager( LinearLayoutManager(activity))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        itemDetailDate = activity.findViewById(R.id.item_detail_date)

        val projection = arrayOf("_id", "SubGoalName", "GoalId", "Status", "TargetDate")
        val selection = null
        val selectionArgs = null
        val sortOrder = null

        val cursor = activity.contentResolver.query(GoalsConstant.GOAL_LIST_CONTENT_URI, arrayOf("max(_id)"), null, null, null)

        cursor.moveToFirst()

        val itemDetail  = activity as ItemDetailActivity
        goalId = itemDetail.goalId

        Log.i("GOAL", "########## onActivityCreated goalId: " + goalId)
        /*goalId = activity.findViewById<TextView>(R.id.goal_id)
        val maxId: Int = cursor.getInt(0) + 1

        Log.i("GOAL", "################ maxId: " + maxId)
        goalId?.text = maxId.toString()*/

        loaderManager.initLoader( GoalsConstant.GOAL, null, this)
        loaderManager.initLoader( GoalsConstant.SUBGOAL, null, this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.item_detail, container, false)

        // Show the dummy content as text in a TextView.
        if (mItem != null) {

           // @Suppress("INACCESSIBLE_TYPE")
           // (rootView.findViewById(R.id.item_detail) as TextView).text = mItem!!.details
        }

        return rootView
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {


    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {

        when(loader?.id) {
            GoalsConstant.GOAL -> {

                data?.moveToFirst()
                itemDetailDate?.text = data?.getString(1)
            }

            GoalsConstant.SUBGOAL -> {

                subGoalList(data!!)
            }
        }
    }



    override fun onCreateLoader(id: Int, bundle: Bundle?): Loader<Cursor> {

        var cursorLoader: CursorLoader? = null
        var baseUri: Uri? = null
        var projection: Array<String>? = null

                when(id) {
         GoalsConstant.GOAL -> {

            // baseUri = Uri.parse("content://com.moodi.breakyourgoal.provider/GoalList")
             Log.i("GOAL", "################ In onCreateLoader: goalId: "
                     + goalId)
             projection = arrayOf("_id", "GoalName", "Category", "Duration", "FromDate", "ToDate")
             cursorLoader = CursorLoader(activity, GoalsConstant.GOAL_LIST_CONTENT_URI,
                     projection, "_id=?", arrayOf(goalId), null)
         }
        GoalsConstant.SUBGOAL -> {

            projection = arrayOf("_id", "SubGoalName", "GoalId", "Status", "TargetDate")
            cursorLoader = CursorLoader(activity, GoalsConstant.SUB_GOAL_CONTENT_URI,
                    projection, null, null, null)

        }


        }

        return cursorLoader!!
    }

    public fun subGoalList(cursor: Cursor) {

        val projection = arrayOf("_id", "SubGoalName", "GoalId", "Status", "TargetDate")

        /*  val c = contentResolver.query(GoalsConstant.SUB_GOAL_CONTENT_URI, projection, "_id=?",
                  arrayOf(goalId!!.text.toString()), null)*/

        if (extras == null) {
            extras = MatrixCursor(arrayOf("_id", "SubGoalName", "GoalId", "Status", "TargetDate"))
        }

        extras?.addRow(arrayOf("-1", subGoalDialogFragment?.subgoalName?.text.toString(),
                goalId, "open", subGoalDialogFragment?.subgoalDate?.text.toString()))

        val cursors = arrayOf<Cursor>(cursor, extras!!)
        extendedCursor = MergeCursor(cursors)


        val from = arrayOf("_id", "SubGoalName")
        val to = intArrayOf(R.id.subgoal_id, R.id.subgoal_item_goal_name)

        // val adapter = SimpleCursorAdapter(this, R.layout.item_list_subgoal, extendedCursor, from, to, 0)

        Log.i("GOAL", "################# cursor count: " + extendedCursor?.count)
        recyclerAdapter = SubGoalCursorAdapter(activity, extendedCursor!!)

        if(recyclerAdapter == null) {
            Log.i("GOAL", "#### recyclerAdapter is null")
        }else {
            Log.i("GOAL", "#### recyclerAdapter is NOT null")
        }

        subGoalList?.adapter = recyclerAdapter

//        subGoalDialogFragment!!.dismiss()
    }


    /**
     * The dummy content this fragment is presenting.
     */
    private var mItem: Map<String, String>? = null




    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        val ARG_ITEM_ID = "item_id"
    }
}
