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
            //intent.putExtra(ANSWER_KEY, isAnswerTrue)
            return intent
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        //hooks up buttons -- currently only displays toasts saying what should be launched.
        photo_button.setOnClickListener{Toast.makeText(baseContext,"Access photos", Toast.LENGTH_SHORT).show()}
        post_button.setOnClickListener{Toast.makeText(baseContext,"Commits the post", Toast.LENGTH_SHORT).show()}
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