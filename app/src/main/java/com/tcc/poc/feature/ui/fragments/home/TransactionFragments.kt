package com.tcc.poc.feature.ui.fragments.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tcc.poc.R
import com.tcc.poc.databinding.FragmentTransactionBinding
import com.tcc.poc.domain.models.BasicState
import com.tcc.poc.domain.models.TransactionResponse
import com.tcc.poc.domain.models.toTransactionModel

class TransactionFragments : Fragment(R.layout.fragment_transaction) {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransactionViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewBinding
        _binding = FragmentTransactionBinding.bind(view)

        // Sample data
//        val transactions = listOf(
//            Transaction("12345", 150.0, "2024-10-01", "Vendor A"),
//            Transaction("67890", 250.0, "2024-10-02", "Vendor B")
//        )

        viewModel.getTransactionByCustomerId(arguments?.get("userId").toString())

        // Find RecyclerView and set it up
        val recyclerView: RecyclerView = binding.rvTransactions
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
       // recyclerView.adapter = TransactionAdapter(transactions)

        // Set up UI interactions and listeners
        setupUI()
        observeViewModel(recyclerView)
    }

    private fun observeViewModel(recyclerView: RecyclerView) {
        // Observe login state
        lifecycleScope.launchWhenStarted {
            viewModel.getTransactionState.collect { state ->
                when (state) {
                    is BasicState.Loading -> showLoading(true)
                    is BasicState.Success -> {
                        viewModel?.transactions
                            ?.map(TransactionResponse::toTransactionModel)
                            ?.sortedByDescending {
                                it.date
                                it.time
                            }
                            .let {
                                recyclerView.adapter =
                                    it?.let { items -> TransactionAdapter(items) }
                            }
                    }

                    is BasicState.Error -> {
                        showLoading(false)
                        Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    }

                    BasicState.Idle -> {

                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        //binding.llProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        //binding.loginBtn.isEnabled = !isLoading
    }

    private fun setupUI() {

        binding.textView.text = "Welcome! " + arguments?.get("userName").toString()

        binding.btnAddCard.setOnClickListener {

            findNavController().navigate(
                TransactionFragmentsDirections.actionTransactionFragmentsToSuccessfulFragment(
                    arguments?.get("userId").toString(),
                    arguments?.get("userName").toString()
                )
            )
        }

        binding.btnLogout.setOnClickListener {

            findNavController().navigate(
                TransactionFragmentsDirections.actionTransactionFragmentsToLoginFragment()
            )
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // To prevent memory leaks
    }
}