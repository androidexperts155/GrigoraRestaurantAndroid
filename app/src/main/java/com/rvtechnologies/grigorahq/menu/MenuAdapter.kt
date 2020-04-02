package com.rvtechnologies.grigorahq.menu

import android.content.Intent
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.services.models.GetRestaurentItemsModel
import kotlinx.android.synthetic.main.dialog_layout.view.*


class MenuAdapter(val list: ArrayList<GetRestaurentItemsModel.Data>, var listner: IDeleteOnClick) :
    RecyclerView.Adapter<MenuAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var pojo = list.get(position)
        Glide.with(holder.itemView.context).load(pojo.image)
            .apply(RequestOptions.placeholderOf(R.drawable.loding)).into(holder.iv_car)
        holder.tv_dish.setText(pojo.name)
        if (pojo.inOffer.equals("0")) {
            holder.iv_offer.visibility = VISIBLE
            Glide.with(holder.itemView.context)
                .asGif()
                .load(R.drawable.inoffer)
                .into(holder.iv_offer);
        } else {
            holder.iv_offer.visibility = GONE

        }
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val factory = LayoutInflater.from(context)
            val dialogView = factory.inflate(R.layout.dialog_layout, null)
            val dialog = AlertDialog.Builder(context).create()
            dialog.setView(dialogView)

            Glide.with(context).load(pojo.image).into(dialogView.imageView3)
            dialogView.tv_actual_price.setText(context.getString(R.string.price) + " $ " + pojo.price)
            dialogView.tv_offer_price.setText(context.getString(R.string.offer_price) + " $ " + pojo.offerPrice)
            if (pojo.pureVeg == "0") {
                dialogView.iv_food_type.setImageResource(R.drawable.ic_food_veg)
            } else {
                dialogView.iv_food_type.setImageResource(R.drawable.ic_food_nonveg)
            }
            dialogView.textView13.setText(pojo.name)
            dialogView.textView15.setText(pojo.description)
            dialogView.textView15.setMovementMethod(ScrollingMovementMethod())



            dialogView.button7.setOnClickListener {
                dialog.dismiss()
                context.startActivity(
                    Intent(context, AddNewItem::class.java).putExtra(
                        "typeItem",
                        "edit"
                    ).putExtra("data", pojo)
                )
            }
            dialogView.button8.setOnClickListener {
                listner.deleteOnClick(pojo, dialog)
            }


            dialog.show()
        }

    }

    private fun setDeleteApi() {

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
        var iv_offer = itemView.findViewById<ImageView>(R.id.iv_avial)
    }

}