package com.tcc.poc.feature.ui.fragments.signup.steps

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.tcc.poc.R
import com.tcc.poc.databinding.FragmentStep3Binding


import com.tcc.poc.feature.ui.fragments.signup.SignUpViewModel
import com.tcc.poc.feature.ui.fragments.signup.StepDataCollector

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class Step3Fragment : Fragment(R.layout.fragment_step3), StepDataCollector {

    // ViewBinding instance
    private var _binding: FragmentStep3Binding? = null
    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewBinding
        _binding = FragmentStep3Binding.bind(view)

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
        // binding.llProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
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
        val emailtcc = binding.emailEt.text.toString()
        val phoneNum = binding.phoneNumberEt.text.toString()
        val password = binding.passwordEt.text.toString()
        val pin = binding.tccictNumEt.text.toString()

        // Perform validation (optional)
        if (emailtcc.isBlank()) {
            binding.emailEt.error = getString(R.string.this_field_is_required)
            return false
        }
        if (phoneNum.isBlank()) {
            binding.phoneNumberEt.error = getString(R.string.this_field_is_required)
            return false
        }
        if (password.isBlank()) {
            binding.passwordEt.error = getString(R.string.this_field_is_required)
            return false
        }
        if (pin.isBlank()) {
            binding.tccictNumEt.error = getString(R.string.password_confirmation_not_matched)
            return false
        }

        // Save data to ViewModel
        viewModel.setStep3Data(SignUpViewModel.Step3(emailtcc, phoneNum, password, pin))
        return true
    }
}
