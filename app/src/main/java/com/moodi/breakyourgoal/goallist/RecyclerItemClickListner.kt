package com.moodi.breakyourgoal.goallist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 * Created by ningappamoodi on 14/9/17.
 */
class RecyclerItemClickListner :
        RecyclerView.OnItemTouchListener {

   // private val clicklistener: ClickListener? = null
    private val gestureDetector: GestureDetector? = null


    private var mListener: OnItemClickListener? = null

    public interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }

    var mGestureDetector: GestureDetector? = null

    constructor (context: Context, listener: OnItemClickListener) {
        mListener = listener

        mGestureDetector = GestureDetector(context, object :
                GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }
        })


    }

    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {

    }

    override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {

        val childView = rv!!.findChildViewUnder(e!!.getX(), e!!.getY())
        if (childView != null && mListener != null && mGestureDetector!!.onTouchEvent(e)) {
            mListener!!.onItemClick(childView, rv.getChildAdapterPosition(childView))
        }
        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }
}