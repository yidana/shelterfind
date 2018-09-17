package findhome.com.example.android.findhomeb

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.viewpagerindicator.CirclePageIndicator
import kotlinx.android.synthetic.main.fragment_view.*
import androidx.work.impl.Schedulers.schedule
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import findhome.com.example.android.findhomeb.HomeFragment.Companion.passingDataCloudData
import findhome.com.example.android.findhomeb.adaptors.*
import findhome.com.example.android.findhomeb.model.CloudData
import findhome.com.example.android.findhomeb.viewmodel.MyViewModel
import findhome.com.example.android.findhomeb.viewmodel.SimilarFacilityViewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ViewFragment : Fragment(), OnMapReadyCallback,SimilarFacilityRecycleAdaptor.OnItemClickListener {



    override fun onItemClick(data: CloudData) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private lateinit var mMap: GoogleMap
    private lateinit var mMapView: MapView
    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey1"
    lateinit var mFirebaseFirestore: FirebaseFirestore
    lateinit var mViewModel: SimilarFacilityViewModel
    private var similarFacilityAdd:SimilarFacilityRecycleAdaptor? = null



    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap as GoogleMap

        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Seed nay"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirebaseFirestore= FirebaseFirestore.getInstance()



        mViewModel= ViewModelProviders.of(this).get( SimilarFacilityViewModel::class.java)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view= inflater.inflate(R.layout.fragment_view, container, false)
        mMapView=view.findViewById(R.id.view_Mapview)

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


        if (passingDataCloudData.overview!=null){

            Log.v("mCLOUDY234",passingDataCloudData.overview.toString())
        }


        my_toolbar.setOnClickListener {

            Navigation.findNavController(view).navigate(R.id.homeFragment, null)
        }


        val amenlist=passingDataCloudData.amenities as ArrayList<String>
        val imagelist=passingDataCloudData.photourl as ArrayList<String>
        val roomtypelist=ArrayList<String>()

        val moverView=passingDataCloudData.overview as HashMap<String,String>

        facility_name.text= moverView["title"]

        val rating_view=LayoutInflater.from(this@ViewFragment.context).inflate(R.layout.rate_layout, null, false)

        rate_owner.setOnClickListener { view12 ->


            MaterialDialog(this@ViewFragment.context!!)
                    .show {
                        title(R.string.rate_title)
                        this.positiveButton(R.string.rate) {dismiss()  }
                        this.negativeButton(R.string.cancel) {dismiss()}
                        this.setContentView(rating_view)
                    }

        }



        val facility_type=passingDataCloudData.type as String



      val  dataRecyclerView=similar_facility_recycleview
        val dbcloud:ArrayList<CloudData>?= ArrayList()


        mFirebaseFirestore
                .document("user/facilities")
                .collection(facility_type)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                    if (firebaseFirestoreException!=null){

                    }else{
                        for (documentChange: DocumentChange in querySnapshot!!.documentChanges){

                            if (documentChange.type== DocumentChange.Type.ADDED){

                                val mstatus=documentChange.document.data.keys


                                if (documentChange.document.getBoolean("statuscomplete")==false){

                                    val managerData=documentChange.document.toObject(CloudData::class.java)

                                    dbcloud!!.add(managerData)

                                    mViewModel.getArrayCloudList(dbcloud).observe(this,  android.arch.lifecycle.Observer {cloudata->


                                        similarFacilityAdd= SimilarFacilityRecycleAdaptor(cloudata!!,this)

                                        dataRecyclerView?.layoutManager = LinearLayoutManager(this.context)
                                        dataRecyclerView?.adapter =similarFacilityAdd


                                    })





                                }






                            }

                        }
                    }



                }








        about_description.text= moverView["description"]

        val roomtype_chipsadaptor=ChipsAdaptor(this@ViewFragment.context!!,roomtypelist)

        roomtype_list.adapter=roomtype_chipsadaptor


        val amen_adaptor=AmenitiesViewAdaptor(this@ViewFragment.context,amenlist)

        showamenities_list.adapter=amen_adaptor

        val slider_adaptor=ImageSliderViewPagerAdaptor(this@ViewFragment.context!!,imagelist)
        pager.adapter=slider_adaptor
        val mIndicator= view.findViewById<CirclePageIndicator>(R.id.indicator)

        mIndicator.setViewPager(pager)

        val density: Float  = resources.displayMetrics.density

        mIndicator.radius=5*density
       NUM_PAGES =imagelist.size


        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage ==NUM_PAGES) {
              currentPage = 0
            }
            if (pager!=null){
                pager.setCurrentItem(currentPage++, true)

            }


        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 3000)


        mIndicator.setOnPageChangeListener(object:ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(position: Int) {
                currentPage = position
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

               }

            override fun onPageSelected(p0: Int) {

               }


        })





        rate_owner.setOnClickListener {


        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()

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



    companion object {

        @JvmStatic
        fun newInstance() = ViewFragment()

        private  var currentPage:Int = 0
        private  var NUM_PAGES:Int = 0
    }
}
