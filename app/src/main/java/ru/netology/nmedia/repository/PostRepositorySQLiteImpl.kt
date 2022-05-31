package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.database.dao.PostsDao
import ru.netology.nmedia.dto.Post

class PostRepositorySQLiteImpl(private val dao: PostsDao) : PostRepository {
    private var posts = emptyList<Post>()
    override val data = MutableLiveData(posts)

    init {
        posts = dao.getAll()
        data.value = posts
    }


    fun getAll(): LiveData<List<Post>> = data


    override fun likeById(id: Long) {
        dao.likeById(id)
        data.value = dao.getAll()
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
        data.value = dao.getAll()
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) = if (post.id == 0L) insert(post) else update(post)

    private fun insert(post: Post) {
        val saved = dao.save(post)
        posts = listOf(saved) + posts
        data.value = posts
    }

    private fun update(post: Post) {
        val saved = dao.save(post)
        posts = posts.map {
            if (it.id != post.id) it else saved
        }
        data.value = posts
    }
}