package com.example.growfood.models

import com.google.gson.annotations.SerializedName

data class Disease(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("diseaseDescription")
	val diseaseDescription: String? = null,

	@field:SerializedName("diseaseImage")
	val diseaseImage: DiseaseImage? = null,

	@field:SerializedName("publishedAt")
	val publishedAt: String? = null,

	@field:SerializedName("diseaseName")
	val diseaseName: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
