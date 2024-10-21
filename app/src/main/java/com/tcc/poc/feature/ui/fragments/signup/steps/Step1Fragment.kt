package com.tcc.poc.feature.ui.fragments.signup.steps

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tcc.poc.R
import com.tcc.poc.databinding.FragmentStep1Binding


import com.tcc.poc.feature.ui.fragments.signup.SignUpViewModel
import com.tcc.poc.feature.ui.fragments.signup.StepDataCollector
import com.tcc.poc.feature.ui.fragments.util.isValidEmail

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class Step1Fragment : Fragment(R.layout.fragment_step1),StepDataCollector {

    // ViewBinding instance
    private var _binding: FragmentStep1Binding? = null
    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewBinding
        _binding = FragmentStep1Binding.bind(view)

        // Set up UI interactions and listeners
        setupUI()
        observeViewModel()
    }

    private fun setupUI() {



    }

    private fun observeViewModel() {
        // Observe login state
    }

    private fun isValidInput(username: String, password: String): Boolean {
        return username.isNotEmpty() && password.isNotEmpty()
    }

    private fun showLoading(isLoading: Boolean) {
      //  binding.llProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
      //  binding.loginBtn.isEnabled = !isLoading
    }

    private fun handleLoginSuccess() {
        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
        // Navigate or handle successful login
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Avoid memory leaks by nullifying the binding reference
    }

    override fun collectStepData(): Boolean {
        val email=binding.emailEt.text.toString()
        val phone=binding.phoneNumberEt.text.toString()
        val firstName = binding.firstNameEt.text.toString()
        val lastName = binding.lastNameEt.text.toString()

        // Perform validation (optional)
        if (firstName.isBlank()) {
            binding.firstNameEt.error = "This field is required"
            return false
        }
        if (lastName.isBlank()) {
            binding.lastNameEt.error = "This field is required"
            return false
        }
        if (email.isBlank()) {
            binding.emailEt.error = "This field is required"
            return false
        }
        if (!isValidEmail(email))
        {
            binding.emailEt.error = "Please provide correct email"
            return false

        }
        if (phone.isBlank()) {
            binding.phoneNumberEt.error = "This field is required"
            return false
        }
        if (phone.length<=9) {
            binding.phoneNumberEt.error = "Please provide correct phone number"
            return false
        }


        // Save data to ViewModel
        viewModel.setStep1Data(SignUpViewModel.Step1(email,phone,firstName,lastName))
        return true
    }
}
