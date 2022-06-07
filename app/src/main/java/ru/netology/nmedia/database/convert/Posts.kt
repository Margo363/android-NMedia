package ru.netology.nmedia.database.convert

import ru.netology.nmedia.database.entity.PostEntity
import ru.netology.nmedia.dto.Post

internal fun PostEntity.toModel() = Post(
    id = id,
    author = author,
    published = published,
    content = content,
    likes = likes,
    likedByMe = likedByMe,
    share = share,
    views = views

)

internal fun Post.toPostEntity() = PostEntity(
    id = id,
    author = author,
    published = published,
    content = content,
    likes = likes,
    likedByMe = likedByMe,
    share = share,
    views = views

)