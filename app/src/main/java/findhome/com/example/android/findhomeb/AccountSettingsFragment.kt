package findhome.com.example.android.findhomeb

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.Navigation
import androidx.work.*
import androidx.work.Data
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import findhome.com.example.android.findhomeb.workers.ComUploadSingleImage
import kotlinx.android.synthetic.main.fragment_account_settings.*
import kotlinx.android.synthetic.main.fragment_account_settings.view.*
import java.io.IOException


class AccountSettingsFragment : Fragment() {

    private var check_edit=true
    private val SELECT_PICTURE=2
    private val KEY_IMG_PATH = "IMG_PATH"
    lateinit var mFirebaseFirestore: FirebaseFirestore
    private val USER_PREF_PROFILE="my visible profile"
    lateinit var mImageRef: StorageReference
    lateinit var mFirebaseStorage: FirebaseStorage



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirebaseFirestore = FirebaseFirestore.getInstance()
        mFirebaseStorage = FirebaseStorage.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val connectivityManager= context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo=connectivityManager.activeNetworkInfo


        if (networkInfo.isConnected){




            mFirebaseFirestore.document("/user/userinfo").get().addOnSuccessListener {doc->

                val imgurl=doc.getString("photourl")
                val mname=doc.getString("name")
                val email=doc.getString("email")
                val country=doc.getString("country")
                welcome_text.text="Hello, " + mname

                if (!imgurl!!.isEmpty()){


                    Picasso.get().load( Uri.parse(imgurl)).fit().into(app_bar_image)

                }


                view.profile_name.setText(mname)
                view.profile_email.setText(email)
                view. profile_country.setText(country)




            }





        }

        else{
            mFirebaseFirestore.document("/user/userinfo").get().addOnSuccessListener {doc->


                val mname=doc.getString("name")
                val email=doc.getString("email")
                val country=doc.getString("country")

                welcome_text.text="Hello, " + mname


                view.profile_name.setText(mname)
                view.profile_email.setText(email)
                view. profile_country.setText(country)




            }





        }


        toolbar.setNavigationOnClickListener {

            Navigation.findNavController(it).navigate(R.id.profileFragment, null)
        }



        profile_edit.setOnClickListener {fab->

            var checkstu=0
            if (check_edit){

                profile_edit.setImageDrawable(context!!.getDrawable(R.drawable.ic_save))
                profile_upload_image.visibility=View.VISIBLE
                check_edit=false

                profile_name.isEnabled=true
                profile_country.isEnabled=true


            }


            else{
                profile_upload_image.visibility=View.INVISIBLE
                profile_edit.setImageDrawable(context!!.getDrawable(R.drawable.ic_mode_edit))
                check_edit=true

                profile_name.isEnabled=false
                profile_country.isEnabled=false



                val mconnectivityManager= context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                val mnetworkInfo=mconnectivityManager.activeNetworkInfo


                if (mnetworkInfo.isConnected){

                    if (mimageuri!=null){


                        mFirebaseFirestore.document("/user/userinfo").get().addOnSuccessListener {doc->

                            val imgurl=doc.getString("photourl")

                            if (imgurl!!.isNotEmpty()){

                                mFirebaseStorage.getReferenceFromUrl(imgurl)
                                        .delete().addOnSuccessListener {


                                            Log.e("firebasestorage", "onSuccess: deleted file")


                                            val builder = Data.Builder()
                                            builder.putString(KEY_IMG_PATH,mimageuri.toString() )

                                            val myConstraints = Constraints.Builder()
                                                    .setRequiredNetworkType(NetworkType.CONNECTED)
                                                    .build()


                                            val work: OneTimeWorkRequest =
                                                    OneTimeWorkRequest.Builder(ComUploadSingleImage::class.java)
                                                            .setInputData(builder.build())
                                                            .setConstraints(myConstraints)
                                                            .build()
                                            WorkManager.getInstance().enqueue(work)

                                            WorkManager.getInstance().getStatusById(work.id)
                                                    .observe(this, Observer {workStatus->

                                                        val isWorkFinished = !workStatus?.state!!.isFinished

                                                        if (isWorkFinished){


                                                            mFirebaseFirestore.document("/user/userinfo").get().addOnSuccessListener {doc->

                                                                val mimgurl=doc.getString("photourl")

                                                                Picasso.get().load( Uri.parse(mimgurl)).fit().into(app_bar_image)
                                                            }

                                                        }else{

                                                        }
                                                    })






                                        }
                            }
                            else{


                                val builder = Data.Builder()
                                builder.putString(KEY_IMG_PATH,mimageuri.toString() )

                                val myConstraints = Constraints.Builder()
                                        .setRequiredNetworkType(NetworkType.CONNECTED)
                                        .build()


                                val work: OneTimeWorkRequest =
                                        OneTimeWorkRequest.Builder(ComUploadSingleImage::class.java)
                                                .setInputData(builder.build())
                                                .setConstraints(myConstraints)
                                                .build()
                                WorkManager.getInstance().enqueue(work)

                                WorkManager.getInstance().getStatusById(work.id)
                                        .observe(this, Observer {workStatus->

                                            val isWorkFinished = !workStatus?.state!!.isFinished

                                            if (isWorkFinished){



                                                mFirebaseFirestore.document("/user/userinfo").get().addOnSuccessListener {doc->

                                                    val nimgurl=doc.getString("photourl")

                                                    Picasso.get().load( Uri.parse(nimgurl)).fit().into(app_bar_image)
                                                }

                                            }else{

                                            }
                                        })



                            }


                        }




                    }


                    if (profile_name.text.isNotEmpty() && profile_name.isEnabled==true){

                        val namedb = HashMap<String, Any>()
                        namedb["name"] =profile_name.text.toString()
                        mFirebaseFirestore.document("/user/userinfo")
                                .set(namedb, SetOptions.merge()).addOnSuccessListener {

                                    checkstu=1

                                }
                    }



                    if (profile_country.text.isNotEmpty() && profile_country.isEnabled==true){

                        val countrydb = HashMap<String, Any>()
                        countrydb["country"] =profile_country.text.toString()
                        mFirebaseFirestore.document("/user/userinfo")
                                .set(countrydb, SetOptions.merge())

                        checkstu=2
                    }

                    if (checkstu==2){


                        Snackbar.make(view,"Profile successfully Updated",Snackbar.LENGTH_LONG).show()

                    }




                }
                else{

                    Snackbar.make(view,"Failed to Update profile, Please check your Internet Connection",Snackbar.LENGTH_LONG).show()
                }





            }




        }



        profile_visible_switch.setOnCheckedChangeListener { buttonView, isChecked ->


            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

            with(sharedPref!!.edit()) {
                putBoolean(USER_PREF_PROFILE, isChecked)
                apply()
            }

        }



        profile_upload_image.setOnClickListener{

            val intent= Intent()
            intent.type="image/*"
            intent.action= Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Profile Image"),SELECT_PICTURE)


        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode==SELECT_PICTURE ){

            if (data!=null){

                try {

                    mimageuri =data.data!!

                    Picasso.get().load(mimageuri).fit().into(profile_photo)

                }catch (e: IOException){
                    e.printStackTrace()
                }


            }


        }




    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()

    }



    companion object {
        var  mimageuri:Uri?=null
        @JvmStatic
        fun newInstance() =
                AccountSettingsFragment()
    }
}
