package com.tcc.poc.domain.models

data class LoginRequest(
    val email: String,
    val password: String
)