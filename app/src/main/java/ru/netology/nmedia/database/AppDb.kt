package ru.netology.nmedia.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.database.dao.PostsDao
import ru.netology.nmedia.database.dao.PostsDaoImpl

class AppDb private constructor(db: SQLiteDatabase) {
    val postDao: PostsDao = PostsDaoImpl(db)

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: AppDb(
                    buildDatabase(context, arrayOf(PostsTable.CREATE_SCRIPT))
                ).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context, createTablesScripts: Array<String>) = DbHelper(
            context, 1, "app.db", createTablesScripts,
        ).writableDatabase
    }
}
