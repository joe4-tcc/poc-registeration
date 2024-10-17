package com.tcc.poc.feature.ui.fragments.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tcc.poc.R
import com.tcc.poc.databinding.FragmentSucessFaceCaptureBinding
import com.tcc.poc.domain.models.CardRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class SuccessfulFaceFragment : Fragment(R.layout.fragment_sucess_face_capture) {

    private var _binding: FragmentSucessFaceCaptureBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewBinding
        _binding = FragmentSucessFaceCaptureBinding.bind(view)

        // Set up UI interactions and listeners
        setupUI()
    }

    private fun setupUI() {

        binding.proceedBtn.setOnClickListener {
            findNavController().navigate(SuccessfulFaceFragmentDirections.actionSuccessfulFaceFragmentToLoginFragment())
        }

    }
}