package findhome.com.example.android.findhomeb.adaptors


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import findhome.com.example.android.findhomeb.R
import findhome.com.example.android.findhomeb.model.CloudData
import kotlinx.android.synthetic.main.liftstatus_view.view.*

class ProgressLiftingAdaptor(data: ArrayList<CloudData>, listener: OnItemClickListener):
        RecyclerView.Adapter<ProgressLiftingAdaptor.RecyclerViewHolder>() {


    private var listData: List<CloudData> = data

    private var listenerData: OnItemClickListener = listener


    interface OnItemClickListener {
        fun onItemClick(data: CloudData)
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {

        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.liftstatus_view, parent, false))

    }



    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        val currentData: CloudData = listData[position]




        val nameData = currentData.overview

        val moverview=nameData as HashMap<String,String>


        holder.mName.text = moverview["title"]
        holder. mprogress.text =  currentData.progress
        holder.mprogress_bar.progress =  currentData.progress!!.toInt()

        holder.bind(currentData, listenerData)





    }


    override fun getItemCount(): Int {

        return listData.size
    }






    class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mName = itemView.progress_name!!
        var mprogress = itemView.progressbar_number!!
        var mprogress_bar = itemView.liftingStatus_progress!!



        fun bind(data: CloudData, listener: OnItemClickListener) {

            itemView.btn_continue.setOnClickListener {
                listener.onItemClick(data)

            }

        }
    }



}