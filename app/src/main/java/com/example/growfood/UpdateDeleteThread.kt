package com.example.growfood

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.growfood.models.ThreadModel
import com.google.firebase.database.*

class UpdateDeleteThread: AppCompatActivity() {
    private lateinit var iv_profile: ImageView
    private lateinit var et_description: EditText
    private lateinit var btn_edit: Button
    private lateinit var btn_delete: Button
    private lateinit var btn_back: ImageButton
    private lateinit var toolbar_title: TextView
    private lateinit var person_name: TextView
    private lateinit var time: TextView
    private var firebaseDatabase: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_delete_thread_activity)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.getReference()

        mAuth = FirebaseAuth.getInstance()

        // bind view
        iv_profile = findViewById(R.id.thread_avatar)
        et_description = findViewById(R.id.et_description)
        btn_edit = findViewById(R.id.edit_button)
        btn_back = findViewById(R.id.btn_back)
        btn_delete = findViewById(R.id.button_delete)
        toolbar_title = findViewById(R.id.toolbar_title)
        person_name = findViewById(R.id.thread_title)
        time = findViewById(R.id.thread_time)

        // get initial intent
        val intent = getIntent()
        val description = intent.getStringExtra("thread_description").toString()
        val key = intent.getStringExtra("thread_key").toString()
        val personId = intent.getStringExtra("thread_person_id").toString()
        val threadTime = intent.getStringExtra("thread_time").toString()

        person_name.setText(personId)
        time.setText(threadTime)

        iv_profile.setImageResource(R.drawable.ic_launcher_background);
        et_description.setText(description)

        if(mAuth!!.currentUser?.uid != personId) {
            btn_edit.visibility = View.GONE
            btn_delete.visibility = View.GONE
            toolbar_title.setText("Detail Diskusi")
            et_description.isEnabled = false
        }

        btn_edit.setOnClickListener{ v ->
            editData(key)
        }

        btn_delete.setOnClickListener{v ->
            deleteData(key)
        }

        btn_back.setOnClickListener{ v ->
            startActivity(Intent(this@UpdateDeleteThread, CommunityActivity::class.java))
        }
    }

    fun editData (key: String) {
        if(mAuth?.currentUser == null) return

        databaseReference!!.child("threads").child(key).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val thread = snapshot.getValue(ThreadModel::class.java)
                if(thread != null) {
                    val updatedValues = HashMap<String, Any>()
                    updatedValues["description"] = et_description.text.toString()

                    val threadRef = databaseReference!!.child("threads").child(key)
                    threadRef.updateChildren(updatedValues)

                    Toast.makeText(this@UpdateDeleteThread, "Thread updated successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@UpdateDeleteThread, CommunityActivity::class.java))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UpdateDeleteThread, "Sorry, error occured!", Toast.LENGTH_SHORT).show()
            }

        })

    }

    fun deleteData(key: String){
        if(mAuth?.currentUser == null) return

        val snapshotRef = databaseReference!!.child("threads").child(key)

        snapshotRef.removeValue()

        startActivity(Intent(this@UpdateDeleteThread, CommunityActivity::class.java))
    }
}
