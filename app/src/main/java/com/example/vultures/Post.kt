package com.example.vultures

import com.google.type.Date


class Post {


    var imageRef: String? = null
    var id: String = ""
    var mtitle: String? = null
    var mlocation: String? = null
    var mlat: Double = 0.0
    var mlng: Double = 0.0
    var mTimestamp: Date? = null

    constructor() {}

    constructor(title: String, location: String, path: String, lat: Double, lng: Double) {
        mtitle = title
        mlocation = location
        imageRef = path
        mlat = lat
        mlng = lng
    }

    constructor(id: String, title: String, location: String, path: String, lat: Double, lng: Double) {
        this.id = id
        mtitle = title
        mlocation = location
        imageRef = path
        mlat = lat
        mlng = lng
    }

    fun toMap(): Map<String, Any> {

        val result = HashMap<String, Any>()
        result.put("id", id)
        result.put("title", mtitle!!)
        result.put("location", mlocation!!)
        result.put("imagePath", imageRef!!)
        result.put("latitude", mlat!!)
        result.put("longitude", mlng!!)
        return result
    }

}