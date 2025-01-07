package com.example.demo.shared.modules

import com.example.demo.network.service.RemoteDataSource
import org.koin.dsl.module


val repositoryModule = module {
    factory {  RemoteDataSource(get()) }
}