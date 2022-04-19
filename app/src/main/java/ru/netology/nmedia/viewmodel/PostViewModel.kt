package ru.netology.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemory

class PostViewModel : ViewModel(), PostInteractionListener {

    private val repository: PostRepository = PostRepositoryInMemory()
    val data by repository::data //val data get() = repository.data

    val editPost = MutableLiveData<Post?>()

    fun onSaveButtonClicked(postContent: String) {
        val updatedPost = editPost.value?.copy(content = postContent) ?: Post(
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