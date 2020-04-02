package com.rvtechnologies.grigorahq.home

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.orders.adapter.inner_adapter.OnGoingDishTypeAdapter
import com.rvtechnologies.grigorahq.services.models.HomeModel
import kotlinx.android.synthetic.main.custom_sub_item.view.*

class HomeDialogAdapter(val list: ArrayList<HomeModel.Data.OrderDetail> = ArrayList()) :
    RecyclerView.Adapter<HomeDialogAdapter.ViewHolder>() {


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

        holder.tv_dish_name.setText(pojo.itemName)
        holder.tv_qty.setText(pojo.quantity.toString())

        if (pojo.itemChoices.isNotEmpty()) {
            holder.tv_add_on.visibility = VISIBLE
            var item_sub_list =
                ArrayList<HomeModel.Data.OrderDetail.ItemChoice.ItemSubCategory>()
            for (i in 0 until pojo.itemChoices.size) {
                item_sub_list.addAll(pojo.itemChoices.get(i).itemSubCategory)
            }
            holder.rv_dishItem.layoutManager = LinearLayoutManager(holder.itemView.context)
            holder.rv_dishItem.adapter = OnGoingDishTypeAdapter(item_sub_list)
        } else
            holder.tv_add_on.visibility = GONE




        for (i in 0 until list.size) {
            holder.tv_snum.text = (i + 1).toString() + "."

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_dish_name = itemView.tv_dish_name
        val tv_qty = itemView.tv_qty
        val tv_snum = itemView.tv_snum
        val rv_dishItem = itemView.rv_dishItem
        val tv_add_on = itemView.tv_add_on
    }
}