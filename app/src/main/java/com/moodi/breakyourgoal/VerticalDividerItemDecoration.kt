package com.moodi.breakyourgoal

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by ningappamoodi on 30/8/17.
 */
class VerticalDividerItemDecoration : RecyclerView.ItemDecoration {

    private var verticalOrientation = true
    private var space: Int? = null

    constructor (value: Int, verticalOrientation: Boolean) : super(){

        this.space = value
        this.verticalOrientation = verticalOrientation
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                       state: RecyclerView.State) {
        // skip first item in the list
        if (parent.getChildAdapterPosition(view) != 0) {

            if (verticalOrientation) {

                outRect.set(0, space!!, 0, 0)

            } else if (!verticalOrientation) {

                outRect.set(space!!, 0, 0, 0)
            }
        }
    }
}