package com.example.fundoonotes.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, "notes.db", null, 1) {

    val COLUMN_DESC = "DESCRIPTION"
    val TABLE_NAME = "user_notes"
    val COLUMN_TITLE = "TITLE"
    val COLUMN_PRIORITY = "PRIORITY"
    val COLUMN_ARCHIVE = "ARCHIVE"
    val COLUMN_CREATED = "CREATED"
    val COLUMN_ID = "ID"

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableStatement = "CREATE TABLE $TABLE_NAME ($COLUMN_ID STRING(100) PRIMARY KEY, $COLUMN_TITLE STRING, $COLUMN_DESC STRING, $COLUMN_PRIORITY INT, $COLUMN_ARCHIVE INT, $COLUMN_CREATED STRING)"

        db?.execSQL(createTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}