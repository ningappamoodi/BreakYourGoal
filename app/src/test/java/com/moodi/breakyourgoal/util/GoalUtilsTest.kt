package com.moodi.breakyourgoal.util

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by ningappa on 2/1/18.
 */

@RunWith(JUnit4::class)
class GoalUtilsTest {

    @Test
    fun testDate() {

        val currentTimeMillis = System.currentTimeMillis()
        val hours = currentTimeMillis + ((60 * 60 * 1000) * 1)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy")

        println(" date : " + dateFormat.format(Date(hours)))

    }

    @Test
    fun testDate2() {

        val calendar = Calendar.getInstance()

        calendar.add(Calendar.MONTH, 2)

        println(" new date : " + calendar.time)
    }
    @Test
     fun testDurationDate() {

        var durationDate = GoalUtils.getDurationDate(0)
        println("durationDate Hours : " + durationDate)

         durationDate = GoalUtils.getDurationDate(2)
        println("durationDate Days : " + durationDate)

         durationDate = GoalUtils.getDurationDate(14)
        println("durationDate Weeks : " + durationDate)

         durationDate = GoalUtils.getDurationDate(60)
        println("durationDate Months : " + durationDate)

        durationDate = GoalUtils.getDurationDate(365)
        println("durationDate Years : " + durationDate)
    }
}