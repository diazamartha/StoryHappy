package com.example.storyhappy.data.repository

import com.example.storyhappy.data.Result
import com.example.storyhappy.data.source.remote.AuthService
import com.example.storyhappy.data.source.remote.request.LoginRequest
import com.example.storyhappy.data.source.remote.request.RegisterRequest
import com.example.storyhappy.data.source.remote.response.LoginResponse
import com.example.storyhappy.data.source.remote.response.RegisterResponse
import com.example.storyhappy.domain.interfaces.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(
    private val authService: AuthService
) : AuthRepository {
    override fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Result<RegisterResponse>> = flow {
        emit(Result.Loading)
        val registerRequest = RegisterRequest(name, email, password)
        val response = authService.register(registerRequest)
        emit(Result.Success(response))
    }

    override fun login(
        email: String,
        password: String
    ): Flow<Result<LoginResponse>> = flow {
        emit(Result.Loading)
        val loginRequest = LoginRequest(email, password)
        val response = authService.login(loginRequest)
        emit(Result.Success(response))
    }
}