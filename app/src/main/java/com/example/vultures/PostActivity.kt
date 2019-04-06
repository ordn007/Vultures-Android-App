package com.example.vultures

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_nest.*
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity() {


    private var firestoreDB: FirebaseFirestore? = null
    internal var id: String = ""


    companion object {
        private const val LOG_TAG = "448.PostActivity"

        fun createIntent(baseContext: Context) : Intent {
            val intent = Intent( baseContext, PostActivity::class.java)
            return intent
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        firestoreDB = FirebaseFirestore.getInstance()

        val bundle = intent.extras
        if (bundle != null) {
            id = bundle.getString("UpdatePostId")

            title_field.setText(bundle.getString("UpdatePostTitle"))
            location_field.setText(bundle.getString("UpdatePostContent"))
        }

        //hooks up buttons -- currently only displays toasts saying what would happen
        photo_button.setOnClickListener{
            Toast.makeText(baseContext,"Access photos", Toast.LENGTH_SHORT).show()
        }
        post_button.setOnClickListener{
            val title = title_field.text.toString()
            val location = location_field.text.toString()
            val extraInfo = extra_info_field.text.toString()

            if (title.isEmpty() || location.isEmpty()) {
                Toast.makeText(baseContext,"Enter A Title and Location", Toast.LENGTH_SHORT).show()
            }else {
                if (id.isNotEmpty()) {
                    updatePost(id, title, location)
                } else {
                    addPost(title, location)
                }
            }
        }
        // hooks up the bottom panel
        post_bottom_panel_nest.setOnClickListener{
            finish()
        }
    }



    private fun updatePost(id: String, title: String, location: String) {
        val post = Post(id, title, location).toMap()

        firestoreDB!!.collection("posts")
            .document(id)
            .set(post)
            .addOnSuccessListener {
                Log.e(LOG_TAG, "Post document update successful!")
                Toast.makeText(applicationContext, "Post has been updated!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e(LOG_TAG, "Error adding Post document", e)
                Toast.makeText(applicationContext, "Post could not be updated!", Toast.LENGTH_SHORT).show()
            }
    }


    private fun addPost(title: String, location: String) {
        val post = Post(title, location).toMap()

        firestoreDB!!.collection("posts")
            .add(post)
            .addOnSuccessListener { documentReference ->
                Log.e(LOG_TAG, "DocumentSnapshot written with ID: " + documentReference.id)
                Toast.makeText(applicationContext, "Post has been added!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e(LOG_TAG, "Error adding Post document", e)
                Toast.makeText(applicationContext, "Post could not be added!", Toast.LENGTH_SHORT).show()
            }
    }

    //Launches the nest activity
    // Will commit the post for others to view
    private fun commitPost(){
        val title = title_field.text
        val location = location_field.text
        val extraInfo = extra_info_field.text
        Toast.makeText(baseContext,"Commits post with Title: ${title}, Location: ${location}, Extra: ${extraInfo}", Toast.LENGTH_SHORT).show()

        // Resets the fields
        cleanUpTextFields()
    }

    fun cleanUpTextFields() {
        title_field.text = null
        location_field.text = null
        extra_info_field.text = null
    }

    //Life Cycles Methods
    override fun onStart() {
        super.onStart()
        Log.d(LOG_TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, "onResume() called")
    }

    override fun onPause() {
        Log.d(LOG_TAG, "onPause() called")
        super.onPause()
    }

    override fun onStop() {
        Log.d(LOG_TAG, "onStop() called")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "onDestroy() called")
        super.onDestroy()
    }
}