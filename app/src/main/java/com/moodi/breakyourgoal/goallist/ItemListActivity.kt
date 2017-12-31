package com.moodi.breakyourgoal.goallist

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.moodi.breakyourgoal.R
import com.moodi.breakyourgoal.common.GoalsConstant
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*
import android.support.v4.view.ViewCompat.setAlpha
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity(),
        GoalListViewI {

     var presenter: GoalListPresenterI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        presenter = GoalListPresenterImpl(this)
        presenter?.addFragment()

        onClickFab()

        presenter?.setupRecyclerView(item_list as RecyclerView)

        presenter?.loaddata()
    }

    override fun onResume() {

        presenter?.restartLoader()
        super.onResume()
    }

    override fun onPause() {

        presenter?.isLongClick(false)
        presenter?.getRecyclerAdapter()?.clearData()
        super.onPause()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        Log.d("GOAL", " ###### onNewIntent: ")

        presenter?.setupRecyclerView(item_list as RecyclerView)

        presenter?.loaddata()
    }

    override fun onClickFab() {

        fab.setOnClickListener { _ ->
            presenter?.addGoal()
        }
    }


    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        Log.d("GOAL", "Inside onConfigurationChanged!! ")
        Log.d("GOAL", "newConfig?.orientation : " + newConfig?.orientation)

        presenter?.removeDetailedFragment(newConfig)

    }

    override fun onDestroy() {
        super.onDestroy()

        presenter?.removeDetailedFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {


        menuInflater.inflate(R.menu.menu_home, menu)


        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        val item = menu?.findItem(R.id.action_delete)

        val color = ContextCompat.getColor(this, android.R.color.white)

        val drawable = item?.getIcon()
        if (drawable != null) {
            // If we don't mutate the drawable, then all drawable's with this id will have a color
            // filter applied to it.
            drawable!!.mutate()
            drawable!!.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            drawable!!.setAlpha(255)
        }

       if(presenter!!.getIsLongClick())
           item?.setVisible(true)
        else
           item?.setVisible(false)

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId) {

            R.id.action_delete ->
                presenter?.deleteGoals()




            else ->  false

        }

        return false
    }

}