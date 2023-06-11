package com.example.storyhappy.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyhappy.domain.usecase.StoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update

class DetailViewModel(
    private val storyUseCase: StoryUseCase
) : ViewModel() {

    fun getStoryDetail(id: String) =
        storyUseCase.getStoryDetail(id).asLiveData()
}