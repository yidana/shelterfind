package findhome.com.example.android.findhomeb

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import findhome.com.example.android.findhomeb.adaptors.HostelAdaptor
import findhome.com.example.android.findhomeb.model.CloudData
import findhome.com.example.android.findhomeb.viewmodel.HostelViewModel
import kotlinx.android.synthetic.main.fragment_home_filter.*
import kotlinx.android.synthetic.main.fragment_hostel.*


class HostelFragment : Fragment(), HostelAdaptor.OnItemClickListener {


    override fun onItemClick(data: CloudData) {

    }


    private var dataRecyclerView:EmptyRecyclerView?=null
    private var recyclerViewAdapter: HostelAdaptor? = null

    private var listmyData:ArrayList<Data>?=ArrayList()
    lateinit var mFirebaseFirestore: FirebaseFirestore
    lateinit var mViewModel: HostelViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFirebaseFirestore= FirebaseFirestore.getInstance()



        mViewModel= ViewModelProviders.of(this).get(HostelViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hostel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        dataRecyclerView=rvDatahostel
        val dbcloud:ArrayList<CloudData>?= ArrayList()


        mFirebaseFirestore
                .document("user/facilities")
                .collection("hostels")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                    if (firebaseFirestoreException!=null){

                    }else{
                        for (documentChange: DocumentChange in querySnapshot!!.documentChanges){

                            if (documentChange.type== DocumentChange.Type.ADDED){

                                if (documentChange.document.exists()) {

                                    mFirebaseFirestore
                                            .document(documentChange.document.reference.path)
                                            .collection("hostels")
                                            .addSnapshotListener { basequerySnapshot, basefirebaseFirestoreException ->

                                                if ( basefirebaseFirestoreException!=null){

                                                }else {

                                                    for (basedocumentChange: DocumentChange in basequerySnapshot!!.documentChanges) {

                                                        if (basedocumentChange.document.exists()) {



                                                            val mstatus=documentChange.document.data.keys


                                                            if (basedocumentChange.document.getBoolean("statuscomplete")==false){

                                                                val managerData=basedocumentChange.document.toObject(CloudData::class.java)

                                                                dbcloud!!.add(managerData)

                                                                mViewModel.getArrayCloudList(dbcloud).observe(this, Observer {cloudata->


                                                                    recyclerViewAdapter = HostelAdaptor(cloudata!!, this)
                                                                    dataRecyclerView?.layoutManager = LinearLayoutManager(this.context)
                                                                    dataRecyclerView?.adapter = recyclerViewAdapter
                                                                    dataRecyclerView?.setEmptyView(empty_view_hostel)




                                                                })





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




    }



    override fun onAttach(context: Context) {
        super.onAttach(context)

    }


    companion object {

        @JvmStatic
        fun newInstance() =
                HostelFragment()
    }
}
