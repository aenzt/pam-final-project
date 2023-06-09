package com.example.growfood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.growfood.api.APIConfig.apiService
import com.example.growfood.databinding.ActivityEnsiklopediaBinding
import com.example.growfood.models.*
import com.example.growfood.recyclerview.DiseaseAdapter
import com.example.growfood.recyclerview.PlantAdapter
import com.example.growfood.recyclerview.ProductAdapter
import com.example.modul10.Helper.LoadingState
import com.google.android.material.navigation.NavigationBarView
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EnsiklopediaActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEnsiklopediaBinding
    private var responseDataPlant = ArrayList<DataResponse>()
    private var responseDataDisease = ArrayList<DataResponseDisease>()
    private lateinit var plantAdapter: PlantAdapter
    private lateinit var diseaseAdapter: DiseaseAdapter
    private lateinit var productAdapter: ProductAdapter
    private lateinit var loadingState: LoadingState
    private var plantData = ArrayList<Plant>()
    private var diseaseData = ArrayList<Disease>()
    private var productData = ArrayList<Product>()
    private val dotenv = dotenv {
        directory = "./assets"
        filename = "env"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEnsiklopediaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadingState = LoadingState(this@EnsiklopediaActivity)


        fetchDataPlant()
        fetchDataDisease()
        addProductData()
        setRecycle()

        binding.seeMorePlant.setOnClickListener {
            startActivity(Intent(this@EnsiklopediaActivity, PlantListActivity::class.java))
        }
        binding.seeMoreDisease.setOnClickListener {
            startActivity(Intent(this@EnsiklopediaActivity, DiseaseListActivity::class.java))
        }

        binding.bottomNav.selectedItemId = R.id.item_ensiklopedia

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId){
                R.id.item_beranda -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.item_ensiklopedia -> {
                    true
                }
                R.id.item_forum -> {
                    startActivity(Intent(this, CommunityActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.item_profil -> {
                    startActivity(Intent(this, Profile::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNav.selectedItemId = R.id.item_ensiklopedia
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0)
        finish()
    }

    private fun addProductData(){
        productData.add(Product("Fungisida Antracol 70WP 250 gram", "42.000", "/uploads/image_33_6f53ab4c85.png", "5.0", "Surabaya"))
        productData.add(Product("Pupuk Organik EM4 1 Liter", "21.500", "/uploads/image_34_e9a9cf0726.png", "4.8", "Malang"))
        productData.add(Product("Gardening Tool Set 3 Pcs", "30.000", "/uploads/image_35_40eb83a26d.png", "4.9", "Malang"))
        productData.add(Product("Cangkul dan Garpu 2 in 1", "29.000", "/uploads/image_36_dd8f19c2c9.png", "4.9", "Malang"))
    }

    private fun setRecycle(){
        plantAdapter = PlantAdapter()
        binding.rvTanamanPopuler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = plantAdapter
        }
        diseaseAdapter = DiseaseAdapter()
        binding.rvPenyakitPopuler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = diseaseAdapter
        }
        productAdapter = ProductAdapter()
        binding.rvProdukPopuler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = productAdapter
        }
        productAdapter.setProduct(productData)
    }

    private fun fetchDataPlant(){
        loadingState.show()
        val token = dotenv["API_TOKEN"]
        val client = apiService.getAllPlants("Bearer $token", "*", 3)
        client.enqueue(object : Callback<GetAllPlantsResponse> {
            override fun onResponse(
                call: Call<GetAllPlantsResponse>,
                response: Response<GetAllPlantsResponse>
            ) {
                loadingState.dismiss()
                if (response.isSuccessful){
                    responseDataPlant = response.body()!!.data as ArrayList<DataResponse>
                    for (x in responseDataPlant) {
                        plantData.add(x.plant)
                    }
                    plantAdapter.setPlant(plantData)
                }
            }

            override fun onFailure(call: Call<GetAllPlantsResponse>, t: Throwable) {
                loadingState.dismiss()
                Log.e("Retrofit Error", "onFailure: " + t.message)
            }

        })
    }
    private fun fetchDataDisease(){
        loadingState.show()
        val token = dotenv["API_TOKEN"]
        val client = apiService.getAllDiseases("Bearer $token", "*", 3)
        client.enqueue(object : Callback<GetAllDiseasesResponse> {
            override fun onResponse(
                call: Call<GetAllDiseasesResponse>,
                response: Response<GetAllDiseasesResponse>
            ) {
                loadingState.dismiss()
                if (response.isSuccessful){
                    responseDataDisease = response.body()!!.data as ArrayList<DataResponseDisease>
                    for (x in responseDataDisease) {
                        diseaseData.add(x.disease)
                    }
                    diseaseAdapter.setDisease(diseaseData)
                }
            }

            override fun onFailure(call: Call<GetAllDiseasesResponse>, t: Throwable) {
                loadingState.dismiss()
                Log.e("Retrofit Error", "onFailure: " + t.message)
            }

        })
    }
}