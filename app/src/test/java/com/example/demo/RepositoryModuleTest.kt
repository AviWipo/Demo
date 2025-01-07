package com.example.demo

import com.example.demo.network.service.ApiService
import com.example.demo.network.service.RemoteDataSource
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.testng.Assert.assertNotNull

class RepositoryModuleTest: KoinTest {

    @MockK
    lateinit var mockApiService: ApiService

    private val repositoryModule = module {
        single { mockApiService }
        factory { RemoteDataSource(get()) }
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        startKoin {
            modules(repositoryModule)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `RemoteDataSource is injected successfully with inject`() = runBlocking {
        // Using inject
        val remoteDataSource: RemoteDataSource by inject()

        // Assert that RemoteDataSource is not null
        assertNotNull(remoteDataSource, "RemoteDataSource should not be null")
    }
}