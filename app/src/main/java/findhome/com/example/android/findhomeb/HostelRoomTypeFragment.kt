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
import kotlinx.android.synthetic.main.fragment_hostel_room_type.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


class HostelRoomTypeFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    val preference_file_key = "MYDESTINATION"
    val myKitchen: MyKitchen = MyKitchen()
    lateinit var mFirebaseFirestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFirebaseFirestore= (activity as MainActivity).mFirebaseFirestore
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hostel_room_type, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        val progressBar: ProgressBar? = view.findViewById<ProgressBar>(R.id.progressBar)


            myKitchen.SetProgress(progressBar!!, 10)




        val buttonnext: FloatingActionButton? = view.findViewById<FloatingActionButton>(R.id.button_next_hostel_roomtype)


        buttonnext?.setOnClickListener {
            val dialog = MaterialDialog(this@HostelRoomTypeFragment.context!!)
                    .title(R.string.room_type_title)
                    .message(R.string.roomtype_progress_report)

            dialog.show()

            when{
                !checkbox_four_in_a_room.isChecked &&
                        !checkbox_three_in_a_room.isChecked &&
                        !checkbox_two_in_a_room.isChecked &&
                        !checkbox_one_in_a_room.isChecked ->{
                    Snackbar.make(
                            view, // Parent view
                            " Choose one of the Room Types to Continue", // Message to show
                            Snackbar.LENGTH_LONG // How long to display the message.
                    ).show()
                }else ->{


                val prefs= activity?.getPreferences(Context.MODE_PRIVATE) ?: return@setOnClickListener


                val destin = prefs.getString(preference_file_key,"none")

                when(destin!!){
                    "hostel"->{

                        when{

                            checkbox_four_in_a_room.isChecked->{

                                with(prefs.edit()) {
                                    putString("4_in_a_room", "true")
                                    apply()
                                }

                            }
                            checkbox_three_in_a_room.isChecked->{

                                with(prefs.edit()) {
                                    putString("3_in_a_room", "true")
                                    apply()
                                }

                            }
                            checkbox_two_in_a_room.isChecked->{

                                with(prefs.edit()) {
                                    putString("2_in_a_room", "true")
                                    apply()
                                }

                            }
                            checkbox_one_in_a_room.isChecked->{

                                with(prefs.edit()) {
                                    putString("1_in_a_room", "true")
                                    apply()
                                }

                            }
                            else->{
                                with(prefs.edit()) {
                                    remove("4_in_a_room")
                                    remove("3_in_a_room")
                                    remove("2_in_a_room")
                                    remove("1_in_a_room")
                                    apply()
                                }
                            }

                        }


                        val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/hostels")
                        myCollectionReference.get().addOnCompleteListener {task ->

                            if (task.isSuccessful){

                                val objdb=HashMap<String,Any?>()
                                objdb["4 in a room"]=checkbox_four_in_a_room.isChecked
                                objdb["3 in a room"]=checkbox_three_in_a_room.isChecked
                                objdb["2 in a room"]=checkbox_two_in_a_room.isChecked
                                objdb["1 in a room"]=checkbox_one_in_a_room.isChecked

                                val roomdb=HashMap<String,Any>()
                                roomdb["roomtype"] =objdb
                                Log.e("FailureCloud",roomdb.toString())
                                mFirebaseFirestore.collection("/user/facilities/hostels")
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
            // TODO: Update argument type and name
            fun onFragmentInteraction(uri: Uri)
        }

        companion object {
            @JvmStatic
            fun newInstance() =
                    HostelRoomTypeFragment()
        }
    }



