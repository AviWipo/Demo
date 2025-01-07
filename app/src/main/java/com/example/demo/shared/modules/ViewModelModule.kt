package com.example.demo.shared.modules

import com.example.demo.screen.main.viewModel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule= module {
    viewModel{ MainViewModel(get()) }
}


