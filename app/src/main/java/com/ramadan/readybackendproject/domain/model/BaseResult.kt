package com.ramadan.readybackendproject.domain.model

sealed class BaseResult<out T> {
    data class Success<T>(val data: T) : BaseResult<T>()
    data class Error(val message: String, val code: Int) : BaseResult<Nothing>()
    data object Loading : BaseResult<Nothing>()
}