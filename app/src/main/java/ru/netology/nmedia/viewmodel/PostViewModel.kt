package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.database.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryRoomImpl


class PostViewModel(application: Application) : AndroidViewModel(application),
    PostInteractionListener {

    private val repository: PostRepository = PostRepositoryRoomImpl(
        AppDb.getInstance(application).postDao
    )
    val data by repository::data

    val editPost = MutableLiveData<Post?>()

    fun onSaveButtonClicked(postContent: String) {
        val updatedPost = editPost.value?.copy(content = postContent)
            ?: Post(
                id = 0L,
                author = "",
                published = "",
                content = postContent,
                //likes = 10_000,
                likedByMe = false,
                //share = 1_299_999,
                sharedByMe = false,
                //views = 111_598,
                viewedByMe = false
            )
        repository.save(updatedPost)
        editPost.value = null
    }

    fun emptyPost(content: String) {
        Post(
            id = 0L,
            author = "",
            published = "",
            content = "",
            //likes = 10_000,
            likedByMe = false,
            //share = 1_299_999,
            sharedByMe = false,
            //views = 111_598,
            viewedByMe = false
        )
        editPost.value = null
    }


    // region PostInteractionListener implementation

    override fun onLike(post: Post) {
        repository.likeById(post.id)
    }

    override fun onShare(post: Post) {
        repository.shareById(post.id)
    }

    override fun onRemove(post: Post) {
        repository.removeById(post.id)
    }

    override fun onEdit(post: Post) {
        editPost.value = post
    }


    // endregion PostInteractionListener implementation

}