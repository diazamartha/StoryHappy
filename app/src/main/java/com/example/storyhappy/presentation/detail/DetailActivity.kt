package com.example.storyhappy.presentation.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.storyhappy.R
import com.example.storyhappy.data.Result
import com.example.storyhappy.databinding.ActivityDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpView()
    }

    private fun setUpView() {
        val storyId = intent.getStringExtra(STORY_ID)
        if (storyId != null) {
            detailViewModel.getStoryDetail(storyId).observe(this) { detailStoryState ->
                when (detailStoryState) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val story = detailStoryState.data
                        binding.apply {
                            nameStory.text = story.name
                            createdAtStory.text = story.createdAt
                            descriptionStory.text = story.description
                            Glide.with(this@DetailActivity)
                                .load(story.photoUrl)
                                .fitCenter()
                                .into(imageStory)
                        }
                    }

                    is Result.Error -> {
                        Toast.makeText(this@DetailActivity, getString(R.string.error_can_not_load_data), Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val STORY_ID = "story_id"
    }
}