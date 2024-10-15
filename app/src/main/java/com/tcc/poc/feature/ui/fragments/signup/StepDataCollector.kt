package com.tcc.poc.feature.ui.fragments.signup

interface StepDataCollector {
    fun collectStepData(): Boolean  // Return false if validation fails
}