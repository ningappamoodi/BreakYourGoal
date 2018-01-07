package com.moodi.breakyourgoal.goaldetail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.NavUtils
import android.view.MenuItem
import com.moodi.breakyourgoal.R
import com.moodi.breakyourgoal.dialogfragment.SubGoalDialogFragment
import com.moodi.breakyourgoal.goallist.ItemListActivity
import com.moodi.breakyourgoal.util.GoalActivityUtil
import kotlinx.android.synthetic.main.activity_item_detail.*

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [ItemListActivity].
 */


class ItemDetailActivity : AppCompatActivity() {

    var subGoalDialogFragment: SubGoalDialogFragment? = null
   public var goalId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)




      //  val toolbar = findViewById<View>(R.id.detail_toolbar) as Toolbar
        setSupportActionBar(detail_toolbar)

      /*  val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/

        // Show the Up button in the action bar.
        //val actionBar = supportActionBar


        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val arguments = Bundle()

            val fragment = ItemDetailFragment()
            fragment.arguments = arguments
            supportFragmentManager.beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit()
        }

        //val appBarLayout = findViewById<View>(R.id.toolbar_layout) as CollapsingToolbarLayout
        if (toolbar_layout != null) {
            // appBarLayout.title = mItem!!.get("list_item_goal_date")
            toolbar_layout.title = "Goal details"
            // appBarLayout.setExpandedTitleColor(resources.getColor(R.color.colorAccent, null))
        }

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (GoalActivityUtil.isLargeScreenAndLandscape(resources)) {

            val sharedPref = getSharedPreferences("GOALS", MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("ItemDetailFragment", "ItemDetail")
            editor.commit()

            onBackPressed()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, Intent(this, ItemListActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    public  fun itemAddSubGoal(view: View) {

        subGoalDialogFragment = SubGoalDialogFragment()
        subGoalDialogFragment!!.show(supportFragmentManager, "subGoalDialogFragment")

    }
}
