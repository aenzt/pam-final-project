package com.example.growfood

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.growfood.adapters.ReplyAdapter
import com.example.growfood.models.PersonModel
import com.example.growfood.models.Replies
import com.google.firebase.auth.FirebaseAuth
import com.example.growfood.models.ThreadModel
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class UpdateDeleteThread: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var iv_profile: ImageView
    private lateinit var et_description: EditText
    private lateinit var et_add_replies: EditText
    private lateinit var btn_edit: Button
    private lateinit var btn_delete: Button
    private lateinit var btn_reply: Button
    private lateinit var btn_back: ImageButton
    private lateinit var toolbar_title: TextView
    private lateinit var person_name: TextView
    private lateinit var time: TextView
    lateinit var cameraButton: ImageView

    private var firebaseDatabase: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private lateinit var storageRef: StorageReference

    val repliesModels: ArrayList<Replies> = ArrayList()

    private val PICK_IMAGE_REQUEST = 1

    private lateinit var downloadedImageUri: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_delete_thread_activity)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.reference

        mAuth = FirebaseAuth.getInstance()
        storageRef = FirebaseStorage.getInstance().reference

        // bind view
        iv_profile = findViewById(R.id.thread_avatar)
        et_description = findViewById(R.id.et_description)
        et_add_replies = findViewById(R.id.et_add_replies)
        btn_edit = findViewById(R.id.edit_button)
        btn_back = findViewById(R.id.btn_back)
        btn_reply = findViewById(R.id.button_reply)
        btn_delete = findViewById(R.id.button_delete)
        toolbar_title = findViewById(R.id.toolbar_title)
        person_name = findViewById(R.id.person_name)
        time = findViewById(R.id.replies_time)
        cameraButton = findViewById(R.id.thread_images)

        // get initial intent
        val intent = getIntent()
        val description = intent.getStringExtra("thread_description").toString()
        val key = intent.getStringExtra("thread_key").toString()
        val personId = intent.getStringExtra("thread_person_id").toString()
        val personName = intent.getStringExtra("thread_person_name").toString()
        val threadTime = intent.getStringExtra("thread_time").toString()
        val threadImageUrl = intent.getStringExtra("thread_image_url").toString()

        person_name.setText(personName)
        time.setText(threadTime)

        iv_profile.setImageResource(R.drawable.ic_launcher_background);
        et_description.setText(description)
        assignRepliesModels(key)

        recyclerView = findViewById(R.id.replies_recycler_view)
        val adapter = ReplyAdapter(this, repliesModels)

        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        if(mAuth!!.currentUser?.uid != personId) {
            btn_edit.visibility = View.GONE
            btn_delete.visibility = View.GONE
            toolbar_title.text = "Detail Diskusi"
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

        btn_reply.setOnClickListener{ v->
            addReply(key, PersonModel(mAuth!!.currentUser!!.uid, mAuth!!.currentUser?.displayName!!, 0))
        }

        cameraButton.setOnClickListener{ v->
            if(threadImageUrl.isNotEmpty())
                Glide.with(this@UpdateDeleteThread)
                    .load(threadImageUrl)
                    .fitCenter()
                    .into(cameraButton)

            if(mAuth!!.currentUser?.uid == personId)
                uploadImage()
        }
    }

    fun assignRepliesModels(key: String){
        if(mAuth?.currentUser == null) return

        val dbReference = databaseReference!!.child("threads").child(key).child("replies")

        val threadListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(childSnapshot in dataSnapshot.children) {
                    val key = childSnapshot!!.key;

                    val replies = childSnapshot.getValue(Replies::class.java)
                    replies?.key = key!!

                    replies?.let { repliesModels.add(it) }

                    val adapter = ReplyAdapter(this@UpdateDeleteThread, repliesModels)
                    recyclerView.adapter = adapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
            }
        }

        dbReference.addValueEventListener(threadListener)
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

    fun addReply(key: String, person: PersonModel) {
        if(mAuth?.currentUser == null) return

        val snaphotRef = databaseReference!!.child("threads").child(key)

        if(et_add_replies.text != null) {
            // get current date
            val currentDate = Date()
            val dateFormat = SimpleDateFormat("MM-dd-yyyy, HH.mm", Locale.getDefault())
            val formattedDate = dateFormat.format(currentDate)

            val newReply = Replies(et_add_replies.text.toString(), formattedDate, person)
            repliesModels.add(newReply)
            snaphotRef.child("replies").setValue(repliesModels).addOnSuccessListener {
                Toast.makeText(this@UpdateDeleteThread, "Success add reply", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@UpdateDeleteThread, CommunityActivity::class.java))
            }.addOnFailureListener{
                Toast.makeText(this@UpdateDeleteThread, "Error add reply", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this@UpdateDeleteThread, "reply cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }

    fun uploadImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data

            if (imageUri != null) {
                getImage(imageUri)
            }
        }
    }

    fun getImage(imageUri: Uri) {
        val filename = UUID.randomUUID().toString()
        val storageRef: StorageReference = storageRef.child("images/$filename")

        storageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl
                    .addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()

                        Glide.with(this@UpdateDeleteThread)
                            .load(downloadUrl)
                            .fitCenter()
                            .into(cameraButton)

                        downloadedImageUri = downloadUrl
                    }
                    .addOnFailureListener { exception ->

                    }
            }
            .addOnFailureListener { exception ->

            }
    }
}
