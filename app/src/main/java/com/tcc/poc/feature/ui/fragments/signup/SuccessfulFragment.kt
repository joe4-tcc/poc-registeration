package com.tcc.poc.feature.ui.fragments.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.tcc.poc.R
import com.tcc.poc.databinding.FragmentStep1Binding
import com.tcc.poc.databinding.FragmentSuccessfulRegisterationBinding
import com.tcc.poc.domain.models.BasicState
import com.tcc.poc.domain.models.CardRequest
import com.tcc.poc.feature.ui.fragments.login.LoginFragmentDirections


import com.tcc.poc.feature.ui.fragments.signup.SignUpViewModel
import com.tcc.poc.feature.ui.fragments.signup.StepDataCollector

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class SuccessfulFragment : Fragment(R.layout.fragment_successful_registeration) {

    // ViewBinding instance
    private var _binding: FragmentSuccessfulRegisterationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddCardViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewBinding
        _binding = FragmentSuccessfulRegisterationBinding.bind(view)

        // Set up UI interactions and listeners
        setupUI()
        observeViewModel()
    }

    private fun setupUI() {


     //   binding.welcomeMsg.text = "Welcome! " + arguments?.get("userName").toString()

        expWatcher()
        cardWatcher()

        binding.cardSubmit.setOnClickListener {

            var name = binding.cardName.text.toString().trim()
            var card = binding.cardNum.text.toString().trim()
            var cvv = binding.cvv.text.toString().trim()
            var expDate = binding.exp.text.toString().trim()

            if (isValidInput(name, card, cvv, expDate)) {

                viewModel.addCard(
                    CardRequest(
                        arguments?.get("userId").toString(),
                        nameOnCard = name,
                        cardNumber = card,
                        expiryMonth = "12",
                        expiryYear = "10",
                        cvv = cvv
                    )
                )

            } else {
                Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signout.setOnClickListener {

            findNavController().navigate(SuccessfulFragmentDirections.actionSuccessfulFragmentToLoginFragment())
        }

        binding.profileImage.setOnClickListener {
            findNavController().navigate(
                SuccessfulFragmentDirections.actionSuccessfulFragmentToTransactionFragments(
                    arguments?.get("userId").toString(),
                    arguments?.get("userName").toString()
                )
            )
        }

    }

    private fun expWatcher() =
        // TextWatcher for expiry date formatting
        binding.exp.addTextChangedListener(object : TextWatcher {
            private var isFormatting: Boolean = false
            private var deletingBackward: Boolean = false

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                deletingBackward = count > 0 && after == 0
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (isFormatting) return

                isFormatting = true

                val input = s.toString().replace("/", "")

                // Insert a slash (/) after 2 characters for MM/YY format
                val formatted = StringBuilder()
                for (i in input.indices) {
                    formatted.append(input[i])
                    if (i == 1 && input.length > 2) {
                        formatted.append("/")
                    }
                }

                // Update the text with the formatted string
                binding.exp.removeTextChangedListener(this)
                binding.exp.setText(formatted.toString())
                binding.exp.setSelection(formatted.length)
                binding.exp.addTextChangedListener(this)

                isFormatting = false
            }
        })

    private fun cardWatcher() =
        // Add TextWatcher for formatting the card number input
        binding.cardNum.addTextChangedListener(object : TextWatcher {
            private var isFormatting: Boolean = false
            private var deletingHyphen: Boolean = false
            private var hyphenStart: Int = 0
            private var deletingBackward: Boolean = false

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                if (count > 0 && after == 0) {
                    deletingBackward = true
                } else {
                    deletingBackward = false
                }
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (isFormatting) return

                isFormatting = true

                // Remove all spaces
                val input = s.toString().replace(" ", "")

                // Insert a space after every 4 characters
                val formatted = StringBuilder()
                for (i in input.indices) {
                    formatted.append(input[i])
                    if ((i + 1) % 4 == 0 && (i + 1) != input.length) {
                        formatted.append(" ")
                    }
                }

                // Update the text with the formatted string
                binding.cardNum.removeTextChangedListener(this)
                binding.cardNum.setText(formatted.toString())
                binding.cardNum.setSelection(formatted.length)
                binding.cardNum.addTextChangedListener(this)

                isFormatting = false
            }
        })

    private fun isValidInput(name: String, card: String, cvv: String, date: String) =
        name.isNotEmpty() && card.isNotEmpty() && cvv.isNotEmpty() && date.isNotEmpty()


    private fun observeViewModel() {
        // Observe login state
        lifecycleScope.launchWhenStarted {
            viewModel.addCardState.collect { state ->
                when (state) {
                    is BasicState.Loading -> showLoading(true)
                    is BasicState.Success -> {
                        showLoading(false)
                        handleCardSuccess()
                    }

                    is BasicState.Error -> {
                        showLoading(false)
                        Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    }

                    BasicState.Idle -> {}
                }
            }
        }
    }

    private fun isValidInput(username: String, password: String): Boolean {
        return username.isNotEmpty() && password.isNotEmpty()
    }

    private fun showLoading(isLoading: Boolean) {
        //  binding.llProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        //  binding.loginBtn.isEnabled = !isLoading
    }

    private fun handleCardSuccess() {
        Toast.makeText(context, "Card information is saved", Toast.LENGTH_SHORT).show()
        binding.cardName.setText("")
        binding.cardNum.setText("")
        binding.cvv.setText("")
        binding.exp.setText("")

        findNavController().navigate(
            SuccessfulFragmentDirections.actionSuccessfulFragmentToTransactionFragments(
                arguments?.get("userId").toString(),
                arguments?.get("userName").toString()
            )
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Avoid memory leaks by nullifying the binding reference
    }

}
