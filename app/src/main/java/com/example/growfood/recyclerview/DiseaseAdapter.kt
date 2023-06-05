package com.example.growfood.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.growfood.R
import com.example.growfood.databinding.ItemRvBinding
import com.example.growfood.models.Disease
import io.github.cdimascio.dotenv.dotenv

class DiseaseAdapter : RecyclerView.Adapter<DiseaseAdapter.ViewHolder>() {
    lateinit var onItemClick: ((Disease) -> Unit)
    lateinit var onDeleteClick: ((Disease) -> Unit)

    private val dotenv = dotenv {
        directory = "./assets"
        filename = "env"
    }

    var diseaseList = arrayListOf<Disease>()

    fun setDisease(disease: ArrayList<Disease>) {
        diseaseList.clear()
        diseaseList.addAll(disease)
        notifyDataSetChanged()
    }

    class ViewHolder (var binding : ItemRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return diseaseList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val disease = diseaseList[position]

        holder.binding.tvNamePlant.text = disease.diseaseName
        holder.binding.imgTanamanPopuler.setImageResource(R.drawable.placeholder)

        val baseurl = dotenv["API_URL"]
        val uri = disease.diseaseImage?.data?.attributes?.url

        Glide.with(holder.itemView.context)
            .load("${baseurl}${uri}")
            .fitCenter()
            .into(holder.binding.imgTanamanPopuler)
    }
}