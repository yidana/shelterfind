package findhome.com.example.android.findhomeb

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Switch
import findhome.com.example.android.findhomeb.R.layout.amenities_listview
import kotlinx.android.synthetic.main.amenities_listview.view.*


class AmenitiesListAdaptor(context: Context?, amenityname: ArrayList<String>): BaseAdapter() {



    var mContext: Context? = context
    var mamenities = amenityname

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup?): View? {

        val item=mamenities[position]

        val inflator = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val itemamenities=inflator.inflate(R.layout.amenities_listview,null)


        itemamenities.switch_amentities.text=item

        if (myAmenities.isNotEmpty() ){

            myAmenities.map { myitem->

             if(   myitem==itemamenities.switch_amentities.text ) itemamenities.switch_amentities.isChecked=true

            }
        }





        itemamenities.switch_amentities.setOnCheckedChangeListener { buttonView, isChecked ->


            if (isChecked){
                myAmenities.add(mamenities[position])

            }else{

            }

            if (!isChecked){
                myAmenities.remove(mamenities[position])
            }

        }



        return itemamenities
    }

    override fun getItem(position: Int): Any {
        return  mamenities[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return  mamenities.size
    }


    companion object {
        val myAmenities=ArrayList<String>()
    }
}

