package com.example.demo.screen.main.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.network.model.DogResponse
import com.example.demo.network.service.RemoteDataSource
import com.example.demo.shared.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val dataSource: RemoteDataSource): ViewModel() {

    private val _uiStateDog = MutableStateFlow<UiState<DogResponse>>(UiState.Idle)
    val uiStateDog: StateFlow<UiState<DogResponse>> = _uiStateDog

    fun fetchRandomDogImage(){
        viewModelScope.launch {
            dataSource.getRandomDogImages().collect { value ->
                _uiStateDog.value = value
            }
        }
    }
}