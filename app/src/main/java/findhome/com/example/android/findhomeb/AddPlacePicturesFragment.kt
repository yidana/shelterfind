package findhome.com.example.android.findhomeb

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.support.v7.widget.Toolbar
import androidx.navigation.Navigation
import androidx.work.*
import androidx.work.Data
import kotlinx.android.synthetic.main.fragment_add_place_pictures.*
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import findhome.com.example.android.findhomeb.adaptors.GridImageAdaptor
import findhome.com.example.android.findhomeb.adaptors.GridImageAdaptor.Companion.imgList
import findhome.com.example.android.findhomeb.workers.CompressAndUploadWorker


class AddPlacePicturesFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private val SELECTPICTURES=2
    val myKitchen:MyKitchen=MyKitchen()
    lateinit var mFirebaseStorage: FirebaseStorage
    lateinit var mStorageRef: StorageReference
    lateinit var mImageRef: StorageReference
    lateinit var imagesAdapter: GridImageAdaptor
    lateinit var mFirebaseFirestore: FirebaseFirestore
    val preference_file_key="MYDESTINATION"
    val KEY_IMG_PATH = "IMG_PATH"
    val PHOTO_URL="MYURLS"
    val USERIDPH="id for upload"

    val REQUEST_LOCATION_PERMISSION = 200


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFirebaseFirestore=(activity as MainActivity).mFirebaseFirestore
        mFirebaseStorage= FirebaseStorage.getInstance()
        imgList.clear()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_place_pictures, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressBar: ProgressBar?=view.findViewById<ProgressBar>(R.id.progressBar)


        myKitchen.SetProgress(progressBar!!,70)



        val toolbar = view.findViewById<Toolbar>(R.id.my_toolbar) as Toolbar

        toolbar.setNavigationOnClickListener {

            Navigation.findNavController(it).navigate(R.id.profilePictureFragment, null)
        }



        if (ActivityCompat.checkSelfPermission(this.context!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED   ) {

            ActivityCompat.requestPermissions(this.activity!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_LOCATION_PERMISSION)
            ActivityCompat.requestPermissions(this.activity!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_LOCATION_PERMISSION)
        } else {
            Log.e("DB", "PERMISSION NOT GRANTED")
        }



        val buttonnext: FloatingActionButton?= view.findViewById(R.id.button_next)

        buttonnext?.setOnClickListener{ it ->
            myPuturls.clear()

            if (imgList.isEmpty()) {

                Snackbar.make(this.view!!, "Add Images for upload to continue",
                        Snackbar.LENGTH_LONG)
                        .show()


            }else {
                val prefs= activity?.getPreferences(Context.MODE_PRIVATE)


                val destin = prefs!!.getString(preference_file_key,"none")



                val dialog = MaterialDialog(this@AddPlacePicturesFragment.context!!)
                        .title(R.string.img_upload_title)
                        .message(R.string.img_progress_report)

                dialog.show()

                FirebaseAuth.AuthStateListener { usrID ->

                    val builder = Data.Builder()

                    val myurls=imgList.map {uriit-> uriit.toString() }.toTypedArray()
                    builder.putStringArray(KEY_IMG_PATH,myurls )
                    builder.putString(preference_file_key,destin!!)
                    builder.putString(USERIDPH,usrID.currentUser!!.uid)

                    val myConstraints = Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()


                    val work:   OneTimeWorkRequest  =
                            OneTimeWorkRequest.Builder(CompressAndUploadWorker::class.java)
                                    .setInputData(builder.build())
                                    .setConstraints(myConstraints)
                                    .build()
                    WorkManager.getInstance().enqueue(work)

                    WorkManager.getInstance().getStatusById(work.id)
                            .observe(this, Observer {workStatus->

                                val isWorkFinished = !workStatus?.state!!.isFinished

                                if (isWorkFinished){


                                    dialog.dismiss()


                                    Navigation.findNavController(view).navigate(R.id.amenitiesFragment, null)

                                }else{
                                    Log.v("WORKM","It is Still in Progress")
                                }
                            })





                }




            }
        }

        picbutton.setOnClickListener {


            if (imgList.size == 10) {

                Snackbar.make(this.view!!, "Image List Exceeded 10. You can only Upload 10 images",
                        Snackbar.LENGTH_LONG)
                        .show()


            } else {
                val intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Images"), SELECTPICTURES)

            }


        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)



        if (requestCode==SELECTPICTURES && data!=null){
                Log.v("YESO","it worked")


            val clipData = data.clipData

            if (clipData != null)
            { // handle multiple photo

                for (i in 0 until clipData.itemCount) {
                    val uri = clipData.getItemAt(i).uri
                    imgList.add(uri!!)
                }
            } else { // handle single photo
                val uri = data.data
                imgList.add(uri!!)
            }


                imagesAdapter = GridImageAdaptor(this@AddPlacePicturesFragment.context)

                grid_view.adapter=imagesAdapter


        }
    }



    private fun showSnackbar(
            mainTextStringId: Int,
            actionStringId: Int,
            listener: View.OnClickListener
    ) {
        Snackbar.make(this.view!!, getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener)
                .show()
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
                AddPlacePicturesFragment()

        var myPuturls=ArrayList<String>()
    }
}
