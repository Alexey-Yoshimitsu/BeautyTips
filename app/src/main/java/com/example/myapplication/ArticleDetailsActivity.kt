package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ArticleDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_details)

        val back = findViewById<ImageView>(R.id.backButton)
        val titleBar = findViewById<TextView>(R.id.titleBar)
        val titleText = findViewById<TextView>(R.id.articleTitle)
        val timeText = findViewById<TextView>(R.id.articleTime)
        val contentText = findViewById<TextView>(R.id.articleContent)
        val shareBtn = findViewById<ImageView>(R.id.shareButton)

        val title = intent.getStringExtra(EXTRA_TITLE).orEmpty()
        val content = intent.getStringExtra(EXTRA_PREVIEW).orEmpty()
        val timeAgo = intent.getStringExtra(EXTRA_TIME)

        titleBar.text = "Статья"
        titleText.text = title
        contentText.text = content
        timeText.text = timeAgo ?: ""

        back.setOnClickListener { finish() }

        shareBtn.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, title)
                putExtra(Intent.EXTRA_TEXT, "$title\n\n$content")
            }
            startActivity(Intent.createChooser(share, "Поделиться статьёй"))
        }
    }

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_PREVIEW = "extra_preview"
        const val EXTRA_TIME = "extra_time"
    }
}
