package com.tcc.poc.domain.models

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class TransactionResponse(
    val id: String,
    val biometricVerification_ID: String,
    val paymentCard_ID: String,
    val account_ID: String,
    val transactionType: Int,
    val billNumber: String,
    val amount: Double,
    val createdDate: String,
    val transactionNumber: String,
    val status: Int,
    val business: BusinessResponse,
    val biometricVerification: String,
    val paymentCard: String
)

data class BusinessResponse(
    val id: String,
    val name: String,
    val businessTypes: Int,
    val email: String,
    val mobile: String,
    val address: String,
    val createdDate: String,
    val isEmailVerified: String,
    val isMobileVerified: String,
    val status: Int,
    val account: String
)

@SuppressLint("NewApi")
fun TransactionResponse.toTransactionModel() =
    this.let {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.nnnnnnn")
        val localDateTime = LocalDateTime.parse(it.createdDate, formatter)
        Transaction(
            when {
                it.billNumber.length > 10 -> "***${it.billNumber.takeLast(10)}"
                else -> it.transactionNumber
            },
            it.amount,
            localDateTime.toLocalDate(),
            localDateTime.toLocalTime(),
            it.business?.name
        )
    }