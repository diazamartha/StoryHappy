package com.example.storyhappy.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyhappy.data.source.remote.response.ListStoryItem
import com.example.storyhappy.databinding.ActivityMainBinding
import com.example.storyhappy.presentation.detail.DetailActivity
import com.example.storyhappy.presentation.login.LoginActivity
import com.example.storyhappy.presentation.maps.MapsActivity
import com.example.storyhappy.presentation.upload.UploadActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var storiesAdapter: StoriesAdapter
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storiesAdapter = StoriesAdapter()

        setupRecyclerView()
        navigateToUploadActivity()
        navigateToMapsActivity()
        checkLoginStatus()
        logout()

        showStories()

        storiesAdapter.setOnItemClickCallback(object : StoriesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.STORY_ID, data.id)
                    startActivity(it)
                }
            }
        })
    }

    private fun showStories() {
        val adapter = StoriesAdapter()
        adapter.setOnItemClickCallback(object : StoriesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.STORY_ID, data.id)
                    startActivity(it)
                }
            }
        })
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        mainViewModel.getToken().observe(this) { token ->
            mainViewModel.getStories(token).observe(this) {
                adapter.submitData(lifecycle, it)
            }
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(
            binding.rvStories.context,
            layoutManager.orientation
        )
        binding.rvStories.addItemDecoration(dividerItemDecoration)
        binding.rvStories.adapter = storiesAdapter
    }

    private fun checkLoginStatus() {
        mainViewModel.getToken().observe(this) { token ->
            if (token.isEmpty()) {
                navigateToLoginActivity()
            }
        }
    }

    private fun logout() {
        binding.buttonLogout.setOnClickListener {
            mainViewModel.logout()
        }
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToUploadActivity() {
        binding.addStoryButton.setOnClickListener {
            val intent = Intent(this@MainActivity, UploadActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun navigateToMapsActivity() {
        binding.btnMap.setOnClickListener {
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}


