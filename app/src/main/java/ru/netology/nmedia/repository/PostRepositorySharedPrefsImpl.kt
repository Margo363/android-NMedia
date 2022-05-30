package ru.netology.nmedia.repository

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post
import kotlin.properties.Delegates

class PostRepositoryInFileImpl(private val application: Application) : PostRepository {


    private val gson = Gson()
    private val type = TypeToken.getParameterized(
        List::class.java, Post::class.java
    ).type

    private val prefs = application.getSharedPreferences(
        "repo", Context.MODE_PRIVATE
    )

    private var nextId by Delegates.observable(
        prefs.getLong(SHARED_PREFS_NEXT_ID_KEY, 1L)
    ) { _, _, newValue ->
        prefs.edit {
            putLong(SHARED_PREFS_NEXT_ID_KEY, newValue)
        }
    }

    private var posts
        get() = checkNotNull(data.value) {
            "Posts should be always not null"
        }
        set(value) {
            data.value = value
            application.openFileOutput(
                FILE_NAME, Context.MODE_PRIVATE
            ).bufferedWriter().use {
                it.write(gson.toJson(value))
            }
        }

    override val data by lazy {
        val file = application.filesDir.resolve(FILE_NAME)
        val posts: List<Post> = if (file.exists()) {
            application.openFileInput(FILE_NAME)
                .bufferedReader()
                .use { bufferedReader ->
                    gson.fromJson(bufferedReader, type)
                }
        } else emptyList()
        MutableLiveData(posts)
    }

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else
                if (it.likedByMe) it.copy(likedByMe = !it.likedByMe, likes = it.likes - 1) else
                    it.copy(likedByMe = !it.likedByMe, likes = it.likes + 1)
        }
        posts = posts
    }


    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(share = (it.share + 1))
        }
        posts = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filterNot {
            it.id == id
        }
    }

    private companion object {
        const val NEW_POST_ID = 0L
        const val FILE_NAME = "posts.json"
        const val SHARED_PREFS_NEXT_ID_KEY = "next"
    }

    override fun save(post: Post) = if (post.id == NEW_POST_ID) insert(post) else update(post)


    private fun insert(post: Post) {
        val identifiedPost = post.copy(id = nextId++)
        posts = listOf(identifiedPost) + posts
    }

    private fun update(post: Post) {
        posts = posts.map {
            if (it.id == post.id) post else it
        }
    }

}