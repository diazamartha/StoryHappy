package com.example.storyhappy.presentation.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.storyhappy.R
import com.example.storyhappy.presentation.login.LoginViewModel
import com.example.storyhappy.presentation.main.MainActivity
import com.example.storyhappy.presentation.welcome.WelcomeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DELAY = 2000L
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            checkLoginStatus()
            finish()
        }, SPLASH_DELAY)
    }

    private fun checkLoginStatus() {
        loginViewModel.getToken().observe(this) { token ->
            if (token.isNotEmpty()) {
                navigateToMainActivity()
            } else {
                navigateToWelcomeActivity()
            }
        }
    }

    private fun navigateToWelcomeActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}