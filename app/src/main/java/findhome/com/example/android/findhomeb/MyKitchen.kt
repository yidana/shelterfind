package findhome.com.example.android.findhomeb

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.ProgressBar
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class MyKitchen {

    data class myPlace(val location:LatLng,
                       val id:String,
                       val name:String,
                       val placeId:String,
                       val vicinity:String,
                       val type: MutableList<Int>)


    data class roomtype(val bedrooms:Int,val bathrooms:Int,val balcony:Boolean,val mainhall:Boolean )


     val REQUEST_LOCATION_PERMISSION = 100

    fun SetProgress(progressBar: ProgressBar, progInt: Int) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBar.setProgress(progInt, true)
        } else {
            progressBar.progress = progInt
        }

    }




    fun CheckPermission(context: Context, activity: Activity) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED   ) {

            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_LOCATION_PERMISSION)
        } else {
            Log.e("DB", "PERMISSION NOT GRANTED")
        }


    }


    companion object {


        val TAG = "LocationTrackingService"



    }




}


