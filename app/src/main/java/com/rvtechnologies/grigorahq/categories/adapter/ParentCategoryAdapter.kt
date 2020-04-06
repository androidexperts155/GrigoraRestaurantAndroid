package com.rvtechnologies.grigorahq.categories.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.services.models.GetParentCategoryModel
import com.rvtechnologies.grigorahq.utils.IOnRecyclerItemClick

class ParentCategoryAdapter(
    var list: ArrayList<GetParentCategoryModel.Data>,
    var listner: IOnRecyclerItemClick
) : RecyclerView.Adapter<ParentCategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_parent_categories,
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
        holder.tv_parent_level1.text = pojo.name
        holder.itemView.setOnClickListener {
            listner.onClick(pojo, holder.adapterPosition)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_parent_level1 = itemView.findViewById<TextView>(R.id.tv_parent_level1)
    }

}