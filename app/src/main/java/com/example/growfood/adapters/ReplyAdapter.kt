package com.example.growfood.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.growfood.R
import com.example.growfood.UpdateDeleteThread
import com.example.growfood.models.Replies

class ReplyAdapter(private val context: Context, repliesList: ArrayList<Replies>): RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder>() {
    private val repliesList: List<Replies>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.replies_recycler_view, parent, false)
        return ReplyViewHolder(view);
    }

    override fun getItemCount(): Int {
        return repliesList.size
    }

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        val reply = repliesList[position];
        holder.tvDescription.text = reply.description
        holder.tvPersonName.text = reply.person.name
        holder.tvTime.text = reply.time
    }

    inner class ReplyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var repliesLayout: CardView
        var tvDescription: TextView
        var tvTime: TextView
        var tvPersonName: TextView
        override fun onClick(v: View) {
            clickListener!!.onItemClick(adapterPosition, itemView)
        }

        init {
            repliesLayout = itemView.findViewById(R.id.replies_layout)
            tvDescription = itemView.findViewById(R.id.replies_description)
            tvTime = itemView.findViewById(R.id.replies_time)
            tvPersonName = itemView.findViewById(R.id.person_name)
        }
    }

    fun setOnItemClickListener(clickListener: ClickListener?) {
        Companion.clickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(position: Int, v: View?)
    }

    companion object {
        private var clickListener: ClickListener? = null
    }

    init {
        this.repliesList = repliesList
    }
}