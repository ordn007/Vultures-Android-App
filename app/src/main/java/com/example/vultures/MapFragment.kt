package com.example.vultures

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.*
import android.widget.Toast
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MapFragment : SupportMapFragment(){


    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private lateinit var googleMap: GoogleMap
    private lateinit var lastLocation: Location


    companion object {
        private const val LOG_TAG = "448.LocatorFragment"
        const val REQUEST_LOC_ON = 0
        const val REQUEST_LOC_PERMISSION = 1

    }


    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate() called")

        setHasOptionsMenu(true)
        locationRequest = LocationRequest.create()
        locationRequest.interval = 0
        locationRequest.numUpdates = 1
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity as Activity)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult)
            {
                super.onLocationResult(locationResult)
                Log.d(LOG_TAG, "Got a location: ${locationResult.lastLocation}")
                lastLocation = locationResult.lastLocation
                updateUI()
            }
        }

        addPersitedMarkers()
        getMapAsync {
            googleMap = it
            activity?.invalidateOptionsMenu()
        }

    }

    private fun addPersitedMarkers() {
        db.collection("posts")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(PostActivity.LOG_TAG, "${document.id} => ${document.data}")
                    var lat = document.data["latitude"] as Double
                    var lon = document.data["longitude"] as Double
                    var title = document.data["title"] as String
                    var location = document.data["location"] as String

                    var name : String = "${title} @ ${location}"


                    val markerPos = LatLng(lat, lon)
                    val dbMarkers = MarkerOptions().position(markerPos).title(name)
                    googleMap.addMarker(dbMarkers)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(PostActivity.LOG_TAG, "Error getting documents.", exception)
            }
    }
    override fun onStart() {
        super.onStart()
        checkIfLocationCanBeRetrieved()
        Log.d(LOG_TAG, "onStart() called")
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(LOG_TAG, "onCreateView() called")
        val mapView = super.onCreateView(inflater, container, savedInstanceState)

        return mapView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.fragment_locator, menu)

        val locationItem = menu?.findItem(R.id.get_location_menu_item)
        locationItem?.isEnabled = (locationUpdateState && ::googleMap.isInitialized)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
        when (item?.itemId) {
            R.id.get_location_menu_item -> {
                Log.d(LOG_TAG, "Get Location Item was Clicked")
                checkPermissionAndGetLocation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun checkIfLocationCanBeRetrieved() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(activity as Activity)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            locationUpdateState = true
            activity?.invalidateOptionsMenu()
        }
        task.addOnFailureListener {e -> locationUpdateState = false
            activity?.invalidateOptionsMenu()
            if(e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(activity as Activity, REQUEST_LOC_ON)
                } catch (e: IntentSender.SendIntentException) {
                    // do nothing, they cancelled so ignore error
                }
            }
        }
    }

    fun checkPermissionAndGetLocation() {

        if (ContextCompat.checkSelfPermission(activity as Context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // permission not granted
            // check if we should ask

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity as Activity, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // user already said now, don't ask again
                Toast.makeText(activity, "We must access your location to plot where you are", Toast.LENGTH_LONG).show()
            } else {
                // user hasn't previously declined, ask them
                requestPermissions(listOf(android.Manifest.permission.ACCESS_FINE_LOCATION).toTypedArray(),  REQUEST_LOC_PERMISSION)

            }
        } else
        // permission has been granted, do what we want
//             Toast.makeText(activity, "Thanks!", Toast.LENGTH_SHORT).show()
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == REQUEST_LOC_PERMISSION) {
            if (!permissions.isEmpty()) {
                if (permissions[0] == android.Manifest.permission.ACCESS_FINE_LOCATION) {
                    if (!grantResults.isEmpty()) {
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            checkPermissionAndGetLocation()
                        }
                    }
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override  fun  onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode != Activity.RESULT_OK) {
            return
        }
        if(requestCode == REQUEST_LOC_ON) {
            locationUpdateState = true
            activity?.invalidateOptionsMenu()
        }
    }

    private fun getAddress(location: Location): String {
        val geocoder = Geocoder(activity as Activity)
        val addresses: List<Address>?
        val address: Address
        var addressText = ""
        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if(addresses != null && addresses.isNotEmpty()) {
                address = addresses[0]
                for(i in 0..address.maxAddressLineIndex) {
                    addressText += if(i == 0) {
                        address.getAddressLine(i)
                    }
                    else {
                        "\n" + address.getAddressLine(i)
                    }
                }
            }
        } catch (e: IOException) {
            Log.e(LOG_TAG, e.localizedMessage)
        }
        return addressText
    }

    private fun updateUI() {
        // make sure we have a map and a location
        if( !::googleMap.isInitialized || !::lastLocation.isInitialized ) {
            return
        }
        // create a point for the corresponding lat/long
        val myLocationPoint = LatLng(lastLocation.latitude, lastLocation.longitude)
        // Step 3 will go here

        // create the marker
        val myMarker = MarkerOptions().position(myLocationPoint).title( getAddress(lastLocation) )

        // clear any prior markers on the map
//        googleMap.clear()

        // add the new markers
//        googleMap.addMarker(myMarker)

        // include all points that should be within the bounds of the zoo
        // convex hull
        val bounds = LatLngBounds.Builder().include(myLocationPoint).build()

        // add a margin
        val margin = resources.getDimensionPixelSize(R.dimen.map_inset_margin)

        // create a camera to smoothly move the map view
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, margin)

        // move our camera!
        googleMap.animateCamera(cameraUpdate)
    }

}