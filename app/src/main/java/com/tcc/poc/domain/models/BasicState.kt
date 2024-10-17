package com.tcc.poc.domain.models

sealed class BasicState{
    object Idle : BasicState()
    object Loading : BasicState()
    data class Success<T>(val data: T) : BasicState()  // Success can hold data of any type
    data class Error(val message: String) : BasicState()
}