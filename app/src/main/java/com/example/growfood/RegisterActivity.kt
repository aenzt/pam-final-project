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
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.growfood.databinding.LoginactivityBinding
import com.example.growfood.databinding.RegisterActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: RegisterActivityBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)
        binding = RegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val textView: TextView = findViewById(R.id.sudah_punya_akun)
        val text = getString(R.string.masuk_span)
        val spannableString = SpannableString(text)

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Tindakan yang akan dilakukan saat teks diklik
                // Misalnya, membuka halaman pendaftaran
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                // Mengatur tampilan teks yang diklik
                ds.isUnderlineText = false // Menghapus garis bawah pada teks yang diklik
                ds.color = ContextCompat.getColor(this@RegisterActivity, R.color.green_500) // Mengatur warna teks yang diklik
            }


        }

// Menambahkan ClickableSpan pada bagian teks yang ingin diklik
        spannableString.setSpan(clickableSpan, text.indexOf("Masuk"), text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // Mengatur gaya teks tebal pada "Masuk"

        val boldSpan = StyleSpan(Typeface.BOLD)
        spannableString.setSpan(boldSpan, text.indexOf("Masuk"), text.indexOf("Masuk") + "Masuk".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

// Mengatur teks pada TextView
        textView.text = spannableString
        textView.movementMethod = LinkMovementMethod.getInstance() // Mengaktifkan aksi klik pada teks yang diklik
        textView.highlightColor = Color.TRANSPARENT

        onAction()

    }

    private fun onAction() {
        binding.apply {
            btnRegister.setOnClickListener {
                val name = etName.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val pass = etPassword.text.toString().trim()

                if (checkValidation(name, email, pass)) {
                    hideSoftKeyboard(this@RegisterActivity, binding.root)
                    createUserAuth(name, email, pass)
                }
            }
        }
    }

    private fun createUserAuth(name: String, email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                startActivity(Intent(this, HomeActivity::class.java))
            }
            .addOnFailureListener {  exception ->
                Log.d("RegisterActivity", exception.message.toString())
            }
    }

    private fun checkValidation(
        name: String,
        email: String,
        pass: String,
    ): Boolean {
        binding.apply {
            when{
                name.isEmpty() -> {
                    etName.error = getString(R.string.silahkan_isi_nama_anda)
                    etName.requestFocus()
                }
                email.isEmpty() -> {
                    etEmail.error = getString(R.string.silahkan_isi_email_anda)
                    etEmail.requestFocus()
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    etEmail.error = getString(R.string.silahkan_gunakan_email_yang_valid)
                    etEmail.requestFocus()
                }
                pass.isEmpty() -> {
                    etPassword.error = getString(R.string.silahkan_isi_kata_sandi_anda)
                    etPassword.requestFocus()
                }
                pass.length < 8 -> {
                    etPassword.error = getString(R.string.silahkan_isi_kata_sandi_minimal_8_karakter)
                    etPassword.requestFocus()
                }
                else -> return true
            }
        }
        return false
    }


}