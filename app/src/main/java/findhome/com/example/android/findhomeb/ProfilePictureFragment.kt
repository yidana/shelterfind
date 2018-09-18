package findhome.com.example.android.findhomeb

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.button.MaterialButton
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.support.v7.widget.Toolbar
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import findhome.com.example.android.findhomeb.adaptors.GridImageAdaptor.Companion.imgList
import kotlinx.android.synthetic.main.fragment_profile_picture.*
import java.io.ByteArrayOutputStream
import java.io.IOException

class ProfilePictureFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    val SELECT_PICTURE=1
    lateinit var mFirebaseStorage:FirebaseStorage
    lateinit var mStorageRef:StorageReference
    lateinit var mImageRef:StorageReference
    val preference_file_key="MYDESTINATION"
    lateinit var mFirebaseFirestore: FirebaseFirestore

    val myKitchen:MyKitchen=MyKitchen()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirebaseStorage= FirebaseStorage.getInstance()
        mStorageRef=mFirebaseStorage.reference
        imgList.clear()
        mFirebaseFirestore= FirebaseFirestore.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_picture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val progressBar: ProgressBar?=view.findViewById<ProgressBar>(R.id.progressBar)

        val uploadPhoto:MaterialButton=view.findViewById<MaterialButton>(R.id.addphoto)

        val toolbar = view.findViewById<Toolbar>(R.id.my_toolbar) as Toolbar

        toolbar.setNavigationOnClickListener {

            Navigation.findNavController(it).navigate(R.id.overviewFragment, null)
        }

        uploadPhoto.setOnClickListener {



                val intent= Intent()
                intent.type="image/*"
                intent.action= Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Image"),SELECT_PICTURE)




        }




            myKitchen.SetProgress(progressBar!!,60)



        val buttonnext: FloatingActionButton?= view.findViewById<FloatingActionButton>(R.id.button_next)

        buttonnext?.setOnClickListener{

            when (imageuri) {
                null -> {
                    Snackbar
                            .make(view, "Choose a caption photo to Continue", Snackbar.LENGTH_LONG).show()
                }
                else -> {






                    Upload(imageuri!!,this@ProfilePictureFragment.context!!)


                }
            }


        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode==SELECT_PICTURE ){
            Log.e("ACTIVITYRESULT", "onActivityResult on Activity A: CODE")


            if (data!=null){

                try {

                    imageuri=data.data!!

                    Picasso.get().load(imageuri).fit().into(profile_pic)

                }catch (e:IOException){
                    e.printStackTrace()
                }


            }


        }


    }




  fun Upload(uri: Uri,context: Context){
      val dialog = MaterialDialog(this@ProfilePictureFragment.context!!)
              .title(R.string.img_upload_title)
              .message(R.string.img_progress_report)

      dialog.show()
        val uriPath=uri.lastPathSegment
        mImageRef = mStorageRef.child("caption photos/$uriPath")

        val compressedImg=compressImage(uri, context)



        mImageRef.putBytes(compressedImg).addOnProgressListener {

        } .addOnProgressListener { taskSnapshot ->
            val sessionUri=  taskSnapshot.uploadSessionUri

            val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
            resources.getString(R.string.img_progress_report,progress.toString())
            if (progress.toInt()==100){


            }

        }
                .addOnCompleteListener {taskSnapshot->


                    val prefs= activity?.getPreferences(Context.MODE_PRIVATE)


                    val destin = prefs!!.getString(preference_file_key,"none")

                    mImageRef.downloadUrl.addOnSuccessListener { uri ->


                        val duri=uri.toString()

                        Log.v("DURIN",duri)

                        when(destin!!.toString()){
                            "house"->{
                                val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/homes")
                                myCollectionReference.get().addOnCompleteListener {task ->
                                    if (task.isSuccessful){
                                        Log.v("pVGF","Good")
                                        val photosdb=HashMap<String,Any>()
                                        photosdb["captionurl"]= duri
                                        photosdb["progress"]="60"
                                        mFirebaseFirestore.collection("/user/facilities/homes")
                                                .document(task.result.last().id)
                                                .set( photosdb, SetOptions.merge())
                                                .addOnFailureListener { failure->
                                                    Log.v("STURI", "FAIl")
                                                }.addOnSuccessListener {
                                                    Log.v("STURI", "SUCCESS")

                                                    dialog.dismiss()

                                                    Navigation.findNavController(this@ProfilePictureFragment.view!!)
                                                            .navigate(R.id.addPlacePicturesFragment, null)
                                                }
                                    }
                                }
                            }
                            "hostel"->{

                                val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/hostels")
                                myCollectionReference.get().addOnCompleteListener {task ->
                                    if (task.isSuccessful){
                                        val photosdb=HashMap<String,Any>()
                                        photosdb["captionurl"] =duri
                                        photosdb["progress"]="60"
                                        mFirebaseFirestore.collection("/user/facilities/hostels")
                                                .document(task.result.last().id)
                                                .set(photosdb, SetOptions.merge())
                                                .addOnFailureListener { failure->

                                                    Log.e("FailureCloud",failure.toString())
                                                }.addOnSuccessListener {

                                                    dialog.dismiss()

                                                    Navigation.findNavController(this@ProfilePictureFragment.view!!)
                                                            .navigate(R.id.addPlacePicturesFragment, null)
                                                }
                                    }
                                }
                            }
                            "hotel"->{
                                val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/hotels")
                                myCollectionReference.get().addOnCompleteListener {task ->
                                    if (task.isSuccessful){
                                        val photosdb=HashMap<String,Any>()
                                        photosdb["captionurl"] =duri
                                        photosdb["progress"]="60"
                                        mFirebaseFirestore.collection("/user/facilities/hotels")
                                                .document(task.result.last().id)
                                                .set(photosdb, SetOptions.merge())
                                                .addOnFailureListener { failure->

                                                }.addOnSuccessListener {
                                                    dialog.dismiss()

                                                    Navigation.findNavController(this@ProfilePictureFragment.view!!)
                                                            .navigate(R.id.addPlacePicturesFragment, null)
                                                }
                                    }
                                }
                            }
                            "apartment"->{
                                val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/apartments")
                                myCollectionReference.get().addOnCompleteListener {task ->
                                    if (task.isSuccessful){
                                        val photosdb=HashMap<String,Any>()
                                        photosdb["captionurl"] = duri
                                        photosdb["progress"]="60"
                                        mFirebaseFirestore.collection("/user/facilities/apartments")
                                                .document(task.result.last().id)
                                                .set(photosdb, SetOptions.merge())
                                                .addOnFailureListener { failure->

                                                }.addOnSuccessListener {
                                                    dialog.dismiss()

                                                    Navigation.findNavController(this@ProfilePictureFragment.view!!)
                                                            .navigate(R.id.addPlacePicturesFragment, null)
                                                }
                                    }
                                }
                            }

                        }


                    }



                }


    }




     fun compressImage(uri:Uri, context: Context):ByteArray{

        val bitmap:Bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        val imgdata:ByteArray=   baos.toByteArray()

        return imgdata
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
                ProfilePictureFragment()

        var  imageuri:Uri?=null
    }


}
