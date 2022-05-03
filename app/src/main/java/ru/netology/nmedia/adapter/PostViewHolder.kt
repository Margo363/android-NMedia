package ru.netology.nmedia.adapter

import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.counter
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

class PostViewHolder(
    private val binding: CardPostBinding,
    private val listener: PostInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var post: Post

    private val popupMenu by lazy {

        PopupMenu(itemView.context, binding.menu).apply {
            inflate(R.menu.options_post)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.remove -> {
                        listener.onRemove(post)
                        true
                    }
                    R.id.edit -> {
                        listener.onEdit(post)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    init {
        binding.like.setOnClickListener {
            listener.onLike(post)
        }

        binding.share.setOnClickListener {
            listener.onShare(post)
        }
        binding.menu.setOnClickListener {
            popupMenu.show()
        }
    }

    fun bind(post: Post) {
        this.post = post
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            like.text = counter(post.likes)
            share.text = counter(post.share)
            views.text = counter(post.views)
            like.isChecked = post.likedByMe
        }
    }
}