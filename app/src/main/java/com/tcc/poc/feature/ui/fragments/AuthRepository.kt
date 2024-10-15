package com.tcc.poc.feature.ui.fragments

import com.tcc.poc.remote.api.ApiService
import com.tcc.poc.domain.models.BasicResponse
import com.tcc.poc.domain.models.CardRequest
import com.tcc.poc.domain.models.CardResponse
import com.tcc.poc.domain.models.LoginRequest
import com.tcc.poc.domain.models.SignUpRequest
import com.tcc.poc.domain.models.TransactionResponse
import com.tcc.poc.domain.models.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun login(request: LoginRequest): Result<BasicResponse<User>?> {
        return try {
            val response = apiService.login(request) // Assuming your API has a login endpoint
            if (response.isSuccessful) {
                Result.success(response.body())
            } else if (response.toString().contains("503")) {
                Result.failure(Exception("Server is down"))
            } else if (response.toString().contains("400")) {
                Result.failure(Exception("Bad request"))
            } else if (response.toString().contains("500")) {
                Result.failure(Exception("Internal server error"))
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(request: SignUpRequest): Result<BasicResponse<User>?> {
        return try {
            val response = apiService.signup(request) // Assuming your API has a login endpoint
            if (response.isSuccessful) {
                Result.success(response.body())
            } else if (response.toString().contains("503")) {
                Result.failure(Exception("Server is down"))
            } else if (response.toString().contains("400")) {
                Result.failure(Exception("Bad request"))
            } else if (response.toString().contains("500")) {
                Result.failure(Exception("Internal server error"))
            } else {
                Result.failure(Exception("Signup failed"))
            }
        } catch (e: Exception) {
            print(e.stackTrace)
            Result.failure(e)
        }
    }

    suspend fun addCard(request: CardRequest): Result<BasicResponse<CardResponse>?> =
        try {
            val response = apiService.addCard(request)
            if (response.isSuccessful) {
                Result.success(response.body())
            } else if (response.toString().contains("503")) {
                Result.failure(Exception("Server is down"))
            } else if (response.toString().contains("400")) {
                Result.failure(Exception("Bad request"))
            } else if (response.toString().contains("500")) {
                Result.failure(Exception("Internal server error"))
            } else {
                Result.failure(Exception("Failed to add card information"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun getTransactionByCustomer(id: String): Result<BasicResponse<List<TransactionResponse>>?> =
        try {
            val response = apiService.getTransaction(id)
            if (response.isSuccessful) {
                Result.success(response.body())
            } else if (response.toString().contains("503")) {
                Result.failure(Exception("Server is down"))
            } else if (response.toString().contains("400")) {
                Result.failure(Exception("Bad request"))
            } else if (response.toString().contains("500")) {
                Result.failure(Exception("Internal server error"))
            } else {
                Result.failure(Exception("Failed to add card information"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
}