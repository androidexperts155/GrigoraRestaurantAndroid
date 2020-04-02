package com.rvtechnologies.grigorahq.categories.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.services.models.DishModel
import com.rvtechnologies.grigorahq.utils.IAddCategory

class ItemCatAdapter(val list: ArrayList<DishModel.Data.ItemCategory>, val context: Context) :
    RecyclerView.Adapter<ItemCatAdapter.ViewHolder>() {

    private var listener: IAddCategory? = null

    init {
        listener = context as IAddCategory
    }

//    listener!!.clicked(date)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_item_cat,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rv_child_cat.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.rv_child_cat.adapter =
            ChildCategoryAdapter(position, list[position].itemSubCategory, context)

        holder.rv_child_cat.isNestedScrollingEnabled = false
        val category = list.get(position)

        val subCatList = category.itemSubCategory


        holder.ed_item_cat.setText(category.name)

        holder.ed_item_cat.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                category.name = s.toString()
                listener!!.onCategoryUpdated(position, category)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        if (category.selection.equals("1")) {
            holder.rb_single.isChecked = true
        } else {
            holder.rb_multilple.isChecked = true
        }

//
        holder.iv_select_child.setOnClickListener {
            holder.iv_select_remove_child.visibility = VISIBLE

            listener!!.onAdOnAdded(position)


        }

        holder.iv_select_remove_child.setOnClickListener {
            listener?.onItemRemove(position)


        }
        holder.rg_selection.setOnCheckedChangeListener { group, checkedId ->
            category.selection = if (checkedId == R.id.rb_single) "1" else "2"
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ed_item_cat = itemView.findViewById<EditText>(R.id.ed_item_cat)
        var iv_select_child = itemView.findViewById<TextView>(R.id.iv_select_child)
        var iv_select_remove_child = itemView.findViewById<ImageView>(R.id.iv_select_remove_child)
        var rv_child_cat = itemView.findViewById<RecyclerView>(R.id.rv_child_cat)
        var rg_selection = itemView.findViewById<RadioGroup>(R.id.rg_selection)
        var rb_single = itemView.findViewById<RadioButton>(R.id.rb_single)
        var rb_multilple = itemView.findViewById<RadioButton>(R.id.rb_multilple)

    }

}