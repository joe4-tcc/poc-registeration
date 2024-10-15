package com.tcc.poc.domain.models

sealed class BasicState {
    object Idle : BasicState()
    object Loading : BasicState()
    object Success : BasicState()
    data class Error(val message: String) : BasicState()
}