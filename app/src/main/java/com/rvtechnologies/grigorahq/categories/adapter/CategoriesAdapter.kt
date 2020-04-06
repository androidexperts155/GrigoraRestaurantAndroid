package com.rvtechnologies.grigorahq.categories.adapter

import android.content.res.ColorStateList
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.services.models.DishModel
import com.rvtechnologies.grigorahq.utils.IOnRecyclerItemClick


class CategoriesAdapter(
    var list: ArrayList<DishModel.Data>,
    var listner: IOnRecyclerItemClick
) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var pojo = list.get(position)
        Glide.with(holder.itemView.context).load(pojo.image)
            .apply(RequestOptions.placeholderOf(R.drawable.loding)).into(holder.iv_car)
        holder.tv_dish.setText(pojo.name)
        holder.tv_price.setText(holder.itemView.context.getString(R.string.price) + pojo.price)
        if (pojo.status.equals("0")) {


            if (pojo.pureVeg.equals("1")) {
                holder.tv_food_type.setText(holder.itemView.context.getString(R.string.veg))
            } else if (pojo.pureVeg.equals("0")) {
                holder.tv_food_type.setText(holder.itemView.context.getString(R.string.non_veg))
            } else {
                holder.tv_food_type.setText(holder.itemView.context.getString(R.string.contain_egg))
            }
            holder.iv_avial.isChecked = false
            holder.tv_food_type.text
            holder.iv_car.alpha = 0.4f
            holder.tv_food_type.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.un_avail_radio),
                null,
                null,
                null
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.iv_feature.setTextColor(
                    holder.itemView.context.resources.getColor(
                        R.color.transculent_black,
                        holder.itemView.context.theme
                    )
                )
                holder.tv_price.setTextColor(
                    holder.itemView.context.resources.getColor(
                        R.color.transculent_black,
                        holder.itemView.context.theme
                    )
                )
                holder.tv_food_type.setTextColor(
                    holder.itemView.context.resources.getColor(
                        R.color.transculent_black,
                        holder.itemView.context.theme
                    )
                )
                holder.tv_dish.setTextColor(
                    holder.itemView.context.resources.getColor(
                        R.color.transculent_black,
                        holder.itemView.context.theme
                    )
                )
                CompoundButtonCompat.setButtonTintList(
                    holder.iv_feature, ColorStateList
                        .valueOf(
                            holder.itemView.context.resources.getColor(
                                R.color.transculent_black,
                                holder.itemView.context.theme
                            )
                        )
                );



            } else {
                holder.iv_feature.setTextColor(holder.itemView.context.resources.getColor(R.color.transculent_black))
                holder.tv_price.setTextColor(holder.itemView.context.resources.getColor(R.color.transculent_black))
                holder.tv_food_type.setTextColor(holder.itemView.context.resources.getColor(R.color.transculent_black))
                holder.tv_dish.setTextColor(holder.itemView.context.resources.getColor(R.color.transculent_black))


            }

        } else {
            if (pojo.pureVeg.equals("1")) {
                holder.tv_food_type.setText(holder.itemView.context.getString(R.string.veg))
                holder.tv_food_type.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.veg),
                    null,
                    null,
                    null
                )
            } else if (pojo.pureVeg.equals("0")) {
                holder.tv_food_type.setText(holder.itemView.context.getString(R.string.non_veg))
                holder.tv_food_type.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.non_veg),
                    null,
                    null,
                    null
                )
            } else {
                holder.tv_food_type.setText(holder.itemView.context.getString(R.string.contain_egg))
                holder.tv_food_type.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.egg_radio),
                    null,
                    null,
                    null
                )
            }
            holder.iv_avial.isChecked = true
        }


//        holder.tv_food_type.setText(pojo.pureVeg)
        holder.itemView.setOnClickListener {
            listner.onClick(pojo, holder.adapterPosition)
        }

        holder.iv_avial.setOnCheckedChangeListener { compoundButton, b ->
            if (b)
                listner.onSwitchAvail(pojo, 1)
            else {
                listner.onSwitchAvail(pojo, 0)

            }
        }

        holder.iv_feature.setOnCheckedChangeListener { compoundButton, b ->
            if (b)
                listner.onFeatured(pojo, 1)
            else {
                listner.onFeatured(pojo, 0)
            }
        }

        holder.iv_feature.isChecked = pojo.featured.equals("1")

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(p0.context).inflate(
                R.layout.menu_item,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_car = itemView.findViewById<ImageView>(R.id.profilePic)
        var tv_dish = itemView.findViewById<TextView>(R.id.textView10)
        var tv_food_type = itemView.findViewById<TextView>(R.id.tv_food_type)
        var tv_price = itemView.findViewById<TextView>(R.id.tv_price)
        var iv_avial = itemView.findViewById<SwitchCompat>(R.id.iv_avial)
        var iv_feature = itemView.findViewById<CheckBox>(R.id.iv_feature)
    }

    fun filterList(filterdNames: ArrayList<DishModel.Data>) {
        this.list = filterdNames
        notifyDataSetChanged()
    }

}