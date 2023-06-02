package com.example.growfood

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailThreadActivity: AppCompatActivity() {
    lateinit var iv_profile: ImageView
    lateinit var et_description: EditText
    lateinit var btn_like: Button
    lateinit var tv_reply: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_thread_activity)

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
    }
}