package com.example.vultures

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LogIn : AppCompatActivity() {


    companion object {
        private const val LOG_TAG = "448.LogIn"
        private const val SIGNUP = 1
    }


    // Checks database for user login info, then login/not
    fun logIn() {
        var userName = findViewById<EditText>(R.id.login_userName_field)
        var myText = "Logged in as " + userName.text
        val toast = Toast.makeText(applicationContext, myText, Toast.LENGTH_SHORT)
        toast.show()

        //Bypassing actual login and just launching NestActivity
        val intent = NestActivity.createIntent( baseContext)
        startActivity(intent)
    }
    // Made this reset the Username and password fields
    fun cleanUpTextFields() {
        login_userName_field.text=null
        login_password_field.text=null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        link_signUp.setOnClickListener{

            val intent = SignUp.createIntent(this)
            startActivityForResult(intent, SIGNUP)
        }
        login_btn_submit.setOnClickListener {

            val userName = login_userName_field.text.toString()
            val password = login_password_field.text.toString()

            if (userName.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "Enter a valid Username and Password", Toast.LENGTH_SHORT).show()
            }else {
                logIn()
            }

        }
        login_btn_reset.setOnClickListener { cleanUpTextFields() }
    
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
