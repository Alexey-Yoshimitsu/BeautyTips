package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.network.ApiClient
import com.example.myapplication.network.models.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class RegisterActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private val api by lazy { ApiClient.create(sessionManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sessionManager = SessionManager(this)

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val loginButton = findViewById<Button>(R.id.loginButton)

        registerButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()
            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, R.string.error_required_fields, Toast.LENGTH_SHORT).show()
            } else {
                registerUser(name, email, password, registerButton)
            }
        }

        loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun registerUser(name: String, email: String, password: String, actionButton: Button) {
        lifecycleScope.launch {
            setLoading(actionButton, true)
            try {
                val tokenResponse = withContext(Dispatchers.IO) {
                    api.register(RegisterRequest(name, email, password))
                    api.login(email, password)
                }
                sessionManager.saveAuthToken(tokenResponse.accessToken)
                sessionManager.setHasAccount(true)
                openMain()
            } catch (ex: HttpException) {
                Toast.makeText(this@RegisterActivity, ex.message(), Toast.LENGTH_SHORT).show()
            } catch (ex: Exception) {
                Toast.makeText(this@RegisterActivity, R.string.error_register_failed, Toast.LENGTH_SHORT).show()
            } finally {
                setLoading(actionButton, false)
            }
        }
    }

    private fun openMain() {
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun setLoading(button: Button, loading: Boolean) {
        button.isEnabled = !loading
        button.alpha = if (loading) 0.5f else 1f
    }
}
