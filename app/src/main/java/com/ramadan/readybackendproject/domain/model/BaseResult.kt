package com.ramadan.readybackendproject.domain.model

sealed class BaseResult<out T> {
    data class Success<T>(val data: T) : BaseResult<T>()
    data class Error(val error: ApiError) : BaseResult<Nothing>()
    data object Loading : BaseResult<Nothing>()
}

sealed class ApiError {
    abstract val message: String
    data class Network(override val message: String = "Network error occurred") : ApiError()
    data class NotFound(override val message: String = "Resource not found") : ApiError()
    data class AccessDenied(override val message: String = "Access denied") : ApiError()
    data class ServiceUnavailable(override val message: String = "Service unavailable") : ApiError()
    data class ServerError(override val message: String, val code: Int) : ApiError() // Renamed from ApiError
    data class Unknown(override val message: String = "An unknown error occurred") : ApiError()
}