package com.example.storyhappy

import android.app.Application
import com.example.storyhappy.di.apiModule
import com.example.storyhappy.di.authModule
import com.example.storyhappy.di.storyModule
import com.example.storyhappy.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext

class StoryHappyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@StoryHappyApp)
            modules(
                listOf(
                    apiModule,
                    authModule,
                    storyModule,
                    viewModelModule
                )
            )
        }
    }
}