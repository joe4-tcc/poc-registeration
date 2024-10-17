package com.tcc.poc.feature.ui.fragments.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tcc.poc.R
import com.tcc.poc.databinding.FragmentTransactionBinding
import com.tcc.poc.domain.models.Transaction

class TransactionFragments : Fragment(R.layout.fragment_transaction) {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransactionViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            // Initialize ViewBinding
            _binding = FragmentTransactionBinding.bind(view)

            // Sample data
            val transactions = listOf(
                Transaction("12345", 150.0, "2024-10-01", "Vendor A"),
                Transaction("67890", 250.0, "2024-10-02", "Vendor B")
            )


            viewModel.getTransactionByCustomerId("3BB0AFE3-99D5-4695-8BDA-08DCE21C0F79")

            // Find RecyclerView and set it up
            val recyclerView: RecyclerView = binding.rvTransactions
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
          //  recyclerView.adapter = TransactionAdapter(transactions)

            // Set up UI interactions and listeners
            setupUI()

    }

    private fun setupUI() {

        binding.btnAddCard.setOnClickListener {

            findNavController().navigate(
                TransactionFragmentDirections.actionTransactionFragmentsToSuccessfulFragment(
                    arguments?.get("userId").toString(),
                    arguments?.get("userName").toString()
                )
            )
        }

        binding.btnLogout.setOnClickListener {

            findNavController().navigate(
                TransactionFragmentDirections.actionTransactionFragmentsToLoginFragment()
            )
        }

        binding.btnFilterWeek.setOnClickListener{

            binding.btnFilterMonth.background=resources.getDrawable(R.drawable.square)
            binding.btnFilterWeek.background=resources.getDrawable(R.drawable.primary_btn)
            binding.btnFilterYear.background=resources.getDrawable(R.drawable.square)


        }
        binding.btnFilterMonth.setOnClickListener{
            binding.btnFilterMonth.background=resources.getDrawable(R.drawable.primary_btn)
            binding.btnFilterWeek.background=resources.getDrawable(R.drawable.square)
            binding.btnFilterYear.background=resources.getDrawable(R.drawable.square)
        }
        binding.btnFilterYear.setOnClickListener{
            binding.btnFilterMonth.background=resources.getDrawable(R.drawable.square)
            binding.btnFilterWeek.background=resources.getDrawable(R.drawable.square)
            binding.btnFilterYear.background=resources.getDrawable(R.drawable.primary_btn)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // To prevent memory leaks
    }
}