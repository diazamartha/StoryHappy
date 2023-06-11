package com.example.storyhappy.di

import com.example.storyhappy.data.repository.StoryRepositoryImpl
import com.example.storyhappy.domain.interfaces.StoryRepository
import com.example.storyhappy.domain.usecase.StoryInteractor
import com.example.storyhappy.domain.usecase.StoryUseCase
import org.koin.dsl.module

val storyModule = module {
    single<StoryRepository> { StoryRepositoryImpl(get()) }
    single<StoryUseCase> { StoryInteractor(get()) }
}