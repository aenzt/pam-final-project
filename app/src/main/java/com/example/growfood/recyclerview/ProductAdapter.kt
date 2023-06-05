package com.example.growfood.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.growfood.R
import com.example.growfood.databinding.ItemRvBinding
import com.example.growfood.databinding.ItemRvTokpedBinding
import com.example.growfood.models.Disease
import com.example.growfood.models.Product
import io.github.cdimascio.dotenv.dotenv

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    lateinit var onItemClick: ((Product) -> Unit)
    lateinit var onDeleteClick: ((Product) -> Unit)

    var productList = arrayListOf<Product>()

    private val dotenv = dotenv {
        directory = "./assets"
        filename = "env"
    }

    fun setProduct(product: ArrayList<Product>) {
        productList.clear()
        productList.addAll(product)
        notifyDataSetChanged()
    }

    class ViewHolder (var binding : ItemRvTokpedBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRvTokpedBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]

        holder.binding.tvNameProduct.text = product.productName
        holder.binding.tvPriceProduct.text = "Rp ${product.productPrice}"
        holder.binding.tvLocationProduct.text = product.productLocation
        holder.binding.tvRatingProduct.text = "${product.productRating} | Terjual 500+"
        holder.binding.imgProduct.setImageResource(R.drawable.placeholder)

        val baseurl = dotenv["API_URL"]
        val uri = product.productImageUrl

        Glide.with(holder.itemView.context)
            .load("${baseurl}${uri}")
            .fitCenter()
            .into(holder.binding.imgProduct)
    }
}