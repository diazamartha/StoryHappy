package com.example.storyhappy.data.repository

import com.example.storyhappy.data.Result
import com.example.storyhappy.data.source.local.UserPreferenceImpl
import com.example.storyhappy.data.source.remote.StoryService
import com.example.storyhappy.data.source.remote.response.AddStoryResponse
import com.example.storyhappy.data.source.remote.response.StoryDetailResponse
import com.example.storyhappy.data.source.remote.response.StoryResponse
import com.example.storyhappy.domain.interfaces.StoryRepository
import com.example.storyhappy.domain.interfaces.UserPreferenceRepository
import com.example.storyhappy.domain.model.StoryDetail
import com.example.storyhappy.domain.model.StoryItem
import com.example.storyhappy.domain.model.toStoryDetail
import com.example.storyhappy.domain.model.toStoryItem
import com.example.storyhappy.utils.reduceFileImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryRepositoryImpl(
    private val storyService: StoryService
) : StoryRepository {

    override fun getStories(token: String): Flow<Result<List<StoryItem>>> = flow {
        emit(Result.Loading)
        val response: StoryResponse = storyService.getStories(
            token,
            1,
            100
        )
        if (!response.error) {
            val storyItems: List<StoryItem> = response.listStory.toStoryItem()
            emit(Result.Success(storyItems))
        } else {
            emit(Result.Error(response.message))
        }
    }

    override fun getStoryDetail(id: String): Flow<Result<StoryDetail>> = flow {
        emit(Result.Loading)
        val response: StoryDetailResponse = storyService.getStoryDetail(
            id,
            "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXg3UmFSaUV3WUxkRERHd2IiLCJpYXQiOjE2ODU4NDk3NjN9.uqeVqjyoc_V9hCzcPEJytZrQ8Kxwy0y598orC7Hpq2E"
        )
        if (!response.error) {
            val storyDetails: StoryDetail = response.story.toStoryDetail()
            emit(Result.Success(storyDetails))
        } else {
            emit(Result.Error(response.message))
        }
    }

    override fun uploadStory(
        token: String,
        photo: File,
        description: String
    ): Flow<Result<AddStoryResponse>> = flow {
        emit(Result.Loading)
        try {
            val compressedFile = reduceFileImage(photo, 1_000_000)
            val descriptionRequestBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
            val imageRequestBody = compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val multipart =
                MultipartBody.Part.createFormData("photo", compressedFile.name, imageRequestBody)
            val response =
                storyService.uploadStory("bearer $token", multipart, descriptionRequestBody)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}