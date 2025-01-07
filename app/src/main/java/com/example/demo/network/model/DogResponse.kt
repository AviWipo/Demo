package com.example.demo.network.model

import com.google.gson.annotations.SerializedName

data class DogResponse (
    @SerializedName("message") val image: String
)
