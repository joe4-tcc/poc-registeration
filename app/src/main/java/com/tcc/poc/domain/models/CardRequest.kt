package com.tcc.poc.domain.models

data class CardRequest (
    val customer_ID: String,
    val nameOnCard: String,
    val cardType: String = "VISA",
    val cardNumber: String,
    val expiryMonth: String,
    val expiryYear: String,
    val cvv: String,
    val isPrimary: Boolean = true
)