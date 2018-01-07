package com.moodi.breakyourgoal.addgoal



import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatTextView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.moodi.breakyourgoal.R
import com.moodi.breakyourgoal.dialogfragment.DatePickerFragment
import com.moodi.breakyourgoal.util.GoalUtils
import kotlinx.android.synthetic.main.fragment_add_goal.*

/**
 * Created by ningappamoodi on 14/8/17.
 */
class AddGoalFragment : Fragment(), AddGoalViewI {


    var presenter: AddGoalPresenterI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       //setContentView(R.layout.fragment_add_goal)

        Log.i("GOAL", "########## AddGoalFragment: onCreate");

        presenter = AddGoalPresenterImpl(this)
        presenter!!.removeDialogFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        subgoalBtnClickListener()
        fromDateClickListener()
        toDateClickListener()
        saveSubGoalBtnClickListener()
        durationBtnClickListener()
        setFromDate()

        presenter!!.onActivityCreated()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.fragment_add_goal, container, false)
    }



    override fun onPause() {
        super.onPause()
        val ft = fragmentManager.beginTransaction()
        val prev = fragmentManager.findFragmentByTag("subGoalDialogFragment")
        if (prev != null) {
            (prev as DialogFragment).dismiss()
            ft.remove(prev)
            ft.addToBackStack(null)
        }
    }

    override fun subgoalBtnClickListener() {

        add_goal_add_subgoal_btn.setOnClickListener {
            presenter!!.getSubGoalDialogFragment().show(fragmentManager,
                    "subGoalDialogFragment")
        }
    }

    override fun durationBtnClickListener() {


        add_goal_duration.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                val spinnerDuration = p1 as AppCompatTextView

                when( spinnerDuration.text) {

                    "Hours" -> add_goal_to_date.setText(GoalUtils.getDurationDate(0))
                    "Days" -> add_goal_to_date.setText(GoalUtils.getDurationDate(2))
                    "Weeks" -> add_goal_to_date.setText(GoalUtils.getDurationDate(14))
                    "Months" -> add_goal_to_date.setText(GoalUtils.getDurationDate(60))
                    "Years" -> add_goal_to_date.setText(GoalUtils.getDurationDate(365))
                }
            }




        }


    }

   private fun setFromDate() {


      add_goal_from_date.setText(GoalUtils.getDurationDate(0))
    }

    override fun fromDateClickListener() {

        add_goal_from_date.setOnClickListener { p0 ->
            val newFragment = DatePickerFragment()

            newFragment.editText = add_goal_from_date
            newFragment.show(fragmentManager, "datePicker")

            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(p0?.windowToken, 0)
        }

    }

    override fun toDateClickListener() {


        add_goal_to_date.setOnClickListener { p0 ->
            val newFragment = DatePickerFragment()

            newFragment.editText = add_goal_to_date
            newFragment.show(fragmentManager, "datePicker")

            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(p0?.windowToken, 0)
        }
    }

    override fun saveSubGoalBtnClickListener() {

        add_goal_save_subgoal_btn.setOnClickListener { p0 -> presenter!!.saveGoal(p0!!) }
    }

   override fun setCategoryAdapter(arrayAdapter: ArrayAdapter<CharSequence>) {

       add_goal_category.adapter = arrayAdapter

    }

   override fun setDurationAdapter(arrayAdapter: ArrayAdapter<CharSequence>) {
       add_goal_duration.adapter = arrayAdapter
    }
}