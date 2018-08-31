package findhome.com.example.android.findhomeb

import android.content.Context
import android.net.Uri
import android.os.Bundle
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.fragment_hostelprice.*
import kotlinx.android.synthetic.main.fragment_hostelprice.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


class PriceHostelFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    val myKitchen:MyKitchen=MyKitchen()

    lateinit var mFirebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        mFirebaseFirestore= (activity as MainActivity).mFirebaseFirestore

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
       val view=inflater.inflate(R.layout.fragment_hostelprice, container, false)


        val prefs= activity?.getPreferences(Context.MODE_PRIVATE)

        val fourinroom:String?=prefs!!.getString("4_in_a_room","none")
        val threeinroom:String?=prefs.getString("3_in_a_room","none")
        val twoinroom:String?=prefs.getString("2_in_a_room","none")
        val oneinroom:String?=prefs.getString("1_in_a_room","none")



        when{
            fourinroom!="none"->{
                view.ln_4.visibility=View.VISIBLE
                view.amount_4_in_a_room.isEnabled=true
                view.peroid_4_in_a_room.isEnabled=true




            }
            threeinroom!="none"->{

                view.ln_3.visibility=View.VISIBLE
                view. amount_3_in_a_room.isEnabled=true
                view.  peroid_3_in_a_room.isEnabled=true

            }
            twoinroom!="none"->{
                view.ln_2.visibility=View.VISIBLE
                view.amount_2_in_a_room.isEnabled=true
                view.peroid_2_in_a_room.isEnabled=true

            }
            oneinroom!="none"->{

                view.ln_1.visibility=View.VISIBLE
                view.amount_1_in_a_room.isEnabled=true
                view.peroid_1_in_a_room.isEnabled=true

            }
        }




        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mycheck_roomtype_money:HashMap<String,Int>?=HashMap()
        val mycheck_roomtype_peroid:HashMap<String,String>?=HashMap()

        val progressBar: ProgressBar?=view.findViewById<ProgressBar>(R.id.progressBar)


            myKitchen.SetProgress(progressBar!!,40)



        val buttonnext: FloatingActionButton?= view.findViewById<FloatingActionButton>(R.id.button_next)




        buttonnext?.setOnClickListener{
            val dialog = MaterialDialog(this@PriceHostelFragment.context!!)
                    .title(R.string.room_type_title)
                    .message(R.string.roomtype_progress_report)

            dialog.show()
            when {
                amount_4_in_a_room.text.isEmpty() &&
                        amount_3_in_a_room.text.isEmpty() &&
                        amount_2_in_a_room.text.isEmpty() &&
                        amount_1_in_a_room.text.isEmpty() -> {
                    Snackbar.make(
                            view, // Parent view
                            " Set the general price to Continue", // Message to show
                            Snackbar.LENGTH_LONG // How long to display the message.
                    ).show()

                }
                else->{
                val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/hostels")

                        when{
                    ln_4.visibility==View.VISIBLE->{

                        myCollectionReference.get().addOnCompleteListener { task ->

                            if (task.isSuccessful) {
                                val objdb=HashMap<String,Any?>()
                                objdb["4 in a room"]=amount_4_in_a_room.text.toString().toInt()

                                val roomdb=HashMap<String,Any>()
                                roomdb["roomtypeprice"] =objdb
                                Log.e("FailureCloud",roomdb.toString())
                                mFirebaseFirestore.collection("/user/facilities/hostels")
                                        .document(task.result.last().id)
                                        .set(roomdb, SetOptions.merge())
                                        .addOnSuccessListener {succes->

                                        }.addOnFailureListener { failure->

                                            Log.e("FailureCloud",failure.toString())
                                        }

                            }


                        }

                    }
                   ln_3.visibility==View.VISIBLE->{

                       myCollectionReference.get().addOnCompleteListener { task ->

                           if (task.isSuccessful) {
                               val objdb=HashMap<String,Any?>()
                               objdb["3 in a room"]=amount_3_in_a_room.text.toString().toInt()

                               val roomdb=HashMap<String,Any>()
                               roomdb["roomtypeprice"] =objdb
                               Log.e("FailureCloud",roomdb.toString())
                               mFirebaseFirestore.collection("/user/facilities/hostels")
                                       .document(task.result.last().id)
                                       .set(roomdb, SetOptions.merge())
                                       .addOnSuccessListener {succes->

                                       }.addOnFailureListener { failure->

                                           Log.e("FailureCloud",failure.toString())
                                       }

                           }


                       }



                   }
                   ln_2.visibility==View.VISIBLE->{

                       myCollectionReference.get().addOnCompleteListener { task ->

                                    if (task.isSuccessful) {
                                        val objdb=HashMap<String,Any?>()
                                        objdb["2 in a room"]=amount_2_in_a_room.text.toString().toInt()

                                        val roomdb=HashMap<String,Any>()
                                        roomdb["roomtypeprice"] =objdb
                                        Log.e("FailureCloud",roomdb.toString())
                                        mFirebaseFirestore.collection("/user/facilities/hostels")
                                                .document(task.result.last().id)
                                                .set(roomdb, SetOptions.merge())
                                                .addOnSuccessListener {succes->

                                                }.addOnFailureListener { failure->

                                                    Log.e("FailureCloud",failure.toString())
                                                }

                                    }


                                }

                            }
                   ln_1.visibility==View.VISIBLE->{

                       myCollectionReference.get().addOnCompleteListener { task ->

                                    if (task.isSuccessful) {
                                        val objdb=HashMap<String,Any?>()
                                        objdb["1 in a room"]=amount_1_in_a_room.text.toString().toInt()

                                        val roomdb=HashMap<String,Any>()
                                        roomdb["roomtypeprice"] =objdb
                                        Log.e("FailureCloud",roomdb.toString())
                                        mFirebaseFirestore.collection("/user/facilities/hostels")
                                                .document(task.result.last().id)
                                                .set(roomdb, SetOptions.merge())
                                                .addOnSuccessListener {succes->

                                                }.addOnFailureListener { failure->

                                                    Log.e("FailureCloud",failure.toString())
                                                }

                                    }


                                }



                            }
                }


                    Navigation.findNavController(it).navigate(R.id.overviewFragment, null)


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
                PriceHostelFragment()
    }
}
