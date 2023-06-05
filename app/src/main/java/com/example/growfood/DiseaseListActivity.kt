package com.example.growfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.growfood.api.APIConfig
import com.example.growfood.databinding.ActivityDiseaseListBinding
import com.example.growfood.models.*
import com.example.growfood.recyclerview.DiseaseListAdapter
import com.example.modul10.Helper.LoadingState
import io.github.cdimascio.dotenv.dotenv
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiseaseListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDiseaseListBinding
    private var responseDataDisease = ArrayList<DataResponseDisease>()
    private lateinit var diseaseListAdapter: DiseaseListAdapter
    private var diseaseData = ArrayList<Disease>()
    private lateinit var loadingState : LoadingState
    private val dotenv = dotenv {
        directory = "./assets"
        filename = "env"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiseaseListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingState = LoadingState(this@DiseaseListActivity)
        setRecycle()
        fetchDataDisease()

        binding.backButton.setOnClickListener {
            finish()
        }

    }

    private fun setRecycle(){
        diseaseListAdapter = DiseaseListAdapter()
        binding.rvDiseaseList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = diseaseListAdapter
        }
    }

    private fun fetchDataDisease(){
        loadingState.show()
        val token = dotenv["API_TOKEN"]
        val client = APIConfig.apiService.getAllDiseases("Bearer $token", "*", null)
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
                    diseaseListAdapter.setDisease(diseaseData)
                }
            }

            override fun onFailure(call: Call<GetAllDiseasesResponse>, t: Throwable) {
                loadingState.dismiss()
                Log.e("Retrofit Error", "onFailure: " + t.message)
            }

        })
    }
}