package com.rvtechnologies.grigorahq.orders.adapter.inner_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.services.models.RestaurantNewOrdersModel
import kotlinx.android.synthetic.main.custom_sub_item.view.*

class SubItemAdapter(val list: ArrayList<RestaurantNewOrdersModel.Data.OrderDetail> = ArrayList()) :
    RecyclerView.Adapter<SubItemAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_sub_item,
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
        var item_sub_list =
            ArrayList<RestaurantNewOrdersModel.Data.OrderDetail.ItemChoice.ItemSubCategory>()
        holder.tv_dish_name.setText(pojo.itemName)
        holder.tv_qty.setText(holder.itemView.context.getString(R.string.quantity) + pojo.quantity.toString())
        holder.tv_price.setText("₦ " + pojo.price)
        for (i in 0 until pojo.itemChoices.size) {
            item_sub_list.addAll(pojo.itemChoices.get(i).itemSubCategory)
        }
        holder.rv_dishItem.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.rv_dishItem.adapter = DishTypeAdapter(item_sub_list)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_dish_name = itemView.tv_dish_name
        val tv_qty = itemView.tv_qty
        val tv_price = itemView.tv_snum
        val rv_dishItem = itemView.rv_dishItem
    }
}