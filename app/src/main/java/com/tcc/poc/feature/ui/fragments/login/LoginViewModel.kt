package com.tcc.poc.feature.ui.fragments.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcc.poc.domain.models.LoginRequest
import com.tcc.poc.domain.models.BasicState
import com.tcc.poc.domain.models.User
import com.tcc.poc.feature.ui.fragments.AuthRepository
import com.tcc.poc.feature.ui.fragments.util.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: AuthRepository,
    private val sharedPreferencesManager: SharedPreferencesManager

) : ViewModel() {

    private val _loginState = MutableStateFlow<BasicState>(BasicState.Idle)
    val loginState: StateFlow<BasicState> = _loginState

    var userData: User? = null

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            _loginState.value = BasicState.Loading
            try {
                val result = loginRepository.login(request)
                if (result.isSuccess) {
                    userData = result.getOrNull()?.data
                    _loginState.value = BasicState.Success(userData)
                } else {
                    _loginState.value = BasicState.Error("${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                _loginState.value = BasicState.Error("Error: ${e.message}")
            }
        }
    }

    fun saveData(username : String,userId : String)  {
        sharedPreferencesManager.saveUserName( username)
        sharedPreferencesManager.saveUserId(userId)
    }


}
