package com.example.vultures

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_nest.*

class NestActivity : AppCompatActivity() {


    companion object {
        private const val LOG_TAG = "448.NestActivity"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nest)

        //hooks up buttons -- currently only displays toasts saying what should be launched.
        make_a_post.setOnClickListener{Toast.makeText(baseContext,"Launch PostActivity", Toast.LENGTH_SHORT).show()}
        view_map.setOnClickListener{Toast.makeText(baseContext,"Displays the Map", Toast.LENGTH_SHORT).show()}
        view_all_posts.setOnClickListener{Toast.makeText(baseContext,"Launch AllPostsActivity", Toast.LENGTH_SHORT).show()}

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