package com.rvtechnologies.grigorahq.orders.adapter.inner_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.services.models.RestaurantPastOrders
import kotlinx.android.synthetic.main.custom_dishtype.view.*

class HistoryOrderAddOnAdapter(val list: ArrayList<RestaurantPastOrders.Data.OrderDetail.ItemChoice.ItemSubCategory> = ArrayList()) :
    RecyclerView.Adapter<HistoryOrderAddOnAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_dishtype,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var pojo = list.get(position)


        holder.tv_dish_name.setText(pojo.itemChoiceName)
        holder.tv_dish_type.setText(pojo.name)
        holder.tv_price.setText("₦ " + pojo.addOnPrice)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_dish_name = itemView.tv_dish_name
        val tv_dish_type = itemView.tv_dish_type
        val tv_price = itemView.tv_price
    }
}