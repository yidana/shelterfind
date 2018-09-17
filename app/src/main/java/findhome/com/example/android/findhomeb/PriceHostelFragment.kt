package findhome.com.example.android.findhomeb

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.fragment_hostelprice.*
import kotlinx.android.synthetic.main.fragment_hostelprice.view.*
import kotlinx.android.synthetic.main.price_per_night_layout.*
import kotlinx.android.synthetic.main.price_per_night_layout.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


class PriceHostelFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    val myKitchen:MyKitchen=MyKitchen()

    lateinit var mFirebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        mFirebaseFirestore= FirebaseFirestore.getInstance()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
       val view=inflater.inflate(R.layout.fragment_hostelprice, container, false)


        val prefs= activity?.getPreferences(Context.MODE_PRIVATE)

        val mixed_dorm:String?=prefs!!.getString("mixed_dorm","none")
        val family_dorm:String? =prefs.getString("family_room","none")
        val male_dorm:String?   =prefs.getString("male_dorm","none")
        val femal_dorm:String?   =prefs.getString("female_dorm","none")
        val four_bed:String?=prefs.getString("four_bed","none")
        val three_bed:String? =prefs.getString("three_bed","none")
        val double_bed:String?   =prefs.getString("double_bed","none")
        val single_bed:String?   =prefs.getString("single_bed","none")


        if(single_bed != "none") view.single_room_price.visibility=View.VISIBLE
        if(double_bed != "none") view.double_room_price.visibility=View.VISIBLE
        if(three_bed != "none") view.three_room_price.visibility=View.VISIBLE
        if(four_bed != "none") view.four_room_price.visibility=View.VISIBLE
        if(mixed_dorm != "none") view.mixed_room_price.visibility=View.VISIBLE
        if(femal_dorm != "none") view.femal_room_price.visibility=View.VISIBLE
        if(male_dorm != "none") view.male_room_price.visibility=View.VISIBLE
        if(family_dorm != "none") view.family_room_price.visibility=View.VISIBLE



        view. tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

          }

            override fun onTabSelected(tab: TabLayout.Tab?) {

                when{
                    tab!!.position==0->{

                        Log.v("GHT","clicked 0")
                        view.  peroid_lt.visibility=View.INVISIBLE
                        view.peroid_lt.visibility=View.VISIBLE

                    }
                    tab.position==1->{
                        Log.v("GHT","clicked 1")
                        view. per_night_lt.visibility=View.INVISIBLE

                        view.  peroid_lt.visibility=View.VISIBLE
                    }



                }

            }


        })




        when{
            mixed_dorm!="none"->{

                if (view.per_night_lt.visibility==View.VISIBLE){

                    view.mixed_room_price.visibility=View.VISIBLE

                }
            }
            family_dorm!="none"->{
                if (view.per_night_lt.visibility==View.VISIBLE){

                    view.family_room_price.visibility=View.VISIBLE

                }

            }
            male_dorm!="none"->{

                if (view.per_night_lt.visibility==View.VISIBLE){

                    view.male_room_price.visibility=View.VISIBLE

                }
            }
            femal_dorm!="none"->{

                if (view.per_night_lt.visibility==View.VISIBLE){

                    view.femal_room_price.visibility=View.VISIBLE

                }
            }
            four_bed!="none"->{

                if (view.per_night_lt.visibility==View.VISIBLE){

                    view. four_room_price.visibility=View.VISIBLE

                }
            }
            three_bed!="none"->{

                if (view.per_night_lt.visibility==View.VISIBLE){

                    view.three_room_price.visibility=View.VISIBLE

                }

            }
            double_bed!="none"->{

                if (view.per_night_lt.visibility==View.VISIBLE){

                    view.double_room_price.visibility=View.VISIBLE

                }
            }
            single_bed!="none"->{

                if (view.per_night_lt.visibility==View.VISIBLE){

                    view.single_room_price.visibility=View.VISIBLE

                }

            }
            else->{
                view.single_room_price.visibility=View.GONE
                view.double_room_price.visibility=View.GONE
                view.three_room_price.visibility=View.GONE
                view.four_room_price.visibility=View.GONE
                view.male_room_price.visibility=View.GONE
                view.femal_room_price.visibility=View.GONE
                view.family_room_price.visibility=View.GONE
                view.mixed_room_price.visibility=View.GONE



            }
        }




        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        val toolbar = view.findViewById<android.widget.Toolbar>(R.id.my_toolbar) as android.widget.Toolbar
        toolbar.setNavigationOnClickListener {

            Navigation.findNavController(it).navigate(R.id.placeAvailability, null)
        }


        val mycheck_roomtype_money:HashMap<String,Int>?=HashMap()
        val mycheck_roomtype_peroid:HashMap<String,String>?=HashMap()

        val progressBar: ProgressBar?=view.findViewById<ProgressBar>(R.id.progressBar)


            myKitchen.SetProgress(progressBar!!,40)



        val buttonnext: FloatingActionButton?= view.findViewById<FloatingActionButton>(R.id.button_next)




        buttonnext?.setOnClickListener{
            val dialog = MaterialDialog(this@PriceHostelFragment.context!!)
                    .title(R.string.room_type_title)
                    .message(R.string.roomtype_progress_report)

//            dialog.show()
//            when {
//                    amount_4_in_a_room.text.isEmpty() &&
//                        amount_3_in_a_room.text.isEmpty() &&
//                        amount_2_in_a_room.text.isEmpty() &&
//                        amount_1_in_a_room.text.isEmpty() -> {
//                    Snackbar.make(
//                            view, // Parent view
//                            " Set the general price to Continue", // Message to show
//                            Snackbar.LENGTH_LONG // How long to display the message.
//                    ).show()
//
//                }
//                else->{
//                val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/hostels")
//
//                        when{
//                    ln_4.visibility==View.VISIBLE->{
//
//                        myCollectionReference.get().addOnCompleteListener { task ->
//
//                            if (task.isSuccessful) {
//                                val objdb=HashMap<String,Any?>()
//                                objdb["4 in a room"]=amount_4_in_a_room.text.toString().toInt()
//
//                                val roomdb=HashMap<String,Any>()
//                                roomdb["roomtypeprice"] =objdb
//                                Log.e("FailureCloud",roomdb.toString())
//                                mFirebaseFirestore.collection("/user/facilities/hostels")
//                                        .document(task.result.last().id)
//                                        .set(roomdb, SetOptions.merge())
//                                        .addOnSuccessListener {succes->
//
//                                        }.addOnFailureListener { failure->
//
//                                            Log.e("FailureCloud",failure.toString())
//                                        }
//
//                            }
//
//
//                        }
//
//                    }
//                   ln_3.visibility==View.VISIBLE->{
//
//                       myCollectionReference.get().addOnCompleteListener { task ->
//
//                           if (task.isSuccessful) {
//                               val objdb=HashMap<String,Any?>()
//                               objdb["3 in a room"]=amount_3_in_a_room.text.toString().toInt()
//
//                               val roomdb=HashMap<String,Any>()
//                               roomdb["roomtypeprice"] =objdb
//                               Log.e("FailureCloud",roomdb.toString())
//                               mFirebaseFirestore.collection("/user/facilities/hostels")
//                                       .document(task.result.last().id)
//                                       .set(roomdb, SetOptions.merge())
//                                       .addOnSuccessListener {succes->
//
//                                       }.addOnFailureListener { failure->
//
//                                           Log.e("FailureCloud",failure.toString())
//                                       }
//
//                           }
//
//
//                       }
//
//
//
//                   }
//                   ln_2.visibility==View.VISIBLE->{
//
//                       myCollectionReference.get().addOnCompleteListener { task ->
//
//                                    if (task.isSuccessful) {
//                                        val objdb=HashMap<String,Any?>()
//                                        objdb["2 in a room"]=amount_2_in_a_room.text.toString().toInt()
//
//                                        val roomdb=HashMap<String,Any>()
//                                        roomdb["roomtypeprice"] =objdb
//                                        Log.e("FailureCloud",roomdb.toString())
//                                        mFirebaseFirestore.collection("/user/facilities/hostels")
//                                                .document(task.result.last().id)
//                                                .set(roomdb, SetOptions.merge())
//                                                .addOnSuccessListener {succes->
//
//                                                }.addOnFailureListener { failure->
//
//                                                    Log.e("FailureCloud",failure.toString())
//                                                }
//
//                                    }
//
//
//                                }
//
//                            }
//                   ln_1.visibility==View.VISIBLE->{
//
//                       myCollectionReference.get().addOnCompleteListener { task ->
//
//                                    if (task.isSuccessful) {
//                                        val objdb=HashMap<String,Any?>()
//                                        objdb["1 in a room"]=amount_1_in_a_room.text.toString().toInt()
//
//                                        val roomdb=HashMap<String,Any>()
//                                        roomdb["roomtypeprice"] =objdb
//                                        Log.e("FailureCloud",roomdb.toString())
//                                        mFirebaseFirestore.collection("/user/facilities/hostels")
//                                                .document(task.result.last().id)
//                                                .set(roomdb, SetOptions.merge())
//                                                .addOnSuccessListener {succes->
//
//                                                }.addOnFailureListener { failure->
//
//                                                    Log.e("FailureCloud",failure.toString())
//                                                }
//
//                                    }
//
//
//                                }
//
//
//
//                            }
//                }
//
//
//                    Navigation.findNavController(it).navigate(R.id.overviewFragment, null)
//
//
//            }

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
                PriceHostelFragment()
    }
}
