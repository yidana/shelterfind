package findhome.com.example.android.findhomeb
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import findhome.com.example.android.findhomeb.R.layout.item_card_view
import kotlinx.android.synthetic.main.item_card_view.view.*

  class HomeRecyclerViewAdaptor(data: ArrayList<Data>, listener: OnItemClickListener):
        RecyclerView.Adapter<HomeRecyclerViewAdaptor.RecyclerViewHolder>() {


    private var listData: List<Data> = data

    private var listenerData: OnItemClickListener = listener


    interface OnItemClickListener {
        fun onItemClick(data: Data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {

        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(item_card_view, parent, false))

    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        var currentData: Data = listData[position]
        Log.v("LIST SIZE", position.toString() )

        var nameData = currentData.name
        var ratingData = currentData.rating
        var locationData = currentData.location
        var imageData = currentData.image


        holder.mName.text = nameData
        Picasso.get().load(imageData).resize(300,300).centerCrop().into(holder.mImage)
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


        fun bind(data: Data, listener: OnItemClickListener) {
            itemView.setOnClickListener {
                listener.onItemClick(data)
            }
        }


    }
}