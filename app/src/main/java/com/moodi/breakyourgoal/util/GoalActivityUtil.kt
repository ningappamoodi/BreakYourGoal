package com.moodi.breakyourgoal.util

import android.content.res.Configuration
import android.content.res.Resources

/**
 * Created by ningappamoodi on 14/9/17.
 */
class GoalActivityUtil {

    companion object {


        public fun isLargeScreenAndLandscape(resources: Resources): Boolean {

            if ((resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
                    == Configuration.SCREENLAYOUT_SIZE_XLARGE &&
                    resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) ||
                    (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
                            == Configuration.SCREENLAYOUT_SIZE_LARGE &&
                            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)) {

                return true
            }

            return false
        }
    }
}