package com.rvtechnologies.grigorahq.categories.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.services.models.DishModel
import com.rvtechnologies.grigorahq.utils.IAddSubCategory
import kotlinx.android.synthetic.main.custom_sub_item_cat.view.*

class ChildCategoryAdapter(
    val subcatPosition: Int,
    val list: ArrayList<DishModel.Data.ItemCategory.ItemSubCategory>,
    iAddSubCategory: Context
) :
    RecyclerView.Adapter<ChildCategoryAdapter.ViewHolder>() {

    private var listener: IAddSubCategory? = null

    init {
        listener = iAddSubCategory as IAddSubCategory
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subCat = list[position]
        holder.ed_item_cat.setText(subCat.name)
        holder.ed_item_price.setText(subCat.addOnPrice)
        holder.ed_item_price.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                subCat.addOnPrice = s.toString()

                listener!!.onAddOnUpdated(subcatPosition, position, subCat)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
        holder.ed_item_cat.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                subCat.name = s.toString()
                listener!!.onAddOnUpdated(subcatPosition, position, subCat)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
        holder.iv_minus_child.setOnClickListener {
            list.removeAt(position)
            notifyItemRangeRemoved(position, list.size)
            notifyDataSetChanged()

        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_sub_item_cat,
                parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ed_item_cat = itemView.ed_item_cat
        val ed_item_price = itemView.ed_item_price
        val iv_minus_child = itemView.iv_minus_child
    }

}