package findhome.com.example.android.findhomeb.adaptors

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.squareup.picasso.Picasso
import findhome.com.example.android.findhomeb.R
import kotlinx.android.synthetic.main.gridadaptorview.view.*


class GridImageAdaptor(context: Context?) : BaseAdapter() {


    private var mContext: Context? = context

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {



        val img= imgList[position]

        val inflator = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val imgView=inflator.inflate(R.layout.gridadaptorview,null)


        imgView.grid_imageview.setOnClickListener {

           Log.v("CLICKED ID",it.toString())

        }

        imgView.imageButton.setOnClickListener {
            remove(position)
            notifyDataSetChanged()

        }



        Picasso.get().load(img).resize(1800,1800).centerCrop().into(imgView.grid_imageview)




        return imgView
    }


    override fun getItem(position: Int): Any {


       return imgList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {


        return imgList.count()
    }

    private fun remove(position: Int) {
        imgList.removeAt(position)

    }


    companion object {

         var imgList=ArrayList<Uri>()
    }
}