package com.example.vultures

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_post.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PostActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var firestoreDB: FirebaseFirestore? = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance("gs://vultures-bc2b7.appspot.com")
    var storageRef = storage.reference
    lateinit var bitmap: Bitmap
    var imageRef = storageRef.child("default.jpg")
    var imagePath = "default.png"

    internal var id: String = ""

    private val GALLERY = 1
    private val CAMERA = 2

    var longitude : Double = 0.0
    var latitude: Double = 0.0

    companion object {
        const val LOG_TAG = "448.PostActivity"
        private const val IMAGE_DIRECTORY = "/demonuts"

        var REFERENCE_ID = ""

        fun createIntent(baseContext: Context) : Intent {
            val intent = Intent( baseContext, PostActivity::class.java)
            return intent
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when(parent?.getItemAtPosition(position).toString()){
            "Your Location" -> getLatLon(0.0, 0.0)
            "Alderson Hall" -> getLatLon(39.750513, -105.220661)
            "Berthoud Hall" -> getLatLon(339.750171, -105.222602)
            "Brown Hall" -> getLatLon(39.749620, -105.221690)
            "CTLM" -> getLatLon(39.750440, -105.218790)
            "Coolbaugh House" -> getLatLon(39.752443, -105.221253)
            "CoorsTek" -> getLatLon(39.751050, -105.221940)
            "Maple Hall" -> getLatLon(39.748730, -105.221130)
            "Marquez Hall" -> getLatLon(39.752331, -105.220062)
            "Ball-Rooms A-E" -> getLatLon(39.748960, -105.222300)

        }
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun getLatLon(lat: Double, lon : Double){
        latitude = lat
        longitude = lon
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val bundle = intent.extras
        if (bundle != null) {
            id = bundle.getString("UpdatePostId")

            title_field.setText(bundle.getString("UpdatePostTitle"))
//            location_field.setText(bundle.getString("UpdatePostContent"))
        }

        //hooks up buttons -- currently only displays toasts saying what would happen
        photo_button.setOnClickListener{ showPictureDialog() }
        post_button.setOnClickListener{
            val title = title_field.text.toString()
            val location = location_spinner.selectedItem.toString()
            val long = longitude
            val latt = latitude
            val extraInfo = extra_info_field.text.toString()

            if (title.isEmpty() || location.isEmpty()) {
                Toast.makeText(baseContext,"Enter A Title and Location", Toast.LENGTH_SHORT).show()
            }else {
                if (id.isNotEmpty()) {
                    updatePost(id, title, location, imagePath, latt, long)
                } else {
                    addPost(title, location, imagePath, latt, long)
                }

                if(::bitmap.isInitialized) {
                    var uploadImage = bitmap


                    val baos = ByteArrayOutputStream()
                    uploadImage.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()

                    var uploadTask = imageRef.putBytes(data)
                    uploadTask.addOnFailureListener {
                        println("Image uploaded sucessfully")
                    }.addOnSuccessListener {
                        // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                        // ...
                    }
                }

            }
        }
        // hooks up the bottom panel
        post_bottom_panel_nest.setOnClickListener{
            finish()
        }

        val spinner: Spinner = location_spinner

//        ArrayAdapter.createFromResource(this, R.array.location_list, android.R.layout.simple_spinner_item).also {
//                adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            spinner.adapter = adapter
//        }

        val adapter = ArrayAdapter.createFromResource(this, R.array.location_list, android.R.layout.simple_spinner_item);
        spinner.adapter = adapter

        spinner.onItemSelectedListener = this
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data!!.data
                try
                {
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)

                    imagePath = UUID.randomUUID().toString()
                    imageRef = storageRef.child("${imagePath}.png")


                    val path = saveImage(bitmap)
                    Toast.makeText(this@PostActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
                    my_image_view.setImageBitmap(bitmap)

                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@PostActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        }
        else if (requestCode == CAMERA)
        {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            my_image_view!!.setImageBitmap(thumbnail)
            imagePath = UUID.randomUUID().toString()
            imageRef = storageRef.child("${imagePath}.png")
            saveImage(thumbnail)
            Toast.makeText(this@PostActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        Log.d("fee",wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {

            wallpaperDirectory.mkdirs()
        }

        try
        {
            Log.d("heel",wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                .getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    private fun updatePost(id: String, title: String, location: String, path: String, lat: Double, lon: Double) {
        val post = Post(id, title, location, path, lat, lon).toMap()

        firestoreDB!!.collection("posts")
            .document(id)
            .set(post)
            .addOnSuccessListener {
                Log.e(LOG_TAG, "Post document update successful!")
                Toast.makeText(applicationContext, "Post has been updated!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e(LOG_TAG, "Error adding Post document", e)
                Toast.makeText(applicationContext, "Post could not be updated!", Toast.LENGTH_SHORT).show()
            }
    }


    private fun addPost(title: String, location: String, path: String, lat: Double, lon: Double) {
        val post = Post(title, location, path, lat, lon).toMap()

        firestoreDB!!.collection("posts")
            .add(post)
            .addOnSuccessListener { documentReference ->
                Log.e(LOG_TAG, "DocumentSnapshot written with ID: " + documentReference.id)
                Toast.makeText(applicationContext, "Post has been added!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e(LOG_TAG, "Error adding Post document", e)
                Toast.makeText(applicationContext, "Post could not be added!", Toast.LENGTH_SHORT).show()
            }
    }

    //Launches the nest activity
    // Will commit the post for others to view
    private fun commitPost(){
        val title = title_field.text
//        val location = location_field.text
        val location = location_spinner.selectedItem
        val extraInfo = extra_info_field.text
        Toast.makeText(baseContext,"Commits post with Title: ${title}, Location: ${location}, Extra: ${extraInfo}", Toast.LENGTH_SHORT).show()

        // Resets the fields
        cleanUpTextFields()
    }

    fun cleanUpTextFields() {
        title_field.text = null
//        location_field.text = null
        extra_info_field.text = null
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