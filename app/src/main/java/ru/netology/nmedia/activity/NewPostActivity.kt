package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import androidx.activity.result.contract.ActivityResultContract
import ru.netology.nmedia.databinding.ActivityNewPostBinding
import ru.netology.nmedia.R

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.editPost.requestFocus()
        binding.ok.setOnClickListener {
            onOkButtonClicked(binding.editPost.text,binding.videoUrl.text) }
    }

    private fun onOkButtonClicked(text: Editable, videoUrl: Editable) {
        val intent = Intent()
        if (text.isBlank()) {
            setResult(Activity.RESULT_CANCELED, intent)
        }else{
            val newPostContent = text.toString()
            val videoUrlString = videoUrl.toString()
            intent.putExtra(NEW_POST_CONTENT_KEY, newPostContent)
            intent.putExtra(VIDEO_URL_KEY, videoUrlString)
            setResult(Activity.RESULT_OK, intent)
        }
        finish()
    }

    object ResultContract : ActivityResultContract<Unit, Pair<String?, String?>>() {

        override fun createIntent(context: Context, input: Unit) =
            Intent(context, NewPostActivity::class.java)

        override fun parseResult(resultCode: Int, intent: Intent?): Pair<String?, String?> {
            var postPair: Pair<String?, String?> = Pair(null, null)
            if (resultCode == Activity.RESULT_OK) {
                val text = intent?.getStringExtra(NEW_POST_CONTENT_KEY)
                val video = intent?.getStringExtra(VIDEO_URL_KEY)
                postPair = text to video
                return postPair
            } else {
                return postPair
            }
        }
    }

    companion object {
        const val NEW_POST_CONTENT_KEY = "newPostContent"
        const val VIDEO_URL_KEY = "videoUrlContent"
    }
}

