package findhome.com.example.android.findhomeb

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import findhome.com.example.android.findhomeb.adaptors.HomeFilterAdaptor
import findhome.com.example.android.findhomeb.adaptors.HomeRecyclerViewAdaptor
import findhome.com.example.android.findhomeb.model.CloudData
import findhome.com.example.android.findhomeb.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_all.*
import kotlinx.android.synthetic.main.fragment_home_filter.*


class HomeFilterFragment : Fragment(),HomeFilterAdaptor.OnItemClickListener {



    override fun onItemClick(data: CloudData) {

    }


    private var dataRecyclerView:EmptyRecyclerView?=null
    private var recyclerViewAdapter: HomeFilterAdaptor? = null

    private var listmyData:ArrayList<Data>?=ArrayList()
    lateinit var mFirebaseFirestore: FirebaseFirestore
    lateinit var mViewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFirebaseFirestore= FirebaseFirestore.getInstance()



        mViewModel= ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        dataRecyclerView=rvDatahome
        val dbcloud:ArrayList<CloudData>?= ArrayList()


        mFirebaseFirestore
                .document("user/facilities")
                .collection("homes")
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


                                        recyclerViewAdapter = HomeFilterAdaptor(cloudata!!, this)
                                        dataRecyclerView?.layoutManager = LinearLayoutManager(this.context)
                                        dataRecyclerView?.adapter = recyclerViewAdapter
                                        dataRecyclerView?.setEmptyView(empty_view_homefilter)




                                    })





                                }






                            }

                        }


                    }



                }


    }






    companion object {

        @JvmStatic
        fun newInstance() =
                HomeFilterFragment()
    }
}
