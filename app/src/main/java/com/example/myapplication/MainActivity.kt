package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Set up RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.articlesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        // Create sample data for the articles
        val articles = listOf(
            Article("Косметика Anua: За и про...", "Косметика Anua является довольно по...", "3 мин", true),
            Article("Уход за кожей осенью", "Осенний уход за кожей требует особого внимания...", "5 мин", false),
            Article("Маски для лица: топ-5", "Расскажем о лучших масках для лица...", "7 мин", true),
            Article("Утренний ритуал красоты", "Как правильно начать утро для ухода за собой...", "4 мин", false)
        )
        
        val adapter = ArticleAdapter(articles) { article ->
            // Handle article click
        }
        recyclerView.adapter = adapter
        
        // Set up random button
        findViewById<Button>(R.id.randomButton).setOnClickListener {
            // Handle random button click - could select a random article
        }
        
        // Set up bottom navigation
        setupBottomNavigation()
    }
    
    private fun setupBottomNavigation() {
        val bottomNavigation = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Already on home
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}

data class Article(
    val title: String,
    val preview: String,
    val timeAgo: String,
    val isPopular: Boolean
)