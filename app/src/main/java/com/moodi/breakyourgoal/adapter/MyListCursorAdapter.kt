package com.moodi.breakyourgoal.adapter


import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moodi.breakyourgoal.R
import com.moodi.breakyourgoal.common.GoalsConstant
import com.moodi.breakyourgoal.goaldetail.ItemDetailActivity
import com.moodi.breakyourgoal.goaldetail.ItemDetailFragment
import com.moodi.breakyourgoal.goallist.ItemListActivity
import com.moodi.breakyourgoal.util.GoalActivityUtil
import kotlinx.android.synthetic.main.item_detail.view.*
import kotlinx.android.synthetic.main.item_list_content.view.*


/**
 * Created by ningappamoodi on 28/7/17.
 */

class MyListCursorAdapter : CursorRecyclerViewAdapter<MyListCursorAdapter.ViewHolder> {


    var context: Context? = null


    var statusMap: Map<String, Int>? = HashMap<String, Int>()


    var selectedPosition: Int? = null
    var newDataPosition: Int? = null

    val selectedPositions: ArrayList<Int> = ArrayList<Int>()

    constructor(context: Context, cursor: Cursor) : super(context, cursor)  {


        this.context = context

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
       /* override fun onClick(p0: View?) {


        }*/

        val view: View = view

       /* override fun onLongClick(p0: View?): Boolean {

            return true
        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent!!.getContext())
                .inflate(R.layout.item_list_content, parent, false)
        val vh = ViewHolder(itemView)
        return vh
    }



   override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

       Log.d("GOAL", "onBindViewHolder")

        super.cursor?.moveToPosition(position)
        val myListItem = MyListItem.fromCursor(super.cursor!!)

       Log.d("GOAL", "newDataPosition: " + newDataPosition)
       Log.d("GOAL", "viewHolder.goalId.text.toString() : "
               + viewHolder.view.list_item_goalId.text.toString())

       setValues(viewHolder, myListItem)


       val card =  viewHolder.view.list_item_row as CardView

       val color = ContextCompat.getColor(context, android.R.color.white)
       card.setCardBackgroundColor(color)
       (context as ItemListActivity).invalidateOptionsMenu()

       largeScreenSetup(position, card, viewHolder)


       setColors(myListItem, viewHolder)


        setOnClickListener(viewHolder)

       setOnLongClickListener(viewHolder, position)


    }

    private fun largeScreenSetup(position: Int, card: CardView, viewHolder: ViewHolder) {
        if (GoalActivityUtil.isLargeScreenAndLandscape(context!!.resources)) {

            setBackGroundColor(position, card, viewHolder)
        }
    }

    private fun setValues(viewHolder: ViewHolder, myListItem: MyListItem) {
        viewHolder.view.list_item_goalId.setText(myListItem.goalId)
        viewHolder.view.list_item_goal_name.setText(myListItem.goalName)
        viewHolder.view.list_item_goal_category.setText(myListItem.goalCategory)
        viewHolder.view.list_item_goal_duration.setText(myListItem.goalDuration)
        viewHolder.view.list_item_date.setText(myListItem.goalDate)
    }

    private fun setBackGroundColor(position: Int, card: CardView, viewHolder: ViewHolder) {
        if (position == selectedPosition) {

            card.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPurple_50))

        } else if (!TextUtils.isEmpty(viewHolder.view.list_item_goalId.text.toString()) &&
                viewHolder.view.list_item_goalId.text.toString().toInt() == newDataPosition) {

            card.isSelected = true
            card.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPurple_50))
            newDataPosition = null
        } else {

            card.isSelected = false
            card.setCardBackgroundColor(Color.WHITE)

        }
    }

    private fun setColors(myListItem: MyListItem, viewHolder: ViewHolder) {
        var value = statusMap?.get(myListItem.goalId + ":" + GoalsConstant.IN_PROGRESS)
        if (value == null) value = 0

        viewHolder.view.list_item_goal_status_inprogress_txt.setText(value.toString())


        var value1 = statusMap?.get(myListItem.goalId + ":" + GoalsConstant.COMPLETED)
        if (value1 == null) value1 = 0

        viewHolder.view.list_item_goal_status_completed_txt.setText(value1.toString())


        var value2 = statusMap?.get(myListItem.goalId + ":" + GoalsConstant.OPEN)
        if (value2 == null) value2 = 0

        viewHolder.view.list_item_goal_status_open_txt.setText(value2.toString())


        val totalCount: Int = value + value1 + value2

        var valueFloat: Float = 0f
        if (totalCount == 0) {
            value = 0
        } else {
            valueFloat = (((value / 2.0f) + (value1 * 1)) / totalCount) * 100f
        }

        val valueInt = valueFloat.toInt()
        viewHolder.view.list_item_goal_percentage.setText(valueInt.toString() + "%")

        if (valueInt >= 80) {

            val color = ContextCompat.getColor(context, R.color.colorGreen)
            viewHolder.view.list_item_goal_percentage.setTextColor(color)
        } else if (valueInt >= 50 && valueInt < 80) {

            val color = ContextCompat.getColor(context, R.color.colorOrange)
            viewHolder.view.list_item_goal_percentage.setTextColor(color)
        } else {

            val color = ContextCompat.getColor(context, R.color.colorGrey)
            viewHolder.view.list_item_goal_percentage.setTextColor(color)
        }
    }

    private fun  setOnClickListener(viewHolder: ViewHolder) {

        viewHolder.view.setOnClickListener(object : View.OnClickListener {

            override fun onClick(p0: View?) {

                onListItemClick(p0)

            }

            private fun onListItemClick(p0: View?) {
                if (GoalActivityUtil.isLargeScreenAndLandscape(
                        (context as ItemListActivity)!!.resources)) {
                    addFragment(p0)

                } else {

                    callActivity(p0)
                }
            }
        })
    }

    private fun callActivity(p0: View?) {
        Log.i("GOAL", "Inside onCLick!!")
        val intent = Intent(context, ItemDetailActivity::class.java)
        val goalId = p0?.list_item_goalId?.text
        intent.putExtra("GoalId", goalId)
        context?.startActivity(intent)
    }

    private fun addFragment(p0: View?) {
        // Create the detail fragment and add it to the activity
        // using a fragment transaction.
        val arguments = Bundle()

        arguments.putString("GoalId", p0!!.list_item_goalId.text.toString())
        arguments.putString("goalName", p0!!.list_item_goal_name.text.toString())

        val fragment = ItemDetailFragment()
        fragment.arguments = arguments
        (context as ItemListActivity)!!.supportFragmentManager.beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit()
    }


    private fun setOnLongClickListener(viewHolder: ViewHolder, position: Int) {
        viewHolder.view.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(var1: View): Boolean {

              val color = ContextCompat.getColor(context, R.color.colorPurple_50)
                val colorWhite = ContextCompat.getColor(context, android.R.color.white)

                /*  viewHolder.view.setBackgroundColor(color)*/

                val card =  viewHolder.view.list_item_row as CardView


                setColor(card, colorWhite, color)

                invalidateOptionsMenu()

                return true
            }

            private fun setColor(card: CardView, colorWhite: Int, color: Int) {
                if (selectedPositions.contains(position)) {
                    card.setCardBackgroundColor(colorWhite)
                    selectedPositions.remove(position)
                } else {
                    card.setCardBackgroundColor(color)
                    selectedPositions.add(position)
                }
            }

            private fun invalidateOptionsMenu() {
                if (selectedPositions.size == 0) {
                    (context as ItemListActivity).presenter!!.isLongClick(false)
                    (context as ItemListActivity).invalidateOptionsMenu()
                } else if (selectedPositions.size == 1) {


                    (context as ItemListActivity).presenter!!.isLongClick(true)
                    (context as ItemListActivity).invalidateOptionsMenu()
                }
            }
        })
    }

    public fun clearData() {

        selectedPositions.clear()
    }
}