package com.ntt.kchallenge.data.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ntt.kchallenge.data.model.User

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val CREATE_USER_TABLE = ("CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_PASSWORD + " TEXT" + ")")

    private val DROP_USER_TABLE = "DROP TABLE IF EXISTS $TABLE_USER"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DROP_USER_TABLE)
        onCreate(db)
    }

    fun addUser(user: User) {
        val contentValues = ContentValues()
        contentValues.put(COLUMN_USER_NAME, user.username)
        contentValues.put(COLUMN_PASSWORD, user.password)

        val db = this.writableDatabase
        db.insert(TABLE_USER, null, contentValues)
        db.close()
    }

    fun validateUser(username: String, password: String): Boolean {
        val columns = arrayOf(COLUMN_USER_ID)
        val selection = "$COLUMN_USER_NAME = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)

        val db = this.readableDatabase
        val cursor = db.query(TABLE_USER, columns, selection, selectionArgs, null, null, null)
        val count = cursor.count
        cursor.close()
        db.close()
        return count > 0
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "KChallengeDB"
        private val TABLE_USER = "user"
        private val COLUMN_USER_ID = "userId"
        private val COLUMN_USER_NAME = "username"
        private val COLUMN_PASSWORD = "password"
    }

}