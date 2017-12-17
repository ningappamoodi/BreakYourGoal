package com.moodi.breakyourgoal.adapter


import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.database.*
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import com.moodi.breakyourgoal.R
import com.moodi.breakyourgoal.common.GoalsConstant
import com.moodi.breakyourgoal.goallist.ItemListActivity


/**
 * Created by ningappamoodi on 28/7/17.
 */

class SubGoalCursorAdapter: CursorRecyclerViewAdapter<SubGoalCursorAdapter.ViewHolder> {

    var context: Context? = null
    var cursor1: Cursor? = null

    var mRecyclerView: RecyclerView?  = null

    var goalId: String? = null
    var extras: MatrixCursor? = null

    var activity: ItemListActivity? = null

    constructor(context: Context, cursor: Cursor, activity: ItemListActivity?) : super(context, cursor)  {


        this.context = context
        this.cursor1 = cursor
        this.activity = activity
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var subGoalName: TextView
        var subGoalId: TextView
        var targetDate: TextView
        var statusTxt: TextView

        var editSubGoal: ImageView
        var deleteSubGoal: ImageView
        var statusImg: ImageView

        init {
            subGoalName = view.findViewById(R.id.subgoal_item_goal_name)
            subGoalId = view.findViewById(R.id.subgoal_id)
            editSubGoal = view.findViewById(R.id.subgoal_item_edit)
            deleteSubGoal = view.findViewById(R.id.subgoal_item_delete)
            targetDate = view.findViewById(R.id.subgoal_item_goal_duration)
            statusTxt = view.findViewById(R.id.subgoal_item_status_txt)
            statusImg = view.findViewById(R.id.subgoal_item_status)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {

        Log.d("GOAL", "SubGoalCursorAdapter : onCreateViewHolder : ")

        val itemView = LayoutInflater.from(parent!!.getContext())
                .inflate(R.layout.item_list_subgoal, parent, false)
        val vh = ViewHolder(itemView)
        return vh
    }

   override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

       Log.d("GOAL", "onBindViewHolder")

        super.cursor?.moveToPosition(position)
        val myListItem = SubGoalListItem.fromCursor(super.cursor!!)

        viewHolder.subGoalName.setText(myListItem.subGoalName)
       viewHolder.subGoalId.setText(myListItem.subGoalId)
       viewHolder.targetDate.setText(myListItem.targetDate)
       viewHolder.statusTxt.setText(myListItem.statusTxt)

       Log.d("GOAL", "statusTxt : " + myListItem.statusTxt)
       var color: Int? = null
       when(myListItem.statusTxt) {

           GoalsConstant.POSTPONED ->  color = ContextCompat.getColor(context, R.color.colorRed)
           GoalsConstant.IN_PROGRESS -> color = ContextCompat.getColor(context, R.color.colorOrange)
           GoalsConstant.COMPLETED ->   color = ContextCompat.getColor(context, R.color.colorGreen)
       }

       if(color != null) {
           viewHolder.statusImg.setColorFilter(color)
       }


       viewHolder.editSubGoal.setOnClickListener(object : View.OnClickListener {
           override fun onClick(p0: View?) {

               Log.d("GOAL", "editSubGoal onClick listner")


               val builder =  AlertDialog.Builder(context!!)
               // Set the dialog title
               builder.setTitle("Status").setSingleChoiceItems(R.array.array_sub_goal_status,
                       R.layout.select_dialog_singlechoice_material, object :
                       DialogInterface.OnClickListener {
                   override fun onClick(dialog: DialogInterface?, p1: Int) {

                       val lv = (dialog as AlertDialog).listView
                       lv.tag = p1
                   }

               }).setPositiveButton("OK", object : DialogInterface.OnClickListener {
                   override fun onClick(dialog: DialogInterface?, p1: Int) {

                       val lv = (dialog as AlertDialog).listView
                       val selected = lv.tag as Int
                       if (selected != null) {
                           val statusArray = context!!.resources.
                                   getStringArray(R.array.array_sub_goal_status)

                           Log.d("GOAL", "statusArray  : "+
                                   statusArray.size)

                           Log.d("GOAL", "selected array : "+
                                   statusArray.get(selected))

                           val values = ContentValues()
                           values.put("Status", statusArray.get(selected))

                           context!!.contentResolver.update(GoalsConstant.SUB_GOAL_CONTENT_URI,
                                   values, "_id=?", arrayOf(viewHolder.subGoalId.text.toString()))

                           var color: Int? = null
                           when(statusArray.get(selected)) {

                               GoalsConstant.OPEN ->        color = ContextCompat.getColor(context, R.color.colorGrey)
                               GoalsConstant.POSTPONED ->   color = ContextCompat.getColor(context, R.color.colorRed)
                               GoalsConstant.IN_PROGRESS -> color = ContextCompat.getColor(context, R.color.colorOrange)
                               GoalsConstant.COMPLETED ->   color = ContextCompat.getColor(context, R.color.colorGreen)
                           }

                           if(color != null) {
                               viewHolder.statusImg.setColorFilter(color!!)
                           }

                           Log.d("GOAL", "subgoal id : "
                                   + viewHolder.subGoalId.text.toString())

                           if(viewHolder.subGoalId.text.toString().toInt() < 0) {

                               val extras2 = MatrixCursor(arrayOf("_id", "SubGoalName", "GoalId", "Status", "TargetDate"))

                               if(extras != null) {

                                   extras!!.moveToFirst()
                                   while (!extras!!.isAfterLast) {


                                       if (!(TextUtils.equals(myListItem.subGoalId.toString(), extras!!.getString(0)))) {
                                           extras2.addRow(arrayOf(extras!!.getString(0), extras!!.getString(1),
                                                   extras!!.getString(2), extras!!.getString(3),
                                                   extras!!.getString(4)))
                                       } else {

                                           Log.d("GOAL", "myListItem.statusTxt.toString: " +
                                                   myListItem.statusTxt.toString())
                                           extras2.addRow(arrayOf(extras!!.getString(0), extras!!.getString(1),
                                                   extras!!.getString(2), statusArray.get(selected),
                                                   extras!!.getString(4)))
                                       }

                                       extras!!.moveToNext()
                                   }

                                   extras = extras2
                               }
                           }

                           Log.d("GOAL", "SubGoalCursorAdapter reload item list: "
                                   +  activity)
                           activity?.loaderManager?.restartLoader(GoalsConstant.SUBGOAL, null,
                                   activity!!.presenter!!.getLoader())
                           activity?.loaderManager?.restartLoader(GoalsConstant.GOAL, null,
                                   activity!!.presenter!!.getLoader())

                       }
                   }

               }).setNegativeButton("CANCEL", object : DialogInterface.OnClickListener {
                   override fun onClick(p0: DialogInterface?, p1: Int) {

                   }

               })

               builder.create().show()

           }
       })
       viewHolder.deleteSubGoal.setOnClickListener(object : View.OnClickListener {
           override fun onClick(view: View?) {

               Log.d("GOAL", "deleteSubGoal onClick listner")

               Log.d("GOAL", "subGoalId value: " + myListItem.subGoalId.toString())


               context!!.contentResolver.delete(GoalsConstant.SUB_GOAL_CONTENT_URI,"_id=?",
                       arrayOf(myListItem.subGoalId.toString()))


               mRecyclerView!!.post(object : Runnable {
                   override fun run() {

                       Log.d("GOAL", "id : " + view!!.id)
                       Log.d("GOAL", "verticalScrollbarPosition : "
                               + view!!.verticalScrollbarPosition)

                       var c: Cursor? = null
                       if(goalId != null) {
                           val projection = arrayOf("_id", "SubGoalName", "GoalId", "Status", "TargetDate")
                           c = context!!.contentResolver.query(GoalsConstant.SUB_GOAL_CONTENT_URI, projection,
                                   "GoalId=?", arrayOf(goalId), null)
                       }

                       val extras2 = MatrixCursor(arrayOf("_id", "SubGoalName", "GoalId", "Status", "TargetDate"))
                     //  var isDataChanged: Boolean = false
                       if(extras != null) {

                           extras!!.moveToFirst()
                           while (!extras!!.isAfterLast) {

                               Log.d("GOAL", "subGoalId: " + myListItem.subGoalId.toString())
                               Log.d("GOAL", "_id: " + extras!!.getString(0))
                               if (!(TextUtils.equals(myListItem.subGoalId.toString(), extras!!.getString(0)))) {
                                   extras2.addRow(arrayOf(extras!!.getString(0), extras!!.getString(1),
                                           extras!!.getString(2), extras!!.getString(3),
                                           extras!!.getString(4)))
                                  // isDataChanged = true
                               }

                               extras!!.moveToNext()
                           }

                           extras = extras2
                       }



                       var cursors: Array<Cursor>? = null
                       if(extras2 != null) {

                           if(c != null) {
                                cursors = arrayOf<Cursor>(extras2!!, c)
                           } else {
                                cursors = arrayOf<Cursor>(extras2!!)
                           }
                               val extendedCursor = MergeCursor(cursors)
                               swapCursor(extendedCursor)

                       } else {
                           swapCursor(c!!)
                       }

                       notifyItemRemoved(position)
                   }
               })

           }
       })

    }

   override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }
}