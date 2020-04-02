package com.rvtechnologies.grigorarestaurant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rvtechnologies.grigora.utils.IRecyclerItemClick
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.orders.adapter.OnGoingOrderFoodAdapter
import com.rvtechnologies.grigorahq.services.models.RestaurantOnGoingOrdersModel
import kotlinx.android.synthetic.main.new_order_item.view.*


class OngoingOrdersAdapter(
    val list: ArrayList<RestaurantOnGoingOrdersModel.Data> = ArrayList()
    ,
    var iRecyclerItemClick: IRecyclerItemClick
) : RecyclerView.Adapter<OngoingOrdersAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var pojo = list.get(position)

        holder.textView5.setText(pojo.userName)
        holder.tv_order_id.setText(holder.itemView.context.getString(R.string.order_id) + " : " + pojo.id)
        holder.textView4.setText(holder.itemView.context.getString(R.string.total) + " ₦ " + pojo.finalPrice)
        holder.textView8.setText(holder.itemView.context.getString(R.string.total_items) + pojo.quantity.toString())
        holder.textView2.setText(pojo.deliveryAddress)
        holder.textView3.setText(
            holder.itemView.context.getString(R.string.date) + pojo.createdAt.substring(
                0,
                10
            )
        )

        Glide.with(holder.itemView.context).load(pojo.userImage)
            .apply(RequestOptions.circleCropTransform()).into(holder.profilePic)


//
        if (pojo.orderDetails != null)
            holder.rvOrder.adapter =
                OnGoingOrderFoodAdapter(pojo.orderDetails)

        holder.textView11.setOnClickListener {
            val context = holder.itemView.context
//            context.startActivity(
//                Intent(
//                    context,
//                    NewOrderDetails::class.java
//                ).putExtra("pojo_extra", pojo).putExtra("type", "going")
//            )
        }
//        holder.ll_order_ongoing_despached.visibility = VISIBLE
//        holder.ll_order_ongoing_despached.setOnClickListener {
//            iRecyclerItemClick.onClick(pojo)
//        }
//        if (pojo.dispatch.equals("1")) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                holder.tv_order_ongoing_despached.setBackgroundColor(
//                    holder.itemView.context.resources.getColor(
//                        R.color.text_primary,
//                        holder.itemView.context.theme
//                    )
//                )
//                holder.tv_order_ongoing_despached.setTextColor(
//                    holder.itemView.context.resources.getColor(
//                        R.color.white,
//                        holder.itemView.context.theme
//                    )
//                )
//            } else {
//                holder.tv_order_ongoing_despached.setBackgroundColor(
//                    holder.itemView.context.resources.getColor(
//                        R.color.text_primary
//                    )
//                )
//                holder.tv_order_ongoing_despached.setTextColor(
//                    holder.itemView.context.resources.getColor(
//                        R.color.white
//                    )
//                )
//            }
//
//            holder.ll_order_ongoing_despached.isClickable = false
//        }
//
//        if (pojo.orderStatus.equals("4")) {
//            holder.ll_order_ongoing.visibility = VISIBLE
//            holder.ll_order_ongoing_despached.visibility = GONE
//        }

        iRecyclerItemClick
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
        val textView11 = itemView.textView11
        val button4 = itemView.button4
        val button5 = itemView.button5
        val textView5 = itemView.textView5
        val textView3 = itemView.textView3
        val textView8 = itemView.textView8
        //        val ll_order_ongoing = itemView.ll_order_ongoing
//        val ll_order_ongoing_despached = itemView.ll_order_ongoing_despached
//        val tv_order_ongoing_despached = itemView.tv_order_ongoing_despached
        val textView4 = itemView.textView4
        val tv_order_id = itemView.tv_order_id
        val textView2 = itemView.textView2
        val profilePic = itemView.profilePic
    }

}