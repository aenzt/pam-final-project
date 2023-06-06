package com.example.growfood

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.growfood.adapters.ThreadAdapter
import com.example.growfood.models.ThreadModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CommunityActivity: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    lateinit var et_topic_search: EditText
    val threadModels: ArrayList<ThreadModel> = ArrayList()
    lateinit var floatingButton: FloatingActionButton
    private var mAuth: FirebaseAuth? = null
    private var firebaseDatabase: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_activity)
        mAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.getReference("threads")

        assignThreadModel()

        et_topic_search = findViewById(R.id.et_topic_search)
        floatingButton = findViewById(R.id.btn_add_community)

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
        if(mAuth!!.currentUser == null) return

        val threadListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                for(childSnapshot in dataSnapshot.children) {
                    val key = childSnapshot!!.key;

                    val thread = childSnapshot.getValue(ThreadModel::class.java)
                    thread?.key = key!!

                    thread?.let { threadModels.add(it) }

                    val adapter = ThreadAdapter(this@CommunityActivity, threadModels)
                    recyclerView.adapter = adapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }

        databaseReference?.addValueEventListener(threadListener)
    }
}