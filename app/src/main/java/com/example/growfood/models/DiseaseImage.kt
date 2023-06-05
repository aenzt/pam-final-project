package com.example.growfood.models

import com.google.gson.annotations.SerializedName

data class DiseaseImage(
    @field:SerializedName("data")
    val data:DataImage? = null
)
