package com.example.growfood.models

import com.google.gson.annotations.SerializedName

data class GetAllDiseasesResponse(
    @field:SerializedName("data")
    val data : List<DataResponseDisease>
)

data class DataResponseDisease (
    @field:SerializedName("id")
    val id : Int,

    @field:SerializedName("attributes")
    val disease: Disease
)
