package com.example.vultures

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_nest.*

class NestActivity : AppCompatActivity() {


    companion object {
        private const val LOG_TAG = "448.NestActivity"

        fun createIntent(baseContext: Context) : Intent {
            val intent = Intent( baseContext, NestActivity::class.java)
            return intent
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nest)

        //hooks up buttons -- currently only displays toasts saying what should be launched.
        make_a_post.setOnClickListener{
            launchPostActivity()
        }
        view_map.setOnClickListener{
//            Toast.makeText(baseContext,"Displays the Map", Toast.LENGTH_SHORT).show()
            launchMapActivity()
        }
        view_all_posts.setOnClickListener{
            launchPostActivityDetails()
        }

        sign_out.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            finish()
        }

        val user = FirebaseAuth.getInstance().currentUser
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.vul_menu, menu)
        return true
    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_search -> {
            msgShow("Search")
            true
        }
        R.id.action_profile -> {
            msgShow("Profile")
            true
        }
        R.id.action_setting -> {
            msgShow("Setting")
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    fun msgShow(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun launchPostActivity(){
        val intent = PostActivity.createIntent(baseContext)
        startActivity(intent)
    }

    private fun launchPostActivityDetails(){
        val intent = PostList.createIntent(baseContext)
        startActivity(intent)
    }

    private fun launchMapActivity(){
        val intent = MapActivity.createIntent(baseContext)
        startActivity(intent)
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