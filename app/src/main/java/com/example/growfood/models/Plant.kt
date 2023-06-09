package com.example.growfood.models

import com.google.gson.annotations.SerializedName

data class Plant(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("plant_description")
	val plantDescription: String? = null,

	@field:SerializedName("plant_image")
	val plantImage: PlantImage? = null,

	@field:SerializedName("publishedAt")
	val publishedAt: String? = null,

	@field:SerializedName("plant_name")
	val plantName: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
