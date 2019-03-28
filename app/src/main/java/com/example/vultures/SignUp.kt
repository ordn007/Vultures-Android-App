package com.example.vultures

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup.*

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()


    companion object {
        private const val LOG_TAG = "448.Welcome"

        fun createIntent(context: Context?): Intent {
            val intent = Intent(context, SignUp::class.java)
            return intent
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup);


        signUp_btn_submit.setOnClickListener {
            createAccount(email_field.text.toString(), signUp_password_field.text.toString())
        }
        signUp_btn_reset.setOnClickListener {
            Name_field.text = null
            email_field.text = null
            signUp_userName_field.text = null
            signUp_password_field.text = null
            signUp_password_field2.text = null
        }

        auth = FirebaseAuth.getInstance()
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        //TODO need to check if user is signed in
        //updateUI(currentUser)
    }

    private fun validateForm(): Boolean {
        var valid = true

        val name = Name_field.text.toString()
        if (TextUtils.isEmpty(name)) {
            Name_field.error = "Required."
            valid = false
        } else {
            Name_field.error = null
        }

        val username = signUp_userName_field.text.toString()
        if (TextUtils.isEmpty(username)) {
            signUp_userName_field.error = "Required."
            valid = false
        } else {
            signUp_userName_field.error = null
        }

        val email = email_field.text.toString()
        if (TextUtils.isEmpty(email)) {
            email_field.error = "Required."
            valid = false
        } else {
            email_field.error = null
        }

        val password = signUp_password_field.text.toString()
        if (TextUtils.isEmpty(password)) {
            signUp_password_field.error = "Required."
            valid = false
        }else if(password != signUp_password_field2.text.toString()){
            signUp_password_field2.error = "Passwords do not match."
            valid = false
        }else {
            signUp_password_field.error = null
        }

        return valid
    }

    private fun createAccount(email: String, password: String) {
        Log.d(LOG_TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        //showProgressDialog()

        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(LOG_TAG, "createUserWithEmail:success")
                    val user = auth.currentUser

                    Toast.makeText(baseContext, "Authentication sucessfull with ${user?.email}",
                        Toast.LENGTH_SHORT).show()

                    // Create a new user with a first and last name
                    val entry = HashMap<String, Any>()
                    entry["name"] = Name_field.text.toString()
                    entry["email"] = user?.email.toString()
                    entry["username"] = signUp_userName_field.text.toString()

                    // Add a new document with a generated ID
                    db.collection("users").document(user?.email.toString())
                        .set(entry)
                        .addOnSuccessListener { Log.d(LOG_TAG, "DocumentSnapshot successfully written!") }
                        .addOnFailureListener { e -> Log.w(LOG_TAG, "Error writing document", e) }

                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(LOG_TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }

                // [START_EXCLUDE]
                //hideProgressDialog()
                // [END_EXCLUDE]
            }
        // [END create_user_with_email]
    }
}