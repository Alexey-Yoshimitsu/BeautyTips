package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class ArticleAdapter(
    private val onArticleClick: (ArticleUiModel) -> Unit
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    private val articles = mutableListOf<ArticleUiModel>()

    class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeAgoText: TextView = view.findViewById(R.id.timeAgoText)
        val popularTag: TextView = view.findViewById(R.id.popularTag)
        val titleText: TextView = view.findViewById(R.id.titleText)
        val previewText: TextView = view.findViewById(R.id.previewText)
        val root: CardView = view.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.article_item, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.timeAgoText.text = article.timeAgo
        holder.titleText.text = article.title
        holder.previewText.text = article.preview
        
        if (article.isPopular) {
            holder.popularTag.visibility = View.VISIBLE
        } else {
            holder.popularTag.visibility = View.GONE
        }
        
        holder.root.setOnClickListener {
            onArticleClick(article)
        }
    }

    override fun getItemCount() = articles.size

    fun submitList(newArticles: List<ArticleUiModel>) {
        articles.clear()
        articles.addAll(newArticles)
        notifyDataSetChanged()
    }
}
