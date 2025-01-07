package com.example.demo

import com.example.demo.network.service.toResultFlow
import com.example.demo.shared.utils.UiState
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ToResultFlowTest {

    @Test
    fun `toResultFlow emits Loading and Success when call succeeds`() = runBlocking {
        // Given
        val expectedResponse = "Successful API Response"
        val mockCall: suspend () -> String = { expectedResponse }

        // When
        val flow: Flow<UiState<String>> = toResultFlow(mockCall)
        val emissions = flow.toList() // Collect all emitted states

        // Then
        assertEquals(2, emissions.size) // Loading and Success
        assertTrue(emissions[0] is UiState.Loading) // First emission is Loading
        assertTrue(emissions[1] is UiState.Success) // Second emission is Success
        val successState = emissions[1] as UiState.Success
        assertEquals(expectedResponse, successState.data) // Verify the response data
    }

    @Test
    fun `toResultFlow emits Loading and Error when call throws exception`() = runBlocking {
        // Given
        val exceptionMessage = "API error"
        val mockCall: suspend () -> String = { throw Exception(exceptionMessage) }

        // When
        val flow: Flow<UiState<String>> = toResultFlow(mockCall)
        val emissions = flow.toList() // Collect all emitted states

        // Then
        assertEquals(2, emissions.size) // Loading and Error
        assertTrue(emissions[0] is UiState.Loading) // First emission is Loading
        assertTrue(emissions[1] is UiState.Error) // Second emission is Error
        val errorState = emissions[1] as UiState.Error
        assertEquals(exceptionMessage, errorState.message) // Verify the error message
    }
}