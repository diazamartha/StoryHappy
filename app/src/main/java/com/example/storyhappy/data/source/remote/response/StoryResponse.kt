package com.example.storyhappy.data.source.remote.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.storyhappy.data.source.local.entity.StoryItemEntity
import com.example.storyhappy.domain.model.StoryItem
import com.google.gson.annotations.SerializedName

data class StoryResponse(

    @field:SerializedName("listStory")
    val listStory: List<ListStoryItem>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

@Entity(tableName = "story")
data class ListStoryItem(

    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @PrimaryKey
    @field:SerializedName("id")
    val id: String,

)
