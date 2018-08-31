package findhome.com.example.android.findhomeb

import android.content.Context
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.fragment_hotel_general_price.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


class HotelGeneralPriceFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    val myKitchen:MyKitchen=MyKitchen()
    private var hotelPrice:String?=null
    lateinit var mFirebaseFirestore: FirebaseFirestore
    val preference_file_key="MYDESTINATION"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFirebaseFirestore= (activity as MainActivity).mFirebaseFirestore
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hotel_general_price, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val progressBar: ProgressBar?=view.findViewById<ProgressBar>(R.id.progressBar)

            myKitchen.SetProgress(progressBar!!,40)


        val buttonnext: FloatingActionButton?= view.findViewById<FloatingActionButton>(R.id.button_next)

        buttonnext?.setOnClickListener{


            when{

                hotel_price_input.text!!.isEmpty()->{ Snackbar
                        .make(view, "Set The Price to Continue", Snackbar.LENGTH_LONG).show()}
                else->{

                    hotelPrice=hotel_price_input.text.toString()

                }
            }

            val prefs= activity?.getPreferences(Context.MODE_PRIVATE) ?: return@setOnClickListener


            val destin = prefs.getString(preference_file_key,"none")

            when(destin!!){
                "hotel"->{
                    val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/homes")
                    myCollectionReference.get().addOnCompleteListener {task ->

                        if (task.isSuccessful){

                            val objdb=HashMap<String,Any?>()
                            objdb["amount"]=hotelPrice

                            val pricedb=HashMap<String,Any>()
                            pricedb["roomtypeprice"] =objdb
                            Log.e("FailureCloud",pricedb.toString())
                            mFirebaseFirestore.collection("/user/facilities/homes")
                                    .document(task.result.last().id)
                                    .set(pricedb, SetOptions.merge())
                                    .addOnSuccessListener {succes->
                                        Navigation.findNavController(it).navigate(R.id.overviewFragment, null)
                                    }.addOnFailureListener { failure->

                                        Log.e("FailureCloud",failure.toString())
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
                HotelGeneralPriceFragment()
    }
}
