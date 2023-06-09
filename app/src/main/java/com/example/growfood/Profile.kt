package com.example.growfood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.growfood.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class Profile : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.selectedItemId = R.id.item_profil

        binding.keluarTV.setOnClickListener {
            mAuth!!.signOut()
            startActivity(Intent(this@Profile, LoginActivity::class.java))
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId){
                R.id.item_beranda-> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                R.id.item_ensiklopedia -> {
                    startActivity(Intent(this, EnsiklopediaActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.item_forum -> {
                    startActivity(Intent(this, CommunityActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.item_profil -> {
                    true
                }
                else -> false
            }
        }

        binding.halo.text = mAuth!!.currentUser?.displayName
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNav.selectedItemId = R.id.item_profil
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, 0)
        finish()
    }
}