package findhome.com.example.android.findhomeb

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import findhome.com.example.android.findhomeb.R.drawable.image2
import findhome.com.example.android.findhomeb.R.drawable.image_1
import findhome.com.example.android.findhomeb.R.layout.fragment_places

import kotlinx.android.synthetic.main.fragment_places.*


class PlacesFragment : Fragment(), PlacesRecyclerViewAdaptor.OnItemClickListener  , OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mMapView: MapView

    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"


    override fun onItemClick(data: Data) {
        Toast.makeText(this.context, "Hello it worked", Toast.LENGTH_SHORT).show()

    }

    private var dataRecyclerView: RecyclerView?=null
    private var recyclerViewAdapter: PlacesRecyclerViewAdaptor? = null

    private var listmyData:ArrayList<Data>?=ArrayList()


    private var listener: OnFragmentInteractionListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view:View =inflater.inflate(fragment_places, container, false)

        mMapView=view.findViewById(R.id.mapView)

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)
        }

        mMapView.onCreate(mapViewBundle)
        mMapView.getMapAsync(this)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataRecyclerView=  rvPlaces
        listmyData?.add(Data("Ian Hostl","354fdAvenue", 1.4F,image_1 ))
        listmyData?.add(Data("Yidana Hostl","354fdAvenue", 4.4F,image2 ))
        listmyData?.add(Data("Ian Hostl","354fdAvenue", 1.4F, image_1    ))
        listmyData?.add(Data("KGR Hostl","354fdAvenue", 4.4F,image2 ))
        listmyData?.add(Data("RFW Hostl","354fdAvenue", 2.4F,image_1 ))
        listmyData?.add(Data("DER Hostl","354fdAvenue", 3.4F,image2 ))
        listmyData?.add(Data("FEW Hostl","354fdAvenue", 5.4F,image_1 ))
        listmyData?.add(Data("WRF Hostl","354fdAvenue", 0.4F,image2 ))
        listmyData?.add(Data("WRW Hostl","354fdAvenue", 2.9F,image_1 ))
        listmyData?.add(Data("GHYT Hostl","354fdAvenue", 5.0F,image2 ))
        listmyData?.add(Data("JYE Hostl","354fdAvenue", 0.7F,image_1 ))
        listmyData?.add(Data("CVB Hostl","354fdAvenue", 3.8F,image2 ))
        listmyData?.add(Data("XSD Hostl","354fdAvenue", 4.7F,image_1 ))
        listmyData?.add(Data("WER Hostl","354fdAvenue", 4.4F,image2 ))


        recyclerViewAdapter = PlacesRecyclerViewAdaptor(listmyData!!, this)
        dataRecyclerView?.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        dataRecyclerView?.adapter = recyclerViewAdapter


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
        mMapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMapView.onStop()
    }

    override fun onPause() {
        mMapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mMapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }



    override fun onMapReady(map: GoogleMap?) {
        mMap = map as GoogleMap

        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Seed nay"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
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
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                PlacesFragment()
    }


}
