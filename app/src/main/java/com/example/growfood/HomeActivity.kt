package com.example.growfood
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.growfood.databinding.HomePageBinding
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : AppCompatActivity() {

    private lateinit var binding : HomePageBinding
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = HomePageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bottomNav.selectedItemId = R.id.item_beranda

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId){
                R.id.item_beranda-> {
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
                    startActivity(Intent(this, Profile::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth!!.currentUser
        if (currentUser != null) {
            binding.halo.text = "Halo, ${currentUser.displayName}"
        }
    }
}