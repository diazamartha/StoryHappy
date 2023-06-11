package com.example.storyhappy.domain.interfaces

import com.example.storyhappy.data.Result
import com.example.storyhappy.data.source.remote.response.LoginResponse
import com.example.storyhappy.data.source.remote.response.RegisterResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun register(name: String, email: String, password: String): Flow<Result<RegisterResponse>>

    fun login(email: String, password: String): Flow<Result<LoginResponse>>

}