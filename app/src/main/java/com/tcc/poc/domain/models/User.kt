package com.tcc.poc.domain.models

data class User(
    val biometric: List<Biometric>,
    val email: String,
    var firstName: String,
    var id: String,
    val lastName: String,
    val mobile: String,
    val password: String,
    val pin: String,
    val status: Int
)