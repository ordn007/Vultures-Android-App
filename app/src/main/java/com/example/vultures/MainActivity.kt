package com.example.vultures

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {


    companion object {
        private const val LOG_TAG = "448.MainActivity"

    }


    // Checks database for user login info, then login/not
    fun logIn() {
        var userName = findViewById<EditText>(R.id.login_userName_field)
        var myText = "Attempting to log in as " + userName.text
        val toast = Toast.makeText(applicationContext, myText, Toast.LENGTH_SHORT)
        toast.show()
    }

    fun cleanUpTextFields() {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val newAccount = findViewById<TextView>(R.id.link_signUp)
        val loginButton = findViewById<Button>(R.id.login_btn_submit)
        val resetButton = findViewById<Button>(R.id.login_btn_reset)

        newAccount.setOnClickListener { setContentView(R.layout.activity_signup) }
        loginButton.setOnClickListener { logIn() }
        resetButton.setOnClickListener { cleanUpTextFields() }
    
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
