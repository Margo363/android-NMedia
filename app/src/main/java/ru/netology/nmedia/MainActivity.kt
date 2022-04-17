package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.hideKeyboard
import ru.netology.nmedia.viewmodel.PostViewModel
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val groupEditPostContent = binding.groupEditPostContent
        groupEditPostContent.visibility = View.GONE

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(viewModel)

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

//        binding.saveButton.setOnClickListener {
//            val content = binding.contentEditText.text.toString()
//            viewModel.onSaveButtonClicked(content)
//            binding.contentEditText.clearFocus()
//            binding.contentEditText.hideKeyboard()
//        }

        viewModel.editPost.observe(this) { post: Post? ->
            val content = post?.content ?: ""
            binding.contentEditText.setText(content)

            groupEditPostContent.visibility = View.VISIBLE
        }

        binding.saveButton.setOnClickListener {
            with(binding.contentEditText) {
                val content = text.toString()
                viewModel.onSaveButtonClicked(content)

                groupEditPostContent.visibility = View.GONE

                clearFocus()
                hideKeyboard()
            }
        }
        binding.undoEditingButton.setOnClickListener {
            groupEditPostContent.visibility = View.GONE

            binding.undoEditingButton.clearFocus()
            binding.undoEditingButton.hideKeyboard()
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
    val decimalFormat = DecimalFormat("#.#")
    decimalFormat.roundingMode = RoundingMode.FLOOR
    return decimalFormat.format(number).toString()
}
