package com.example.growfood.models

import com.google.gson.annotations.SerializedName

data class GetAllPlantsResponse(
    @field:SerializedName("data")
    val data : List<DataResponse>
)

data class DataResponse (
    @field:SerializedName("id")
    val id : Int,

    @field:SerializedName("attributes")
    val plant: Plant
    )
