package ru.netology.nmedia.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.netology.nmedia.database.entity.PostEntity

interface PostsDao {

    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Insert
    fun insert(post: PostEntity)

    @Update
    fun update(post: PostEntity)

    @Query(
        """
        UPDATE posts SET
        likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
    """
    )
    fun likeById(id: Long)

    @Query("DELETE FROM posts WHERE id = :id")

    fun removeById(id: Long)

    @Query(
        """
        UPDATE posts SET
                share = share + 1
                WHERE id = :id
    """
    )

    fun shareById(id: Long)
}