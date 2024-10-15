package com.tcc.poc.domain.models

data class Transaction (
    val transactionNumber: String,
    val amount: Double,
    val date: String,
    val vendor: String
)