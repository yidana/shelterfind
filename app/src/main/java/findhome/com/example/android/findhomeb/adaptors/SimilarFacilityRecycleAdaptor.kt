package findhome.com.example.android.findhomeb.adaptors

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import findhome.com.example.android.findhomeb.R
import findhome.com.example.android.findhomeb.model.CloudData
import kotlinx.android.synthetic.main.item_card_view.view.*

class SimilarFacilityRecycleAdaptor(data: ArrayList<CloudData>, listener: OnItemClickListener): RecyclerView.Adapter<SimilarFacilityRecycleAdaptor.RecyclerViewHolder>() {


    private var listData: List<CloudData> = data

    private var listenerData: OnItemClickListener = listener


    interface OnItemClickListener {
        fun onItemClick(data: CloudData)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {

        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.similar_facility_layout, parent, false))

    }



    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        val currentData: CloudData = listData[position]

        val nameData = currentData.overview

        val moverview=nameData as HashMap<String,String>

        val ratingData = 5.0f
        val locationData = "Hello"
        val imageData = Uri.parse( currentData.captionurl)

        holder.mName.text = moverview["title"]
        Picasso.get().load(imageData) .fit().into(holder.mImage)
        holder.mLocation.text = locationData
        holder.mRating.rating = ratingData

        holder.bind(currentData, listenerData)





    }


    override fun getItemCount(): Int {

        return listData.size
    }


    class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mName = itemView.card_name!!
        var mLocation = itemView.card_location_name!!
        var mRating = itemView.card_rating_bar!!
        var mImage = itemView.card_imageview!!


        fun bind(data: CloudData, listener: OnItemClickListener) {
            itemView.view_btn.setOnClickListener {

                listener.onItemClick(data)

            }
        }


    }
}