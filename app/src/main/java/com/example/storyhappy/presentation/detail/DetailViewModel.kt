package com.example.storyhappy.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyhappy.domain.usecase.StoryUseCase

class DetailViewModel(
    private val storyUseCase: StoryUseCase
) : ViewModel() {

    fun getStoryDetail(id: String) = storyUseCase.getStoryDetail(id).asLiveData()
}