package com.example.storyhappy.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.storyhappy.data.source.local.dao.RemoteKeysDao
import com.example.storyhappy.data.source.local.dao.StoryDao
import com.example.storyhappy.data.source.local.entity.RemoteKeysEntity
import com.example.storyhappy.data.source.local.entity.StoryItemEntity
import com.example.storyhappy.data.source.remote.response.ListStoryItem
import javax.xml.validation.Schema

@Database(
    entities = [ListStoryItem::class, RemoteKeysEntity::class],
    version = 1,
    exportSchema = false
)

abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}