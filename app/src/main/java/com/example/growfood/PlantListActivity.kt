package com.example.growfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.growfood.api.APIConfig
import com.example.growfood.databinding.ActivityPlantListBinding
import com.example.growfood.models.DataResponse
import com.example.growfood.models.GetAllPlantsResponse
import com.example.growfood.models.Plant
import com.example.growfood.recyclerview.PlantListAdapter
import com.example.modul10.Helper.LoadingState
import io.github.cdimascio.dotenv.dotenv
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlantListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPlantListBinding
    private var responseDataPlant = ArrayList<DataResponse>()
    private lateinit var plantListAdapter : PlantListAdapter
    private var plantData = ArrayList<Plant>()
    private lateinit var loadingState: LoadingState
    private val dotenv = dotenv {
        directory = "./assets"
        filename = "env"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingState = LoadingState(this@PlantListActivity)
        setRecycle()
        fetchDataPlant()

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun setRecycle(){
        plantListAdapter = PlantListAdapter()
        binding.rvPlantList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = plantListAdapter
        }
    }

    private fun fetchDataPlant(){
        loadingState.show()
        val token = dotenv["API_TOKEN"]
        val client = APIConfig.apiService.getAllPlants("Bearer $token", "*", null)
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
                    plantListAdapter.setPlant(plantData)
                }
            }

            override fun onFailure(call: Call<GetAllPlantsResponse>, t: Throwable) {
                loadingState.dismiss()
                Log.e("Retrofit Error", "onFailure: " + t.message)
            }

        })
    }
}