package com.moodi.breakyourgoal

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.database.sqlite.SQLiteQueryBuilder
import android.database.sqlite.SQLiteDatabase
import android.content.ContentUris
import android.database.SQLException
import android.util.Log


/**
 * Created by ningappamoodi on 26/7/17.
 */
class GoalContentProvider : ContentProvider() {

    private var db: SQLiteDatabase? = null

    companion object {

        private  final val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)


        init {
            sUriMatcher.addURI("com.moodi.breakyourgoal.provider", "GoalList", 1);
            sUriMatcher.addURI("com.moodi.breakyourgoal.provider", "SubGoal", 3);
        }
    }
    override fun insert(uri: Uri?, cv: ContentValues?): Uri {

         var rowID: Long = 0

        when(sUriMatcher.match(uri)) {

          GoalsConstant.GOAL ->  rowID = db?.insert("GoalList", "", cv) as Long

            GoalsConstant.SUBGOAL ->  rowID = db?.insert("SubGoal", "", cv) as Long

        }


        /**
         * If record is added successfully
         */
        if (rowID > 0) {

            val _uri = ContentUris.withAppendedId(uri, rowID)
            context!!.contentResolver.notifyChange(_uri, null)
            return _uri
        }

        throw SQLException("Failed to add a record into " + uri)
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {

        Log.d("GOAL", "In Goal Content provider Query method")
        val qb = SQLiteQueryBuilder()


        var rowID: Long = 0

        when(sUriMatcher.match(uri)) {

            GoalsConstant.GOAL ->  qb.tables = "GoalList"
            GoalsConstant.GOAL_ID -> qb.tables = "GoalList"
            GoalsConstant.SUBGOAL ->  qb.tables = "SubGoal"

        }

       val c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder) as Cursor

        Log.d("GOAL", "In Goal Content provider Query method: Count : " + c.count)

        return c;
    }

    override fun onCreate(): Boolean {

        val dbHelper = GoalDatabaseHelper(context)

        db = dbHelper.getWritableDatabase() as SQLiteDatabase

        return if (db == null) false else true
    }

    override fun update(uri: Uri?, cv: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {

        var count = 0;
        when (sUriMatcher.match(uri)) {
            GoalsConstant.GOAL -> count = db!!.update("GoalList", cv, selection, selectionArgs)
            GoalsConstant.SUBGOAL -> count = db!!.update("SubGoal", cv, selection, selectionArgs)

        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;

    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        var count = 0 as Int
        when (sUriMatcher.match(uri)){
            GoalsConstant.GOAL -> count = db!!.delete("GoalList", selection, selectionArgs)
            GoalsConstant.SUBGOAL -> count = db!!.delete("SubGoal", selection, selectionArgs)
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    override fun getType(uri: Uri?): String {

        when (sUriMatcher.match(uri)) {
        /**
         * Get all student records
         */
            GoalsConstant.GOAL -> return "vnd.android.cursor.dir/vnd.com.moodi.breakyourgoal"
        /**
         * Get a particular student
         */
            GoalsConstant.SUBGOAL -> return "vnd.android.cursor.item/vnd.com.moodi.breakyourgoal"
            else -> throw IllegalArgumentException("Unsupported URI: " + uri)
        }
    }
}