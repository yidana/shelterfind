package findhome.com.example.android.findhomeb.workers

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.net.Network
import android.net.NetworkInfo
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.impl.constraints.NetworkState
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.search_on_map_fragment.view.*
import java.util.*

const val PLACE_ADDRESS="place_address"
const val LOG_TAG="LOCATION_WORKER_LOG"
const val LOCATION_AD="converted_address"
const val COUNTRY_NAME="country_name"
const val CITY_NAME="city_name"
class LocationWorker:Worker(){


    override fun doWork(): Result= try {


        val status = NetworkInfo.State.CONNECTED

        if (status==NetworkInfo.State.CONNECTED){


            val address=inputData.getString(PLACE_ADDRESS)

            val gc = Geocoder(this.applicationContext, Locale.getDefault())
            val list: List<Address> = gc.getFromLocationName(address, 5)

            val mlocation: Address = list[0]



            val countryName= mlocation.countryName
            val cityName= mlocation.locality
            val latitude = mlocation.latitude
            val longitude = mlocation.longitude

            val gps  = LatLng(latitude,longitude)

            val mylocationArray=DoubleArray(2)

            mylocationArray.set(0,gps.latitude)
            mylocationArray.set(1,gps.longitude)

            val output=Data.Builder()
                    .putDoubleArray(LOCATION_AD,mylocationArray)
                    .putString(COUNTRY_NAME,countryName)
                    .putString(CITY_NAME,cityName)
                    .build()


            Result.SUCCESS

        }else{


            Result.FAILURE
        }



        }catch (e: Throwable) {
            Log.e(LOG_TAG, "Error executing work: " + e.message, e)
            Result.FAILURE
        }




}


