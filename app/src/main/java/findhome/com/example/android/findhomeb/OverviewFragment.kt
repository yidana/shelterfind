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
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


class OverviewFragment : Fragment() {
    lateinit var mFirebaseFirestore: FirebaseFirestore
    private var listener: OnFragmentInteractionListener? = null
    private var mtitle:String?= null
    private var mdescription:String?= null
    private var mphonenumber:String?= null
    val myKitchen:MyKitchen=MyKitchen()

    val preference_file_key="MYDESTINATION"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFirebaseFirestore= (activity as MainActivity).mFirebaseFirestore
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val progressBar: ProgressBar?=view.findViewById<ProgressBar>(R.id.progressBar)


            myKitchen.SetProgress(progressBar!!,50)




        val prefs= activity?.getPreferences(Context.MODE_PRIVATE) ?: return


        val destin = prefs.getString(preference_file_key,"none")

        val buttonnext: FloatingActionButton?= view.findViewById<FloatingActionButton>(R.id.button_next)

        buttonnext?.setOnClickListener{
            val dialog = MaterialDialog(this@OverviewFragment.context!!)
                    .title(R.string.room_type_title)
                    .message(R.string.roomtype_progress_report)

            dialog.show()

            when{
                title.text!!.isEmpty()->{
                    Snackbar
                        .make(view, "Title Should not be empty",Snackbar.LENGTH_LONG).show()

                }
                description.text!!.isEmpty()->{ Snackbar
                        .make(view, "Description Should not be empty",Snackbar.LENGTH_LONG).show()  }
                phonenumber.text!!.isEmpty()->{ Snackbar
                        .make(view, "Phone Number Should not be empty",Snackbar.LENGTH_LONG).show()}

                else->{
                    mtitle=title.text.toString()
                    mphonenumber=phonenumber.text.toString()
                    mdescription=description.text.toString()

                    when((destin!!)){
                        "house"->{
                            val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/homes")
                            myCollectionReference.get().addOnCompleteListener {task ->

                                if (task.isSuccessful){

                                    val objdb=HashMap<String,Any?>()
                                    objdb["title"]=mtitle
                                    objdb["description"]= mdescription
                                    objdb["phonenumber"]=mphonenumber

                                    val overviewdb=HashMap<String,Any>()
                                    overviewdb["overview"] =objdb
                                    Log.e("FailureCloud",overviewdb.toString())
                                    mFirebaseFirestore.collection("/user/facilities/homes")
                                            .document(task.result.last().id)
                                            .set(overviewdb, SetOptions.merge())
                                            .addOnSuccessListener {succes->
                                                dialog.dismiss()
                                                Navigation.findNavController(it).navigate(R.id.profilePictureFragment, null)
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


                                    val objdb=HashMap<String,Any?>()
                                    objdb["title"]=mtitle
                                    objdb["description"]= mdescription
                                    objdb["phonenumber"]=mphonenumber

                                    val overviewdb=HashMap<String,Any>()
                                    overviewdb["overview"] =objdb
                                    Log.e("FailureCloud",overviewdb.toString())
                                    mFirebaseFirestore.collection("/user/facilities/apartments")
                                            .document(task.result.last().id)
                                            .set(overviewdb, SetOptions.merge())
                                            .addOnSuccessListener {succes->
                                                Navigation.findNavController(it).navigate(R.id.profilePictureFragment, null)
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


                                    val objdb=HashMap<String,Any?>()
                                    objdb["title"]=mtitle
                                    objdb["description"]= mdescription
                                    objdb["phonenumber"]=mphonenumber

                                    val overviewdb=HashMap<String,Any>()
                                    overviewdb["overview"] =objdb
                                    Log.e("FailureCloud",overviewdb.toString())
                                    mFirebaseFirestore.collection("/user/facilities/hotels")
                                            .document(task.result.last().id)
                                            .set(overviewdb, SetOptions.merge())
                                            .addOnSuccessListener {succes->
                                                Navigation.findNavController(it).navigate(R.id.profilePictureFragment, null)
                                            }.addOnFailureListener { failure->

                                                Log.e("FailureCloud",failure.toString())
                                            }


                                }
                            }


                        }
                        "hostel"->{
                            val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/hostels")

                            myCollectionReference.get().addOnCompleteListener {task ->

                                if (task.isSuccessful){


                                    val objdb=HashMap<String,Any?>()
                                    objdb["title"]=mtitle
                                    objdb["description"]= mdescription
                                    objdb["phonenumber"]=mphonenumber

                                    val overviewdb=HashMap<String,Any>()
                                    overviewdb["overview"] =objdb
                                    Log.e("FailureCloud",overviewdb.toString())
                                    mFirebaseFirestore.collection("/user/facilities/hostels")
                                            .document(task.result.last().id)
                                            .set(overviewdb, SetOptions.merge())
                                            .addOnSuccessListener {succes->
                                                Navigation.findNavController(it).navigate(R.id.profilePictureFragment, null)
                                            }.addOnFailureListener { failure->

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
                OverviewFragment()
    }
}
