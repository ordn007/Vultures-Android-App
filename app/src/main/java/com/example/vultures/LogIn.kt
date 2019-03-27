package com.example.vultures

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LogIn : AppCompatActivity() {

    companion object {
        private const val LOG_TAG = "448.LogIn"
        private const val SIGNUP = 1
        private const val RC_SIGN_IN = 123
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

    private fun createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
          //  AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())
           // AuthUI.IdpConfig.FacebookBuilder().build(),
           // AuthUI.IdpConfig.TwitterBuilder().build())

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.vultureaid_icon)      // Set logo drawable
                .setTheme(R.style.AppTheme)
                .build(),
            RC_SIGN_IN)
        // [END auth_fui_create_intent]
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(applicationContext, "Sucesfully signed in", Toast.LENGTH_SHORT).show()
                // Successfully signed in
                signOut()
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(applicationContext, user?.email.toString(), Toast.LENGTH_SHORT).show()


                // ...
            } else {
                Toast.makeText(applicationContext, "Sign in failed", Toast.LENGTH_SHORT).show()

                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    private fun signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                // ...
            }
        // [END auth_fui_signout]
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
        google_sign_in.setOnClickListener {
            createSignInIntent()
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
