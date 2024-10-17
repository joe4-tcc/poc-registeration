package com.tcc.poc.feature.ui.fragments.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tcc.poc.domain.models.BasicState
import com.tcc.poc.domain.models.CardRequest
import com.tcc.poc.feature.ui.fragments.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCardViewModel @Inject constructor(
    private val addCardRepo: AuthRepository
) : ViewModel() {
    private val _addCardState = MutableStateFlow<BasicState>(BasicState.Idle)
    val addCardState: StateFlow<BasicState> = _addCardState

    fun addCard(request: CardRequest) {
        viewModelScope.launch {
            _addCardState.value = BasicState.Loading
            try {
                val result = addCardRepo.addCard(request)
                if (result.isSuccess) {
                    _addCardState.value = BasicState.Success(result.getOrNull()?.data)
                } else {
                    _addCardState.value = BasicState.Error("${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                _addCardState.value = BasicState.Error("Error: ${e.message}")
            }
        }
    }

}