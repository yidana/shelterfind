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
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.fragment_general_price.*
import kotlinx.android.synthetic.main.fragment_general_price.view.*
import kotlinx.android.synthetic.main.fragment_general_room_type.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


class GeneralPriceFragment : Fragment() {

    val preference_file_key="MYDESTINATION"
    lateinit var mFirebaseFirestore: FirebaseFirestore
    private var listener: OnFragmentInteractionListener? = null
    val myKitchen:MyKitchen=MyKitchen()
    var dateChoose:String="Always"




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        mFirebaseFirestore=  (activity as MainActivity).mFirebaseFirestore

        val prefs= activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val destin = prefs.getString(preference_file_key,"none")
        val dateList= (activity as MainActivity).peroidavailable

        dateChoose = dateList?.toString() ?: "Always"
        when((destin!!)){
            "house"->{
                val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/homes")


                myCollectionReference.get().addOnCompleteListener {task ->

                    if (task.isSuccessful){


                        val roomavailabiliydb=HashMap<String,String>()
                        roomavailabiliydb["roomavailability"] =dateChoose
                        Log.e("FailureCloud",roomavailabiliydb.toString())
                        mFirebaseFirestore.collection("/user/facilities/homes")
                                .document(task.result.last().id)
                                .set(roomavailabiliydb as Map<String, Any>, SetOptions.merge())
                                .addOnSuccessListener {succes->

                                }.addOnFailureListener { failure->

                                    Log.e("FailureCloud",failure.toString())
                                }


                    }
                }

            }
            "apartment"->{

                val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/apartments")



                myCollectionReference.get().addOnCompleteListener {task ->

                    if (task.isSuccessful){

                        val roomavailabiliydb=HashMap<String,String>()
                        roomavailabiliydb["roomavailability"] =dateChoose

                        mFirebaseFirestore.collection("/user/facilities/apartments")
                                .document(task.result.last().id)
                                .set(roomavailabiliydb as Map<String, Any>, SetOptions.merge())
                                .addOnSuccessListener {succes->
                                }.addOnFailureListener { failure->

                                    Log.e("FailureCloud",failure.toString())
                                }


                    }
                }

            }
            "hotel"->{
                val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/hotels")


                myCollectionReference.get().addOnCompleteListener {task ->

                    if (task.isSuccessful){


                        val roomavailabiliydb=HashMap<String,String>()
                        roomavailabiliydb["roomavailability"] =dateChoose
                        mFirebaseFirestore.collection("/user/facilities/hotels")
                                .document(task.result.last().id)
                                .set(roomavailabiliydb as Map<String, Any>, SetOptions.merge())
                                .addOnSuccessListener {succes->
                                }.addOnFailureListener { failure->

                                    Log.e("FailureCloud",failure.toString())
                                }


                    }
                }


            }
        }




    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_general_price, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        val toolbar = view.findViewById<android.widget.Toolbar>(R.id.my_toolbar) as android.widget.Toolbar


     toolbar.setNavigationOnClickListener {

            Navigation.findNavController(it).navigate(R.id.placeAvailability, null)
        }


        val progressBar: ProgressBar?=view.findViewById<ProgressBar>(R.id.progressBar)


            myKitchen.SetProgress(progressBar!!,40)



        val buttonnext:FloatingActionButton?= view.findViewById<FloatingActionButton>(R.id.button_next)




        val prefs= activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val destin = prefs.getString(preference_file_key,"none")


        buttonnext?.setOnClickListener{

            val dialog = MaterialDialog(this@GeneralPriceFragment.context!!)
                    .title(R.string.room_type_title)
                    .message(R.string.roomtype_progress_report)

            dialog.show()

            when{
                general_price.text!!.isEmpty()->{
                    Snackbar
                            .make(view, "Set The Price to Continue", Snackbar.LENGTH_LONG).show()
                }

                else->{
                    when((destin!!)){
                        "house"->{
                            val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/homes")


                            myCollectionReference.get().addOnCompleteListener {task ->

                                if (task.isSuccessful){


                                    val objdb=HashMap<String,Any>()
                                    objdb["amount"]=general_price.text.toString().toInt()
                                    objdb["peroid"]=peroid_spinner.selectedItem.toString()

                                    val roompricedb=HashMap<String,Any>()
                                    roompricedb["roomtypeprice"] =objdb
                                    roompricedb["progress"]="40"
                                    mFirebaseFirestore.collection("/user/facilities/homes")
                                            .document(task.result.last().id)
                                            .set(roompricedb, SetOptions.merge())
                                            .addOnSuccessListener {succes->
                                                dialog.dismiss()
                                                Navigation.findNavController(it).navigate(R.id.overviewFragment, null)
                                            }.addOnFailureListener { failure->
                                                dialog.dismiss()
                                                Log.e("FailureCloud",failure.toString())
                                            }


                                }
                            }

                        }
                        "apartment"->{

                            val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/apartments")



                            myCollectionReference.get().addOnCompleteListener {task ->

                                if (task.isSuccessful){

                                    val objdb=HashMap<String,Any>()
                                    objdb["amount"]=general_price.text.toString().toInt()
                                    objdb["peroid"]=peroid_spinner.selectedItem.toString()

                                    val roompricedb=HashMap<String,Any>()
                                    roompricedb["roomtypeprice"] = objdb
                                    roompricedb["progress"]="40"
                                    mFirebaseFirestore.collection("/user/facilities/apartments")
                                            .document(task.result.last().id)
                                            .set(roompricedb, SetOptions.merge())
                                            .addOnSuccessListener {succes->
                                                dialog.dismiss()
                                                Navigation.findNavController(it).navigate(R.id.overviewFragment, null)
                                            }.addOnFailureListener { failure->
                                                dialog.dismiss()
                                                Log.e("FailureCloud",failure.toString())
                                            }


                                }
                            }

                        }
                    }
                }
            }

        }
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
                GeneralPriceFragment()
    }
}
