package com.moodi.breakyourgoal

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

/**
 * Created by ningappamoodi on 3/8/17.
 */

class SubGoalDialogFragment : DialogFragment() {

    var subgoalId: TextView? = null
    var subgoalName: EditText? = null
    var subgoalDate: EditText? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = activity.layoutInflater.inflate(R.layout.dialog_subgoal, null)

        subgoalId = view.findViewById<TextView>(R.id.subgoal_id)
        subgoalName = view.findViewById<EditText>(R.id.subgoal_name)
        subgoalDate = view.findViewById<EditText>(R.id.subgoal_date)

        val builder = AlertDialog.Builder(activity)
        builder.setCancelable(false)
        builder.setView(view)

        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)

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

}