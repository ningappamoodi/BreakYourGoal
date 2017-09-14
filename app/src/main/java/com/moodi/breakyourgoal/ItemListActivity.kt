package com.moodi.breakyourgoal

import android.app.LoaderManager
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.content.Loader
import android.content.res.Configuration
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.widget.TextView

import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.text.method.Touch.onTouchEvent
import android.view.*
import android.widget.AdapterView
import android.widget.FrameLayout


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {


    public lateinit var recyclerAdapter : MyListCursorAdapter




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

       // setHasOptionsMenu(true)

        val sharedPref = getSharedPreferences("GOALS", MODE_PRIVATE)


        Log.d("GOAL", "AddGoalFragment: " +  sharedPref.contains("AddGoalFragment"))
        Log.d("GOAL", "ItemDetailFragment: " +  sharedPref.contains("ItemDetailFragment"))

        Log.d("GOAL", "AddGoalFragment: " +  sharedPref.getString("AddGoalFragment", null))

       if( sharedPref.contains("AddGoalFragment")) {

           val arguments = Bundle()

           Log.d("GOAL", " In Home Activity fragment: ")
           // arguments.putString("GoalId", goalId.text.toString())
           //arguments.putString("goalName", goalName.text.toString())

           val fragment = AddGoalFragment()
           fragment.arguments = arguments
           supportFragmentManager.beginTransaction()
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
           supportFragmentManager.beginTransaction()
                   .replace(R.id.item_detail_container, fragment)
                   .commit()

           val editor = sharedPref.edit()
           editor.remove("ItemDetailFragment")
          // editor.remove("GoalId")
           editor.commit()

       }

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()*/
/*
            val intent =  Intent(baseContext, AddGoalActivity::class.java)
            startActivity(intent)*/

            Log.d("GOAL", " In Home Activity fab.setOnClickListener  : ")

            if ((resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
                    == Configuration.SCREENLAYOUT_SIZE_XLARGE &&
                    resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) ||
                    (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
                            == Configuration.SCREENLAYOUT_SIZE_LARGE &&
                            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)) {
                // Create the detail fragment and add it to the activity
                // using a fragment transaction.
                val arguments = Bundle()

                Log.d("GOAL", " In Home Activity fragment: ")
                 // arguments.putString("GoalId", goalId.text.toString())
                //arguments.putString("goalName", goalName.text.toString())

                val fragment = AddGoalFragment()
                fragment.arguments = arguments
                supportFragmentManager.beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit()

                recyclerAdapter.selectedPosition = null
                recyclerAdapter.notifyDataSetChanged()

            } else {

                Log.d("GOAL", " In Home Activity activity: ")
                val intent =  Intent(baseContext, AddGoalActivity::class.java)
                startActivity(intent)
            }
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

        loaderManager.initLoader(GoalsConstant.SUBGOAL, null, this)
        loaderManager.initLoader(GoalsConstant.GOAL, null, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
       // setIntent(intent)
        Log.d("GOAL", " ###### onNewIntent: ")

        val recyclerView = findViewById<View>(R.id.item_list)!!

        setupRecyclerView(recyclerView as RecyclerView)

        loaderManager.initLoader(GoalsConstant.SUBGOAL, null, this)
        loaderManager.initLoader(GoalsConstant.GOAL, null, this)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {

        val projection = arrayOf("_id", "GoalName", "Category", "Duration", "FromDate", "ToDate")
        val selection = null
        val selectionArgs = null
        val sortOrder = null

        val c = contentResolver.query(GoalsConstant.GOAL_LIST_CONTENT_URI, projection, selection,
                selectionArgs, sortOrder)

        recyclerView.addItemDecoration(VerticalDividerItemDecoration(20, true))

        recyclerAdapter = MyListCursorAdapter(this, c)

        val sharedPref = getSharedPreferences("GOALS", MODE_PRIVATE)

        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerAdapter.newDataPosition = sharedPref.getString("GoalId", null)?.toInt()
        }

        val editor = sharedPref.edit()
        editor.remove("GoalId")
        editor.commit()

        recyclerView.adapter = recyclerAdapter

       recyclerView.addOnItemTouchListener(RecyclerItemClickListner(this, object :RecyclerItemClickListner.OnItemClickListener {
           override fun onItemClick(view: View, position: Int) {
              Log.d("GOAL", "Inside addOnItemTouchListener")

              // view.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.colorGrey))
               view.isSelected = true
               recyclerAdapter.selectedPosition = position

               recyclerView.adapter.notifyDataSetChanged()

               val goalId = view.findViewById<TextView>(R.id.list_item_goalId)
               val goalName = view.findViewById<TextView>(R.id.list_item_goal_name)


               Log.d("GOAL", "$$$$$$$$$$$$$ GoalId: " + goalId.text.toString())

               if ((resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
                       == Configuration.SCREENLAYOUT_SIZE_XLARGE &&
                       resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) ||
                       (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
                               == Configuration.SCREENLAYOUT_SIZE_LARGE &&
                               resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)) {
                   // Create the detail fragment and add it to the activity
                   // using a fragment transaction.
                   val arguments = Bundle()

                   arguments.putString("GoalId", goalId.text.toString())
                   arguments.putString("goalName", goalName.text.toString())

                   val fragment = ItemDetailFragment()
                   fragment.arguments = arguments
                   supportFragmentManager.beginTransaction()
                           .replace(R.id.item_detail_container, fragment)
                           .commit()

               } else {
                   val intent = Intent(baseContext, ItemDetailActivity::class.java)
                   intent.putExtra("GoalId", goalId.text.toString())
                   intent.putExtra("goalName", goalName.text.toString())
                   startActivity(intent)
               }
           }

       }))

    }

    override fun onCreateLoader(p0: Int, p1: Bundle?): Loader<Cursor> {

        var loader: Loader<Cursor>? = null

        when(p0) {
            GoalsConstant.GOAL -> {

                val projection = arrayOf("_id", "GoalName", "Category", "Duration", "FromDate", "ToDate")


                Log.d("GOAL", "Item list activity : On create loader method : ")

                loader = CursorLoader(this, GoalsConstant.GOAL_LIST_CONTENT_URI,
                        projection, null, null,null)
            }

            GoalsConstant.SUBGOAL -> {

                val projection = arrayOf("GoalId", "Status")

                loader = CursorLoader(this, GoalsConstant.SUB_GOAL_CONTENT_URI,
                        projection, null, null,null)
            }
        }

        return loader!!

    }

    override fun onLoadFinished(loader: Loader<Cursor>?, cursor: Cursor?) {

        when (loader?.id) {
            GoalsConstant.GOAL -> {

        // The asynchronous load is complete and the data
        // is now available for use. Only now can we associate
        // the queried Cursor with the SimpleCursorAdapter.
                    //recyclerAdapter.newDataPosition = cursor!!.count - 1

                Log.d("GOAL", "ItemListActivity : onLoadFinished: goal data")
                    recyclerAdapter.swapCursor(cursor!!)
                    recyclerAdapter.notifyDataSetChanged()

        }

            GoalsConstant.SUBGOAL -> {

                Log.d("GOAL", "onLoadFinished : SUBGOAL: cursor?.count: "
                        + cursor?.count)
                recyclerAdapter.statusMap = MyListItem.fromCursorForStatus(cursor!!)
                recyclerAdapter.swapCursor(recyclerAdapter.cursor!!)
            }
        }
    }

    override fun onLoaderReset(p0: Loader<Cursor>?) {
        //       recyclerAdapter!!.swapCursor(null!!)
        Log.d("GOAL", "In list activity onLoaderReset")
    }


    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)


        Log.d("GOAL", "Inside onConfigurationChanged!! ")
        Log.d("GOAL", "newConfig?.orientation : " + newConfig?.orientation)


      if (newConfig?.orientation == Configuration.ORIENTATION_PORTRAIT) {

          val fragment = supportFragmentManager.findFragmentById(R.id.item_detail_container)

          if(fragment != null) {
              supportFragmentManager.beginTransaction().remove(fragment).commit()
          }
      }
    }

    override fun onDestroy() {
        super.onDestroy()

        val fragment = fragmentManager.findFragmentById(R.id.item_detail_container)

        if(fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
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

    /*override fun onCreateOptionsMenu(menu: Menu, inflater:MenuInflater): Boolean {
        //val inflater = getMenuInflater()
        inflater.inflate(R.menu.menu_home, menu)
        return true
    }*/

  //  override fun onM
}