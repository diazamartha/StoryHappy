package com.example.storyhappy.presentation.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.storyhappy.R
import com.example.storyhappy.data.Result
import com.example.storyhappy.databinding.ActivityRegisterBinding
import com.example.storyhappy.presentation.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpAction()
        playAnimation()
    }

    private fun setUpAction() {
        binding.btnLogin.setOnClickListener { navigateToLoginActivity() }
        binding.btnRegister.setOnClickListener {
            if (inputValidCondition()) {
                registerViewModel.register(
                    binding.etInputName.text.toString(),
                    binding.etInputEmail.text.toString(),
                    binding.etInputPassword.text.toString()
                ).observe(this@RegisterActivity) { registerState ->
                    when (registerState) {
                        is Result.Success -> {
                            showLoading(false)
                            showToast(registerState.data.message)
                            navigateToLoginActivity()
                        }

                        is Result.Error -> {
                            showLoading(false)
                            showToast(registerState.message)
                        }

                        is Result.Loading -> {
                            showLoading(true)
                        }

                        else -> {}
                    }
                }
            } else {
                showToast(getString(R.string.label_check_input))
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        val visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.loading.visibility = visibility
    }

    private fun inputValidCondition(): Boolean {
        val isEmailValid = binding.etInputEmail.error == null
        val isUsernameValid = binding.etInputName.error == null
        val isPasswordValid = binding.etInputPassword.error == null
        return isEmailValid && isUsernameValid && isPasswordValid
    }

    private fun playAnimation() {
        val logoAnimator =
            ObjectAnimator.ofFloat(binding.imgStoryhappyLogo, View.TRANSLATION_X, -30f, 30f).apply {
                duration = 6000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }
        logoAnimator.start()

        val animationDuration = 300L
        val textActionAnimator = ObjectAnimator.ofFloat(binding.tvActionRegister, View.ALPHA, 1f)
            .setDuration(animationDuration)
        val textNameAnimator =
            ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(animationDuration)
        val editTextNameAnimator =
            ObjectAnimator.ofFloat(binding.etName, View.ALPHA, 1f).setDuration(animationDuration)
        val textEmailAnimator =
            ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(animationDuration)
        val editTextEmailAnimator =
            ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(animationDuration)
        val textPasswordAnimator = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f)
            .setDuration(animationDuration)
        val editTextPasswordAnimator = ObjectAnimator.ofFloat(binding.etPassword, View.ALPHA, 1f)
            .setDuration(animationDuration)
        val buttonLoginAnimator =
            ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(animationDuration)
        val textQuestionAnimator = ObjectAnimator.ofFloat(binding.tvQuestion, View.ALPHA, 1f)
            .setDuration(animationDuration)
        val buttonRegisterAnimator = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f)
            .setDuration(animationDuration)

        AnimatorSet().apply {
            playSequentially(
                textActionAnimator,
                textNameAnimator,
                editTextNameAnimator,
                textEmailAnimator,
                editTextEmailAnimator,
                textPasswordAnimator,
                editTextPasswordAnimator,
                buttonRegisterAnimator,
                textQuestionAnimator,
                buttonLoginAnimator
            )
            start()
        }
    }
}
