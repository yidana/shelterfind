package findhome.com.example.android.findhomeb

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import findhome.com.example.android.findhomeb.HomeFragment.Companion.passingDataCloudData
import findhome.com.example.android.findhomeb.adaptors.HomeRecyclerViewAdaptor
import findhome.com.example.android.findhomeb.model.CloudData
import findhome.com.example.android.findhomeb.viewmodel.MyViewModel
import kotlinx.android.synthetic.main.fragment_all.*


class AllFragment : Fragment(), HomeRecyclerViewAdaptor.OnItemClickListener {



    override fun onItemClick(data: CloudData) {


        passingDataCloudData=data

        Log.v("mCLOUDY",passingDataCloudData.overview.toString())
        Navigation.findNavController(view!!).navigate(R.id.viewFragment, null)

    }

    private var listener: OnFragmentInteractionListener? = null

    private var dataRecyclerView:EmptyRecyclerView?=null
    private var recyclerViewAdapter: HomeRecyclerViewAdaptor? = null

    private var listmyData:ArrayList<Data>?=ArrayList()
    lateinit var mFirebaseFirestore: FirebaseFirestore
    lateinit var mViewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFirebaseFirestore= FirebaseFirestore.getInstance()



        mViewModel= ViewModelProviders.of(this).get(MyViewModel::class.java)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_all, container, false)
    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        dataRecyclerView= rvDataall
        val dbcloud:ArrayList<CloudData>?= ArrayList()
        val facilities=HashMap<String,String>()
        facilities.put("homes","homes")
        facilities.put("hostels","hostels")
        facilities.put("hotels","hotels")
        facilities.put("apartments","apartments")

        for (i:String in   facilities.keys){
            val mTarget=facilities[i]

            mFirebaseFirestore
                    .document("user/facilities")
                    .collection(mTarget!!)
                    .addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                        if (firebaseFirestoreException!=null){

                        }else{

                            for (documentChange: DocumentChange in querySnapshot!!.documentChanges){

                                if (documentChange.type== DocumentChange.Type.ADDED){

                                    val mstatus=documentChange.document.data.keys


                                    if (documentChange.document.getBoolean("statuscomplete")==false){

                                        val managerData=documentChange.document.toObject(CloudData::class.java)

                                        dbcloud!!.add(managerData)

                                        mViewModel.getArrayCloudList(dbcloud).observe(this, Observer {cloudata->


                                            recyclerViewAdapter = HomeRecyclerViewAdaptor(cloudata!!, this)
                                            dataRecyclerView?.layoutManager = LinearLayoutManager(this.context)
                                            dataRecyclerView?.adapter = recyclerViewAdapter
                                            dataRecyclerView?.setEmptyView(empty_view)




                                        })





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

        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                AllFragment()

    }
}
