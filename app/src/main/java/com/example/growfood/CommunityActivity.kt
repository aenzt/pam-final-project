package com.example.growfood

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.growfood.adapters.ThreadAdapter
import com.example.growfood.models.PersonModel
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
        val replies: ArrayList<String> = arrayListOf("good article", "bad article")
        val times: Array<String> = resources.getStringArray(R.array.thread_times)
        val imageLists = arrayListOf("https://images.unsplash.com/flagged/photo-1570612861542-284f4c12e75f?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=870&q=80")
        val person = PersonModel("Rijal", R.drawable.ic_launcher_background)

        for(element in descriptions.indices) {
            threadModels.add(ThreadModel(
                descriptions[element],
                times[element],
                likeCounts[element],
                replies,
                imageLists,
                person)
            )
        }
    }
}