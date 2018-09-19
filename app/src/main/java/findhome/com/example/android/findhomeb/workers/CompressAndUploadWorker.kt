package findhome.com.example.android.findhomeb.workers

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import com.google.android.gms.flags.IFlagProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import findhome.com.example.android.findhomeb.AddPlacePicturesFragment.Companion.myPuturls
import findhome.com.example.android.findhomeb.adaptors.GridImageAdaptor
import java.io.ByteArrayOutputStream

class CompressAndUploadWorker:Worker() {


    lateinit var mFirebaseStorage: FirebaseStorage
    lateinit var mStorageRef: StorageReference
    lateinit var mImageRef: StorageReference
    lateinit var imagesAdapter: GridImageAdaptor
    lateinit var mFirebaseFirestore: FirebaseFirestore
    val preference_file_key="MYDESTINATION"
    val PHOTO_URL="MYURLS"
    private val myListuri=ArrayList<String>()
    val USERIDPH="id for upload"


    override fun doWork(): Result = try {

        mFirebaseFirestore= FirebaseFirestore.getInstance()
        mFirebaseStorage= FirebaseStorage.getInstance()
        val imgArray = inputData.getStringArray(KEY_IMG_PATH)
        val destination=inputData.getString(preference_file_key)
        val userID=inputData.getString(USERIDPH)




        myCompressThenUpload(imgArray!!,destination!!,userID!!)




        Result.SUCCESS
    } catch (e: Throwable) {
        Log.e(LOG_TAG, "Error executing work: " + e.message, e)
        Result.FAILURE
    }




    private fun myCompressThenUpload(uris: Array<String>,destin:String,userID:String){



        for (mUri:String in uris){



            val uri= Uri.parse(mUri)

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


                mImageRef.downloadUrl.addOnSuccessListener { uri ->

                    val duri=uri.toString()
                    Log.v("MURI",duri)
                    myListuri.add( duri)
                    Log.v("MURIDD", myListuri.toString())


                    if (myListuri.size==uris.size){


                          when(destin){
                              "house"->{
                                  val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/homes/"
                                          +userID+"/"+"homes")
                                  myCollectionReference.get().addOnCompleteListener {task ->
                                      if (task.isSuccessful){
                                          val photodb=HashMap<String,Any>()
                                          photodb["photourl"] =myListuri.toList()
                                          photodb["progress"]="70"
                                          mFirebaseFirestore.collection("/user/facilities/homes/"
                                                  +userID+"/"+"homes")
                                                  .document(task.result.last().id)
                                                  .set( photodb, SetOptions.merge())
                                                  .addOnFailureListener { failure->

                                                  }.addOnSuccessListener {


                                                  }
                                      }
                                  }
                              }
                              "hostel"->{

                                  val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/hostels/"
                                          +userID+"/"+"hostels")
                                  myCollectionReference.get().addOnCompleteListener {task ->
                                      if (task.isSuccessful){
                                          val photodb=HashMap<String,Any>()
                                          photodb["photourl"] =myListuri.toList()
                                          mFirebaseFirestore.collection("/user/facilities/hostels/"
                                                  +userID+"/"+"hostels")
                                                  .document(task.result.last().id)
                                                  .set(photodb, SetOptions.merge())
                                                  .addOnFailureListener { failure->

                                                      Log.e("FailureCloud",failure.toString())
                                                  }.addOnSuccessListener {

                                                  }
                                      }
                                  }
                              }
                              "hotel"->{
                                  val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/hotels/"
                                          +userID+"/"+"hotels")
                                  myCollectionReference.get().addOnCompleteListener {task ->
                                      if (task.isSuccessful){
                                          val photodb=HashMap<String,Any>()
                                          photodb["photourl"] =myListuri.toList()
                                          mFirebaseFirestore.collection("/user/facilities/hotels")
                                                  .document(task.result.last().id)
                                                  .set(photodb, SetOptions.merge())
                                                  .addOnFailureListener { failure->

                                                  }.addOnSuccessListener {

                                                  }
                                      }
                                  }
                              }
                              "apartment"->{
                                  val myCollectionReference=mFirebaseFirestore.collection("/user/facilities/apartments/"
                                          +userID+"/"+"apartments")
                                  myCollectionReference.get().addOnCompleteListener {task ->
                                      if (task.isSuccessful){
                                          val photodb=HashMap<String,Any>()
                                          photodb["photourl"] =myListuri.toList()
                                          mFirebaseFirestore.collection("/user/facilities/apartments/"
                                                  +userID+"/"+"apartments")
                                                  .document(task.result.last().id)
                                                  .set(photodb, SetOptions.merge())
                                                  .addOnFailureListener { failure->

                                                  }.addOnSuccessListener {


                                                  }
                                      }
                                  }
                              }

                          }



                      }




                    }


                }


        }



    }

    companion object {
        private const val KEY_IMG_PATH = "IMG_PATH"
        private const val LOG_TAG = "UploadWorker"


    }


}