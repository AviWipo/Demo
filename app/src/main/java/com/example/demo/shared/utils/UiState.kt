package com.example.demo.shared.utils

sealed class UiState<out T> {

    data class Success<T>(val data: T?) : UiState<T>()

    data class Error<T>(val message: String) : UiState<T>()

    data object Loading : UiState<Nothing>()

    data object Idle : UiState<Nothing>()

}