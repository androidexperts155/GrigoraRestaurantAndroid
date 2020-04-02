package com.rvtechnologies.grigorahq.reasons

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.services.models.ReasonsModel
import com.rvtechnologies.grigorahq.utils.IOnRecyclerItemClick

class ReasonsAdapter(var list: ArrayList<ReasonsModel.Data>, var listner: IOnRecyclerItemClick) :
    RecyclerView.Adapter<ReasonsAdapter.ViewHolder>() {


    private var mSelectedItem = -1

    init {
        var mlist = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custo_reasons, parent, false
            )
        )

    }


    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var pojo = list.get(position)
            holder.tv_reasons.text = pojo.message
            holder.tv_reasons.setOnClickListener {
                mSelectedItem = holder.adapterPosition
                listner.onClick(pojo, pojo.id)
                pojo.isChecked = true
                notifyDataSetChanged()
            }
            holder.tv_reasons.isChecked = mSelectedItem == position
            if (holder.tv_reasons.isChecked) {
                holder.tv_reasons.setTextColor(holder.itemView.context.resources.getColor(R.color.quantum_googgreen))
//            holder.iv_tick.visibility = VISIBLE
            } else {
                holder.tv_reasons.setTextColor(holder.itemView.context.resources.getColor(R.color.grey))
//            holder.iv_tick.visibility = GONE
            }


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_reasons = itemView.findViewById<RadioButton>(R.id.tv_reasons)
        var iv_tick = itemView.findViewById<ImageView>(R.id.iv_tick)
        var rl_reasons = itemView.findViewById<RelativeLayout>(R.id.rl_reasons)
    }
}