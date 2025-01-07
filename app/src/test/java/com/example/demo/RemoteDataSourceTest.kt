package com.example.demo

import com.example.demo.network.model.DogResponse
import com.example.demo.network.service.ApiService
import com.example.demo.network.service.RemoteDataSource
import com.example.demo.shared.utils.UiState
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RemoteDataSourceTest {

    private lateinit var apiService: ApiService
    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setUp() {
        apiService = mockk()  // Mock ApiService
        remoteDataSource = RemoteDataSource(apiService)  // Initialize RemoteDataSource with the mocked ApiService
    }

    @Test
    fun `test getRandomDogImages success`() = runBlocking {
        // Given
        val mockDogResponse = DogResponse("https://example.com/dog1.jpg")
        coEvery { apiService.getRandomDogImages() } returns mockDogResponse // Mock the API response
        // When
        val emissions = remoteDataSource.getRandomDogImages().toList() // Collect all emitted states
        // Then
        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is UiState.Loading)
        assertTrue(emissions[1] is UiState.Success)
        val successState = emissions[1] as UiState.Success
        assertEquals(mockDogResponse, successState.data)
    }

    @Test
    fun `test getRandomDogImages failure`() = runBlocking {
        // Given
        val exceptionMessage = "API error"
        coEvery { apiService.getRandomDogImages() } throws Exception(exceptionMessage)
        // When
        val flow = remoteDataSource.getRandomDogImages()
        // Then
        val emissions = flow.toList() // Collect emissions, handling exceptions properly
        assertEquals(2, emissions.size)
        assertTrue(emissions[0] is UiState.Loading)
        assertTrue(emissions[1] is UiState.Error)
        val errorState = emissions[1] as UiState.Error
        assertEquals(exceptionMessage, errorState.message) // Verify error message
    }
}