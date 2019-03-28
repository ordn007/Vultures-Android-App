package com.example.vultures

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class LogIn : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val LOG_TAG = "448.LogIn"
        private const val RC_SIGN_IN = 123

        fun createIntent(context: Context?): Intent {
            val intent = Intent(context, LogIn::class.java)
            return intent
        }
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                //or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                // Hide the nav bar and status bar
               // or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                //or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
              //  or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }



    // Checks database for user login info, then login/not
    fun logIn() {
        db.collection("users")
            .whereEqualTo("username", login_userName_field.text.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    //Log.d(TAG, "${document.id} => ${document.data}")
                    var email = document.get("email").toString()


                    auth.signInWithEmailAndPassword(email, login_password_field.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(LOG_TAG, "signInWithEmail:success")
                                val user = auth.currentUser

                                //Bypassing actual login and just launching NestActivity
                                val intent = NestActivity.createIntent( baseContext)
                                startActivity(intent)

                                //updateUI(user)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(LOG_TAG, "signInWithEmail:failure", task.exception)
                                Toast.makeText(baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                               // updateUI(null)
                            }

                            // ...
                        }






                }
            }
            .addOnFailureListener { exception ->
                Log.w(LOG_TAG, "Error getting documents: ", exception)
            }




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
                //signOut()
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(applicationContext, user?.email.toString(), Toast.LENGTH_SHORT).show()
                logIn();
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

      //  hideSystemUI()
        setContentView(R.layout.activity_login)
//        link_signUp.setOnClickListener{
//
//            val intent = SignUp.createIntent(this)
//            startActivityForResult(intent, SIGNUP)
//        }
        login_btn_submit.setOnClickListener {

            val userName = login_userName_field.text.toString()
            val password = login_password_field.text.toString()

            if (userName.isEmpty()) {
                login_userName_field.error = "Required."
            }else if(password.isEmpty()){
                login_password_field.error = "Required"
            }else {
                logIn()
            }

        }
        login_btn_reset.setOnClickListener { cleanUpTextFields() }
        google_sign_in.setOnClickListener {
            createSignInIntent()
        }

        auth = FirebaseAuth.getInstance()
    
    }

    //Life Cycles Methods
    override fun onStart() {
        super.onStart()
        Log.d(LOG_TAG, "onStart() called")
        val currentUser = auth.currentUser
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
