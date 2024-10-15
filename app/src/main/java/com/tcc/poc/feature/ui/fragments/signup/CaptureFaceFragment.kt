package com.tcc.poc.feature.ui.fragments.signup


import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import com.identy.face.FaceOutput
import com.identy.face.enums.FaceTemplate
import com.tcc.poc.BuildConfig
import com.tcc.poc.R
import com.tcc.poc.databinding.FragmentFaceCaptureBinding
import com.tcc.poc.domain.models.BasicState
import com.tcc.poc.domain.models.BiometricX
import com.tcc.poc.domain.models.SignUpRequest
import com.tcc.poc.feature.ui.fragments.SavedData
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

    private val viewModel: SignUpViewModel by activityViewModels()
    private val PERMISSIONS_REQUEST_CODE = 101
    var faceCaptured=false
    private val requiredPermissions = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    // Permission request
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {

            //openCamera()
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
            val base64Image = convertImageToBase64(viewModel.photoUri)
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

    private val requestPermissionStorage = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            requestPermissions()
        } else {
            Toast.makeText(requireContext(), "Storage permission denied", Toast.LENGTH_SHORT).show()
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
    private fun requestPermissions() {


        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
    private fun checkAndRequestPermissions() {
        requestPermissionsIfNecessary()
    }
    private fun hasAllPermissions(): Boolean {
        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }
    private fun requestPermissionsIfNecessary() {
        if (!hasAllPermissions()) {
            // Explain why permissions are needed, especially for sensitive permissions
            if (shouldShowRequestPermissionRationale(requiredPermissions[0])) {
                // Show an explanation dialog before requesting permissions
                AlertDialog.Builder(requireContext())
                    .setTitle("Permissions Needed")
                    .setMessage("This app needs these permissions to access the internet, camera, storage, and phone state for its features. Please grant them to continue.")
                    .setPositiveButton(
                        "Grant Permissions"
                    ) { dialog, which ->
                        requestPermissions(
                            requiredPermissions,
                            PERMISSIONS_REQUEST_CODE
                        )
                    }
                    .setNegativeButton(
                        "Cancel"
                    ) { dialog, which ->
                        // Handle case where user denies permission and doesn't want explanation again
                        //                        Toast.makeText(requireContext(), "App functionality may be limited without permissions.", Toast.LENGTH_SHORT).show();
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, directly request permissions
                requestPermissions(requiredPermissions, PERMISSIONS_REQUEST_CODE)
            }
        } else {
            // All permissions already granted, proceed with functionality
            // Example: access the internet, camera, storage, or phone state as needed
            Toast.makeText(requireContext(), "Permissions granted!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openCamera() {
        // Create a file to save the image
        val photoFile = createImageFile()
        viewModel.photoUri = FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.provider",
            photoFile
        )

        // Trigger the camera to take a picture
        capturePhotoLauncher.launch(viewModel.photoUri)


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
        checkAndRequestPermissions()
        observer()

        binding.imgBack.setOnClickListener{

            findNavController().navigateUp()
        }
        binding.reCaptureBtn.setOnClickListener {
            viewModel.initFaceSdk(requireActivity())

        }

        binding.faceCaptureBtn.setOnClickListener {
            if (faceCaptured)
            {
                val base64Image =SavedData.faceBase64
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
            }else{
                viewModel.initFaceSdk(requireActivity())

            }


            //  showProgress()
            //face license response
        }


    }

    private fun observer()
    {
        viewModel.getFaceResponse().observe(requireActivity()) { identyResponse ->
            // hideProgress()

            val faceOutput: FaceOutput = identyResponse!!.prints
            val score = faceOutput!!.score
            if (faceOutput != null) {
                //  showProgress()
                faceCaptured=true
                binding.reCaptureBtn.visibility=View.VISIBLE
                binding.faceCaptureBtn.text=getString(R.string.proceed)
                if (faceOutput.spoofScore > 0.7f) {
                    try {
                        val pngPhoto = faceOutput.templates[FaceTemplate.PNG]

                        // String pngPhoto = identyResponse.getIcaoPrints().getTemplates().get(FaceTemplate.PNG);
                        SavedData.faceBase64 = pngPhoto

                        //                        PreferenceUtil.getInstance(this).saveString(PreferenceUtil.KEY_FACE_BASE64, pngPhoto);
                        val dataBase64 = Base64.decode(pngPhoto, Base64.DEFAULT)
                        printSizeInKb(dataBase64)

                        val pngBitmap =
                            BitmapFactory.decodeByteArray(dataBase64, 0, dataBase64.size)
                        SavedData.setFaceResultBitmap(pngBitmap)

                        binding.imgPerson.setImageBitmap(pngBitmap)

                        //Face  SDK initialization
                        //
                    } catch (e: Exception) {
                        Log.e(TAG, "faceResponse: ", e);
                        // hideProgress();
                        Toast.makeText(
                            requireActivity(),
                            e.getLocalizedMessage(),
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
        }

    }


                 fun printSizeInKb(faceImage: ByteArray) {
                    // Get the size in bytes
                    val sizeInBytes = faceImage.size
                    // Convert bytes to kilobytes (1 KB = 1024 bytes)
                    val sizeInKilobytes = sizeInBytes / 1024.0
                    // Print the size in KB
                    System.out.printf("sematiApi face WSQ Size: %.2f KB\n", sizeInKilobytes)
                }
    fun observeViewModel() {
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

        //face license response

    }
    private fun showProgress() {
      //  runOnUiThread(Runnable { binding.progressLayout.progressLayout.setVisibility(View.VISIBLE) })
    }

    private fun hideProgress() {
      //  runOnUiThread(Runnable { binding.progressLayout.progressLayout.setVisibility(View.GONE) })
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
