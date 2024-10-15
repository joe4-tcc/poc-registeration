package com.tcc.poc.feature.ui.fragments.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.tcc.poc.R
import com.tcc.poc.databinding.FragmentSignupBinding


import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class SignUpFragment : Fragment(R.layout.fragment_signup) {
    private var fragmentlist = arrayListOf<Fragment>()
    var currentPosition = 0
    // ViewBinding instance
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private lateinit var signupPagerAdapter: SignupPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewBinding
        _binding = FragmentSignupBinding.bind(view)

        // Set up UI interactions and listeners
        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        // Setup ViewPager2 with FragmentStateAdapter
        signupPagerAdapter = SignupPagerAdapter(this)  // Use FragmentActivity or Fragment as context
        binding.viewPager2.adapter = signupPagerAdapter

        // Setup stepper with ViewPager2
        binding.dotsIndicator.setViewPager(binding.viewPager2)
        signupPagerAdapter.registerAdapterDataObserver(binding.dotsIndicator.getAdapterDataObserver());
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPosition = position
                if (position==2){
                    binding.continueBtn.setText(getString(R.string.next))
                }else{
                    binding.continueBtn.setText(getString(R.string.continuos))

                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
        binding.continueBtn.setOnClickListener {

            handleNextButtonClick()
            /*
            if (currentPosition == 2) {
                findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToCaptureFaceFragment())
                return@setOnClickListener
            }
            binding.viewPager2.setCurrentItem(
                currentPosition + 1,
                true
            )

             */
        }

        binding.logintv.setOnClickListener{
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
        }


    }
    private fun observeViewModel() {
        // Observe login state

    }
    private fun handleNextButtonClick() {
        val currentFragment = getCurrentStepFragment()

        // Try to collect data from the current step
        if (currentFragment?.collectStepData() == true) {
            // If valid, move to the next step
            if (binding.viewPager2.currentItem < signupPagerAdapter.itemCount - 1) {
                binding.viewPager2.currentItem += 1
            } else {
                // All steps completed, navigate to PictureFragment or submit
               findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToCaptureFaceFragment())
            }
        }
    }

    // Get the currently visible StepFragment
    private fun getCurrentStepFragment(): StepDataCollector? {
        return childFragmentManager.findFragmentByTag("f${binding.viewPager2.currentItem}") as? StepDataCollector
    }
    private fun isValidInput(username: String, password: String): Boolean {
        return username.isNotEmpty() && password.isNotEmpty()
    }

    private fun showLoading(isLoading: Boolean) {
       // binding.llProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
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
}
