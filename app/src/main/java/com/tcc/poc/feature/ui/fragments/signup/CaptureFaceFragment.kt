package com.tcc.poc.feature.ui.fragments.signup

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tcc.poc.R
import com.tcc.poc.databinding.FragmentFaceCaptureBinding


import com.tcc.poc.domain.models.BasicState
import com.tcc.poc.domain.models.BiometricX
import com.tcc.poc.domain.models.SignUpRequest

import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
open class CaptureFaceFragment : Fragment(R.layout.fragment_face_capture) {

    // ViewBinding instance
    private var _binding: FragmentFaceCaptureBinding? = null
    private val binding get() = _binding!!
    private lateinit var photoUri: Uri

    private val viewModel: SignUpViewModel by activityViewModels()

    // Permission request
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            openCamera()
        } else {
            // Handle permission denial
        }
    }

    // Photo capture
    private val capturePhotoLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            // Photo was taken successfully, now convert to Base64
            val base64Image = convertImageToBase64(photoUri)
           val lastName= viewModel.getAllData().step1.lastName
           val firstName= viewModel.getAllData().step1.firstName

            val oldEmail=viewModel.getAllData().step2.email
           val oldPassword= viewModel.getAllData().step2.password

           val email= viewModel.getAllData().step3.tccEmail
           val password= viewModel.getAllData().step3.password
            val phoneNum=viewModel.getAllData().step3.phoneNum
           val pin= viewModel.getAllData().step3.pin
           val photo= listOf(BiometricX(base64Image,1))

            viewModel.signUp(SignUpRequest(photo, email, firstName, lastName, phoneNum, password, pin))
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewBinding
        _binding = FragmentFaceCaptureBinding.bind(view)

        // Set up UI interactions and listeners
        setupUI()
        observeViewModel()
    }
    private fun requestCameraPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
    private fun openCamera() {
        val photoFile = createImageFile()
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            photoFile
        )
        capturePhotoLauncher.launch(photoUri)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }
    private fun convertImageToBase64(photoUri: Uri): String {
        val inputStream = requireContext().contentResolver.openInputStream(photoUri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true)

        val byteArrayOutputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }
    private fun setupUI() {

        binding.faceCaptureBtn.setOnClickListener {

            requestCameraPermission()

        }


    }

    private fun observeViewModel() {
        // Observe login state
        lifecycleScope.launchWhenStarted {
            viewModel.SignUpState.collect { loginState ->
                when (loginState) {
                    is BasicState.Loading -> showLoading(true)
                    is BasicState.Success -> {
                        showLoading(false)
                        handleSignUpSuccess()
                    }
                    is BasicState.Error -> {
                        showLoading(false)
                        Toast.makeText(context, loginState.message, Toast.LENGTH_SHORT).show()
                    }

                    BasicState.Idle -> {

                    }
                }
            }
        }
    }

    private fun isValidInput(username: String, password: String): Boolean {
        return username.isNotEmpty() && password.isNotEmpty()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.llProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
      //  binding.loginBtn.isEnabled = !isLoading
    }

    private fun handleSignUpSuccess() {
        Toast.makeText(context, "Success! Your account has been created.", Toast.LENGTH_SHORT).show()
        findNavController().navigate(CaptureFaceFragmentDirections.actionCaptureFaceFragmentToSuccessfulFaceFragment())
        // Navigate or handle successful login
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Avoid memory leaks by nullifying the binding reference
    }
}
