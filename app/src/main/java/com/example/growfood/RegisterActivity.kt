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
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

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


    }


}