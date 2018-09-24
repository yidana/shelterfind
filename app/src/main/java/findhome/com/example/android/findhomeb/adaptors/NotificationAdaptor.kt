package findhome.com.example.android.findhomeb.adaptors

import findhome.com.example.android.findhomeb.R
import findhome.com.example.android.findhomeb.R.id.notification_recycle_view
import kotlinx.android.synthetic.main.fragment_notification.view.*
import kotlinx.android.synthetic.main.notification_view.view.*


class NotificationAdaptor(data:ArrayList< HashMap<String,String>>):
        android.support.v7.widget.RecyclerView.Adapter<NotificationAdaptor.RecyclerViewHolder>() {

    private var listData:ArrayList< HashMap<String,String>> = data


    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): RecyclerViewHolder {

        return RecyclerViewHolder(android.view.LayoutInflater.from(parent.context).inflate(R.layout.notification_empty_view, parent, false))

    }





    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        val currentData = listData[position]




        val titleData = currentData.get("title")
        val messageData = currentData.get("message")

        holder.mtitle.text =titleData
        holder.mmessage.text =messageData






    }


    override fun getItemCount(): Int {

        return listData.size
    }






    class RecyclerViewHolder(itemView: android.view.View) : android.support.v7.widget.RecyclerView.ViewHolder(itemView) {

        var mtitle = itemView.notification_title!!
        var mmessage = itemView.notification_message!!

        fun bind(data: java.util.HashMap<String, String>) {



            }
        }



}