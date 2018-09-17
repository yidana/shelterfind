package findhome.com.example.android.findhomeb.adaptors

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.squareup.picasso.Picasso
import findhome.com.example.android.findhomeb.R
import kotlinx.android.synthetic.main.view_amenities.view.*

class AmenitiesViewAdaptor(context: Context?,amenities:ArrayList<String>) : BaseAdapter() {


    private var mContext: Context? = context
    private var mamenities: ArrayList<String> =amenities

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val amen_name=  mamenities[position]

        val resID:Int=getAmenities(amen_name)

        Log.v("RESID",resID.toString())

        val inflator = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val amen_item=inflator.inflate(R.layout.view_amenities,null)



        Picasso.get().load(resID).fit().into(amen_item.amenities_view_image)

        amen_item.amenities_text.text=amen_name

        return amen_item
    }



    fun getAmenities(amen:String):Int{
        val amenList=HashMap<String,Int>()

        amenList["Gym"] = R.drawable.ic_gym
        amenList["Generator"] = R.drawable.ic_generator
        amenList["DSTV"] = R.drawable.ic_dstv
        amenList["Wifi"] = R.drawable.ic_wifi
        amenList["Heat"] = R.drawable.ic_heating
        amenList["AirConditioning"] = R.drawable.ic_airconditioner
        amenList["Desk/Workspace"] = R.drawable.ic_desk
        amenList["Iron"] = R.drawable.ic_iron
        amenList["Kitchen"] = R.drawable.ic_cooking
        amenList["Fridge"] = R.drawable.ic_fridge
        amenList["Study Room"] = R.drawable.ic_study_room
        amenList["Water Tank"] = R.drawable.ic_water_tank
        amenList["Shop"] = R.drawable.ic_shop
        amenList["Cleaning Service"] = R.drawable.ic_cleaning_service
        amenList["24 Hour Water Supply"] = R.drawable.ic_faucet
        amenList["TV Room"] = R.drawable.ic_tv_room
        amenList["Shuttle Service"] = R.drawable.ic_shuttle_service
        amenList["Restuarant"] = R.drawable.ic_restuarant
        amenList["Basketball Court"] = R.drawable.ic_basketball_court
        amenList["Snooker"] = R.drawable.ic_snooker
        amenList["Table Tennis"] = R.drawable.ic_table_tennis
        amenList["Football Pitch"] = R.drawable.ic_football_pitch
        amenList["Disability Friendly"] = R.drawable.ic_disability_friendly
        amenList["Gass"] = R.drawable.ic_gas
        amenList["Parking Space"] = R.drawable.ic_parking
        amenList["Essentials(towels,bed sheets,toilet papers, pillows)"] = R.drawable.ic_essentials
        amenList["24 Hours Security"] = R.drawable.ic_security_service
        amenList["Fire Extinquisher"] = R.drawable.ic_fire_extinguisher
        amenList["CCTV Camera"] = R.drawable.ic_cctv
        amenList["First Aid Kit"] = R.drawable.ic_first_aid
        amenList["Smoke Detector"] = R.drawable.ic_smoke_detector
        amenList["Fire Detector"] = R.drawable.ic_fire_detector


        return amenList[amen]!!
    }

    override fun getItem(position: Int): Any {
        return  mamenities[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return  mamenities.count()
    }





}