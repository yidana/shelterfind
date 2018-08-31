package findhome.com.example.android.findhomeb

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.gridadaptorview.view.*
import kotlinx.coroutines.experimental.launch


class GridImageAdaptor(context: Context?) : BaseAdapter() {


    private var mContext: Context? = context

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {


        Log.v("YESO", imgList.toString())
        val img=imgList[position]

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