package com.example.vultures

import android.util.Log
import com.google.firebase.firestore.ServerTimestamp
import com.google.type.Date


class Post {
    var id: String = ""
    var mtitle: String? = null
    var mlocation: String? = null
    var mTimestamp: Date? = null

    constructor() {}

    constructor(title: String, location: String) {
        mtitle = title
        mlocation = location
        Log.d("AAAAAAAAAAAAAAA", mtitle.toString())
    }

    constructor(id: String, title: String, location: String) {
        this.id = id
        mtitle = title
        mlocation = location
    }

    fun toMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        result.put("id", id)
        result.put("title", mtitle!!)
        result.put("location", mlocation!!)

        return result
    }

}