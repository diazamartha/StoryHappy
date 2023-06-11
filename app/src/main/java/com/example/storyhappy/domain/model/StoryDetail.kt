package com.example.storyhappy.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryDetail(
    val id: String,
    val photoUrl: String,
    val name: String,
    val createdAt: String,
    val description: String,
): Parcelable