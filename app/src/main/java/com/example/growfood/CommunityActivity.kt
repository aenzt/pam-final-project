package com.example.growfood

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.growfood.adapters.ThreadAdapter
import com.example.growfood.models.ThreadModel

class CommunityActivity: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    lateinit var tvDescription: TextView
    lateinit var tvTime: TextView
    lateinit var tvLikeCounts: Button
    lateinit var tvReplies: TextView
    val threadModels: ArrayList<ThreadModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_activity)
        assignThreadModel()

        recyclerView = findViewById(R.id.thread_recycler_view)
        val adapter = ThreadAdapter(this, threadModels)

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun assignThreadModel(){
        val descriptions: Array<String> = resources.getStringArray(R.array.thread_descriptions)
        val likeCounts: Array<String> = resources.getStringArray(R.array.thread_like_counts)
        val replies: Array<String> = resources.getStringArray(R.array.thread_reply_counts)
        val times: Array<String> = resources.getStringArray(R.array.thread_times)

        for(element in descriptions.indices) {
            threadModels.add(ThreadModel(
                descriptions[element],
                times[element],
                likeCounts[element],
                replies[element],
                R.drawable.ic_launcher_background)
            )
        }
    }
}