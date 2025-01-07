package com.example.demo.network.service

import com.example.demo.network.model.DogResponse
import retrofit2.http.GET

interface ApiService {
    @GET("breeds/image/random")
    suspend fun getRandomDogImages(): DogResponse
}