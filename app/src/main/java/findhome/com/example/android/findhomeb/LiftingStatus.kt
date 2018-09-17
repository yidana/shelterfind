package findhome.com.example.android.findhomeb

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import findhome.com.example.android.findhomeb.adaptors.HomeRecyclerViewAdaptor
import findhome.com.example.android.findhomeb.model.CloudData
import kotlinx.android.synthetic.main.fragment_account_settings.*
import kotlinx.android.synthetic.main.fragment_all.*
import kotlinx.android.synthetic.main.fragment_lifting_status.*


class LiftingStatus : Fragment() {

    var  lift_userID=""
    lateinit var mFirebaseFirestore: FirebaseFirestore
    var liftprogress=""
    var lifttype=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirebaseFirestore= FirebaseFirestore.getInstance()
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

        }


        val facilities=HashMap<String,String>()
        facilities["homes"] = "homes"
        facilities["hostels"] = "hostels"
        facilities["hotels"] = "hotels"
        facilities["apartments"] = "apartments"

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


                                    if ( documentChange.document.getString("userID")== lift_userID   &&  documentChange.document.getBoolean("statuscomplete")==false  ){

                                        liftprogress= documentChange.document.getString("progress")!!
                                        val managerData=documentChange.document.toObject(CloudData::class.java)

                                        liftingStatus_progress.progress = documentChange.document.getString("progress")!!.toInt()
                                        progressbar_number.text=documentChange.document.getString("progress").plus("%")

                                     val mgt=     managerData.overview as HashMap<String,Any>

                                        lifttype=managerData.type!!

                                        liftprogress_question.text= getString(R.string.continue_from_where_you_started, mgt["title"])

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


        btn_continue.setOnClickListener {


            when( liftprogress){

                "1"->{ Navigation.findNavController(it).navigate(R.id.entryFormoneFragment, null) }
                "10"->{
                    if (lifttype=="home" || lifttype=="apartment" || lifttype=="hotel") Navigation.findNavController(it).navigate(R.id.generalRoomTypeFragment, null)
                    else Navigation.findNavController(it).navigate(R.id.hostelRoomTypeFragment, null)
                }
                "30"->{ Navigation.findNavController(it).navigate(R.id.placeAvailability, null) }
                "40"->{
                    if (lifttype=="home" || lifttype=="apartment") Navigation.findNavController(it).navigate(R.id.generalPriceFragment, null)
                    else if (lifttype=="hotel")  Navigation.findNavController(it).navigate(R.id.hotelGeneralPriceFragment, null)
                    else if (lifttype=="hostel")  Navigation.findNavController(it).navigate(R.id.priceHostelFragment, null)
                }
                "50"->{ Navigation.findNavController(it).navigate(R.id.overviewFragment, null) }
                "60"->{ Navigation.findNavController(it).navigate(R.id.profilePictureFragment, null) }
                "70"->{ Navigation.findNavController(it).navigate(R.id.addPlacePicturesFragment, null) }
                "80"->{ Navigation.findNavController(it).navigate(R.id.amenitiesFragment, null) }
                "95"->{ Navigation.findNavController(it).navigate(R.id.addressFragment, null) }

            }










        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = LiftingStatus()
    }
}
