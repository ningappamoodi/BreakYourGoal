package com.moodi.breakyourgoal


import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.database.Cursor
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.content_add_goal.*
import android.support.design.widget.CoordinatorLayout.Behavior.setTag
import android.support.v4.content.ContextCompat


/**
 * Created by ningappamoodi on 28/7/17.
 */

class SubGoalCursorAdapter: CursorRecyclerViewAdapter<SubGoalCursorAdapter.ViewHolder> {

    var context: Context? = null
    var cursor1: Cursor? = null

    var mRecyclerView: RecyclerView?  = null

    constructor(context: Context, cursor: Cursor) : super(context, cursor)  {


        this.context = context
        this.cursor1 = cursor
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var subGoalName: TextView
        var subGoalId: TextView
        var editSubGoal: ImageView
        var deleteSubGoal: ImageView
        var targetDate: TextView

        var statusTxt: TextView
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

        Log.i("GOAL", "######### SubGoalCursorAdapter : onCreateViewHolder : ")

        val itemView = LayoutInflater.from(parent!!.getContext())
                .inflate(R.layout.item_list_subgoal, parent, false)
        val vh = ViewHolder(itemView)
        return vh
    }

   override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

       Log.i("GOAL", "################# onBindViewHolder")

        super.cursor?.moveToPosition(position)
        val myListItem = SubGoalListItem.fromCursor(super.cursor!!)

        viewHolder.subGoalName.setText(myListItem.subGoalName)
       viewHolder.subGoalId.setText(myListItem.subGoalId)
       viewHolder.targetDate.setText(myListItem.targetDate)
       viewHolder.statusTxt.setText(myListItem.statusTxt)

       Log.i("GOAL", "########### statusTxt : " + myListItem.statusTxt)
       var color: Int? = null
       when(myListItem.statusTxt) {

           "Postpone" ->  color = ContextCompat.getColor(context, R.color.colorRed)
           "In Progress" -> color = ContextCompat.getColor(context, R.color.colorOrange)
           "Completed" ->   color = ContextCompat.getColor(context, R.color.colorGreen)
       }

       if(color != null) {
           viewHolder.statusImg.setColorFilter(color)
       }


       viewHolder.editSubGoal.setOnClickListener(object : View.OnClickListener {
           override fun onClick(p0: View?) {

               Log.i("GOAL", "##################### editSubGoal onClick listner")


               val builder =  AlertDialog.Builder(context!!);
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

                           Log.i("GOAL", "##################### statusArray  : "+
                                   statusArray.size)

                           Log.i("GOAL", "##################### selected array : "+
                                   statusArray.get(selected))

                           val values = ContentValues()
                           values.put("Status", statusArray.get(selected))

                           context!!.contentResolver.update(GoalsConstant.SUB_GOAL_CONTENT_URI,
                                   values, "_id=?", arrayOf(viewHolder.subGoalId.text.toString()))

                           var color: Int? = null
                           when(statusArray.get(selected)) {

                               "Postpone" ->  color = ContextCompat.getColor(context, R.color.colorRed)
                               "In Progress" -> color = ContextCompat.getColor(context, R.color.colorOrange)
                               "Completed" ->   color = ContextCompat.getColor(context, R.color.colorGreen)
                           }

                           if(color != null) {
                               viewHolder.statusImg.setColorFilter(color!!)
                           }

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

               Log.i("GOAL", "##################### deleteSubGoal onClick listner")

               Log.i("GOAL", "################## subGoalId value: " + myListItem.subGoalId.toString())
               context!!.contentResolver.delete(GoalsConstant.SUB_GOAL_CONTENT_URI,"_id=?",
                       arrayOf(myListItem.subGoalId.toString()))


               mRecyclerView!!.post(object : Runnable {
                   override fun run() {

                       Log.i("GOAL", "############ id : " + view!!.id)
                       Log.i("GOAL", "############ verticalScrollbarPosition : "
                               + view!!.verticalScrollbarPosition)

                       val projection = arrayOf("_id", "SubGoalName", "GoalId", "Status", "TargetDate")
                       val c = context!!.contentResolver.query(GoalsConstant.SUB_GOAL_CONTENT_URI, projection,
                               null, null, null)

                       swapCursor(c)
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