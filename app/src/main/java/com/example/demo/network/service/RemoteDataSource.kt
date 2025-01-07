package com.example.demo.network.service

import com.example.demo.network.model.DogResponse
import com.example.demo.shared.utils.UiState
import kotlinx.coroutines.flow.Flow

class RemoteDataSource(private val apiService: ApiService) {

    suspend fun getRandomDogImages(): Flow<UiState<DogResponse>> {
        return toResultFlow {
            apiService.getRandomDogImages()
        }
    }
}