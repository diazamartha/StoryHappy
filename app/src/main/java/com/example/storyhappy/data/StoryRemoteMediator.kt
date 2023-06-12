package com.example.storyhappy.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.storyhappy.data.source.local.StoryDatabase
import com.example.storyhappy.data.source.local.entity.RemoteKeysEntity
import com.example.storyhappy.data.source.remote.StoryService
import com.example.storyhappy.data.source.remote.response.ListStoryItem

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val token: String,
    private val storyDatabase: StoryDatabase,
    private val storyService: StoryService
) : RemoteMediator<Int, ListStoryItem>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStoryItem>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = storyService.getStories(
                "Bearer $token",
                page,
                state.config.pageSize
            )
            val endOfPaginationReached = responseData.listStory.isEmpty()
            storyDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    storyDatabase.storyDao().deleteAll()
                    storyDatabase.remoteKeysDao().deleteRemoteKeys()
                }
                storyDatabase.storyDao()
                    .insertStory(responseData.listStory.map {
                        ListStoryItem(
                            id = it.id,
                            name = it.name,
                            description = it.description,
                            photoUrl = it.photoUrl,
                            createdAt = it.createdAt,
                            lat = it.lat,
                            lon = it.lon
                        )
                    })
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.listStory.map {
                    RemoteKeysEntity(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                storyDatabase.remoteKeysDao().insertAll(keys)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ListStoryItem>): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            storyDatabase.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ListStoryItem>): RemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            storyDatabase.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ListStoryItem>): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                storyDatabase.remoteKeysDao().getRemoteKeysId(id)
            }
        }

    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}