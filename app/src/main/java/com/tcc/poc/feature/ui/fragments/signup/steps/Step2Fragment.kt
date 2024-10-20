package com.tcc.poc.feature.ui.fragments.signup.steps

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tcc.poc.R
import com.tcc.poc.databinding.FragmentStep2Binding


import com.tcc.poc.feature.ui.fragments.signup.SignUpViewModel
import com.tcc.poc.feature.ui.fragments.signup.StepDataCollector

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class Step2Fragment : Fragment(R.layout.fragment_step2),StepDataCollector {

    // ViewBinding instance
    private var _binding: FragmentStep2Binding? = null
    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewBinding
        _binding = FragmentStep2Binding.bind(view)

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
        //binding.loginBtn.isEnabled = !isLoading
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

        val password = binding.passwordEt.text.toString()
        val confirmPassword = binding.confirmPasswordEt.text.toString()
        val pin=binding.pincodeEt.text.toString()
        val confirmPin=binding.confirmPinCodeEt.text.toString()

        // Perform validation (optional)
        if (password.isBlank()) {
            binding.passwordEt.error = "This field is required"
            return false
        }
        if (confirmPassword.isBlank()) {
            binding.confirmPasswordEt.error = "This field is required"
            return false
        }
        if (password != confirmPassword) {
            binding.confirmPasswordEt.error = "Password Not Match"
            return false
        }
        if (pin.isBlank()) {
            binding.pincodeEt.error = "This field is required"
            return false
        }
        if (confirmPin.isBlank()) {
            binding.confirmPinCodeEt.error = "This field is required"
            return false
        }
        if (pin != confirmPin) {
            binding.confirmPinCodeEt.error = "Pin Not Match"
            return false
        }
        // Save data to ViewModel
        viewModel.setStep2Data(SignUpViewModel.Step2(pin,password))
        return true
    }
}
