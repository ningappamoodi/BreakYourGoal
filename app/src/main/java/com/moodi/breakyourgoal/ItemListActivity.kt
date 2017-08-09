package com.moodi.breakyourgoal

import android.app.LoaderManager
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.content.Loader
import android.database.Cursor
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.design.widget.FloatingActionButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.view.MotionEvent
import android.view.GestureDetector
import android.text.method.Touch.onTouchEvent






/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {


    lateinit var recyclerAdapter : MyListCursorAdapter

    override fun onLoadFinished(loader: Loader<Cursor>?, cursor: Cursor?) {

        when (loader?.id) {
            1 ->
                // The asynchronous load is complete and the data
                // is now available for use. Only now can we associate
                // the queried Cursor with the SimpleCursorAdapter.
                recyclerAdapter.swapCursor(cursor!!)
        }
    }

    override fun onLoaderReset(p0: Loader<Cursor>?) {
 //       recyclerAdapter!!.swapCursor(null!!)
        Log.i("GOAL", "#### In list activity onLoaderReset")
    }


    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var mTwoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        toolbar.title = title

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()*/

            val intent =  Intent(baseContext, AddGoalActivity::class.java)
            startActivity(intent)
        }

        val recyclerView = findViewById<View>(R.id.item_list)!!
        setupRecyclerView(recyclerView as RecyclerView)

        if (findViewById<View>(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true
        }

        loaderManager.initLoader(0, null, this)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {


        val projection = arrayOf("_id", "GoalName", "Category", "Duration", "FromDate", "ToDate")
        val selection = null
        val selectionArgs = null
        val sortOrder = null

        val c = contentResolver.query(GoalsConstant.GOAL_LIST_CONTENT_URI, projection, selection,
                selectionArgs, sortOrder)

        recyclerAdapter = MyListCursorAdapter(this, c)
        recyclerView.adapter = recyclerAdapter


       recyclerView.addOnItemTouchListener(RecyclerItemClickListner(this, object :RecyclerItemClickListner.OnItemClickListener {
           override fun onItemClick(view: View, position: Int) {
              Log.i("GOAL", "Inside addOnItemTouchListener")

               val goalId = view.findViewById<TextView>(R.id.list_item_goalId)
               val goalName = view.findViewById<TextView>(R.id.list_item_goal_name)

               val intent =  Intent(baseContext, ItemDetailActivity::class.java)
               intent.putExtra("GoalId", goalId.text.toString())
               intent.putExtra("goalName", goalName.text.toString())
               startActivity(intent)
           }

       }))

    }

    override fun onCreateLoader(p0: Int, p1: Bundle?): Loader<Cursor> {

        val projection = arrayOf("_id", "GoalName", "Category", "Duration", "FromDate", "ToDate")
        val selection = null
        val selectionArgs = null
        val sortOrder = null
        Log.i("GOAL", "################## Item list activity : On create loader method : ")

        return CursorLoader(this, GoalsConstant.GOAL_LIST_CONTENT_URI,
        projection, selection, null,"")
    }

}
class RecyclerItemClickListner :
        RecyclerView.OnItemTouchListener  {


    private var mListener: OnItemClickListener? = null

   public interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    var mGestureDetector: GestureDetector? = null

     constructor (context: Context, listener: OnItemClickListener) {
        mListener = listener

        mGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }
        })


    }

    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {

    }

    override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {

        val childView = rv!!.findChildViewUnder(e!!.getX(), e!!.getY())
        if (childView != null && mListener != null && mGestureDetector!!.onTouchEvent(e)) {
            mListener!!.onItemClick(childView, rv.getChildAdapterPosition(childView))
        }
        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }

}
