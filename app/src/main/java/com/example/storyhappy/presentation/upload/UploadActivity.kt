package com.example.storyhappy.presentation.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.storyhappy.R
import com.example.storyhappy.data.Result
import com.example.storyhappy.databinding.ActivityUploadBinding
import com.example.storyhappy.presentation.main.MainActivity
import com.example.storyhappy.presentation.main.MainViewModel
import com.example.storyhappy.utils.createCustomTempFile
import com.example.storyhappy.utils.uriToFile
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class UploadActivity : AppCompatActivity() {

    private var _binding: ActivityUploadBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModel()
    private var photoFile: File? = null
    private lateinit var currentPhotoPath: String

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                showToast(getString(R.string.error_no_permission))
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissions()
        }

        setClickListeners()

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToMainActivity()
                finish()
            }
        })
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            REQUIRED_PERMISSIONS,
            REQUEST_CODE_PERMISSIONS
        )
    }

    private fun setClickListeners() {
        binding.apply {
            cameraButton.setOnClickListener { startTakePhoto() }
            galleryButton.setOnClickListener { startGallery() }
            uploadButton.setOnClickListener { executeUploadStory() }
        }
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.action_choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@UploadActivity,
                "com.example.storyhappy",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            myFile.let { file ->
                photoFile = file
                binding.previewImageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri

            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@UploadActivity)
                photoFile = myFile
                binding.previewImageView.setImageURI(uri)
            }
        }
    }

    private fun executeUploadStory() {
        showLoading(true)

        if (photoFile == null) {
            showLoading(false)
            showToast(getString(R.string.error_no_image))
            return
        }

        val description = binding.addDescriptionStory.text.toString()
        if (description.isEmpty()) {
            showLoading(false)
            showToast(getString(R.string.error_no_description))
            return
        }

        val photoFile = this.photoFile as File

        mainViewModel.getToken().observe(this) { token ->
            uploadStory(token, photoFile, description)
        }
    }

    private fun uploadStory(
        token: String,
        photo: File,
        description: String
    ) {
        mainViewModel.uploadStory(token, photo, description)
            .observe(this@UploadActivity) { uploadState ->
                when (uploadState) {
                    is Result.Success -> {
                        showLoading(false)
                        showToast(uploadState.data.message)
                        navigateToMainActivity()
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showToast(uploadState.message)
                    }

                    is Result.Loading -> {
                        showLoading(true)
                        showToast(getString(R.string.label_uploading))
                    }

                    else -> {}
                }
            }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@UploadActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this@UploadActivity, message, Toast.LENGTH_SHORT).show()
    }

    /*@Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        navigateToMainActivity()
    }*/

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}
