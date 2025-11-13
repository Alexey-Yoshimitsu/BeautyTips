package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.network.ApiClient
import com.example.myapplication.network.models.ArticlePreviewDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.time.Duration
import java.time.Instant


class MainActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private val api by lazy { ApiClient.create(sessionManager) }
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)
        if (!sessionManager.isLoggedIn()) {
            openLogin()
            return
        }

        setContentView(R.layout.activity_main)

        setupRecyclerView()
        setupRandomButton()
        setupProfileShortcut()
        setupBottomTabs()
        setupName()

        fetchArticles()
    }

    private fun setupRecyclerView() {
        articleAdapter = ArticleAdapter { showArticleDialog(it.title, it.preview) }
        val recyclerView = findViewById<RecyclerView>(R.id.articlesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = articleAdapter
    }

    private fun setupRandomButton() {
        findViewById<Button>(R.id.randomButton).setOnClickListener {
            fetchRandomArticle()
        }
    }

    private fun setupProfileShortcut() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val profileIcon = (0 until toolbar.childCount)
            .map { toolbar.getChildAt(it) }
            .firstOrNull { it is ImageView }
        profileIcon?.setOnClickListener { openProfile() }
    }

    private fun setupName() {
        lifecycleScope.launch {
            try {
                val user = withContext(Dispatchers.IO) { api.getProfile() }
                val name = findViewById<TextView>(R.id.greetingText)
                val mainName = findViewById<TextView>(R.id.main_name)
                name.text = "Добрый день\n" + user.name + "!"
                mainName.text  = user.name


            } catch (ex: HttpException) {
                if (ex.code() == 401) {
                    handleUnauthorized()
                } else {
                    Toast.makeText(this@MainActivity, R.string.error_profile_failed, Toast.LENGTH_SHORT).show()
                }
            } catch (ex: Exception) {
                Toast.makeText(this@MainActivity, R.string.error_profile_failed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupBottomTabs() {
        val articlesTab = findViewById<AppCompatButton>(R.id.tabArticles)
        val diaryTab = findViewById<AppCompatButton>(R.id.tabCenter)
        val botTab = findViewById<AppCompatButton>(R.id.tabBot)
        val tabs = listOf(articlesTab, diaryTab, botTab)

        fun select(tab: AppCompatButton) {
            tabs.forEach { it.isSelected = it == tab }
        }

        select(articlesTab)
        articlesTab.setOnClickListener { select(articlesTab) }

        diaryTab.setOnClickListener {
            select(diaryTab)
            startActivity(Intent(this, BeautyDiaryActivity::class.java))
        }

        botTab.setOnClickListener {
            select(botTab)
            Toast.makeText(this, R.string.tab_bot_placeholder, Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchArticles() {
        lifecycleScope.launch {
            try {
                val previews = withContext(Dispatchers.IO) { api.getArticles() }
                val items = previews.map { it.toUiModel() }
                articleAdapter.submitList(items)
            } catch (ex: HttpException) {
                if (ex.code() == 401) {
                    handleUnauthorized()
                } else {
                    Toast.makeText(this@MainActivity, R.string.error_loading_articles, Toast.LENGTH_SHORT).show()
                }
            } catch (ex: Exception) {
                Toast.makeText(this@MainActivity, R.string.error_loading_articles, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchRandomArticle() {
        lifecycleScope.launch {
            try {
                val article = withContext(Dispatchers.IO) { api.getRandomArticle() }
                showArticleDialog(article.title, article.preview)
            } catch (ex: HttpException) {
                if (ex.code() == 401) {
                    handleUnauthorized()
                } else {
                    Toast.makeText(this@MainActivity, R.string.error_random_article, Toast.LENGTH_SHORT).show()
                }
            } catch (ex: Exception) {
                Toast.makeText(this@MainActivity, R.string.error_random_article, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showArticleDialog(title: String, preview: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(preview)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    private fun ArticlePreviewDto.toUiModel(): ArticleUiModel {
        return ArticleUiModel(
            id = id,
            title = title,
            preview = preview,
            timeAgo = formatRelativeTime(createdAt),
            isPopular = isPopular
        )
    }

    private fun formatRelativeTime(dateTime: String): String {
        return try {
            val created = Instant.parse(dateTime)
            val minutes = Duration.between(created, Instant.now()).toMinutes().coerceAtLeast(0)
            when {
                minutes < 60 -> getString(R.string.time_minutes_ago, minutes)
                minutes < 60 * 24 -> getString(R.string.time_hours_ago, minutes / 60)
                else -> getString(R.string.time_days_ago, minutes / (60 * 24))
            }
        } catch (ex: Exception) {
            getString(R.string.time_just_now)
        }
    }

    private fun openProfile() {
        startActivity(Intent(this, ProfileActivity::class.java))
    }

    private fun openLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun handleUnauthorized() {
        sessionManager.clearSession()
        openLogin()
    }
}
