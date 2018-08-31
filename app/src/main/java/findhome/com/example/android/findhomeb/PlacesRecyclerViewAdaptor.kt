package findhome.com.example.android.findhomeb

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import findhome.com.example.android.findhomeb.R.layout.places_card_view
import kotlinx.android.synthetic.main.places_card_view.view.*

  class PlacesRecyclerViewAdaptor(data: ArrayList<Data>, listener: OnItemClickListener):
        RecyclerView.Adapter<PlacesRecyclerViewAdaptor.RecyclerViewHolder>() {



    private var listData: List<Data> = data

    private var listenerData: OnItemClickListener = listener


    interface OnItemClickListener {
        fun onItemClick(data: Data)
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {

        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(places_card_view, parent, false))
    }

    override fun getItemCount(): Int {

        return listData.size
    }


    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {


        var currentData: Data = listData[position]

        var nameData = currentData.name
        var ratingData = currentData.rating
        var locationData = currentData.location
        var imageData = currentData.image

        holder.mName.text = nameData

        Picasso.get().load(imageData).resize(1200,600).centerCrop().into(holder.mImage)

        holder.mRating.rating = ratingData

        holder.bind(currentData, listenerData)



    }




    class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mName = itemView.places_card_name!!
        var mRating = itemView.places_card_ratingBar!!
        var mImage = itemView.places_card_imageView!!

        fun bind(data: Data, listener: OnItemClickListener) {
            itemView.setOnClickListener {
                listener.onItemClick(data)
            }
        }


    }


}