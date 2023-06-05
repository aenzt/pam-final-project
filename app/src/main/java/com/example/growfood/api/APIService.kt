package com.example.growfood.api

import com.example.growfood.models.GetAllDiseasesResponse
import com.example.growfood.models.GetAllPlantsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("plants")
    fun getAllPlants(
        @Header("Authorization") token : String,
        @Query("populate") query : String,
        @Query("pagination[pageSize]") size: Int?
    ): Call<GetAllPlantsResponse>

    @GET("diseases")
    fun getAllDiseases(
        @Header("Authorization") token : String,
        @Query("populate") query : String,
        @Query("pagination[pageSize]") size: Int?
    ): Call<GetAllDiseasesResponse>
}