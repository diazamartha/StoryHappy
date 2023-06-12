package com.example.storyhappy.di

import androidx.room.Room
import com.example.storyhappy.data.source.local.StoryDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val roomModule = module {
    single {
        Room.databaseBuilder(androidContext(), StoryDatabase::class.java, "story_database")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    single { provideStoryDao(get()) }
    single { provideRemoteKeysDao(get()) }
}

fun provideStoryDao(database: StoryDatabase) = database.storyDao()
fun provideRemoteKeysDao(database: StoryDatabase) = database.remoteKeysDao()