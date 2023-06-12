package com.example.storyhappy.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.storyhappy.data.Result
import com.example.storyhappy.data.source.remote.response.AddStoryResponse
import com.example.storyhappy.data.source.remote.response.ListStoryItem
import com.example.storyhappy.data.source.remote.response.StoryResponse
import com.example.storyhappy.domain.interfaces.StoryRepository
import com.example.storyhappy.domain.model.StoryDetail
import kotlinx.coroutines.flow.Flow
import java.io.File

class StoryInteractor(
    private val storyRepository: StoryRepository
) : StoryUseCase {

    override fun getStories(token: String): LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStories(token)

    override fun getStoryDetail(id: String): Flow<Result<StoryDetail>> =
        storyRepository.getStoryDetail(id)

    override fun uploadStory(
        token: String,
        photo: File,
        description: String
    ): Flow<Result<AddStoryResponse>> = storyRepository.uploadStory(token, photo, description)

    override fun getStoriesWithLocation(token: String): LiveData<Result<StoryResponse>> =
        storyRepository.getStoriesWithLocation(token)
}