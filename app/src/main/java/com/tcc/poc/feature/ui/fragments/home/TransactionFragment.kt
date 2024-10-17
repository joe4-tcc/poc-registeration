package com.tcc.poc.feature.ui.fragments.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tcc.poc.R
import com.tcc.poc.databinding.FragmentLoginBinding
import com.tcc.poc.databinding.FragmentTransactionBinding


import com.tcc.poc.domain.models.LoginRequest
import com.tcc.poc.domain.models.BasicState
import com.tcc.poc.domain.models.Transaction
import com.tcc.poc.domain.models.TransactionResponse
import com.tcc.poc.feature.ui.fragments.login.LoginFragmentDirections

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class TransactionFragment : Fragment(R.layout.fragment_transaction) {

    // ViewBinding instance
    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    private val args: TransactionFragmentArgs by navArgs()  // Automatically generated class

    private val viewModel: TransactionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewBinding
        _binding = FragmentTransactionBinding.bind(view)

        // Set up UI interactions and listeners
        setupUI()
        observeViewModel()
    }

    private fun setupUI() {

        viewModel.getTransactionByCustomerId(viewModel.getUserId())
        binding.txtName.text=viewModel.getUserName()
        binding.btnAddCard.setOnClickListener {

            findNavController().navigate(
                TransactionFragmentDirections.actionTransactionFragmentsToSuccessfulFragment(
                   args.userId,
                   args.userName
                )
            )
        }

        binding.btnLogout.setOnClickListener {

            viewModel.clearData()
            findNavController().navigate(TransactionFragmentDirections.actionTransactionFragmentsToLoginFragment())
        }

        binding.btnFilterWeek.setOnClickListener {

            binding.btnFilterMonth.background = resources.getDrawable(R.drawable.square)
            binding.btnFilterWeek.background = resources.getDrawable(R.drawable.primary_btn)
            binding.btnFilterYear.background = resources.getDrawable(R.drawable.square)
            binding.btnFilterWeek.setTextColor(this.resources.getColor(R.color.white))
            binding.btnFilterMonth.setTextColor(this.resources.getColor(R.color.black))
            binding.btnFilterYear.setTextColor(this.resources.getColor(R.color.black))


        }
        binding.btnFilterMonth.setOnClickListener {
            binding.btnFilterMonth.background = resources.getDrawable(R.drawable.primary_btn)
            binding.btnFilterWeek.background = resources.getDrawable(R.drawable.square)
            binding.btnFilterYear.background = resources.getDrawable(R.drawable.square)
            binding.btnFilterWeek.setTextColor(this.resources.getColor(R.color.black))
            binding.btnFilterMonth.setTextColor(this.resources.getColor(R.color.white))
            binding.btnFilterYear.setTextColor(this.resources.getColor(R.color.black))

        }
        binding.btnFilterYear.setOnClickListener {
            binding.btnFilterMonth.background = resources.getDrawable(R.drawable.square)
            binding.btnFilterWeek.background = resources.getDrawable(R.drawable.square)
            binding.btnFilterYear.background = resources.getDrawable(R.drawable.primary_btn)
            binding.btnFilterWeek.setTextColor(this.resources.getColor(R.color.black))
            binding.btnFilterMonth.setTextColor(this.resources.getColor(R.color.black))
            binding.btnFilterYear.setTextColor(this.resources.getColor(R.color.white))

        }


    }

    private fun observeViewModel() {
        // Observe login state
        lifecycleScope.launchWhenStarted {
            viewModel.getTransactionState.collect { response ->
                when (response) {
                    is BasicState.Loading -> showLoading(true)
                    is BasicState.Success<*> -> {
                        showLoading(false)

                        val transactions = response.data as List<TransactionResponse>  // Replace Transaction with your actual data type

                        val recyclerView: RecyclerView = binding.rvTransactions
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        recyclerView.adapter = TransactionAdapter(transactions)
                    }

                    is BasicState.Error -> {
                        showLoading(false)
                        Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
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
