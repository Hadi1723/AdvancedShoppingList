package com.example.advancedshoppinglist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.text.DecimalFormat

class DetailedInfoAdapter(
    private val context: Context,
    private val detailModels: ArrayList<DetailedInfoModel>

) : RecyclerView.Adapter<DetailedInfoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailedInfoAdapter.ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.detailed_info, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return detailModels.size
    }

    // and we're going to define our own view holder which will encapsulate the memory card view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(position: Int) {
            val priceTv = itemView.findViewById<TextView>(R.id.itemPrice)
            val nameTv = itemView.findViewById<TextView>(R.id.itemName)

            var model: DetailedInfoModel = detailModels.get(position)


            val df = DecimalFormat("#.##")

            priceTv.setText(model.getInfoType() +": $" + df.format(model.getQuantity()).toString())
            nameTv.setText(model.getItemName().toLowerCase())


            //var input: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm")
            //var output: SimpleDateFormat = SimpleDateFormat("hh:mm aa")

            /*
            try {
                var t: Date = input.parse(model.getTime())
                timeTV.setText(output.format(t))
            } catch (e: ParseException) {
                e.printStackTrace()
            } */
        }

    }


}