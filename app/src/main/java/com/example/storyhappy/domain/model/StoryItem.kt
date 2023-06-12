package com.example.storyhappy.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryItem(
    val id: String,
    val photoUrl: String,
    val name: String,
    val createdAt: String
) : Parcelable
