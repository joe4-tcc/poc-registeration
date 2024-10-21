package com.tcc.poc.feature.ui.fragments.signup

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tcc.poc.feature.ui.fragments.signup.steps.Step1Fragment
import com.tcc.poc.feature.ui.fragments.signup.steps.Step2Fragment
import com.tcc.poc.feature.ui.fragments.signup.steps.Step3Fragment

class SignupPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2 // Number of steps (or fragments)
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Step1Fragment()  // Replace with actual fragments for each step
            //1 -> Step2Fragment()
            1 -> Step2Fragment()
            else -> Step1Fragment()
        }
    }
}