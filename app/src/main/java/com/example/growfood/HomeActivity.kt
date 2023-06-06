package com.example.growfood
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class HomeActivity : AppCompatActivity() {
    lateinit var btnCommunity: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        btnCommunity = findViewById(R.id.btn_community)
        btnCommunity.setOnClickListener{
            v ->
            startActivity(Intent(this@HomeActivity, CommunityActivity::class.java))
        }
    }
}