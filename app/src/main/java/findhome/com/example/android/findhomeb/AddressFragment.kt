package findhome.com.example.android.findhomeb

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.support.design.button.MaterialButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import android.os.Build
import android.provider.Settings
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import android.support.v7.widget.Toolbar
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.location.places.*
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.maps.model.*
import findhome.com.example.android.findhomeb.MyKitchen.Companion.TAG
import findhome.com.example.android.findhomeb.workers.CompressAndUploadWorker
import findhome.com.example.android.findhomeb.workers.LocationWorker
import kotlinx.android.synthetic.main.fragment_address.view.*
import kotlinx.android.synthetic.main.search_on_map_fragment.view.*
const val LOCATION_AD="converted_address"
const val COUNTRY_NAME="country_name"
const val CITY_NAME="city_name"
const val PLACE_ADDRESS="place_address"
class AddressFragment : Fragment(), OnMapReadyCallback,
        GoogleMap.OnMyLocationClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener {





    private var listener: OnFragmentInteractionListener? = null
    val myKitchen:MyKitchen= MyKitchen()
    private lateinit var mMap: GoogleMap
    private lateinit var mMapView: MapView
    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey2"
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val PLACE_AUTOCOMPLETE_REQUEST_CODE = 1
    private val GOOGLE_API_CLIENT_ID = 0
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var  mAdapter:PlaceAutocompleteAdapter
    private lateinit var  mAdapterForCity:PlaceAutocompleteAdapter
    private lateinit var  mAdapterForAddress:PlaceAutocompleteAdapter
    private lateinit var  mPlaceDetailsText:String
    private lateinit var  mPlaceDetailsAttribution:String
    private lateinit var  mPlaceDatialsAddres:String
    private var  mcity_record:String=""



 private var  BOUNDS_GREATER_SYDNEY: LatLngBounds = LatLngBounds(
            LatLng(-34.041458, 150.790100), LatLng(-33.682247, 151.383362))



    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {

            mMap = googleMap as GoogleMap
            mMap.setOnMyLocationClickListener(this)
            mMap.uiSettings.isScrollGesturesEnabled = true
            mMap.setOnMarkerDragListener(this)

        }






    override fun onMarkerDragEnd(marker: Marker?) {

    }

    override fun onMarkerDragStart(marker: Marker?) {

    }

    override fun onMarkerDrag(marker: Marker?) {

    }


    override fun onMyLocationClick(location: Location) {

    }


private fun drawMarker(location:Location ) {


			val gps  =LatLng(location.latitude, location.longitude)
			mMap.addMarker(MarkerOptions()
					.position(gps)
					.title("Current Position"))
			//mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 12f))


	}



    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            mGoogleApiClient = GoogleApiClient.Builder(this@AddressFragment.context!!)
                    .enableAutoManage(this@AddressFragment.activity!!, GOOGLE_API_CLIENT_ID /* clientId */, this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build()


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@AddressFragment.context!!)


    }





    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.errorCode)

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this@AddressFragment.context,
                "Could not connect to Google API Client: Error " + connectionResult.errorCode,
                Toast.LENGTH_SHORT).show()
    }

    private fun checkPermissions(): Boolean {
        val permissionState1 = ActivityCompat.checkSelfPermission(this@AddressFragment.context!!,
                Manifest.permission.ACCESS_FINE_LOCATION)

        val permissionState2 = ActivityCompat.checkSelfPermission(this@AddressFragment.context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION)


        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2==PackageManager.PERMISSION_GRANTED
    }



    fun String.toSpanned(): Spanned {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            return Html.fromHtml(this)
        }
    }


    private fun formatPlaceDetails(res: Resources, name: CharSequence, id: String, address: CharSequence?, phoneNumber: CharSequence?, websiteUri: Uri?): Spanned {

        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber, websiteUri))
        return res.getString(R.string.place_details, name, id, address, phoneNumber, websiteUri).toSpanned()

    }





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view:View=inflater.inflate(R.layout.fragment_address, container, false)
        mMapView=view.findViewById<MapView>(R.id.mapviewaddress)

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)
        }

        mMapView.onCreate(mapViewBundle)
        mMapView.getMapAsync(this)

        return view
    }







    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        val toolbar = view.findViewById<Toolbar>(R.id.my_toolbar) as Toolbar



        toolbar.setNavigationOnClickListener {

            Navigation.findNavController(it).navigate(R.id.amenitiesFragment, null)
        }



        view.place_search_dialog_country_ET.setOnItemClickListener { parent, view, position, id ->


            val item : AutocompletePrediction? = mAdapter.getItem(position)
            val placeId:String = item!!.placeId.toString()
            Log.i(TAG, "Autocomplete item selected: " + item.placeTypes)


            val placeResult: PendingResult<PlaceBuffer> = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId)
            placeResult.setResultCallback {

                if (!it.status.isSuccess){

                    // Request did not complete successfully
                    Log.e(TAG, "Place query did not complete. Error: " + it.status.toString())
                    it.release()
                    return@setResultCallback
                }



                // Get the Place object from the buffer.
                val place:Place  = it.get(0)


                // Format details of the place for display and show it in a TextView.
                formatPlaceDetails(resources, place.name,
                        place.id, place.address, place.phoneNumber,
                        place.websiteUri)

                // Display the third party attributions if set.
                val thirdPartyAttribution: CharSequence? = it.attributions
                if (thirdPartyAttribution == null) {
                    mcountry_record =place.name.toString()
                } else {

                }

                Log.i(TAG, "Place details received: " + place.name)


                it.release()

            }
            Log.i(TAG, "Called getPlaceById to get Place details for " + item.placeId)

        }






        val typeFilter: AutocompleteFilter = AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_REGIONS)
                .build()







        val typfilteraddress:AutocompleteFilter= AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build()








        mAdapter = PlaceAutocompleteAdapter(this@AddressFragment.context!!,mGoogleApiClient,BOUNDS_GREATER_SYDNEY,typeFilter)


        view.place_search_dialog_country_ET.setAdapter(mAdapter)





        view.place_search_dialog_city_ET.setOnItemClickListener { parent, view, position, id ->

            val item : AutocompletePrediction? = mAdapterForCity.getItem(position)
            val placeId:String = item!!.placeId.toString()
            Log.i(TAG, "Autocomplete item selected: " + item.placeTypes)


            val placeResult: PendingResult<PlaceBuffer> = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId)
            placeResult.setResultCallback {

                if (!it.status.isSuccess){

                    // Request did not complete successfully
                    Log.e(TAG, "Place query did not complete. Error: " + it.status.toString())
                    it.release()
                    return@setResultCallback
                }



                // Get the Place object from the buffer.
                val place:Place  = it.get(0)

                // Format details of the place for display and show it in a TextView.
                formatPlaceDetails(resources, place.name,
                        place.id, place.address, place.phoneNumber,
                        place.websiteUri)

                // Display the third party attributions if set.
                val thirdPartyAttribution: CharSequence? = it.attributions
                if (thirdPartyAttribution == null) {

                    mcity_record=place.name.toString()
                } else {

                }

                Log.i(TAG, "Place details received: " + place.name)

                it.release()

            }
            Log.i(TAG, "Called getPlaceById to get Place details for " + item.placeId)



        }


        val typfiltercity:AutocompleteFilter= AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build()


        mAdapterForCity=PlaceAutocompleteAdapter(this@AddressFragment.context!!,mGoogleApiClient,BOUNDS_GREATER_SYDNEY,typfiltercity)

        view.place_search_dialog_city_ET.setAdapter(mAdapterForCity)


        view.place_search_dialog_street_ET.setOnItemClickListener { parent, view, position, id ->


            val item : AutocompletePrediction? = mAdapterForAddress.getItem(position)
            val placeId:String = item!!.placeId.toString()
            Log.i(TAG, "Autocomplete item selected: " + item.placeTypes)


            val placeResult: PendingResult<PlaceBuffer> = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId)
            placeResult.setResultCallback {

                if (!it.status.isSuccess){

                    // Request did not complete successfully
                    Log.e(TAG, "Place query did not complete. Error: " + it.status.toString())
                    it.release()
                    return@setResultCallback
                }



                // Get the Place object from the buffer.
                val place:Place  = it.get(0)

                // Format details of the place for display and show it in a TextView.
                formatPlaceDetails(resources, place.name,
                        place.id, place.address, place.phoneNumber,
                        place.websiteUri)

                // Display the third party attributions if set.
                val thirdPartyAttribution: CharSequence? = it.attributions
                if (thirdPartyAttribution == null) {

                } else {

                }

                Log.i(TAG, "Place details received: " + place.name)

                it.release()

            }
            Log.i(TAG, "Called getPlaceById to get Place details for " + item.placeId)


        }

        mAdapterForAddress=PlaceAutocompleteAdapter(this@AddressFragment.context!!,mGoogleApiClient,BOUNDS_GREATER_SYDNEY,typfilteraddress)


        view.place_search_dialog_street_ET.setAdapter(mAdapterForAddress)



        val bottomSheetViewgroup:LinearLayout=view.findViewById(R.id.app_bar)
       val bottomSheetBehavior  = BottomSheetBehavior.from(bottomSheetViewgroup)




        val progressBar: ProgressBar?=view.findViewById<ProgressBar>(R.id.progressBar)


            myKitchen.SetProgress(progressBar!!,95)




        val buttonnext: MaterialButton?= view.findViewById<MaterialButton>(R.id.button_finish)

        buttonnext?.setOnClickListener{

            Navigation.findNavController(it).navigate(R.id.profileFragment, null)
        }


        view.fab.setOnClickListener { fabview ->

            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        // Got last known location. In some rare situations this can be null.

          val gps  =LatLng(location!!.latitude, location.longitude)

          val mymarker: Marker = mMap.addMarker(MarkerOptions()
                  .position(gps).title("Current Location"))
          mymarker.showInfoWindow()
          mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 17f))


                    }


            val result:   PendingResult<PlaceLikelihoodBuffer>  = Places.PlaceDetectionApi
                    .getCurrentPlace(mGoogleApiClient, null)


            result.setResultCallback {


                if (!it.status.isSuccess) {
                    // Request did not complete successfully
                    Log.e(TAG, "Place query did not complete. Error: " + it.status.toString())
                    it.release()
                    return@setResultCallback
                }
                val placeName:String  = String.format("%s", it.get(0).place.name)
                val placeAttributuion:String  = String.format("%s", it.get(0).place.address)
                val placeAddress:String=String.format("%s",  it.get(0).place.address)
                mPlaceDatialsAddres=placeAddress
                mPlaceDetailsText=placeName
                mPlaceDetailsAttribution=placeAttributuion
                it.release()
                Log.v("Place name", mPlaceDetailsText)
            }


        }


        view.locate_place.setOnClickListener {

            val gc = Geocoder(this@AddressFragment.context)


            if (!view.place_search_dialog_street_ET.text.isEmpty()) {

                try {

                    if (view.place_search_dialog_street_ET.text.isEmpty()){

                        Snackbar.make(view,
                                "Couldn't find location. Make sure search result is entered correctly",
                                Snackbar.LENGTH_LONG).show()
                    }else{



                        val myConstraints = Constraints.Builder()
                                .setRequiredNetworkType(NetworkType.CONNECTED)
                                .build()


                        val builder = Data.Builder()
                        builder.putString(PLACE_ADDRESS,view.place_search_dialog_street_ET.text.toString().trim() )

                        val work: OneTimeWorkRequest =
                                OneTimeWorkRequest.Builder(LocationWorker::class.java)
                                        .setInputData(builder.build())
                                        .setConstraints(myConstraints)
                                        .build()
                        WorkManager.getInstance().enqueue(work)


                        WorkManager.getInstance().getStatusById(work.id)
                                .observe(this, Observer { workStatus ->

                                    val isWorkActive = !workStatus?.state!!.isFinished

                                    if (isWorkActive){

                                    val myLatlong= workStatus.outputData.getDoubleArray(LOCATION_AD)

                                        val lat=myLatlong!![0]
                                        val long=myLatlong[1]


                                    val myCountry=workStatus.outputData.getString(COUNTRY_NAME)
                                    val myCity=workStatus.outputData.getString(CITY_NAME)


                                        val gps  =LatLng(lat,long)

                                        val mymarker: Marker = mMap.addMarker(MarkerOptions()
                                                .position(gps).title("Drag to your place if not properly located"))
                                        mymarker.showInfoWindow()

                                        mymarker.isDraggable=true



                                        mymarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon))


                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 18f))



                                    }


                                })





                    }




                }catch (e:Exception){
                    throw  IllegalAccessError(e.toString())
                }



            }else{

                Snackbar.make(view,
                        "Fill out the search form to continue",
                        Snackbar.LENGTH_LONG).show()
            }

        }
    }




    private fun vectorToBitmap(@DrawableRes id : Int, @ColorInt color : Int): BitmapDescriptor {
        val vectorDrawable: Drawable? = ResourcesCompat.getDrawable(resources, id, null)
        if (vectorDrawable == null) {
            Log.e(TAG, "Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)


        var mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)
        }

        mMapView.onSaveInstanceState(mapViewBundle)

    }


    override fun onResume() {
        super.onResume()

        mMapView.onResume()
    }

    override fun onStart() {
        super.onStart()

        if (!checkPermissions()) {
            myKitchen.CheckPermission(this@AddressFragment.context!!, this@AddressFragment.activity!!)
        }
        mMapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMapView.onStop()
    }

    override fun onPause() {
        mMapView.onPause()
        mGoogleApiClient.stopAutoManage(this.activity!!)
        mGoogleApiClient.disconnect()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()

        mMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }



    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                AddressFragment()

         var mcountry_record:String=""

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        if (requestCode != myKitchen.REQUEST_LOCATION_PERMISSION) return


        when {
            grantResults.isEmpty() ->
                Log.i(TAG, "User interaction was cancelled.")
            grantResults[0] == PackageManager.PERMISSION_GRANTED -> // Permission granted.
                Log.i(TAG, "Permission Granted")
            else ->

                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent().apply {
                                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            startActivity(intent)
                        })
        }

    }


    private fun showSnackbar(
            mainTextStringId: Int,
            actionStringId: Int,
            listener: View.OnClickListener
    ) {
        Snackbar.make(this.view!!, getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener)
                .show()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val TAGP="SEARCH AUTOCOMPLETE"
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                RESULT_OK -> {
                    val place: Place  = PlaceAutocomplete.getPlace(this@AddressFragment.context, data)
                    Log.i(TAGP, "Place: " + place.name)
                }
                PlaceAutocomplete.RESULT_ERROR -> {
                    val status: Status = PlaceAutocomplete.getStatus(this@AddressFragment.context, data)
                    // TODO: Handle the error.
                    Log.i(TAGP, status.statusMessage)

                }
                RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
        }
    }
}
