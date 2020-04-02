package com.rvtechnologies.grigorahq.ui.login_signup.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.services.models.GetBrandsModel
import com.rvtechnologies.grigorahq.utils.IOnRecyclerItemClick

class BrandsFillterAdapter(
    var list: ArrayList<GetBrandsModel.Data>,
    var listner: IOnRecyclerItemClick
) :
    RecyclerView.Adapter<BrandsFillterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BrandsFillterAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_brands,
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
        Glide.with(holder.itemView.context).load(pojo.image)
            .apply(RequestOptions.placeholderOf(R.drawable.loding)).into(holder.iv_logo)
        holder.tv_brand.setText(pojo.name)
        holder.itemView.setOnClickListener {
            listner.onClick(pojo, holder.adapterPosition)
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_logo = itemView.findViewById<ImageView>(R.id.iv_logo)
        var tv_brand = itemView.findViewById<TextView>(R.id.tv_brand)
    }

    fun filterList(filterName: ArrayList<GetBrandsModel.Data>) {
        this.list = filterName
        notifyDataSetChanged()
    }
}