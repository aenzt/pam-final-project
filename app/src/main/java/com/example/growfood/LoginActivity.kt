package com.example.growfood

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.growfood.databinding.LoginactivityBinding
import com.example.modul10.Helper.LoadingState
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginactivityBinding
    private lateinit var userAuth: FirebaseAuth
    private lateinit var loadingState: LoadingState
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        loadingState = LoadingState(this@LoginActivity)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginactivity)
        binding = LoginactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val textView: TextView = findViewById(R.id.belum_punya_akun)
        val text = getString(R.string.daftar_span)
        val spannableString = SpannableString(text)

        userAuth = FirebaseAuth.getInstance()

        if (userAuth.currentUser != null){
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
        }
        onAction()

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Tindakan yang akan dilakukan saat teks diklik
                // Misalnya, membuka halaman pendaftaran
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                // Mengatur tampilan teks yang diklik
                ds.isUnderlineText = false // Menghapus garis bawah pada teks yang diklik
                ds.color = ContextCompat.getColor(
                    this@LoginActivity,
                    R.color.green_500
                ) // Mengatur warna teks yang diklik
            }


        }

// Menambahkan ClickableSpan pada bagian teks yang ingin diklik
        spannableString.setSpan(
            clickableSpan,
            text.indexOf("Daftar"),
            text.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        // Mengatur gaya teks tebal pada "Daftar"

        val boldSpan = StyleSpan(Typeface.BOLD)
        spannableString.setSpan(
            boldSpan,
            text.indexOf("Daftar"),
            text.indexOf("Daftar") + "Daftar".length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

// Mengatur teks pada TextView
        textView.text = spannableString
        textView.movementMethod =
            LinkMovementMethod.getInstance() // Mengaktifkan aksi klik pada teks yang diklik
        textView.highlightColor = Color.TRANSPARENT
    }

    private fun onAction() {
        binding.apply {
            btnMasuk.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val pass = etPassword.text.toString().trim()

                if (checkValidate(email, pass)) {
                    hideSoftKeyboard(this@LoginActivity, binding.root)
                    loginToServer(email, pass)
                }
            }


        }
    }

    private fun loginToServer(email: String, pass: String) {
        loadingState.show()
        userAuth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                loadingState.dismiss()
                startActivity(Intent(this, HomeActivity::class.java))
            }
            .addOnFailureListener {
                loadingState.dismiss()
                Toast.makeText(this,  "${it.message.toString()}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkValidate(email: String, password: String): Boolean {
        binding.apply {

                when {
                    email.isEmpty() -> {
                        etEmail.error = getString(R.string.silahkan_isi_email_anda)
                        etEmail.requestFocus()
                    }

                    !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                        etEmail.error = getString(R.string.silahkan_gunakan_email_yang_valid)
                        etEmail.requestFocus()
                    }

                    password.isEmpty() -> {
                        etPassword.error = getString(R.string.silahkan_isi_kata_sandi_anda)
                        etPassword.requestFocus()
                    }

                    password.length < 8 -> {
                        etPassword.error =
                            getString(R.string.silahkan_isi_kata_sandi_minimal_8_karakter)
                        etPassword.requestFocus()
                    }
                    else -> return true
                }
            }
            return false
        }
    }