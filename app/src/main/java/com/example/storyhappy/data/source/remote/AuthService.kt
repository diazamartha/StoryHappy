package com.example.storyhappy.data.source.remote

import com.example.storyhappy.data.source.remote.request.LoginRequest
import com.example.storyhappy.data.source.remote.request.RegisterRequest
import com.example.storyhappy.data.source.remote.response.LoginResponse
import com.example.storyhappy.data.source.remote.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("register")
    suspend fun register(
        @Body register: RegisterRequest
    ): RegisterResponse

    @POST("login")
    suspend fun login(
        @Body login: LoginRequest
    ): LoginResponse
}