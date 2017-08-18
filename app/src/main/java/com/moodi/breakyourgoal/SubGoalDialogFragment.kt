package com.moodi.breakyourgoal

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import kotlinx.android.synthetic.main.dialog_subgoal.*

/**
 * Created by ningappamoodi on 3/8/17.
 */

class SubGoalDialogFragment : DialogFragment(), DatePickerI {

    var subgoalId: TextView? = null
    var subgoalName: EditText? = null
    var subgoalDate: EditText? = null

    var subgoalAddBtn: Button? = null
    var subgoalCancelBtn: Button? = null

    val datePicker = this as DatePickerI

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = activity.layoutInflater.inflate(R.layout.dialog_subgoal, null)

        subgoalId = view.findViewById<TextView>(R.id.subgoal_id)
        subgoalName = view.findViewById<EditText>(R.id.subgoal_name)
        subgoalDate = view.findViewById<EditText>(R.id.subgoal_date)
        subgoalAddBtn = view.findViewById<Button>(R.id.subgoal_add)
        subgoalCancelBtn = view.findViewById<Button>(R.id.subgoal_cancel)

        val builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
        builder.setView(view)

        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)

        subgoalDate?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

                val newFragment = DatePickerFragment()

                newFragment.editText = subgoalDate
                newFragment.show(fragmentManager, "datePicker")

                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(p0?.windowToken, 0)
            }
        })


        subgoalAddBtn?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

                val fragment = fragmentManager.findFragmentById(R.id.item_detail_container)


                if(fragment is ItemDetailFragment) {
                    subgoalAdd(p0!!)

                }
                else if(fragment is AddGoalFragment) {
                    fragment.subgoalAdd(p0!!)
                }
            }
        })

        subgoalCancelBtn?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

               dismiss()
            }
        })

        return alertDialog
    }

    fun validate() : Boolean {

        var validate = true
        if (TextUtils.isEmpty(subgoalName!!.text.toString())) {

            validate = false
           subgoalName?.setHintTextColor(Color.RED)


        }
        if (TextUtils.isEmpty(subgoalDate!!.text.toString())) {

            validate = false
            subgoalDate?.setHintTextColor(Color.RED)


        }

        if(!validate) {
            return false
        }

        return true
    }

    override fun dateSet(datePicker: DatePicker, i: Int, i1: Int, i2: Int) {

        subgoalDate?.setText(i2.toString() + "/" + i1.toString() + "/" + i.toString())
    }

    fun subgoalAdd(view: View) {


        Log.i("GOAL", "Inside subgoalAdd!! ")

        Log.i("GOAL", "subgoalName: " + subgoalName?.text.toString())
        Log.i("GOAL", "subgoalName length: "
                + subgoalName?.text.toString().length)


        val fragmentItemDetail = fragmentManager.findFragmentById(R.id.item_detail_container)
                as ItemDetailFragment

        val values = ContentValues()
        values.put("SubGoalName", subgoalName?.text.toString())
        values.put("Status", "Open")
        values.put("GoalId", fragmentItemDetail.goalId)
        values.put("TargetDate", subgoalDate?.text.toString())

        activity.contentResolver.insert(GoalsConstant.SUB_GOAL_CONTENT_URI,values)

        if(!validate()) {

            return
        }

        fragmentItemDetail.loaderManager.restartLoader(GoalsConstant.SUBGOAL, null, fragmentItemDetail)

        Log.i("GOAL", "$$$$$$$$$$$$$ In item detail fragment is item list acivity? : "
                + (activity is ItemListActivity))

        if(activity is ItemListActivity) {
            activity.loaderManager.restartLoader(GoalsConstant.SUBGOAL, null,
                    activity as ItemListActivity)
            activity.loaderManager.restartLoader(GoalsConstant.GOAL, null, activity as ItemListActivity)
        }

        dismiss()

    }

}