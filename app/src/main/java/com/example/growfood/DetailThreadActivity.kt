package com.example.growfood

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.growfood.models.PersonModel
import com.google.firebase.auth.FirebaseAuth
import com.example.growfood.models.ThreadModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class DetailThreadActivity: AppCompatActivity() {
    lateinit var iv_profile: ImageView
    lateinit var et_description: EditText
    lateinit var btn_like: Button
    lateinit var tv_reply: TextView
    lateinit var btn_submit: Button
    private var firebaseDatabase: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    lateinit var person: PersonModel
    private var mAuth: FirebaseAuth? = null

    var thread: ThreadModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_thread_activity)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.getReference()

        // bind view
        iv_profile = findViewById(R.id.thread_avatar)
        et_description = findViewById(R.id.et_description)


        // get initial intent
        val intent = getIntent()
        val description = intent.getStringExtra("thread_description").toString()
        val likeCounts = intent.getStringExtra("thread_like_counts")
        val time = intent.getStringExtra("thread_time")
        val replyCounts = intent.getStringExtra("thread_reply_counts")
        val imgProfile = intent.getStringExtra("thread_img_profile").toString()

        iv_profile.setImageResource(R.drawable.ic_launcher_background);

        et_description.setText(description)

        btn_submit.setOnClickListener{ v ->
            submitData()
        }
    }

    override fun onStart() {
        super.onStart()
//        val currentUser = mAuth?.currentUser
//        if(currentUser != null) {
//
//        }
    }

    fun submitData () {
        val description = et_description.text.toString()
        val time = Calendar.getInstance().time.toString()
        val replies = arrayListOf("")
        val images = arrayListOf("")
        val person = PersonModel(mAuth!!.currentUser!!.displayName!!, R.drawable.ic_launcher_background)

        thread = ThreadModel(description, time, "0", replies, images, person)

        databaseReference!!.child("threads").push().setValue(thread).addOnSuccessListener(this) {
            Toast.makeText(this@DetailThreadActivity, "new thread added", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener(this) {
            Toast.makeText(this@DetailThreadActivity, "failed to add new thread", Toast.LENGTH_SHORT).show()
        }
    }
}