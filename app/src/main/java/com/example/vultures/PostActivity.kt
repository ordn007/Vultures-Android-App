package com.example.vultures

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_nest.*
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity() {


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

        //hooks up buttons -- currently only displays toasts saying what would happen
        photo_button.setOnClickListener{
            Toast.makeText(baseContext,"Access photos", Toast.LENGTH_SHORT).show()
        }
        post_button.setOnClickListener{
            val title = title_field.text.toString()
            val location = location_field.text.toString()
            val extraInfo = extra_info_field.text.toString()

            if (title.isEmpty() || (location.isEmpty() && extraInfo.isEmpty())) {
                Toast.makeText(baseContext,"Enter A Title and Location or Extra Info", Toast.LENGTH_SHORT).show()
            }else {
                commitPost()
            }
        }

        // hooks up the bottom panel
        post_bottom_panel_nest.setOnClickListener{
            finish()
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