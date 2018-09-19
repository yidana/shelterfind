package findhome.com.example.android.findhomeb

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import findhome.com.example.android.findhomeb.adaptors.HomeRecyclerViewAdaptor
import findhome.com.example.android.findhomeb.adaptors.ProgressLiftingAdaptor
import findhome.com.example.android.findhomeb.model.CloudData
import findhome.com.example.android.findhomeb.viewmodel.MyViewModel
import findhome.com.example.android.findhomeb.viewmodel.ProgressViewModel
import kotlinx.android.synthetic.main.fragment_account_settings.*
import kotlinx.android.synthetic.main.fragment_all.*
import kotlinx.android.synthetic.main.fragment_lifting_status.*


class LiftingStatus : Fragment(),ProgressLiftingAdaptor.OnItemClickListener {



    override fun onItemClick(data: CloudData) {



        when( data.progress){

            "1"->{ Navigation.findNavController(view!!).navigate(R.id.entryFormoneFragment, null) }
            "10"->{
                if (data.type=="home" || data.type=="apartment" || data.type=="hotel") Navigation.findNavController(view!!).navigate(R.id.generalRoomTypeFragment, null)
                else Navigation.findNavController(view!!).navigate(R.id.hostelRoomTypeFragment, null)
            }
            "30"->{ Navigation.findNavController(view!!).navigate(R.id.placeAvailability, null) }
            "40"->{
                if (data.type=="home" || data.type=="apartment") Navigation.findNavController(view!!).navigate(R.id.generalPriceFragment, null)
                else if (data.type=="hotel")  Navigation.findNavController(view!!).navigate(R.id.hotelGeneralPriceFragment, null)
                else if (data.type=="hostel")  Navigation.findNavController(view!!).navigate(R.id.priceHostelFragment, null)
            }
            "50"->{ Navigation.findNavController(view!!).navigate(R.id.overviewFragment, null) }
            "60"->{ Navigation.findNavController(view!!).navigate(R.id.profilePictureFragment, null) }
            "70"->{ Navigation.findNavController(view!!).navigate(R.id.addPlacePicturesFragment, null) }
            "80"->{ Navigation.findNavController(view!!).navigate(R.id.amenitiesFragment, null) }
            "95"->{ Navigation.findNavController(view!!).navigate(R.id.addressFragment, null) }

        }



    }

    var  lift_userID=""
    lateinit var mFirebaseFirestore: FirebaseFirestore
    private var dataRecyclerView:RecyclerView?=null
    private var recyclerViewAdapter: ProgressLiftingAdaptor? = null
    lateinit var mViewModel: ProgressViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirebaseFirestore= FirebaseFirestore.getInstance()
        mViewModel= ViewModelProviders.of(this).get(ProgressViewModel::class.java)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lifting_status, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        FirebaseAuth.AuthStateListener { firebaseAuth ->
            lift_userID=firebaseAuth.uid!!

            dataRecyclerView= progress_recycleview
            val dbcloud:ArrayList<CloudData>?= ArrayList()
            val facilities=HashMap<String,String>()
            facilities.put("homes","homes")
            facilities.put("hostels","hostels")
            facilities.put("hotels","hotels")
            facilities.put("apartments","apartments")

            for (i:String in   facilities.keys){
                val mTarget=facilities[i]

                mFirebaseFirestore
                        .document("user/facilities")
                        .collection(mTarget!!)
                        .addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                            if (firebaseFirestoreException!=null){

                            }else{


                                for (documentChange: DocumentChange in querySnapshot!!.documentChanges){

                                    if (documentChange.type== DocumentChange.Type.ADDED){

                                        if (documentChange.document.exists()){

                                            if(documentChange.document.id==firebaseAuth.currentUser!!.uid  ){

                                                mFirebaseFirestore
                                                        .document(documentChange.document.reference.path)
                                                        .collection(mTarget)
                                                        .addSnapshotListener { basequerySnapshot, basefirebaseFirestoreException ->

                                                            if ( basefirebaseFirestoreException!=null){

                                                            }else {

                                                                for (basedocumentChange: DocumentChange in basequerySnapshot!!.documentChanges) {

                                                                    if (basedocumentChange.document.exists()){

                                                                        if (basedocumentChange.document.getBoolean("statuscomplete")==false){

                                                                            val managerData=basedocumentChange.document.toObject(CloudData::class.java)

                                                                            dbcloud!!.add(managerData)

                                                                            mViewModel.getArrayCloudList(dbcloud).observe(this, Observer {cloudata->


                                                                                recyclerViewAdapter = ProgressLiftingAdaptor(cloudata!!, this)
                                                                                dataRecyclerView?.layoutManager = LinearLayoutManager(this.context)
                                                                                dataRecyclerView?.adapter = recyclerViewAdapter





                                                                            })





                                                                        }



                                                                    }





                                                                }



                                                            }


                                                        }



                                            }



                                        }




                                    }

                                }

                            }


                        }

            }





        }




        toolbar.setNavigationOnClickListener {

            Navigation.findNavController(it).navigate(R.id.profileFragment, null)
        }



        btn_start.setOnClickListener {


            Navigation.findNavController(it).navigate(R.id.entryFormoneFragment, null)
        }


    }

    companion object {

        @JvmStatic
        fun newInstance() = LiftingStatus()
    }
}
