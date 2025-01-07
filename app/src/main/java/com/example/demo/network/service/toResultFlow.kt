package com.example.demo.network.service

import com.example.demo.shared.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

fun <T> toResultFlow(call: suspend () -> T): Flow<UiState<T>> {
    return flow {
        emit(UiState.Loading)
        try {
            val response = call()
            emit(UiState.Success(response))
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Unknown error")) // Use e.message here
        }
    }.flowOn(Dispatchers.IO)
}
