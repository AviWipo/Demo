package com.example.demo

import com.example.demo.network.service.RemoteDataSource
import com.example.demo.screen.main.viewModel.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.testng.Assert.assertNotNull

class ViewModelModuleTest: KoinTest {

    @MockK
    lateinit var mockDependency: RemoteDataSource

    private val viewModelModule = module {
        factory { mockDependency }
        viewModel { MainViewModel(get()) }
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this) // Initialize MockK annotations
        startKoin {
            modules(viewModelModule)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `MainViewModel is injected successfully`() {
        // Inject MainViewModel
        val mainViewModel: MainViewModel by inject()
        assertNotNull(mainViewModel, "MainViewModel should not be null")
    }

}