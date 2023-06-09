package com.example.growfood.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.growfood.R
import com.example.growfood.databinding.ItemRvBinding
import com.example.growfood.models.Plant
import io.github.cdimascio.dotenv.dotenv

class PlantAdapter : RecyclerView.Adapter<PlantAdapter.ViewHolder>() {
    lateinit var onItemClick: ((Plant) -> Unit)
    lateinit var onDeleteClick: ((Plant) -> Unit)

    private val dotenv = dotenv {
        directory = "./assets"
        filename = "env"
    }

    var plantList = arrayListOf<Plant>()

    fun setPlant(plant: ArrayList<Plant>) {
        plantList.clear()
        plantList.addAll(plant)
        notifyDataSetChanged()
    }

    class ViewHolder (var binding : ItemRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return plantList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plant = plantList[position]

        holder.binding.tvNamePlant.text = plant.plantName
        holder.binding.imgTanamanPopuler.setImageResource(R.drawable.placeholder)

        val baseurl = dotenv["API_URL"]
        val uri = plant.plantImage?.data?.attributes?.url

        Glide.with(holder.itemView.context)
            .load("${baseurl}${uri}")
            .fitCenter()
            .into(holder.binding.imgTanamanPopuler)
    }
}