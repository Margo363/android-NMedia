package ru.netology.nmedia.dto

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    var likes: Int = 0,
    var likedByMe: Boolean = false,
    var share: Int = 0,
    var sharedByMe: Boolean = false,
    var views: Int = 0,
    var viewedByMe: Boolean = false
)