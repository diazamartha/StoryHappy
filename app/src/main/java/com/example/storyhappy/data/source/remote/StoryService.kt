package com.example.storyhappy.data.source.remote

import com.example.storyhappy.data.source.remote.response.AddStoryResponse
import com.example.storyhappy.data.source.remote.response.StoryDetailResponse
import com.example.storyhappy.data.source.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface StoryService {
    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): StoryResponse

    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Path("id") id: String,
        @Header("Authorization") authorization: String
    ): StoryDetailResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Header("Authorization") authorization: String,
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): AddStoryResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Header("Authorization") authorization: String,
        @Query("location") location: Int = 1
    ): StoryResponse
}