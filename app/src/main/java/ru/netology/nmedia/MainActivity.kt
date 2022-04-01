package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            likes = 10_000,
            likedByMe = true,
            share = 1_299_999,
            sharedByMe = true,
            views = 111_598,
            viewedByMe = true
        )

        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likeCount.text = counter(post.likes)
            shareCount.text = counter(post.share)
            viewsCount.text = counter(post.views)
            like.setImageResource (if (post.likedByMe) (R.drawable.ic_baseline_like_red_favorite_24dp)
            else R.drawable.ic_baseline_like_favorite_border_24dp)

            like.setOnClickListener {
                println("like clicked")
                if (post.likedByMe) post.likes-- else post.likes++
                post.likedByMe = !post.likedByMe
                likeCount.text = counter(post.likes)
                like.setImageResource (if (post.likedByMe) (R.drawable.ic_baseline_like_red_favorite_24dp)
                else R.drawable.ic_baseline_like_favorite_border_24dp)
            }

            share.setOnClickListener {
                println("share clicked")
                if (post.sharedByMe) post.share++
                shareCount.text = counter(post.share)
            }
        }
    }
}

fun counter(item: Int): String {
    return when (item) {
        in 1000..1099 -> {
            val num = roundOffDecimal(item / 1000.0)
            (num + "K")
        }
        in 1100..9999 -> {
            val num = roundOffDecimal(item / 1000.0)
            (num + "K")
        }
        in 10_000..999_999 -> {
            ((item / 1000).toString() + "K")
        }
        in 1_000_000..1_000_000_000 -> {
            val num = roundOffDecimal(item / 1_000_000.0)
            (num + "M")
        }
        else -> item.toString()
    }
}

fun roundOffDecimal(number: Double): String {
    val demicalFormat = DecimalFormat("#.#")
    demicalFormat.roundingMode = RoundingMode.FLOOR
    return demicalFormat.format(number).toString()
}