package ru.netology.nmedia.database.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.database.PostsTable
import ru.netology.nmedia.dto.Post

class PostsDaoImpl(
    private val db: SQLiteDatabase
) : PostsDao {
    override fun getAll() =
        db.query(
            PostsTable.NAME,
            PostsTable.ALL_COLUMN_NAMES(),
            null, null, null, null,
            "${PostsTable.Column.ID.columnName} DESC"
        ).use { cursor ->
            List(cursor.count) {
                cursor.moveToNext()
                cursor.toPost()
            }
        }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            put(PostsTable.Column.AUTHOR.columnName, "Me")
            put(PostsTable.Column.CONTENT.columnName, post.content)
            put(PostsTable.Column.PUBLISHED.columnName, "now")
        }
        val id = if (post.id == 0L) db.insert(PostsTable.NAME, null, values)
        else {
            db.update(
                PostsTable.NAME,
                values,
                "${PostsTable.Column.ID} = ?",
                arrayOf(post.id.toString())
            )
            post.id
        }
        return db.query(
            PostsTable.NAME,
            PostsTable.ALL_COLUMN_NAMES(),
            "${PostsTable.Column.ID.columnName} = ?",
            arrayOf(id.toString()), null, null, null
        )
            .use {
                it.moveToNext()
                it.toPost()
            }
    }

    override fun likeById(id: Long) {
        db.execSQL(
            """
                UPDATE posts SET
                likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END, 
                likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
                WHERE id = ?;
            """.trimIndent(),
            arrayOf(id)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostsTable.NAME,
            "${PostsTable.Column.ID.columnName} = ?",
            arrayOf(id.toString())
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
            """
                UPDATE posts SET
                reposts = reposts + 1
                WHERE id = ?;
            """.trimIndent(),
            arrayOf(id))
    }


    private fun Cursor.toPost() = Post(
        id = getLong(getColumnIndexOrThrow(PostsTable.Column.ID.columnName)),
        author = getString(getColumnIndexOrThrow(PostsTable.Column.AUTHOR.columnName)),
        content = getString(getColumnIndexOrThrow(PostsTable.Column.CONTENT.columnName)),
        published = getString(getColumnIndexOrThrow(PostsTable.Column.PUBLISHED.columnName)),
        likedByMe = getInt(getColumnIndexOrThrow(PostsTable.Column.LIKED_BY_ME.columnName)) != 0,
        likes = getInt(getColumnIndexOrThrow(PostsTable.Column.LIKES.columnName)),
        share = getInt(getColumnIndexOrThrow(PostsTable.Column.SHARE.columnName)),
        views = getInt(getColumnIndexOrThrow(PostsTable.Column.VIEWS.columnName)),
    )
}