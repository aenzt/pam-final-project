package com.example.growfood

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.growfood.adapters.ThreadAdapter
import com.example.growfood.models.ThreadModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CommunityActivity: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    lateinit var et_topic_search: EditText
    val threadModels: ArrayList<ThreadModel> = ArrayList()
    lateinit var floatingButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_activity)
        assignThreadModel()

        et_topic_search = findViewById(R.id.et_topic_search)
        floatingButton = findViewById(R.id.btn_add_community)

        et_topic_search.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(p0: Editable?) {
                val newText = p0.toString()
                val matchingItem = threadModels.find {
                    it.description.contains(newText)
                }

                matchingItem?.let {
                    it.description = newText
                }
            }

        })

        recyclerView = findViewById(R.id.thread_recycler_view)
        val adapter = ThreadAdapter(this, threadModels)

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        floatingButton.setOnClickListener{ v ->
            val intent = Intent(this@CommunityActivity, DetailThreadActivity::class.java)
            startActivity(intent)
        }
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