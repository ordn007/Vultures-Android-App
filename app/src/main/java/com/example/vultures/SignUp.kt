package com.example.vultures

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_signup.*

class SignUp : AppCompatActivity() {
    companion object {

        fun createIntent(context: Context?): Intent {
            val intent = Intent(context, SignUp::class.java)
            return intent
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup);

        link_logIn.setOnClickListener {
            finish()
        }

    }
}