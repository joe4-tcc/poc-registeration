package com.tcc.poc.feature.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcc.poc.domain.models.BasicState
import com.tcc.poc.domain.models.TransactionResponse
import com.tcc.poc.feature.ui.fragments.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _getTransactionState = MutableStateFlow<BasicState>(BasicState.Idle)
    val getTransactionState: StateFlow<BasicState> = _getTransactionState

    private var transactions : List<TransactionResponse>? = emptyList()

    fun getTransactionByCustomerId(customerId: String) {

        viewModelScope.launch {
            _getTransactionState.value = BasicState.Loading
            try {
                val result = authRepository.getTransactionByCustomer(customerId)
                if (result.isSuccess) {
                    transactions =  result.getOrNull()?.data
                    _getTransactionState.value = BasicState.Success
                } else {
                    _getTransactionState.value = BasicState.Error("${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                _getTransactionState.value = BasicState.Error("Error: ${e.message}")
            }
        }
    }

}