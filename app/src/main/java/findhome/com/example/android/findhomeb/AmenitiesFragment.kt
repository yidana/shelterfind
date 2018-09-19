package findhome.com.example.android.findhomeb

import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.support.design.button.MaterialButton
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import findhome.com.example.android.findhomeb.AmenitiesListAdaptor.Companion.myAmenities
import kotlinx.android.synthetic.main.fragment_amenities.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


class AmenitiesFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    val myKitchen:MyKitchen=MyKitchen()
    private var listmyData:ArrayList<String>?=ArrayList()
    lateinit var mFirebaseFirestore: FirebaseFirestore
    val preference_file_key="MYDESTINATION"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        mFirebaseFirestore= FirebaseFirestore.getInstance()
        myAmenities.clear()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_amenities, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val toolbar = view.findViewById<Toolbar>(R.id.my_toolbar) as Toolbar



        toolbar.setNavigationOnClickListener {

            Navigation.findNavController(it).navigate(R.id.addPlacePicturesFragment, null)
        }


        val progressBar: ProgressBar?=view.findViewById<ProgressBar>(R.id.progressBar)

            myKitchen.SetProgress(progressBar!!,80)

        listmyData?.add("Generator")
        listmyData?.add("DSTV")
        listmyData?.add("Wifi")
        listmyData?.add("Heat")
        listmyData?.add("AirConditioning")
        listmyData?.add("Desk/Workspace")
        listmyData?.add("Iron")
        listmyData?.add("Kitchen")
        listmyData?.add("Fridge")
        listmyData?.add("Study Room")
        listmyData?.add("Water Tank")
        listmyData?.add("Shop")
        listmyData?.add("Cleaning Service")
        listmyData?.add("24 Hour Water Supply")
        listmyData?.add("TV Room")
        listmyData?.add("Shuttle Service")
        listmyData?.add("Restuarant")
        listmyData?.add("Basketball Court")
        listmyData?.add("Snooker")
        listmyData?.add("Table Tennis")
        listmyData?.add("Football Pitch")
        listmyData?.add("Disability Friendly")
        listmyData?.add("Gass")
        listmyData?.add("Parking Space")
        listmyData?.add("Essentials(towels,bed sheets,toilet papers, pillows)")
        listmyData?.add("24 Hours Security")
        listmyData?.add("Fire Extinquisher")
        listmyData?.add("CCTV Camera")
        listmyData?.add("First Aid Kit")
        listmyData?.add("Smoke Detector")
        listmyData?.add("Fire Detector")




        val amenitiesListAdaptor:AmenitiesListAdaptor= AmenitiesListAdaptor(this@AmenitiesFragment.context,listmyData!!)

        listview_amen.adapter=amenitiesListAdaptor

        val buttonnext: FloatingActionButton?= view.findViewById(R.id.button_next)

        buttonnext?.setOnClickListener{ button ->


            if (myAmenities.isEmpty()){

                Snackbar.make(view,
                        "Select your Amenities to continue",
                        Snackbar.LENGTH_LONG).show()
            }else{
                val dialog = MaterialDialog(this@AmenitiesFragment.context!!)
                        .title(R.string.room_type_title)
                        .message(R.string.roomtype_progress_report)

                dialog.show()

                val prefs= activity?.getPreferences(Context.MODE_PRIVATE)


                val destin = prefs!!.getString(preference_file_key,"none")


                FirebaseAuth.AuthStateListener { usrID ->


                    when(destin!!.toString()){
                        "house"->{
                            val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/homes/"
                                    +usrID.currentUser!!.uid+"/"+"homes")
                            myCollectionReference.get().addOnCompleteListener {task ->
                                if (task.isSuccessful){
                                    val amenitiesdb=HashMap<String,Any>()
                                    amenitiesdb["amenities"] =myAmenities
                                    amenitiesdb["progress"]="80"
                                    mFirebaseFirestore.collection("/user/facilities/homes/"
                                            +usrID.currentUser!!.uid+"/"+"homes")
                                            .document(task.result.last().id)
                                            .set( amenitiesdb, SetOptions.merge())
                                            .addOnFailureListener { failure->
                                                dialog.dismiss()
                                            }.addOnSuccessListener {
                                                myAmenities.clear()
                                                dialog.dismiss()
                                                Navigation.findNavController(view).navigate(R.id.addressFragment, null)
                                            }
                                }
                            }
                        }
                        "hostel"->{

                            val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/hostels/"
                                    +usrID.currentUser!!.uid+"/"+"hostels")
                            myCollectionReference.get().addOnCompleteListener {task ->
                                if (task.isSuccessful){
                                    val amenitiesdb=HashMap<String,Any>()
                                    amenitiesdb["amenities"] =myAmenities
                                    mFirebaseFirestore.collection("/user/facilities/hostels/"
                                            +usrID.currentUser!!.uid+"/"+"hostels")
                                            .document(task.result.last().id)
                                            .set(amenitiesdb, SetOptions.merge())
                                            .addOnFailureListener { failure->

                                                Log.e("FailureCloud",failure.toString())
                                            }.addOnSuccessListener {
                                                myAmenities.clear()
                                                dialog.dismiss()
                                                Navigation.findNavController(view).navigate(R.id.addressFragment, null)
                                            }
                                }
                            }
                        }
                        "hotel"->{
                            val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/hotels/"
                                    +usrID.currentUser!!.uid+"/"+"hotels")
                            myCollectionReference.get().addOnCompleteListener {task ->
                                if (task.isSuccessful){
                                    val amenitiesdb=HashMap<String,Any>()
                                    amenitiesdb["amenities"] =myAmenities
                                    mFirebaseFirestore.collection("/user/facilities/hotels/"
                                            +usrID.currentUser!!.uid+"/"+"hotels")
                                            .document(task.result.last().id)
                                            .set(amenitiesdb, SetOptions.merge())
                                            .addOnFailureListener { failure->

                                            }.addOnSuccessListener {
                                                myAmenities.clear()
                                                dialog.dismiss()
                                                Navigation.findNavController(view).navigate(R.id.addressFragment, null)
                                            }
                                }
                            }
                        }
                        "apartment"->{
                            val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/apartments/"
                                    +usrID.currentUser!!.uid+"/"+"apartments")
                            myCollectionReference.get().addOnCompleteListener {task ->
                                if (task.isSuccessful){
                                    val amenitiesdb=HashMap<String,Any>()
                                    amenitiesdb["amenities"] =myAmenities
                                    mFirebaseFirestore.collection("/user/facilities/apartments/"
                                            +usrID.currentUser!!.uid+"/"+"apartments")
                                            .document(task.result.last().id)
                                            .set(amenitiesdb, SetOptions.merge())
                                            .addOnFailureListener { failure->

                                            }.addOnSuccessListener {
                                                myAmenities.clear()
                                                dialog.dismiss()
                                                Navigation.findNavController(view).navigate(R.id.addressFragment, null)
                                            }
                                }
                            }
                        }

                    }





                }



               
            }





        }
    }

    // TODO: Rename method, update argument and hook method into UI event
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
                AmenitiesFragment()
    }
}
