package com.tcc.poc.feature.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.tcc.poc.R

import com.tcc.poc.databinding.ActivityMainBinding
import com.tcc.poc.feature.ui.fragments.signup.SignUpViewModel


import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val signupViewModel: SignUpViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialize navigation
        setupNavigation()

    }


    fun setupNavigation()
    {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
      //  NavigationUI.setupActionBarWithNavController(this, navController)
      //  appBarConfiguration = AppBarConfiguration(navController.graph)
    }

}