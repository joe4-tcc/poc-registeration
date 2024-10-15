package com.tcc.poc.domain.models

data class BasicResponse<T>(
    var data : T,
    val error: Error,
    val success: String
)