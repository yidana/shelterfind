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
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.fragment_account_settings.*
import kotlinx.android.synthetic.main.fragment_general_room_type.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.util.*
import kotlin.collections.HashMap


class GeneralRoomTypeFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    lateinit var mFirebaseFirestore: FirebaseFirestore
    val preference_file_key="MYDESTINATION"

   val myKitchen:MyKitchen=MyKitchen()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFirebaseFirestore= FirebaseFirestore.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_general_room_type, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val toolbar = view.findViewById<android.widget.Toolbar>(R.id.my_toolbar) as Toolbar


        toolbar.setNavigationOnClickListener {

            Navigation.findNavController(it).navigate(R.id.entryFormoneFragment, null)

        }

        val progressBar: ProgressBar?=view.findViewById<ProgressBar>(R.id.progressBar)


            myKitchen.SetProgress(progressBar!!,10)





        val buttonnext: FloatingActionButton?= view.findViewById<FloatingActionButton>(R.id.button_next_generalroom)



        buttonnext?.setOnClickListener {



            val dialog = MaterialDialog(this@GeneralRoomTypeFragment.context!!)
                    .title(R.string.room_type_title)
                    .message(R.string.roomtype_progress_report)

            dialog.show()
            bedroom_count_text
            bathroom_count_text

            when{

                bedroom_count_text.text.isEmpty()->{

                    Snackbar.make(view,"Set the Number of Bedrooms",Snackbar.LENGTH_SHORT ).show()


                    return@setOnClickListener
                }
                bathroom_count_text.text.isEmpty()->{
                    Snackbar.make(view,"Set the Number of Bathrooms",Snackbar.LENGTH_SHORT ).show()


                    return@setOnClickListener
                }
            }

            val prefs= activity?.getPreferences(Context.MODE_PRIVATE) ?: return@setOnClickListener


            val destin = prefs.getString(preference_file_key,"none")





            val balcstatus= balcony_switch.isChecked
            val mainhallstatus=main_hall_switch.isChecked


            when((destin!!)){
                "house"->{
                    val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/homes")

                    val bedroom=bedroom_count_text.text.trim().toString().toInt()

                    val bathroom=bathroom_count_text.text.trim().toString().toInt()


                  myCollectionReference.get().addOnCompleteListener {task ->

                      if (task.isSuccessful){

                          val objdb=HashMap<String,Any>()
                          objdb["bedrooms"]=bedroom
                          objdb["bathrooms"]=bathroom
                          objdb["balcony"]=balcstatus
                          objdb["mainhall"]=mainhallstatus

                          val roomdb=HashMap<String,Any>()
                          roomdb["roomtype"] =objdb
                          roomdb["progress"]="10"
                          Log.e("FailureCloud",roomdb.toString())
                          mFirebaseFirestore.collection("/user/facilities/homes")
                                  .document(task.result.last().id)
                                  .set(roomdb, SetOptions.merge())
                                  .addOnSuccessListener {succes->
                                      dialog.dismiss()
                                      Navigation.findNavController(it).navigate(R.id.placeAvailability, null)
                                  }.addOnFailureListener { failure->
                                      dialog.dismiss()
                                      Log.e("FailureCloud",failure.toString())
                                  }


                      }
                  }

                }
                "apartment"->{

                    val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/apartments")

                    val bedroom=bedroom_count_text.text.trim().toString().toInt()

                    val bathroom=bathroom_count_text.text.trim().toString().toInt()


                    myCollectionReference.get().addOnCompleteListener {task ->

                        if (task.isSuccessful){

                            val objdb=HashMap<String,Any>()
                            objdb["bedrooms"]=bedroom
                            objdb["bathrooms"]=bathroom
                            objdb["balcony"]=balcstatus
                            objdb["mainhall"]=mainhallstatus

                            val roomdb=HashMap<String,Any>()
                            roomdb["roomtype"] =objdb
                            roomdb["progress"]="10"
                            Log.e("FailureCloud",roomdb.toString())
                            mFirebaseFirestore.collection("/user/facilities/apartments")
                                    .document(task.result.last().id)
                                    .set(roomdb, SetOptions.merge())
                                    .addOnSuccessListener {succes->
                                        dialog.dismiss()
                                        Navigation.findNavController(it).navigate(R.id.placeAvailability, null)
                                    }.addOnFailureListener { failure->
                                        dialog.dismiss()
                                        Log.e("FailureCloud",failure.toString())
                                    }


                        }
                    }

                }
                "hotel"->{
                    val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/hotels")

                    val bedroom=bedroom_count_text.text.trim().toString().toInt()

                    val bathroom=bathroom_count_text.text.trim().toString().toInt()



                    myCollectionReference.get().addOnCompleteListener {task ->

                        if (task.isSuccessful){

                            val objdb=HashMap<String,Any>()
                            objdb["bedrooms"]=bedroom
                            objdb["bathrooms"]=bathroom
                            objdb["balcony"]=balcstatus
                            objdb["mainhall"]=mainhallstatus
                            val roomdb=HashMap<String,Any>()
                            roomdb["roomtype"] =objdb
                            roomdb["progress"]="10"
                            mFirebaseFirestore.collection("/user/facilities/hotels")
                                    .document(task.result.last().id)
                                    .set(roomdb, SetOptions.merge())
                                    .addOnSuccessListener {succes->
                                        dialog.dismiss()
                                        Navigation.findNavController(it).navigate(R.id.placeAvailability, null)
                                    }.addOnFailureListener { failure->
                                        dialog.dismiss()
                                        Log.e("FailureCloud",failure.toString())
                                    }


                        }
                    }


                }
            }



        }


        var count_bathroom=0
        var count_bedroom=0

        bathroom_add_button.setOnClickListener {

            count_bathroom+=1

            bathroom_count_text.text= count_bathroom.toString()
        }

        bathroom_substract_button.setOnClickListener {
            count_bathroom-=1

            if (count_bathroom<0){
                count_bathroom=0
            }

            bathroom_count_text.text=   count_bathroom.toString()
        }

        bedroom_add_button.setOnClickListener {
            count_bedroom+=1
            bedroom_count_text.text= count_bedroom.toString()
        }

        bedroom_substract_button.setOnClickListener {
            count_bedroom-=1

            if (count_bathroom<0){
                count_bathroom=0
            }
            bedroom_count_text.text= count_bedroom.toString()
        }




    }

    fun ensureRange(value: Int, min: Int, max: Int): Int {
        return Math.min(Math.max(value, min), max)
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
                GeneralRoomTypeFragment()
    }
}
