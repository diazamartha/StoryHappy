package com.example.storyhappy.viewmodel

import com.example.storyhappy.data.source.remote.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "2023-06-12T09:26:15.603Z",
                "dacascos6541",
                "halow",
                "story-Z0aUkJsSmbaZIpPM",
                106.91048998385668,
                -4.84512187929794
            )
            items.add(story)
        }
        return items
    }
}