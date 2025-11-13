package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.network.ApiClient
import com.example.myapplication.network.models.SupportMessageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ProfileActivity : AppCompatActivity() {

    private lateinit var personalCard: ViewGroup
    private lateinit var personalHeader: View
    private lateinit var personalContent: View
    private lateinit var chevron: ImageView
    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private var expanded = false

    private lateinit var sessionManager: SessionManager
    private val api by lazy { ApiClient.create(sessionManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        sessionManager = SessionManager(this)

        findViewById<ImageView>(R.id.backButton).setOnClickListener { finish() }

        personalCard = findViewById(R.id.personalCard)
        personalHeader = findViewById(R.id.personalHeader)
        personalContent = findViewById(R.id.personalContent)
        chevron = findViewById(R.id.personalChevron)
        nameInput = findViewById(R.id.nameInput)
        emailInput = findViewById(R.id.emailInput)

        expanded = savedInstanceState?.getBoolean("expanded") ?: expanded
        applyExpanded(animated = false)

        personalHeader.setOnClickListener {
            expanded = !expanded
            applyExpanded(animated = true)
        }

        findViewById<View>(R.id.supportCard).setOnClickListener {
            showSupportDialog()
        }

        loadProfile()
    }

    private fun loadProfile() {
        lifecycleScope.launch {
            try {
                val user = withContext(Dispatchers.IO) { api.getProfile() }
                nameInput.setText(user.name)
                emailInput.setText(user.email)
            } catch (ex: HttpException) {
                if (ex.code() == 401) {
                    handleUnauthorized()
                } else {
                    Toast.makeText(this@ProfileActivity, R.string.error_profile_failed, Toast.LENGTH_SHORT).show()
                }
            } catch (ex: Exception) {
                Toast.makeText(this@ProfileActivity, R.string.error_profile_failed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSupportDialog() {
        val subjectInput = EditText(this).apply {
            hint = getString(R.string.support_dialog_subject_hint)
        }
        val messageInput = EditText(this).apply {
            hint = getString(R.string.support_dialog_message_hint)
            minLines = 3
        }
        val padding = (16 * resources.displayMetrics.density).toInt()
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(padding, padding, padding, 0)
            addView(subjectInput)
            addView(messageInput)
        }

        AlertDialog.Builder(this)
            .setTitle(R.string.support_dialog_title)
            .setView(container)
            .setPositiveButton(R.string.support_dialog_positive) { _, _ ->
                val subject = subjectInput.text.toString().trim()
                val message = messageInput.text.toString().trim()
                if (subject.isBlank() || message.isBlank()) {
                    Toast.makeText(this, R.string.error_required_fields, Toast.LENGTH_SHORT).show()
                } else {
                    sendSupportRequest(subject, message)
                }
            }
            .setNegativeButton(R.string.support_dialog_negative, null)
            .show()
    }

    private fun sendSupportRequest(subject: String, message: String) {
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    api.sendSupportMessage(SupportMessageRequest(subject, message))
                }
                Toast.makeText(this@ProfileActivity, R.string.support_dialog_success, Toast.LENGTH_SHORT).show()
            } catch (ex: HttpException) {
                if (ex.code() == 401) {
                    handleUnauthorized()
                } else {
                    Toast.makeText(this@ProfileActivity, R.string.support_dialog_error, Toast.LENGTH_SHORT).show()
                }
            } catch (ex: Exception) {
                Toast.makeText(this@ProfileActivity, R.string.support_dialog_error, Toast.LENGTH_SHORT).show()
            }
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
            .rotation(if (expanded) 90f else 0f)
            .setDuration(if (animated) 220 else 0)
            .start()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("expanded", expanded)
        super.onSaveInstanceState(outState)
    }

    private fun handleUnauthorized() {
        sessionManager.clearSession()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
