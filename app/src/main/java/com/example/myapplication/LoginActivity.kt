package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException


class LoginActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private val api by lazy { ApiClient.create(sessionManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionManager = SessionManager(this)

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()
            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, R.string.error_required_fields, Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password, loginButton)
            }
        }
    }

    private fun loginUser(email: String, password: String, actionButton: Button) {
        lifecycleScope.launch {
            setLoading(actionButton, true)
            try {
                val token = withContext(Dispatchers.IO) {
                    api.login(email, password)
                }
                sessionManager.saveAuthToken(token.accessToken)
                sessionManager.setHasAccount(true)
                openMain()
            } catch (ex: HttpException) {
                Toast.makeText(this@LoginActivity, ex.message(), Toast.LENGTH_SHORT).show()
                Log.e("Login", ex.message())

            } catch (ex: Exception) {
                // todo тут падает
                Toast.makeText(this@LoginActivity, R.string.error_login_failed, Toast.LENGTH_LONG).show()
                Log.e("Login", ex.toString())

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
