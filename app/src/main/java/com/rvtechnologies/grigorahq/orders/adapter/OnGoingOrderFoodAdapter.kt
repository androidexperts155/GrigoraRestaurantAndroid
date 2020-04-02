package com.rvtechnologies.grigorahq.orders.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.services.models.RestaurantOnGoingOrdersModel
import kotlinx.android.synthetic.main.order_details_item.view.*


class OnGoingOrderFoodAdapter(var list: ArrayList<RestaurantOnGoingOrdersModel.Data.OrderDetail>) :
    RecyclerView.Adapter<OnGoingOrderFoodAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var pojo = list.get(position)
        if (pojo.itemName != null && pojo.price != null) {
            holder.textView10.setText(pojo.itemName)
            holder.textView20.setText(" ₦ " + pojo.price)


        }
        holder.textView14.setText(holder.itemView.context.getString(R.string.qty) + (pojo.quantity.toString()))


        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
        }

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(p0.context).inflate(
                R.layout.order_details_item,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView14 = itemView.textView14
        val textView10 = itemView.textView10
        val textView20 = itemView.textView20

    }

}