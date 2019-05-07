package com.example.vultures

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.type.Date


class Post {


    var imageRef: String? = null
    var id: String = ""
    var mtitle: String? = null
    var mlocation: String? = null
    var mTimestamp: Date? = null

    constructor() {}

    constructor(title: String, location: String, path: String) {
        mtitle = title
        mlocation = location
        imageRef = path
        Log.d("AAAAAAAAAAAAAAA", mtitle.toString())
    }

    constructor(id: String, title: String, location: String, path: String) {
        this.id = id
        mtitle = title
        mlocation = location
        imageRef = path
    }

    fun toMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        result.put("id", id)
        result.put("title", mtitle!!)
        result.put("location", mlocation!!)
        result.put("imagePath", imageRef!!)

        return result
    }

}