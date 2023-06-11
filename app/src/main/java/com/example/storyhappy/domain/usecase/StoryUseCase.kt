package com.example.storyhappy.domain.usecase

import com.example.storyhappy.data.Result
import com.example.storyhappy.data.source.remote.response.AddStoryResponse
import com.example.storyhappy.data.source.remote.response.StoryDetailResponse
import com.example.storyhappy.domain.model.StoryDetail
import com.example.storyhappy.domain.model.StoryItem
import kotlinx.coroutines.flow.Flow
import java.io.File

interface StoryUseCase {

    fun getStories(token: String): Flow<Result<List<StoryItem>>>

    fun getStoryDetail(id: String): Flow<Result<StoryDetail>>

    fun uploadStory(token: String, photo: File, description: String): Flow<Result<AddStoryResponse>>

}