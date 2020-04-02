package com.rvtechnologies.grigorahq.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.services.models.ChooseCatPojo
import com.rvtechnologies.grigorahq.services.models.CusinesModel

class ChooseCatAdapter(val click: RecClick, val list: ArrayList<CusinesModel.Data>) :
    RecyclerView.Adapter<ChooseCatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseCatAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_choose_category,
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
        Glide.with(holder.itemView.context).load(pojo.icon).into(holder.iv_cat)
        holder.tv_cat_dish.text = pojo.name
//        holder.ll_cat.setBackgroundResource(pojo.bg_color)
        holder.ll_cat.setOnClickListener {
            click.click(position)
        }
        if (pojo.isChecked) {
//            holder.iv_cat.setColorFilter(holder.itemView.context.resources.getColor(R.color.white))
            holder.tv_cat_dish.setTextColor(holder.itemView.context.resources.getColor(R.color.colorAccent))
//            holder.ll_cat.setBackgroundResource(R.drawable.selected_cat_bg)
        } else {
//            holder.iv_cat.setColorFilter(R.color.transculent_black)
            holder.tv_cat_dish.setTextColor(holder.itemView.context.resources.getColor(R.color.textBlack))
//            holder.ll_cat.setBackgroundResource(R.drawable.category_unselect_bg)
        }

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_cat = itemView.findViewById<ImageView>(R.id.iv_cat)
        var tv_cat_dish = itemView.findViewById<TextView>(R.id.tv_cat_dish)
        var ll_cat = itemView.findViewById<LinearLayout>(R.id.ll_cat)
    }

}