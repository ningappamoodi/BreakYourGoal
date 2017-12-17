package com.moodi.breakyourgoal.dialogfragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_add_goal.*
import java.util.*

/**
 * Created by ningappamoodi on 12/8/17.
 */
class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    var editText: EditText? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(activity, this, year, month, day)
    }

    override fun onDateSet(datePicker: DatePicker, i: Int, i1: Int, i2: Int) {

        editText?.setText(i2.toString() + "/" + i1.toString() + "/" + i.toString())

    }


}