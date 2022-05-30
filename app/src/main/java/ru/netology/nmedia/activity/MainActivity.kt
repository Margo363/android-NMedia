package ru.netology.nmedia.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import android.net.Uri
import androidx.activity.result.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(viewModel)

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        viewModel.shareEvent.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, postContent)
            }
            val shareIntent = Intent.createChooser(
                intent, getString(R.string.chooser_share_post)
            )
            startActivity(shareIntent)
        }

        viewModel.navigateToVideoScreen.observe(this) { videoContent ->
            if (!videoContent.isNullOrEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoContent))
                startActivity(intent)
            }
        }


        val editPostLauncher =
            registerForActivityResult(EditPostActivity.ResultContract) { editPostContent ->
                editPostContent ?: return@registerForActivityResult
                viewModel.changeContent(editPostContent)
            }

        viewModel.navigateToEditPostScreen.observe(this)
        { postContent ->
            editPostLauncher.launch(postContent)
        }

        val newPostLauncher =
            registerForActivityResult(NewPostActivity.ResultContract) { newPostContent ->
                newPostContent ?: return@registerForActivityResult
                newPostContent.first?.let { viewModel.changeContent(newPostContent) }
            }


        viewModel.navigateToNewPostScreen.observe(this)
        {
            newPostLauncher.launch()
        }


        binding.fab.setOnClickListener { viewModel.onnAddNewPostButtonClicked() }

    }
}

//        viewModel.data.observe(this) { posts ->
//            adapter.submitList(posts)
//        }
//
//        viewModel.editPost.observe(this) { posts ->
//            if (post.id == 0l) {
//                return@observe
//            }
//        }

//            binding.groupEditPost.visibility = View.GONE
//        }
//
//        viewModel.editPost.observe(this) { post: Post? ->
//            val content = post?.content ?: ""
//            with(binding) {
//
//                contentEditText.setText(content)
//                if (viewModel.editPost.value == null)
//                    groupEditPost.visibility = View.GONE
//                else
//                    groupEditPost.visibility = View.VISIBLE
//                editMessage.setText(post?.author)
//            }
//        }
//
//        binding.undoEditingButton.setOnClickListener {
//            with(binding.contentEditText) {
//                val content = text.toString()
//                viewModel.emptyPost(content)
//                setText("")
//                clearFocus()
//                hideKeyboard()
//                binding.groupEditPost.visibility = View.GONE
//            }
//        }
//
//        binding.saveButton.setOnClickListener {
//            with(binding.contentEditText) {
//                val content = text.toString()
//                viewModel.onSaveButtonClicked(content)
//                clearFocus()
//                hideKeyboard()
//                binding.groupEditPost.visibility = View.GONE
//            }
//        }









