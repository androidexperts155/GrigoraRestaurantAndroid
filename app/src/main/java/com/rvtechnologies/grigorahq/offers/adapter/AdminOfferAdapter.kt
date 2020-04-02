package com.rvtechnologies.grigorahq.offers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.interfaces.IAdminOfferSelection
import com.rvtechnologies.grigorahq.services.models.AdminOfferPojo
import com.squareup.picasso.Picasso

class AdminOfferAdapter(
    var list: ArrayList<AdminOfferPojo.Data>,
    var listner: IAdminOfferSelection
) :
    RecyclerView.Adapter<AdminOfferAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_offer,
                parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var pojo = list.get(position)
        val circularProgressDrawable = CircularProgressDrawable(holder.itemView.context!!)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Picasso.get()
            .load(pojo.image).placeholder(
                circularProgressDrawable
            )
            .error(
                circularProgressDrawable
            )
            .into(holder.iv_offer)

//        Glide.with(holder.itemView.context).load(pojo.image).into(holder.iv_offer)
        if (pojo.applied) {
            holder.iv_check.visibility = VISIBLE
        } else {
            holder.iv_check.visibility = GONE
        }
        holder.itemView.setOnClickListener {
            listner.onItemClick(pojo)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_offer = itemView.findViewById<ImageView>(R.id.imageView2)
        var iv_check = itemView.findViewById<ImageView>(R.id.iv_check)
    }


}