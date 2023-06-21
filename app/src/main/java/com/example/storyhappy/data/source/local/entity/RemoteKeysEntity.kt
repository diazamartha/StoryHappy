package com.example.storyhappy.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
class RemoteKeysEntity(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)