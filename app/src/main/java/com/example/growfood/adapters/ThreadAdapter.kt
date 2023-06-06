package com.example.growfood.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.growfood.R
import com.example.growfood.UpdateDeleteThread
import com.example.growfood.models.ThreadModel

class ThreadAdapter(private val context: Context, articleList: ArrayList<ThreadModel>): RecyclerView.Adapter<ThreadAdapter.ArticleViewHolder>() {
    private val articleList: List<ThreadModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.community_recycler_view, parent, false)
        return ArticleViewHolder(view);
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val thread = articleList[position];
        holder.tvDescription.text = thread.description
        holder.tvLikeCounts.text = thread.likeCounts
        holder.tvTime.text = thread.time
        holder.tvReplies.text = "${thread.replies.size} replies"
        holder.imgProfile.setImageResource(thread.person.imgProfile)
        holder.threadPersonName.text = thread.person.name

        if(thread.images[0].isEmpty())
            holder.imgThread.visibility = View.GONE

        holder.threadLayout.setOnClickListener { v: View? ->
            val intent = Intent(this.context, UpdateDeleteThread::class.java)
            intent.putExtra("thread_key", thread.key)
            intent.putExtra("thread_description", thread.description)
            intent.putExtra("thread_like_counts", thread.likeCounts)
            intent.putExtra("thread_time", thread.time)
            intent.putExtra("thread_reply_counts", "${thread.replies.size} replies")
            intent.putExtra("thread_img_profile", thread.images)
            intent.putExtra("thread_person_id", thread.person.id)
            intent.putExtra("thread_person_name", thread.person.name)
            intent.putExtra("thread_image_url", thread.images[0])

            context.startActivity(intent)
        }

        Glide.with(holder.itemView.context)
            .load(thread.images[0])
            .into(holder.imgThread)
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var threadLayout: CardView
        var tvDescription: TextView
        var tvTime: TextView
        var tvLikeCounts: Button
        var tvReplies: TextView
        var imgProfile: ImageView
        var threadPersonName: TextView
        var imgThread: ImageView
        override fun onClick(v: View) {
            clickListener!!.onItemClick(adapterPosition, itemView)
        }

        init {
            threadLayout = itemView.findViewById(R.id.thread_layout)
            tvDescription = itemView.findViewById(R.id.thread_description)
            tvTime = itemView.findViewById(R.id.thread_time)
            tvLikeCounts = itemView.findViewById(R.id.thread_like_count)
            tvReplies = itemView.findViewById(R.id.thread_replies_count)
            imgProfile = itemView.findViewById(R.id.thread_avatar)
            threadPersonName = itemView.findViewById(R.id.thread_title)
            imgThread = itemView.findViewById(R.id.thread_images)
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
        this.articleList = articleList
    }
}