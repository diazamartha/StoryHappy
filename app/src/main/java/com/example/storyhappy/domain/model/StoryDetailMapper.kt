package com.example.storyhappy.domain.model

import com.example.storyhappy.data.source.remote.response.Story

fun Story.toStoryDetail(): StoryDetail {
    return StoryDetail(
        id = id,
        photoUrl = photoUrl,
        name = name,
        createdAt = createdAt,
        description = description
    )
}