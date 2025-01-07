package com.example.demo

import com.example.demo.network.model.DogResponse
import com.example.demo.network.service.ApiService
import com.google.gson.Gson
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/")) // Use the mock server URL
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getRandomDogImages returns valid DogResponse`() = runBlocking {
        // Given
        val mockResponse = DogResponse(image = "https://example.com/dog.jpg")
        mockWebServer.enqueue(
            MockResponse()
                .setBody(Gson().toJson(mockResponse))
                .setResponseCode(200)
        )

        // When
        val response = apiService.getRandomDogImages()

        // Then
        assertEquals("https://example.com/dog.jpg", response.image)
    }

    @Test
    fun `getRandomDogImages throws exception on 500 response`() = runBlocking {
        // Given
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
        )

        // When/Then
        try {
            apiService.getRandomDogImages()
            assert(false) // Fail the test if no exception is thrown
        } catch (e: Exception) {
            assert(e is HttpException)
            assertEquals(500, (e as HttpException).code())
        }
    }
}