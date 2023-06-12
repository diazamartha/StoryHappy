package com.example.storyhappy.domain.model

import android.os.Parcelable
import com.example.storyhappy.data.source.local.entity.StoryItemEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryItem(
    val id: String,
    val photoUrl: String,
    val name: String,
    val createdAt: String
): Parcelable

fun StoryItem.mapToStoryItemEntityDomain(): StoryItemEntity {
    return StoryItemEntity(
        id,
        photoUrl,
        name,
        createdAt
    )
}