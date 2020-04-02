package com.rvtechnologies.grigorahq.complete_profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.services.models.CusinesModel
import com.rvtechnologies.grigorahq.utils.IOnRecyclerItemClick

class CusinesAdapter(
    val list: ArrayList<CusinesModel.Data>,
    var listner: IOnRecyclerItemClick
) :
    RecyclerView.Adapter<CusinesAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var pojo = list.get(position)
        Glide.with(holder.itemView.context).load(pojo.image).into(holder.iv_img)
        holder.tv_cusine_name.setText(pojo.name)
        holder.chk_cusine.isChecked = pojo.isChecked

        holder.itemView.setOnClickListener {

            listner.onClick(pojo, holder.adapterPosition)


        }
        holder.chk_cusine.setOnClickListener {
            listner.onClick(pojo, holder.adapterPosition)
        }

        holder.itemView.setOnLongClickListener(View.OnLongClickListener {


            return@OnLongClickListener true
        })


    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(p0.context).inflate(
                R.layout.custom_cusines,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_img = itemView.findViewById<ImageView>(R.id.iv_img)
        var tv_cusine_name = itemView.findViewById<TextView>(R.id.tv_cusine_name)
        var chk_cusine = itemView.findViewById<CheckBox>(R.id.chk_cusine)
    }
}
