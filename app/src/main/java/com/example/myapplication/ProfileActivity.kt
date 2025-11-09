package com.example.myapplication

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible

class ProfileActivity : AppCompatActivity() {

    private lateinit var personalCard: ViewGroup
    private lateinit var personalHeader: View
    private lateinit var personalContent: View
    private lateinit var chevron: ImageView
    private var expanded = false   // поставь true, если нужно стартовать раскрытым

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // back
        findViewById<ImageView>(R.id.backButton).setOnClickListener { finish() }

        // expandable
        personalCard = findViewById(R.id.personalCard)
        personalHeader = findViewById(R.id.personalHeader)
        personalContent = findViewById(R.id.personalContent)
        chevron = findViewById(R.id.personalChevron)

        expanded = savedInstanceState?.getBoolean("expanded") ?: expanded
        applyExpanded(animated = false)

        personalHeader.setOnClickListener {
            expanded = !expanded
            applyExpanded(animated = true)
        }

        // support
        findViewById<View>(R.id.supportCard).setOnClickListener {
            // TODO: открыть экран/чат поддержки
        }
    }

    private fun applyExpanded(animated: Boolean) {
        if (animated) {
            TransitionManager.beginDelayedTransition(
                personalCard, AutoTransition().setDuration(220)
            )
        }
        personalContent.isVisible = expanded
        chevron.animate()
            .rotation(if (expanded) 90f else 0f) // из ">" в "v"
            .setDuration(if (animated) 220 else 0)
            .start()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("expanded", expanded)
        super.onSaveInstanceState(outState)
    }
}
