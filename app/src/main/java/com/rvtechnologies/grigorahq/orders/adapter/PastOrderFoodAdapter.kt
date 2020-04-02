package com.rvtechnologies.grigorahq.orders.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.orders.adapter.inner_adapter.HistoryOrderAddOnAdapter
import com.rvtechnologies.grigorahq.services.models.RestaurantPastOrders
import kotlinx.android.synthetic.main.custom_sub_item.view.*


class PastOrderFoodAdapter(var list: ArrayList<RestaurantPastOrders.Data.OrderDetail>) :
    RecyclerView.Adapter<PastOrderFoodAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var pojo = list.get(position)
//        if (pojo.itemName != null && pojo.price != null) {
//            holder.textView10.setText(pojo.itemName)
//            holder.textView20.setText(" ₦ " + pojo.price)
//
//
//        }
//        holder.textView14.setText(pojo.quantity.toString())
//
//
//        holder.itemView.setOnClickListener {
//            val context = holder.itemView.context
//        }
        if (pojo.itemChoices.size > 0) {
            holder.tv_add_on.visibility = View.VISIBLE
            holder.rv_dishItem.visibility = View.VISIBLE
            var item_sub_list =
                ArrayList<RestaurantPastOrders.Data.OrderDetail.ItemChoice.ItemSubCategory>()
            for (i in 0 until pojo.itemChoices.size) {
                item_sub_list.addAll(pojo.itemChoices.get(i).itemSubCategory)
            }

            holder.rv_dishItem.layoutManager = LinearLayoutManager(holder.itemView.context)
            holder.rv_dishItem.adapter = HistoryOrderAddOnAdapter(item_sub_list)

        }else
            holder.tv_add_on.visibility = View.GONE
        holder.tv_qty.setText(pojo.quantity.toString())
        holder.tv_dish_name.setText(pojo.itemName)




        holder.tv_snum.text = (position + 1).toString() + "."


    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(p0.context).inflate(
                R.layout.custom_sub_item,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val textView14 = itemView.textView14
//        val textView10 = itemView.textView10
//        val textView20 = itemView.textView20
        val tv_dish_name = itemView.tv_dish_name
        val tv_qty = itemView.tv_qty
        val tv_snum = itemView.tv_snum
        val tv_add_on = itemView.tv_add_on
        val rv_dishItem = itemView.rv_dishItem
    }

}