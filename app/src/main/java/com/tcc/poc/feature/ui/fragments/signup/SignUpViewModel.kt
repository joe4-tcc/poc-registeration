package com.tcc.poc.feature.ui.fragments.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcc.poc.domain.models.LoginRequest
import com.tcc.poc.domain.models.BasicState
import com.tcc.poc.domain.models.SignUpRequest
import com.tcc.poc.feature.ui.fragments.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val loginRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<BasicState>(BasicState.Idle)
    val loginState: StateFlow<BasicState> = _loginState

    private val _SignUpState = MutableStateFlow<BasicState>(BasicState.Idle)
    val SignUpState: StateFlow<BasicState> = _loginState

    private val _step1Data = MutableStateFlow<Step1?>(null)
    val step1Data: StateFlow<Step1?> = _step1Data

    private val _step2Data = MutableStateFlow<Step2?>(null)
    val step2Data: StateFlow<Step2?> = _step2Data

    private val _step3Data = MutableStateFlow<Step3?>(null)
    val step3Data: StateFlow<Step3?> = _step3Data

    private val _photoDataStep = MutableStateFlow<photoStep?>(null)
    val photoDataStep : StateFlow<photoStep?> = _photoDataStep

    // Functions to update step data
    fun setStep1Data(data: Step1) {
        _step1Data.value = data
    }

    fun setStep2Data(data: Step2) {
        _step2Data.value = data
    }

    fun setStep3Data(data: Step3) {
        _step3Data.value = data
    }

    // Function to get all data combined
    fun getAllData(): SignupData {
        return SignupData(
            step1Data.value ?: Step1("",""),
            step2Data.value ?: Step2("",""),
            step3Data.value ?: Step3("","","",""),
            photoStep = photoDataStep.value?: photoStep("")

        )
    }
    fun login(request: LoginRequest) {
        viewModelScope.launch {
            _loginState.value = BasicState.Loading
            try {
                val result = loginRepository.login(request)
                if (result.isSuccess) {
                    _loginState.value = BasicState.Success
                } else {
                    _loginState.value = BasicState.Error("Login failed: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                _loginState.value = BasicState.Error("Error: ${e.message}")
            }
        }
    }
    fun signUp(request: SignUpRequest) {
        viewModelScope.launch {
            _loginState.value = BasicState.Loading
            try {
                val result = loginRepository.signUp(request)
                if (result.isSuccess) {
                    _loginState.value = BasicState.Success
                } else {
                    _loginState.value = BasicState.Error("SignUp failed: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                _loginState.value = BasicState.Error("Error: ${e.message}")
            }
        }
    }

    data class Step1(
        val firstName: String,
        val lastName: String
    )
    data class Step2(
        val email: String,
        val password : String
    )
    data class Step3(
        val tccEmail : String,
        val phoneNum : String,
        val password : String,
        val pin : String
    )
    data class photoStep(
        val photo64: String
    )
    data class SignupData(

        val step1: Step1,
        val step2: Step2,
        val step3: Step3,
        val photoStep: photoStep


    )
}
