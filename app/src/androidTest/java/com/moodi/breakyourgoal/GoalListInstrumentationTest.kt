package com.moodi.breakyourgoal

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.moodi.breakyourgoal.goallist.ItemListActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by ningappamoodi on 19/11/17.
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class GoalListInstrumentationTest {

    @Rule
    var mActivityRule = ActivityTestRule<ItemListActivity>(ItemListActivity::class.java)

    @Test
    fun testGoalList() {

        onView(withText("TextView")).check(matches(isDisplayed()))
    }
}