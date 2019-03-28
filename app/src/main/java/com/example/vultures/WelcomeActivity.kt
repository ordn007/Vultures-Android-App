package com.example.vultures

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    companion object {
        private const val LOG_TAG = "448.Welcome"
        private const val SIGNUP = 1

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_welcome)


        welcome_logIn.setOnClickListener{
            val intent = LogIn.createIntent(this)
            startActivityForResult(intent, SIGNUP)
        }
        welcome_pg_join_us.setOnClickListener{

            val intent = SignUp.createIntent(this)
            startActivityForResult(intent, SIGNUP)
        }
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