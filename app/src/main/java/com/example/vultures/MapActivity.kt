package com.example.vultures

import android.content.Context
import android.content.Intent
import android.location.Location
import android.support.v4.app.Fragment

class MapActivity : SingleFragmentActivity(){

    companion object {
        private const val LOG_TAG = "448.MapActivity"

        fun createIntent(context: Context) : Intent {
            val intent = Intent(context, MapActivity::class.java)
            return intent
        }

    }


    override fun createFragment(): MapFragment{
        return MapFragment()
    }

    override fun getLogTag() = LOG_TAG

    override  fun  onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == MapFragment.REQUEST_LOC_ON) {
            val locatrFragment: MapFragment? =
                supportFragmentManager.findFragmentById(R.id.fragment_container) as MapFragment?
            locatrFragment?.onActivityResult(requestCode, resultCode, data)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
