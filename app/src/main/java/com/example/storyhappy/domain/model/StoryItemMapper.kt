package com.example.storyhappy.domain.model

import com.example.storyhappy.data.source.remote.response.ListStoryItem

fun List<ListStoryItem>.toStoryItem(): List<StoryItem> {
    return map { response ->
        StoryItem(
            id = response.id,
            photoUrl = response.photoUrl,
            name = response.name,
            createdAt = response.createdAt
        )
    }
}