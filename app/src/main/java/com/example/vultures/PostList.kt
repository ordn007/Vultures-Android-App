package com.example.vultures

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_post_details.*
import kotlinx.android.synthetic.main.post_recycler_view.*


class PostList : AppCompatActivity() {


    private var firestoreDB: FirebaseFirestore? = null
    private var firestoreListener: ListenerRegistration? = null
    private var postsList = mutableListOf<Post>()
    private var adapter: FirestoreRecyclerAdapter<Post, PostHolder>? = null


    companion object {
        private const val LOG_TAG = "448.PostList"

        fun createIntent(baseContext: Context) : Intent {
            val intent = Intent( baseContext, PostList::class.java)
            return intent
        }

    }


    class PostHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView
        var location: TextView

        init {
            title = view.findViewById(R.id.post_title)
            location = view.findViewById(R.id.post_location)
        }

    }

    private fun updatePost(post: Post) {
        val intent = Intent(this, PostActivity::class.java)
        intent.putExtra("UpdatePostId", post.id)
        intent.putExtra("UpdatePostTitle", post.mtitle)
        intent.putExtra("UpdatePostContent", post.mlocation)
        startActivity(intent)
    }

    private fun deletePost(id: String) {
        firestoreDB!!.collection("posts")
            .document(id)
            .delete()
            .addOnCompleteListener {
                Toast.makeText(applicationContext, "Post has been deleted!", Toast.LENGTH_SHORT).show()
            }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_recycler_view)



        firestoreDB = FirebaseFirestore.getInstance()

        val mLayoutManager = LinearLayoutManager(applicationContext)
        post_recycler_view.layoutManager = mLayoutManager
        post_recycler_view.itemAnimator = DefaultItemAnimator()


        loadPostList()

        firestoreListener = firestoreDB!!.collection("posts")
            .addSnapshotListener(EventListener { documentSnapshots, e ->
                if (e != null) {
                    Log.e(LOG_TAG, "Listen failed!", e)
                    return@EventListener
                }

                postsList = mutableListOf()

                if (documentSnapshots != null) {
                    for (doc in documentSnapshots) {
                        val post = Post(doc.id, doc.get("title").toString(), doc.get("location").toString())
                        postsList.add(post)
                    }
                }

                adapter!!.notifyDataSetChanged()
                post_recycler_view.adapter = adapter
            })



        // hooks up the bottom panel
        all_post_bottom_panel_nest.setOnClickListener {
            launchNest()
        }

    }


    private fun loadPostList() {

        val query = FirebaseFirestore.getInstance().collection("posts")

        val response = FirestoreRecyclerOptions.Builder<Post>()
            .setQuery(query, Post::class.java)
            .build()

        adapter = object : FirestoreRecyclerAdapter<Post, PostHolder>(response) {

            override fun onBindViewHolder(holder: PostHolder, position: Int, model: Post) {
                val post = postsList[position]
                holder.title.text = post.mtitle
                holder.location.text = post.mlocation

            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.single_post, parent, false)
                return PostHolder(view)
            }

            override fun onError(e: FirebaseFirestoreException) {
                super.onError(e)
                Toast.makeText(baseContext, "Error getting a snapshot",
                    Toast.LENGTH_SHORT).show()
            }
        }

        adapter!!.notifyDataSetChanged()
        post_recycler_view.adapter = adapter
    }

    //Launches the nest activity
    private fun launchNest(){
        finish()
    }


    //Life Cycles Methods
    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
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
        adapter!!.stopListening()
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "onDestroy() called")
        super.onDestroy()
        firestoreListener!!.remove()
    }
}