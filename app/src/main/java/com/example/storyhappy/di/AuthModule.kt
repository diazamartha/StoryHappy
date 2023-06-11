package com.example.storyhappy.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.storyhappy.data.repository.AuthRepositoryImpl
import com.example.storyhappy.data.source.local.UserPreferenceImpl
import com.example.storyhappy.domain.interfaces.AuthRepository
import com.example.storyhappy.domain.interfaces.UserPreferenceRepository
import com.example.storyhappy.domain.usecase.AuthInteractor
import com.example.storyhappy.domain.usecase.AuthUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val USER_PREFERENCES = "user_preferences"

val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserPreferenceRepository> { UserPreferenceImpl(get()) }
    single<AuthUseCase> { AuthInteractor(get(), get()) }

    single {
        PreferenceDataStoreFactory.create(
            produceFile = { androidContext().preferencesDataStoreFile(USER_PREFERENCES) }
        )
    }
}