package ru.netology.nmedia.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemory
import ru.netology.nmedia.util.Event

class PostViewModel : ViewModel(), PostInteractionListener {

    private val repository: PostRepository = PostRepositoryInMemory()
    val data by repository::data

    private var editedPost = Post(
        id = 0L,
        author = "",
        published = "",
        content = "",
        //likes = 10_000,
        likedByMe = false,
        //share = 1_299_999,
        sharedByMe = false,
        //views = 111_598,
        viewedByMe = false,
        video = ""
    )

    private val empty = Post(
        id = 0L,
        author = "",
        published = "",
        content = "",
        //likes = 10_000,
        likedByMe = false,
        //share = 1_299_999,
        sharedByMe = false,
        //views = 111_598,
        viewedByMe = false,
        video = ""
    )

    private val edited = MutableLiveData(empty)

    val shareEvent = Event<String>()
    val navigateToNewPostScreen = Event<String>()
    val navigateToEditPostScreen = Event<String>()
    val navigateToVideoScreen = Event<String>()


    fun save() {
        val edited = checkNotNull(edited.value) {
            "Edited post should not be null"
        }
        repository.save(edited)
        this.edited.value = empty
    }

    fun changeContent(content: Pair<String?, String?>) {
        val textContent = content.first
        val videoContent = content.second
        if (edited.value?.content == textContent) {
            return
        }
        if (textContent != null) {
            editedPost = editedPost.copy(content = textContent)
        }
        if (videoContent != null) {
            editedPost = editedPost.copy(video = videoContent)
        }
        repository.save(editedPost)
        this.editedPost = empty
    }

    fun onnAddNewPostButtonClicked() {
        navigateToNewPostScreen.call()
    }


    // region PostInteractionListener implementation

    override fun onLike(post: Post) {
        repository.likeById(post.id)
    }

    override fun onShare(post: Post) {
        repository.shareById(post.id)
        shareEvent.value = post.content
    }

    override fun onRemove(post: Post) {
        repository.removeById(post.id)
    }

    override fun onEdit(post: Post) {
        navigateToEditPostScreen.value = post.content
        editedPost = post
        //editPost.value = post
    }

    override fun onVideo(post: Post) {
        navigateToVideoScreen.value = post.video
    }


    // endregion PostInteractionListener implementation

}