package com.example.storyhappy.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyhappy.data.Result
import com.example.storyhappy.data.StoryRemoteMediator
import com.example.storyhappy.data.source.local.StoryDatabase
import com.example.storyhappy.data.source.remote.StoryService
import com.example.storyhappy.data.source.remote.response.AddStoryResponse
import com.example.storyhappy.data.source.remote.response.ListStoryItem
import com.example.storyhappy.data.source.remote.response.StoryDetailResponse
import com.example.storyhappy.data.source.remote.response.StoryResponse
import com.example.storyhappy.domain.interfaces.StoryRepository
import com.example.storyhappy.domain.model.StoryDetail
import com.example.storyhappy.domain.model.toStoryDetail
import com.example.storyhappy.utils.reduceFileImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryRepositoryImpl(
    private val storyService: StoryService,
    private val storyDatabase: StoryDatabase
) : StoryRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getStories(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(token, storyDatabase, storyService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getStories()
            }
        ).liveData
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
            val compressedFile = reduceFileImage(photo)
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

    override fun getStoriesWithLocation(token: String): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = storyService.getStoriesWithLocation(
                "bearer $token",
                1
            )
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}