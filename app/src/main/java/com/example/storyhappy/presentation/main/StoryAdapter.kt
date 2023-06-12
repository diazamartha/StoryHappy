package com.example.storyhappy.presentation.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyhappy.data.source.remote.response.ListStoryItem
import com.example.storyhappy.databinding.ItemStoryBinding
import com.example.storyhappy.domain.model.StoryItem

class StoriesAdapter : PagingDataAdapter<ListStoryItem, StoriesAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    private val stories = mutableListOf<ListStoryItem>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    //override fun getItemCount(): Int = stories.size

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        //holder.bind(stories[position])
        getItem(position)?.let { holder.bind(it) }
    }

    inner class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(story)
            }
            binding.apply {
                nameStory.text = story.name
                createdAtStory.text = story.createdAt
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .fitCenter()
                    .into(imageStory)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}