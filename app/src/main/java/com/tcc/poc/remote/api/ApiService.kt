package com.tcc.poc.remote.api


import com.tcc.poc.domain.models.BasicResponse
import com.tcc.poc.domain.models.CardRequest
import com.tcc.poc.domain.models.CardResponse
import com.tcc.poc.domain.models.LoginRequest
import com.tcc.poc.domain.models.SignUpRequest
import com.tcc.poc.domain.models.TransactionResponse
import com.tcc.poc.domain.models.User
import retrofit2.Response
import retrofit2.http.*

// sub-domain
interface ApiService {

        @POST("/api/customer/login")
        suspend fun login(@Body loginRequest: LoginRequest): Response<BasicResponse<User>>  // Define your response model appropriately

        @POST("/api/customer/create")
        suspend fun signup(@Body loginRequest: SignUpRequest): Response<BasicResponse<User>>

        @POST("/api/paymentCard/create")
        suspend fun addCard(@Body cardRequest: CardRequest): Response<BasicResponse<CardResponse>>  // Define your response model appropriately

        @GET("/api/transaction/getbycustomer")
        suspend fun getTransaction(@Query("Id") id: String): Response<BasicResponse<List<TransactionResponse>>>  // Define your response model appropriately


}

    /*
    @FormUrlEncoded
    @POST("app/mobile-versions/check-update")
    suspend fun checkVersion(
        @FieldMap dto: HashMap<String, String>,
    ): GeneralResponseModel<JsonObject>

     */




