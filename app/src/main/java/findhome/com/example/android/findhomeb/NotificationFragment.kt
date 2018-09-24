package findhome.com.example.android.findhomeb

import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import findhome.com.example.android.findhomeb.R.layout.notification_empty_view
import findhome.com.example.android.findhomeb.adaptors.NotificationAdaptor
import findhome.com.example.android.findhomeb.viewmodel.NotifiactionRecycleVIewmodel
import kotlinx.android.synthetic.main.fragment_notification.*
import java.util.*


class NotificationFragment : Fragment() {

    lateinit var mViewModel: NotifiactionRecycleVIewmodel
     var dataRecyclerView:EmptyRecyclerView?=null

    private var recyclerViewAdapter: NotificationAdaptor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LocalBroadcastManager.getInstance(this@NotificationFragment.context!!).registerReceiver(mHandler(), IntentFilter("findhome.com.example.android.findhomeb_FCM-MESSAGE"))
        mViewModel= ViewModelProviders.of(this).get(NotifiactionRecycleVIewmodel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<Toolbar>(R.id.my_toolbar) as Toolbar

        toolbar.setNavigationOnClickListener {

            Navigation.findNavController(it).navigate(R.id.profileFragment, null)
        }

        dataRecyclerView=notification_recycle_view

      val mdata=  mHandler().not_data


        mViewModel.getDataArrayList(mdata).observe(this,android.arch.lifecycle.Observer {listdata->

            recyclerViewAdapter =NotificationAdaptor(listdata!!)
            dataRecyclerView?.layoutManager = LinearLayoutManager(this.context)

            
            dataRecyclerView?.setEmptyView(empty_view_notification)
            dataRecyclerView?.adapter = recyclerViewAdapter



        })








    }






    class mHandler(): BroadcastReceiver() {
        var not_data=ArrayList<HashMap<String,String>>()

        override fun onReceive(context: Context, intent: Intent) {
            // Post the UI updating code to our Handler

            val mHsMap=HashMap<String,String>()

            val mtitle=intent.getStringExtra("title")
            val mmessage=intent.getStringExtra("message")

            mHsMap["title"]=mtitle
            mHsMap["message"]=mmessage

            not_data.add(mHsMap)



        }
    }

    override fun onPause() {
        super.onPause()

        LocalBroadcastManager.getInstance(this@NotificationFragment.context!!).unregisterReceiver(mHandler())

    }


    companion object {

        @JvmStatic
        fun newInstance() =
                NotificationFragment()
    }
}
