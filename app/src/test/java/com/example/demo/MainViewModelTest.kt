package com.example.demo

import app.cash.turbine.test
import com.example.demo.network.model.DogResponse
import com.example.demo.network.service.RemoteDataSource
import com.example.demo.screen.main.viewModel.MainViewModel
import com.example.demo.shared.utils.UiState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test

class MainViewModelTest {

    @MockK
    lateinit var mockDataSource: RemoteDataSource

    private lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        viewModel = MainViewModel(mockDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchRandomDogImage emits Loading and Success states`() = testScope.runTest {
        // Mock the data source
        val mockResponse = DogResponse("https://mocked-dog-image.url")
        coEvery { mockDataSource.getRandomDogImages() } returns flow {
            emit(UiState.Loading)
            emit(UiState.Success(mockResponse))
        }

        // Start observing the flow
        viewModel.uiStateDog.test {
            // Trigger the function
            viewModel.fetchRandomDogImage()

            // Verify Loading state is emitted
            assertEquals(UiState.Idle, awaitItem())

            // Verify Loading state is emitted
            assertEquals(UiState.Loading, awaitItem())

            // Verify Success state is emitted
            val successState = awaitItem()
            assert(successState is UiState.Success)
            assertEquals(mockResponse, (successState as UiState.Success).data)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchRandomDogImage emits Loading and Error states`() = testScope.runTest {
        // Mock the data source
        coEvery { mockDataSource.getRandomDogImages() } returns flow {
            emit(UiState.Loading)
            emit(UiState.Error("Mocked API error"))
        }

        // Start observing the flow
        viewModel.uiStateDog.test {
            // Trigger the function
            viewModel.fetchRandomDogImage()

            // Verify Loading state is emitted
            assertEquals(UiState.Idle, awaitItem())

            // Verify Loading state is emitted
            assertEquals(UiState.Loading, awaitItem())

            // Verify Error state is emitted
            val errorState = awaitItem()
            assert(errorState is UiState.Error)
            assertEquals("Mocked API error", (errorState as UiState.Error).message)

            cancelAndIgnoreRemainingEvents()
        }
    }
}