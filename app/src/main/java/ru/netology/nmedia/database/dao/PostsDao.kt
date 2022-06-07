package ru.netology.nmedia.database.dao

import ru.netology.nmedia.dto.Post

interface PostsDao {
    fun getAll(): List<Post>
    fun save(post: Post): Post
    fun likeById(id: Long)
    fun removeById(id: Long)
    fun shareById(id: Long)
}