package com.example.storyhappy.domain.interfaces

import kotlinx.coroutines.flow.Flow

interface UserPreferenceRepository {

    fun getToken(): Flow<String>

    suspend fun setToken(token: String)

    suspend fun logout()
}