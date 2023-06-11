package com.example.storyhappy.presentation.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.storyhappy.R
import com.example.storyhappy.presentation.welcome.WelcomeActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DELAY = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            navigateToWelcomeActivity()
            finish()
        }, SPLASH_DELAY)
    }

    private fun navigateToWelcomeActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }
}