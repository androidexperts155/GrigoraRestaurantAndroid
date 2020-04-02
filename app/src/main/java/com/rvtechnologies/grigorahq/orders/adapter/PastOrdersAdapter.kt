package com.rvtechnologies.grigorahq.orders.adapter

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.rvtechnologies.grigora.utils.IRecyclerItemClick
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.orders.details.NewOrderDetails
import com.rvtechnologies.grigorahq.services.models.RestaurantPastOrders
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.new_order_item.view.*


class PastOrdersAdapter(
    val list: ArrayList<RestaurantPastOrders.Data> = ArrayList(),
    var iRecyclerItemClick: IRecyclerItemClick
) : RecyclerView.Adapter<PastOrdersAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var pojo = list.get(position)
        val context = holder.itemView.context
        val circularProgressDrawable = CircularProgressDrawable(context!!)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        holder.tv_timer.visibility = GONE
        holder.textView5.setText(pojo.userName)
        holder.tv_order_id.setText(context.getString(R.string.order_id) + pojo.id)
        holder.textView4.setText(context.getString(R.string.total) + " ₦ " + pojo.finalPrice)
        val deliverTime = CommonUtils.changeUTC_to_local(pojo.updatedAt)

        holder.textView3.setText(
            context.getString(R.string.date) + deliverTime.substring(
                0,
                10
            ) + "\n" + context.getString(R.string.time) + deliverTime.substring(
                11,
                19
            )
        )
        if (pojo.order_type.equals("1")) {
            holder.tv_order_type.text =
                context.getText(R.string.order_type_driver_service)
        } else {
            holder.tv_order_type.text =
                context.getText(R.string.order_type_customer_pickup)

        }



        holder.ll_response.visibility = GONE
        holder.ll_special.visibility = GONE
        holder.textView2.setText(pojo.deliveryAddress)
        holder.textView8.setText(context.getString(R.string.total_items) + pojo.quantity.toString())
        if (pojo.orderDetails != null)
            holder.rvOrder.adapter =
                PastOrderFoodAdapter(pojo.orderDetails)

        if (pojo.userImage.isNullOrEmpty()) {
            Glide.with(context)
                .load(context.getDrawable(R.drawable.ic_user_profile))
                .into(holder.profilePic)

        } else
            Picasso.get()
                .load(pojo.userImage).placeholder(circularProgressDrawable)
                .error(
                    circularProgressDrawable
                )
                .into(holder.profilePic)

        holder.textView11.setOnClickListener {
            val context = context
            context.startActivity(
                Intent(context, NewOrderDetails::class.java).putExtra(
                    "pojo_extra",
                    pojo
                ).putExtra("type", "past")
            )
        }
        when (pojo.orderStatus) {
            5 -> {
                holder.tv_order_status.text = context.resources.getString(R.string.completed_order)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.tv_order_status.setBackgroundColor(context.getColor(R.color.completed))
                } else {
                    holder.tv_order_status.setBackgroundColor(context.getColor(R.color.completed))
                }
            }
            6 -> {
                holder.tv_order_status.text = context.resources.getString(R.string.rejected_by_rest)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.tv_order_status.setBackgroundColor(context.getColor(R.color.rejected_rest))
                } else {
                    holder.tv_order_status.setBackgroundColor(context.getColor(R.color.rejected_rest))
                }
            }
            8 -> {
                holder.tv_order_status.text = context.resources.getString(R.string.cancel_by_cus)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.tv_order_status.setBackgroundColor(context.getColor(R.color.cancel_cust))
                } else {
                    holder.tv_order_status.setBackgroundColor(context.getColor(R.color.cancel_cust))
                }
            }
        }


    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(p0.context).inflate(
                R.layout.new_order_item,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rvOrder = itemView.rvOrder
        val textView8 = itemView.textView8
        val tv_order_id = itemView.tv_order_id
        val textView4 = itemView.textView4
        val textView5 = itemView.textView5
        val textView3 = itemView.textView3
        val textView11 = itemView.textView11
        val textView2 = itemView.textView2
        val ll_response = itemView.ll_response
        val ll_special = itemView.ll_special
        val tv_order_type = itemView.tv_order_type
        val profilePic = itemView.profilePic
        val tv_timer = itemView.tv_timer
        val tv_order_status = itemView.tv_order_status
    }

}