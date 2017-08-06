package com.moodi.breakyourgoal

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import android.util.Log

/**
 * Created by ningappamoodi on 26/7/17.
 */
class GoalDatabaseHelper(context: Context) : SQLiteOpenHelper(context,
        "Goals.db", null, 2) {


    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL("CREATE TABLE IF NOT EXISTS GoalList(_id INTEGER PRIMARY KEY, GoalName VARCHAR,Category VARCHAR, "
                + "Duration VARCHAR, FromDate VARCHAR, ToDate VARCHAR);")

        db?.execSQL("CREATE TABLE IF NOT EXISTS SubGoal(_id INTEGER PRIMARY KEY, SubGoalName VARCHAR,Status VARCHAR, "
                + "GoalId VARCHAR, TargetDate VARCHAR);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

        db?.execSQL("DROP TABLE IF EXISTS GoalList")
        db?.execSQL("DROP TABLE IF EXISTS SubGoal")
        onCreate(db)
    }


    fun getData(id: Int): Cursor {
        val db = this.readableDatabase
        val res = db.rawQuery("select * from GoalList where id=" + id + "", null)
        return res
    }
}