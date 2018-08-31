package findhome.com.example.android.findhomeb.workers

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.work.Worker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import findhome.com.example.android.findhomeb.GridImageAdaptor
import java.io.ByteArrayOutputStream

class CompressAndUploadWorker:Worker() {


    lateinit var mFirebaseStorage: FirebaseStorage
    lateinit var mStorageRef: StorageReference
    lateinit var mImageRef: StorageReference
    lateinit var imagesAdapter: GridImageAdaptor
    lateinit var mFirebaseFirestore: FirebaseFirestore
    val preference_file_key="MYDESTINATION"
    val myurls=ArrayList<String>()



    override fun doWork(): Result = try {

        mFirebaseFirestore= FirebaseFirestore.getInstance()
        mFirebaseStorage= FirebaseStorage.getInstance()
        val imgArray = inputData.getStringArray(KEY_IMG_PATH)
        val destination=inputData.getString(preference_file_key)





        myCompressThenUpload(imgArray!!,destination!!)

        if (!myurls.isEmpty()){


            when(destination){
                "home"->{
                    val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/homes")
                    myCollectionReference.get().addOnCompleteListener {task ->
                        if (task.isSuccessful){
                            val photosdb=HashMap<String,Any>()
                            photosdb["photourl"] =myurls
                            mFirebaseFirestore.collection("/user/facilities/homes")
                                    .document(task.result.last().id)
                                    .set( photosdb, SetOptions.merge())
                                    .addOnFailureListener { failure->

                                    }.addOnSuccessListener {

                                    }
                        }
                    }
                }
                "hostel"->{

                    val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/hostels")
                    myCollectionReference.get().addOnCompleteListener {task ->
                        if (task.isSuccessful){
                            val photosdb=HashMap<String,Any>()
                            photosdb["photourl"] =myurls
                            mFirebaseFirestore.collection("/user/facilities/hostels")
                                    .document(task.result.last().id)
                                    .set(photosdb, SetOptions.merge())
                                    .addOnFailureListener { failure->

                                        Log.e("FailureCloud",failure.toString())
                                    }.addOnSuccessListener {

                                    }
                        }
                    }
                }
                "hotel"->{
                    val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/hotels")
                    myCollectionReference.get().addOnCompleteListener {task ->
                        if (task.isSuccessful){
                            val photosdb=HashMap<String,Any>()
                            photosdb["photourl"] =myurls
                            mFirebaseFirestore.collection("/user/facilities/hotels")
                                    .document(task.result.last().id)
                                    .set(photosdb, SetOptions.merge())
                                    .addOnFailureListener { failure->

                                    }.addOnSuccessListener {

                                    }
                        }
                    }
                }
                "apartment"->{
                    val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/apartments")
                    myCollectionReference.get().addOnCompleteListener {task ->
                        if (task.isSuccessful){
                            val photosdb=HashMap<String,Any>()
                            photosdb["photourl"] =myurls
                            mFirebaseFirestore.collection("/user/facilities/apartments")
                                    .document(task.result.last().id)
                                    .set(photosdb, SetOptions.merge())
                                    .addOnFailureListener { failure->

                                    }.addOnSuccessListener {

                                    }
                        }
                    }
                }

            }



        }


        Result.SUCCESS
    } catch (e: Throwable) {
        Log.e(LOG_TAG, "Error executing work: " + e.message, e)
        Result.FAILURE
    }




    private fun myCompressThenUpload(uris: Array<String>,destin:String) {


        uris.map { myUri->

            val uri= Uri.parse(myUri)

            val uriPath=uri.lastPathSegment
            mStorageRef=mFirebaseStorage.reference
            mImageRef = mStorageRef.child("place photos/$uriPath")



            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.applicationContext.contentResolver, uri)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
            val imgdata:ByteArray=   baos.toByteArray()
            val path:String  = MediaStore.Images.Media.insertImage(this.applicationContext.contentResolver, bitmap, uriPath, null)

           val compressedImg= Uri.parse(path)


            mImageRef.putFile(compressedImg).addOnSuccessListener { taskSnapshot ->

                myurls.add( taskSnapshot.storage.downloadUrl.toString())

            }


        }








    }

    companion object {
        private const val KEY_IMG_PATH = "IMG_PATH"
        private const val LOG_TAG = "UploadWorker"
    }


}