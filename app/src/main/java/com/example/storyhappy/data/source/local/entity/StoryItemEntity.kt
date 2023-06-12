package com.example.storyhappy.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.storyhappy.domain.model.StoryItem
import com.google.gson.annotations.SerializedName

@Entity(tableName = "story")
data class StoryItemEntity(
    @PrimaryKey
    val id: String,
    val photoUrl: String? = null,
    val name: String? = null,
    val createdAt: String? = null,
)

fun StoryItemEntity.mapToStoryItemDomain(): StoryItem {
    return StoryItem(
        id,
        photoUrl!!,
        name!!,
        createdAt!!
    )
}