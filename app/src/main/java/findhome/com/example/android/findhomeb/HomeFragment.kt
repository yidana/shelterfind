package findhome.com.example.android.findhomeb

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import findhome.com.example.android.findhomeb.R.drawable.image2
import findhome.com.example.android.findhomeb.R.drawable.image_1
import findhome.com.example.android.findhomeb.R.layout.fragment_home
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(), HomeRecyclerViewAdaptor.OnItemClickListener {


    private var listener: OnFragmentInteractionListener? = null

    override fun onItemClick(data: Data) {


        Toast.makeText(this.context,"I was Clicked",Toast.LENGTH_SHORT).show()
    }




    private var dataRecyclerView: RecyclerView?=null
    private var recyclerViewAdapter: HomeRecyclerViewAdaptor? = null

    private var listmyData:ArrayList<Data>?=ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataRecyclerView= rvData
        listmyData?.add(Data("Ian Hostl","354fdAvenue", 1.4F, image_1    ))
        listmyData?.add(Data("KGR Hostl","354fdAvenue", 4.4F,image2 ))
        listmyData?.add(Data("RFW Hostl","354fdAvenue", 2.4F,image_1 ))
        listmyData?.add(Data("DER Hostl","354fdAvenue", 3.4F,image2 ))
        listmyData?.add(Data("FEW Hostl","354fdAvenue", 5.4F,image_1 ))
        listmyData?.add(Data("WRF Hostl","354fdAvenue", 0.4F,image2 ))
        listmyData?.add(Data("WRW Hostl","354fdAvenue", 2.9F,image_1 ))
        listmyData?.add(Data("GHYT Hostl","354fdAvenue", 5.0F,image2 ))
        listmyData?.add(Data("JYE Hostl","354fdAvenue", 0.7F,image_1 ))
        listmyData?.add(Data("CVB Hostl","354fdAvenue", 3.8F,image2 ))
        listmyData?.add(Data("XSD Hostl","354fdAvenue", 4.7F,image_1 ))
        listmyData?.add(Data("WER Hostl","354fdAvenue", 4.4F,image2 ))


        recyclerViewAdapter = HomeRecyclerViewAdaptor(listmyData!!, this)
        dataRecyclerView?.layoutManager = LinearLayoutManager(this.context)
        dataRecyclerView?.adapter = recyclerViewAdapter


        category.setOnClickListener {


        val categoryBottomSheet=BottonSheetCat()

            categoryBottomSheet.show(fragmentManager,"CategoryBottomSheet")
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
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
