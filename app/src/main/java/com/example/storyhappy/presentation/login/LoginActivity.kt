package com.example.storyhappy.presentation.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.storyhappy.data.Result
import com.example.storyhappy.databinding.ActivityLoginBinding
import com.example.storyhappy.presentation.main.MainActivity
import com.example.storyhappy.presentation.register.RegisterActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActions()
        checkLoginStatus()
        playAnimation()
    }

    private fun setupActions() {
        binding.btnRegister.setOnClickListener { navigateToRegisterActivity() }
        binding.btnLogin.setOnClickListener {
            if (isInputValid()) {
                loginViewModel.login(
                    binding.etInputEmail.text.toString(),
                    binding.etInputPassword.text.toString()
                ).observe(this@LoginActivity) { loginState ->
                    when (loginState) {
                        is Result.Success -> {
                            showLoading(false)
                            showToast(loginState.data.message)
                            loginViewModel.setToken(loginState.data.loginResult.token)
                        }

                        is Result.Error -> {
                            showLoading(false)
                            showToast(loginState.message)
                        }

                        is Result.Loading -> {
                            showLoading(true)
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun checkLoginStatus() {
        loginViewModel.getToken().observe(this) { token ->
            if (token.isNotEmpty()) {
                navigateToMainActivity()
            }
        }
    }

    private fun navigateToRegisterActivity() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isInputValid(): Boolean {
        with(binding) {
            val emailError = etInputEmail.error
            val passwordError = etInputPassword.error

            return emailError == null && passwordError == null
        }
    }

    private fun showLoading(isLoading: Boolean) {
        val visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.loading.visibility = visibility
    }

    private fun playAnimation() {
        val animDuration = 300L

        val animOpening =
            ObjectAnimator.ofFloat(binding.tvOpeningLogin, View.ALPHA, 1f).setDuration(animDuration)
        val animMessage =
            ObjectAnimator.ofFloat(binding.tvMessageLogin, View.ALPHA, 1f).setDuration(animDuration)
        val animEmail =
            ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(animDuration)
        val animEditEmail =
            ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(animDuration)
        val animPassword =
            ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(animDuration)
        val animEditPassword =
            ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f).setDuration(animDuration)
        val animLoginButton =
            ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(animDuration)
        val animQuestion =
            ObjectAnimator.ofFloat(binding.tvQuestion, View.ALPHA, 1f).setDuration(animDuration)
        val animRegisterButton =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(animDuration)

        AnimatorSet().apply {
            playSequentially(
                animOpening,
                animMessage,
                animEmail,
                animEditEmail,
                animPassword,
                animEditPassword,
                animLoginButton,
                animQuestion,
                animRegisterButton
            )
            start()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
