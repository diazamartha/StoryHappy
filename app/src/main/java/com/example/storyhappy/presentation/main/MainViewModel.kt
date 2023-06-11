package com.example.storyhappy.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyhappy.data.Result
import com.example.storyhappy.domain.model.StoryItem
import com.example.storyhappy.domain.usecase.AuthUseCase
import com.example.storyhappy.domain.usecase.StoryUseCase
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(
    private val authUseCase: AuthUseCase,
    private val storyUseCase: StoryUseCase
) : ViewModel() {

    fun getToken() = authUseCase.getToken().asLiveData()

    fun logout() {
        viewModelScope.launch {
            authUseCase.logout()
        }
    }

    fun getStories(token: String): LiveData<Result<List<StoryItem>>> =
        storyUseCase.getStories("bearer $token").asLiveData()

    fun uploadStory(token: String, photo: File, description: String) =
        storyUseCase.uploadStory(token, photo, description).asLiveData()
}