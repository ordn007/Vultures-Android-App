package com.example.vultures

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_post_details.*

class PostActivityDetails : AppCompatActivity() {


    companion object {
        private const val LOG_TAG = "448.PostActivityDetails"

        fun createIntent(baseContext: Context) : Intent {
            val intent = Intent( baseContext, PostActivityDetails::class.java)
            return intent
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)

        // hooks up the bottom panel
        all_post_bottom_panel_nest.setOnClickListener{
            launchNest()
        }
    }

    //Launches the nest activity
    private fun launchNest(){
        finish()
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