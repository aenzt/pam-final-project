package com.example.growfood.api

import io.github.cdimascio.dotenv.dotenv
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIConfig {
    @JvmStatic
    val apiService: ApiService
        get() {
            val dotenv = dotenv {
                directory = "./assets"
                filename = "env"
            }
            val apiUrl = dotenv["API_URL"]
            val retrofit = Retrofit.Builder()
                .baseUrl("$apiUrl/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
}