package findhome.com.example.android.findhomeb.adaptors

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.squareup.picasso.Picasso
import findhome.com.example.android.findhomeb.R
import kotlinx.android.synthetic.main.chips_layout.view.*

class ChipsAdaptor(context:Context,itemList:ArrayList<String> ):BaseAdapter() {

    private val entryList=itemList
    private val  mcontext=context


    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item= entryList[position]

        val inflator = mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val itemView=inflator.inflate(R.layout.chips_layout,null)


        itemView.item_chip.text=item
        val chip_img=getitemities(item)
        itemView.item_chip.chipIcon=mcontext.resources.getDrawable( chip_img, null)

        return itemView
    }



    fun getitemities(item:String):Int {
        val itemList = HashMap<String, Int>()

        itemList.put("Gym", R.drawable.ic_gym)
        itemList.put("Generator", R.drawable.ic_generator)
        itemList.put("DSTV", R.drawable.ic_dstv)
        itemList.put("Wifi", R.drawable.ic_wifi)
        itemList.put("Heat", R.drawable.ic_heating)
        itemList.put("AirConditioning", R.drawable.ic_airconditioner)
        itemList.put("Desk/Workspace", R.drawable.ic_desk)
        itemList.put("Iron", R.drawable.ic_iron)
        itemList.put("Kitchen", R.drawable.ic_kitchen)
        itemList.put("Fridge", R.drawable.ic_fridge)
        itemList.put("Study Room", R.drawable.ic_study_room)
        itemList.put("Water Tank", R.drawable.ic_water_tank)
        itemList.put("Shop", R.drawable.ic_shop)
        itemList.put("Cleaning Service", R.drawable.ic_cleaning_service)
        itemList.put("24 Hour Water Supply", R.drawable.ic_faucet)
        itemList.put("TV Room", R.drawable.ic_tv_room)

        return itemList[item]!!
    }
    

    override fun getItem(position: Int): Any {

    return  entryList[position]
    }

    override fun getItemId(position: Int): Long {
     return position.toLong()
    }

    override fun getCount(): Int {
      return  entryList.size
    }


}