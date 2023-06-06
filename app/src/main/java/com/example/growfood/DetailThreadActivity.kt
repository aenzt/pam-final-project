package com.example.growfood

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.growfood.models.PersonModel
import com.example.growfood.models.Replies
import com.google.firebase.auth.FirebaseAuth
import com.example.growfood.models.ThreadModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*

class DetailThreadActivity: AppCompatActivity() {
    lateinit var iv_profile: ImageView
    lateinit var et_description: EditText
    lateinit var btn_submit: Button
    lateinit var btn_back: ImageButton
    lateinit var cameraButton: ImageView

    private var firebaseDatabase: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private lateinit var storageRef: StorageReference
    private var mAuth: FirebaseAuth? = null

    var thread: ThreadModel? = null

    private val PICK_IMAGE_REQUEST = 1

    private lateinit var downloadedImageUri: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_thread_activity)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.reference

        mAuth = FirebaseAuth.getInstance()

        storageRef = FirebaseStorage.getInstance().reference

        // bind view
        iv_profile = findViewById(R.id.thread_avatar)
        et_description = findViewById(R.id.et_description)
        btn_submit = findViewById(R.id.submit_button)
        btn_back = findViewById(R.id.btn_back)
        cameraButton = findViewById(R.id.cameraButton)

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

        btn_back.setOnClickListener{ v ->
            startActivity(Intent(this@DetailThreadActivity, CommunityActivity::class.java))
        }

        cameraButton.setOnClickListener { v ->
            uploadImage()
        }
    }

    fun submitData () {
        if(mAuth?.currentUser != null) {
            // get current date
            val currentDate = Date()
            val dateFormat = SimpleDateFormat("MM-dd-yyyy, HH.mm", Locale.getDefault())
            val formattedDate = dateFormat.format(currentDate)

            val description = et_description.text.toString()
            val images = arrayListOf(downloadedImageUri)
            val person = PersonModel(mAuth?.currentUser?.uid!!, mAuth?.currentUser?.displayName!!, R.drawable.ic_launcher_background)
            val replies = arrayListOf(Replies())

            thread = ThreadModel(description, formattedDate, "0", replies, images, person)

            databaseReference!!.child("threads").push().setValue(thread)
                .addOnSuccessListener(this) {
                    Toast.makeText(
                        this@DetailThreadActivity,
                        "new thread added",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@DetailThreadActivity, CommunityActivity::class.java))
                }.addOnFailureListener(this) {
                Toast.makeText(
                    this@DetailThreadActivity,
                    "failed to add new thread",
                    Toast.LENGTH_SHORT
                ).show()
            }
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
                // Image uploaded successfully
                // Get the download URL
                taskSnapshot.storage.downloadUrl
                    .addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()

                        Glide.with(this@DetailThreadActivity)
                            .load(downloadUrl)
                            .fitCenter()
                            .into(cameraButton)

                        downloadedImageUri = downloadUrl

                        // Do something with the download URL (e.g., display the image, save it to a database)
                    }
                    .addOnFailureListener { exception ->
                        // Handle any errors that occurred while retrieving the download URL
                        // Log.e(TAG, "Error getting download URL", exception)
                    }
            }
            .addOnFailureListener { exception ->
                // Handle any errors that occurred during the upload
                // Log.e(TAG, "Error uploading image", exception)
            }
    }
}