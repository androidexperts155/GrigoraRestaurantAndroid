package com.rvtechnologies.grigorahq.categories.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.interfaces.IChildAddOn
import com.rvtechnologies.grigorahq.services.models.AddDishModel
import kotlinx.android.synthetic.main.custom_add_ons.view.*

class ChildAddonAdapter(
    val subcatPosition: Int,
    val list: ArrayList<AddDishModel.Data.ItemCategory.ItemSubCategory.ItemSubSubCategory>,
    context: Context
) : RecyclerView.Adapter<ChildAddonAdapter.ViewHolder>() {

    private var listener: IChildAddOn? = null

    init {
        listener = context as IChildAddOn
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_add_ons,
                parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var pojo = list[position]
        holder.ed_item_cat.setText(pojo.name)
        holder.ed_item_price.setText(pojo.add_on_price)

        holder.ed_item_cat.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                pojo.name = s.toString()
                listener!!.onAddOnUpdated(subcatPosition, pojo)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ed_item_cat = itemView.ed_item_cat
        val ed_item_price = itemView.ed_item_price
    }
}