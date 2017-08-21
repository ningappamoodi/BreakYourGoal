package com.moodi.breakyourgoal

import android.app.ActionBar
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.content.res.Resources
import android.database.Cursor
import android.database.MatrixCursor
import android.database.MergeCursor
import android.graphics.Color
import android.net.Uri

import android.support.design.widget.CollapsingToolbarLayout
import android.os.Bundle
import android.preference.MultiSelectListPreference
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.support.v4.content.CursorLoader
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import android.widget.*
import kotlinx.android.synthetic.main.item_detail.*
import java.text.DateFormat
import java.text.SimpleDateFormat


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


    var goalId: String? = null

    var subGoalList: RecyclerView? = null

    var idGoalTitle: TextView? = null

    var itemDetailDuration: TextView? = null
    var itemDetailCategory: TextView? = null
    var itemDetailGoalFromDate: TextView? = null
    var itemDetailGoalToDate: TextView? = null

    var subGoalCount: TextView? = null;

    lateinit var recyclerAdapter : SubGoalCursorAdapter

    val subGoalDialogFragment  = SubGoalDialogFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Log.d("GOAL", "In ItemDetailFragment onCreate")

            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
           // mItem = DummyContent.ITEM_MAP[arguments.getString(ARG_ITEM_ID)]

        if ((resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
                == Configuration.SCREENLAYOUT_SIZE_XLARGE &&
                resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) ||
                (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
                        == Configuration.SCREENLAYOUT_SIZE_LARGE&&
                        resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)) {

            goalId = arguments.getString("GoalId")
            Log.d("GOAL", "goalId for landscape: " + goalId)

        }
        else {

            goalId = activity.intent.getStringExtra("GoalId")

            Log.d("GOAL", "goalId: " + goalId)

           // val activity = this.activity

        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val cursor = activity.contentResolver.query(GoalsConstant.GOAL_LIST_CONTENT_URI,
                arrayOf("max(_id)"), null, null, null)

        cursor.moveToFirst()

        if ((resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
                == Configuration.SCREENLAYOUT_SIZE_XLARGE &&
                resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) ||
                (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
                        == Configuration.SCREENLAYOUT_SIZE_LARGE &&
                        resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)) {

            val fragmentItemDetail = fragmentManager.findFragmentById(R.id.item_detail_container)
                    as ItemDetailFragment
            goalId = fragmentItemDetail.goalId
        } else {

            goalId = activity.intent.getStringExtra("GoalId")
        }

        Log.d("GOAL", "ItemDetailFragment onActivityCreated goalId: "
                + goalId)
        Log.d("GOAL", "Orientation : " + resources.configuration.orientation)

        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            val sharedPref = activity.getSharedPreferences("GOALS", AppCompatActivity.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("GoalId", goalId)
            editor.commit()
            Log.d("GOAL", "goalId for landscape from shared prefs : "
                    + sharedPref.getString("GoalId", null))
        }

        Log.d("GOAL", "onActivityCreated goalId: " + goalId)

        val addSubGoalBtn = activity.findViewById<Button>(R.id.item_detail_add_sub_goal)
        //val subGoalDatePicker = activity.findViewById<EditText>(R.id.subgoal_date)

        if (addSubGoalBtn == null) {
            Log.d("GOAL", "addSubGoalBtn: " + addSubGoalBtn)
        }

        //val subGoalDialogFragment = SubGoalDialogFragment()


        addSubGoalBtn?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                subGoalDialogFragment.show(fragmentManager, "subGoalDialogFragment")
            }
        })



        loaderManager.initLoader( GoalsConstant.GOAL, null, this)
        loaderManager.initLoader( GoalsConstant.SUBGOAL, null, this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.item_detail, container, false)

        idGoalTitle =  rootView.findViewById(R.id.goal_detail_title)
        subGoalCount =       rootView.findViewById(R.id.item_detail_subgoal_count)
        itemDetailGoalFromDate = rootView.findViewById(R.id.item_detail_goal_from_date)
        itemDetailGoalToDate = rootView.findViewById(R.id.item_detail_goal_to_date)
        itemDetailCategory = rootView.findViewById(R.id.item_detail_category)
        itemDetailDuration = rootView.findViewById(R.id.item_detail_duration)

            subGoalList = rootView.findViewById<RecyclerView>(R.id.item_detail_list)
            subGoalList?.setLayoutManager( LinearLayoutManager(context))

        return rootView
    }


    override fun onDetach() {
        super.onDetach()


    }
    override fun onLoaderReset(loader: Loader<Cursor>?) {

         val cursorLoader = loader as CursorLoader
        recyclerAdapter.swapCursor(cursorLoader.loadInBackground())

    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {

        when(loader?.id) {
            GoalsConstant.GOAL -> {

                data?.moveToFirst()

                val df: DateFormat = SimpleDateFormat("dd/MM/yyyy")
                val df1: DateFormat = SimpleDateFormat("dd-MMM-yy")

                val date = df.parse(data?.getString(4))
                val date1 = df.parse(data?.getString(5))

                idGoalTitle?.text = data?.getString(1)
                itemDetailGoalFromDate?.text = df1.format(date)
                itemDetailGoalToDate?.text =   df1.format(date1)

                itemDetailCategory?.text = data?.getString(2)
                itemDetailDuration?.text = data?.getString(3)
            }

            GoalsConstant.SUBGOAL -> {


                displaySubGoalList(data!!)
            }
        }
    }



    override fun onCreateLoader(id: Int, bundle: Bundle?): Loader<Cursor> {

        var cursorLoader: CursorLoader? = null
        var projection: Array<String>? = null

                when(id) {
         GoalsConstant.GOAL -> {

             Log.d("GOAL", "In onCreateLoader: goalId: "
                     + goalId)
             projection = arrayOf("_id", "GoalName", "Category", "Duration", "FromDate", "ToDate")
             cursorLoader = CursorLoader(context, GoalsConstant.GOAL_LIST_CONTENT_URI,
                     projection, "_id=?", arrayOf(goalId), null)
         }
        GoalsConstant.SUBGOAL -> {

            projection = arrayOf("_id", "SubGoalName", "GoalId", "Status", "TargetDate")
            cursorLoader = CursorLoader(context, GoalsConstant.SUB_GOAL_CONTENT_URI,
                    projection, "GoalId=?", arrayOf(goalId), null)

        }


        }

        return cursorLoader!!
    }

    public fun displaySubGoalList(cursor: Cursor) {

        subGoalCount?.text = cursor.count.toString()

        Log.d("GOAL", "cursor count: " + cursor?.count)


        if (activity is ItemListActivity) recyclerAdapter = SubGoalCursorAdapter(context, cursor,
                activity as ItemListActivity)
        else recyclerAdapter = SubGoalCursorAdapter(context, cursor, null)

        recyclerAdapter.goalId = goalId

        subGoalList?.adapter = recyclerAdapter

        subGoalList?.setLayoutManager(LinearLayoutManager(context));

    }


}
