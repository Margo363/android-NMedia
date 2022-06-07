package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ru.netology.nmedia.database.dao.PostsDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.database.convert.toModel
import ru.netology.nmedia.database.convert.toPostEntity

class PostRepositoryRoomImpl(private val postsDao: PostsDao) : PostRepository {


    override val data: LiveData<List<Post>> by lazy {
        Transformations.map(postsDao.getAll()) { list ->
            list.map { postEntity ->
                postEntity.toModel()
            }
        }
    }


    override fun likeById(id: Long) {
        postsDao.likeById(id)
    }

    private fun insert(post: Post) {
        postsDao.insert(post.toPostEntity())
    }

    private fun update(post: Post) {
        postsDao.update(post.toPostEntity())
    }

    override fun shareById(id: Long) {
        postsDao.shareById(id)
    }

    override fun removeById(id: Long) {
        postsDao.removeById(id)
    }

    override fun save(post: Post) {
        if (post.id == 0L) insert(post) else update(post)
    }
}