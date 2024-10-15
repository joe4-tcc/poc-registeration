package com.tcc.poc.feature.ui.fragments.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tcc.poc.R
import com.tcc.poc.databinding.FragmentLoginBinding


import com.tcc.poc.domain.models.LoginRequest
import com.tcc.poc.domain.models.BasicState

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class LoginFragment : Fragment(R.layout.fragment_login) {

    // ViewBinding instance
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewBinding
        _binding = FragmentLoginBinding.bind(view)

        // Set up UI interactions and listeners
        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.loginBtn.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passwordEt.text.toString().trim()

            if (isValidInput(email, password)) {
                val request = LoginRequest(email, password)
                viewModel.login(request)
            } else {
                Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }

        binding.createNewAccountBtn.setOnClickListener {

            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        }
    }

    private fun observeViewModel() {
        // Observe login state
        lifecycleScope.launchWhenStarted {
            viewModel.loginState.collect { loginState ->
                when (loginState) {
                    is BasicState.Loading -> showLoading(true)
                    is BasicState.Success -> {
                        showLoading(false)
                        handleLoginSuccess()
                        findNavController().navigate(
                            LoginFragmentDirections.actionLoginFragmentToTransactionFragments(
                                viewModel.userData?.id,
                                viewModel.userData?.firstName
                            )
                        )
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
        binding.loginBtn.isEnabled = !isLoading
    }

    private fun handleLoginSuccess() {
        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
        // Navigate or handle successful login
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Avoid memory leaks by nullifying the binding reference
    }
}
