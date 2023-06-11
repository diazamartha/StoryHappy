package com.example.storyhappy.domain.usecase

import com.example.storyhappy.data.Result
import com.example.storyhappy.data.source.remote.response.LoginResponse
import com.example.storyhappy.data.source.remote.response.RegisterResponse
import com.example.storyhappy.domain.interfaces.AuthRepository
import com.example.storyhappy.domain.interfaces.UserPreferenceRepository
import kotlinx.coroutines.flow.Flow

class AuthInteractor(
    private val authRepository: AuthRepository,
    private val userPreferenceRepository: UserPreferenceRepository
) : AuthUseCase {
    override fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Result<RegisterResponse>> = authRepository.register(name, email, password)

    override fun login(
        email: String,
        password: String
    ): Flow<Result<LoginResponse>> = authRepository.login(email, password)

    override fun getToken(): Flow<String> = userPreferenceRepository.getToken()

    override suspend fun setToken(token: String) = userPreferenceRepository.setToken(token)

    override suspend fun logout() = userPreferenceRepository.logout()
}