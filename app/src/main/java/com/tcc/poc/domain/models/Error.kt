package com.tcc.poc.domain.models

data class Error(
    val errorCode: String,
    val errorDetails: String,
    val errorMessage: String
)