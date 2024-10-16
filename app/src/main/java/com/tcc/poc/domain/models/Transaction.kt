package com.tcc.poc.domain.models

import java.time.LocalDate
import java.time.LocalTime


data class Transaction (
    val transactionNumber: String,
    val amount: Double,
    val date: LocalDate,
    val time: LocalTime,
    val vendor: String?
)