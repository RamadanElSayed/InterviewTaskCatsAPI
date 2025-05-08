package com.ramadan.readybackendproject.presentation.screens.uimodel

sealed class ErrorState {
    data object NetworkError : ErrorState()
    data object NotFound : ErrorState()
    data object AccessDenied : ErrorState()
    data object ServiceUnavailable : ErrorState()
    data object EmptyResponse : ErrorState()
    data class ApiError(val message: String, val code: Int) : ErrorState()
    data class UnknownError(val message: String) : ErrorState()
}