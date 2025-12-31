package com.tridevs.serplusvita.data.models

data class ApiResponse<T>(
    val data: T?,
    val error: ApiError?
)
data class ApiError(
    val code: Int,
    val message: String
)


