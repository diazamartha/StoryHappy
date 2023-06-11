package com.example.storyhappy.domain.usecase

import com.example.storyhappy.data.Result
import com.example.storyhappy.data.source.remote.response.LoginResponse
import com.example.storyhappy.data.source.remote.response.RegisterResponse
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {

    fun register(name: String, email: String, password: String): Flow<Result<RegisterResponse>>

    fun login(email: String, password: String): Flow<Result<LoginResponse>>

    fun getToken(): Flow<String>

    suspend fun setToken(token: String)

    suspend fun logout()

}