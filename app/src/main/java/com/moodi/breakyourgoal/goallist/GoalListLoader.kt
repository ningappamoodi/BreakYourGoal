package com.moodi.breakyourgoal.goallist

import android.app.LoaderManager
import android.content.Context
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import com.moodi.breakyourgoal.common.GoalsConstant
import com.moodi.breakyourgoal.adapter.MyListItem

/**
 * Created by ningappamoodi on 14/9/17.
 */
class GoalListLoader: LoaderManager.LoaderCallbacks<Cursor> {

    var presenter: GoalListPresenterI? = null
    var context: Context? = null

    constructor(presenter: GoalListPresenterI, context: Context) {
       this.presenter = presenter
        this.context = context
    }

    override fun onCreateLoader(p0: Int, p1: Bundle?): Loader<Cursor> {

        var loader: Loader<Cursor>? = null

        when(p0) {
            GoalsConstant.GOAL -> {

                val projection = arrayOf("_id", "GoalName", "Category", "Duration", "FromDate", "ToDate")


                Log.d("GOAL", "Item list activity : On create loader method : ")

                loader = CursorLoader(context, GoalsConstant.GOAL_LIST_CONTENT_URI,
                        projection, null, null,null)
            }

            GoalsConstant.SUBGOAL -> {

                val projection = arrayOf("GoalId", "Status")

                loader = CursorLoader(context, GoalsConstant.SUB_GOAL_CONTENT_URI,
                        projection, null, null,null)
            }
        }

        return loader!!

    }

    override fun onLoadFinished(loader: Loader<Cursor>?, cursor: Cursor?) {

        when (loader?.id) {
            GoalsConstant.GOAL -> {

                Log.d("GOAL", "ItemListActivity : onLoadFinished: goal data")
                presenter!!.getRecyclerAdapter().swapCursor(cursor!!)
                presenter!!.getRecyclerAdapter().notifyDataSetChanged()

            }

            GoalsConstant.SUBGOAL -> {

                Log.d("GOAL", "onLoadFinished : SUBGOAL: cursor?.count: "
                        + cursor?.count)
                presenter!!.getRecyclerAdapter().statusMap = MyListItem.fromCursorForStatus(cursor!!)
                presenter!!.getRecyclerAdapter().swapCursor( presenter!!.getRecyclerAdapter().cursor!!)
            }
        }
    }

    override fun onLoaderReset(p0: Loader<Cursor>?) {
        //       recyclerAdapter!!.swapCursor(null!!)
        Log.d("GOAL", "In list activity onLoaderReset")
    }

}