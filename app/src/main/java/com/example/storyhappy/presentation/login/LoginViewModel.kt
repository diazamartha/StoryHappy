package com.example.storyhappy.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyhappy.domain.usecase.AuthUseCase
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    fun login(email: String, password: String) = authUseCase.login(email, password).asLiveData()

    fun getToken() = authUseCase.getToken().asLiveData()

    fun setToken(token: String) {
        viewModelScope.launch {
            authUseCase.setToken(token)
        }
    }
}