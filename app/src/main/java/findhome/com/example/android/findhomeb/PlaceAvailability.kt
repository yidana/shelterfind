package findhome.com.example.android.findhomeb

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.navigation.Navigation

import kotlinx.android.synthetic.main.fragment_place_availability.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


class PlaceAvailability : Fragment() {

    val myKitchen:MyKitchen=MyKitchen()
    val preference_file_key="MYDESTINATION"
    lateinit var getMyDate:Any

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar = activity?.actionBar
        actionBar?.setHomeButtonEnabled(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_place_availability, container, false)
    }







    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val progressBar: ProgressBar?=view.findViewById<ProgressBar>(R.id.progressBar)


            myKitchen.SetProgress(progressBar!!,30)



        val prefs= activity?.getPreferences(Context.MODE_PRIVATE) ?: return


        val destin = prefs.getString(preference_file_key,"none")


        val toolbar = view.findViewById<android.widget.Toolbar>(R.id.my_toolbar) as android.widget.Toolbar




        toolbar.setNavigationOnClickListener {

            if (destin!!.toString()=="hostel"){

                Navigation.findNavController(it).navigate(R.id.hostelRoomTypeFragment, null)
            }else{

                Navigation.findNavController(it).navigate(R.id.generalRoomTypeFragment, null)

            }


        }



        val buttonnext: FloatingActionButton?= view.findViewById<FloatingActionButton>(R.id.button_next_place_avail)
        val alwaysbtn:LinearLayout?= view.findViewById<LinearLayout>(R.id.always)
        val rangebtn: LinearLayout?= view.findViewById<LinearLayout>(R.id.range)
        val oncebtn:LinearLayout?= view.findViewById<LinearLayout>(R.id.one_time)



        val dateList= (activity as MainActivity).peroidavailable




        buttonnext?.setOnClickListener{
            Log.v("ATE",dateList.toString())
            when{
                view.text_availability.text.isEmpty()->{

                    Snackbar.make(view,"Choose a date to continue", Snackbar.LENGTH_SHORT ).show()
                    return@setOnClickListener
                }
            }




            when(destin!!.toString()) {
                "hostel" -> Navigation.findNavController(it).navigate(R.id.priceHostelFragment, null)
                "hotel" -> Navigation.findNavController(it).navigate(R.id.hotelGeneralPriceFragment, null)
                "house" -> Navigation.findNavController(it).navigate(R.id.generalPriceFragment, null)
                "apartment" -> Navigation.findNavController(it).navigate(R.id.generalPriceFragment, null)
            }



        }




        view.text_availability.text=dateList.toString()

        alwaysbtn?.setOnClickListener {

            view.text_availability.text="Always Available"
        }

        rangebtn?.setOnClickListener {
            val fm = fragmentManager
            val mDialogFragment = CalenderDialogFragment().newInstance("Heello", CalenderDialogFragment.DemoMode.RANGE_SELECTION)


            mDialogFragment.show(fm, "fragment_edit_name")

            view.text_availability.text="Range"
        }
        oncebtn?.setOnClickListener {

            val fm = fragmentManager
            val mDialogFragment = CalenderDialogFragment().newInstance("Heello", CalenderDialogFragment.DemoMode.SINGLE_SELECTION)

            mDialogFragment.show(fm, "fragment_edit_name")
            view.text_availability.text="One Time"

        }




    }




    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()

    }



    companion object {

        @JvmStatic
        fun newInstance() =
                PlaceAvailability()
    }
}
