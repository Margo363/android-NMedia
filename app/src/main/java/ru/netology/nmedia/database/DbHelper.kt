package ru.netology.nmedia.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper (context: Context,
                dbVersion: Int,
                dbName: String,
                private val createTableScripts: Array<String>): SQLiteOpenHelper(context,dbName, null, dbVersion) {

    override fun onCreate(db: SQLiteDatabase) {
        createTableScripts.forEach(db::execSQL)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}