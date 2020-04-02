package com.rvtechnologies.grigorahq.orders.adapter

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.rvtechnologies.grigora.utils.IRecyclerItemClick
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.services.models.RestaurantNewOrdersModel
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.CommonUtils.change24To12
import com.rvtechnologies.grigorahq.utils.CommonUtils.changeUTC_to_local
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_shedule_order.view.*
import kotlinx.android.synthetic.main.no_driver_assigned.*

class ScheduleOrderAdapter(
    val list: ArrayList<RestaurantNewOrdersModel.Data> = ArrayList(),
    var iRecyclerItemClick: IRecyclerItemClick
) :
    RecyclerView.Adapter<ScheduleOrderAdapter.ViewHolder>() {


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context

        var pojo = list.get(position)
        holder.textView5.setText(pojo.userName)
        holder.tv_order_id.setText(context.getString(R.string.order_id) + pojo.id)
        var scheduleTime = changeUTC_to_local(pojo.scheduleTime)
        Log.e("sch_Time", scheduleTime)
        holder.textView3.setText(context.getString(R.string.deliver_on) + scheduleTime.substring(0, 10) + "," + CommonUtils.change24To12(context, scheduleTime.substring(11, 19)))
        holder.textView4.setText(context.getString(R.string.total) + context.getString(R.string.naira_sign) + pojo.finalPrice)
        holder.textView4.visibility = View.GONE
        holder.textView2.setText(pojo.deliveryAddress)
        holder.textView8.setText(context.getString(R.string.total_items) + pojo.quantity)
        var orderTime = changeUTC_to_local(pojo.createdAt)
        Log.e("sch_orderTime", orderTime)
        Log.e("sch_orderTimeChange", change24To12(context, orderTime.substring(11, 19)))
        holder.tv_order_type.text = context.getString(R.string.order_on) + orderTime.substring(0,10) + "," + CommonUtils.change24To12(context, orderTime.substring(11, 19))
        when (pojo.orderStatus) {
            0 -> {

                holder.ll_response.visibility = View.VISIBLE
                holder.tv_dispatch.visibility = View.GONE
            }
            2 -> {

                    holder.ll_response.visibility = View.GONE
                    holder.ll_dispatch.visibility = View.VISIBLE
                    holder.tv_dispatch.text = context.resources.getString(R.string.cancel_order)
                    holder.tv_dispatch.setOnClickListener {
                        iRecyclerItemClick.onItemClick(pojo, "2", holder.button5) // rejected
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        holder.tv_dispatch.setBackgroundColor(
                            context.resources.getColor(
                                R.color.colorAccent,
                                context.theme
                            )
                        )
                    else
                        holder.tv_dispatch.setBackgroundColor(
                            context.resources.getColor(
                                R.color.colorAccent
                            )
                        )


            }
            3 -> {
                holder.ll_response.visibility = View.GONE
                holder.ll_dispatch.visibility = View.VISIBLE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.tv_dispatch.setBackgroundColor(
                        context.resources.getColor(
                            R.color.quantum_googgreen400,
                            context.theme
                        )
                    )
                } else {
                    holder.tv_dispatch.setBackgroundColor(
                        context.resources.getColor(
                            R.color.quantum_googgreen400
                        )
                    )
                }
                holder.tv_dispatch.setOnClickListener {
                    iRecyclerItemClick.onClick(pojo, "dispatch", holder.adapterPosition, holder.tv_dispatch) // dispatch
                }
            }
            4 -> {
                holder.ll_response.visibility = View.GONE
                holder.ll_dispatch.visibility = View.VISIBLE
                holder.tv_dispatch.text=context.resources.getString(R.string.order_picked_up)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.tv_dispatch.setBackgroundColor(
                        context.resources.getColor(
                            R.color.quantum_orange,
                            context.theme))
                }
                else {
                    holder.tv_dispatch.setBackgroundColor(
                        context.resources.getColor(
                            R.color.quantum_orange
                        )
                    )
                }
            }
            7 -> {
                holder.ll_response.visibility = View.GONE
                holder.ll_dispatch.visibility = View.VISIBLE
                holder.tv_dispatch.text = context.resources.getString(R.string.dispatched)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    holder.tv_dispatch.setBackgroundColor(
                        context.resources.getColor(
                            R.color.grey,
                            context.theme
                        )
                    )
                else
                    holder.tv_dispatch.setBackgroundColor(
                        context.resources.getColor(
                            R.color.grey
                        )
                    )
            }
        }

//        if (pojo.dispatch == 1) {
//            holder.ll_response.visibility = View.GONE
//            holder.tv_dispatch.visibility = View.VISIBLE
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//                holder.tv_dispatch.setBackgroundColor(
//                    context.resources.getColor(
//                        R.color.grey,
//                        context.theme
//                    )
//                )
//            else
//                holder.tv_dispatch.setBackgroundColor(context.resources.getColor(R.color.grey))
//            holder.tv_dispatch.setText(context.getString(R.string.dispatched))
//            holder.tv_dispatch.setOnClickListener {
//                val mDialogView =
//                    LayoutInflater.from(context).inflate(R.layout.no_driver_assigned, null)
//                val mBuilder = AlertDialog.Builder(context)
//                    .setView(mDialogView)
//
//                val mAlertDialog = mBuilder.show()
//                mAlertDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                mAlertDialog.tv_title.setText(context.getString(R.string.order_dispatched))
//                mAlertDialog.tv_msg.setText(context.getString(R.string.you_have_already_dispatched_this_order))
//                mAlertDialog.tv_ok.setOnClickListener {
//                    mAlertDialog.dismiss()
//                }
//            }
//        } else {
//            holder.tv_dispatch.visibility = View.VISIBLE
//        }

        val circularProgressDrawable = CircularProgressDrawable(holder.itemView.context!!)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        if (pojo.userImage.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(holder.itemView.context.getDrawable(R.drawable.ic_user_profile))
                .into(holder.profilePic)
        } else

            Picasso.get()
                .load(pojo.userImage).placeholder(
                    circularProgressDrawable
                )
                .error(
                    circularProgressDrawable
                )
                .into(holder.profilePic)


//        holder.ll_new.visibility = VISIBLE

        if (pojo.deliveryNote.isNullOrEmpty()) {
            holder.ll_special.visibility = View.GONE
        } else {
            holder.tv_note.setText(pojo.deliveryNote)
        }
        holder.rvOrder.adapter =
            OrderFoodAdapter(pojo.orderDetails)


//        holder.itemView.setOnClickListener {
//            context.startActivity(
//                Intent(
//                    context,
//                    NewOrderDetails::class.java
//                ).putExtra("pojo_extra", pojo).putExtra("type", "new")
//            )
//        }
        holder.button4.setOnClickListener {

            iRecyclerItemClick.onItemClick(pojo, "1", holder.button4) //accepted
        }
        holder.button5.setOnClickListener {

            iRecyclerItemClick.onItemClick(pojo, "2", holder.button5) // rejected

        }



        iRecyclerItemClick
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(p0.context).inflate(
                R.layout.custom_shedule_order,
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
        val textView4 = itemView.textView4
        val button4 = itemView.button4
        val button5 = itemView.button5
        val textView5 = itemView.textView5
        val textView3 = itemView.textView3
        val textView2 = itemView.textView2
        val textView8 = itemView.textView8
        val profilePic = itemView.profilePic
        val textView11 = itemView.textView11
        val tv_dispatch = itemView.tv_dispatch
        val ll_dispatch = itemView.ll_dispatch
        val ll_response = itemView.ll_response
        val tv_note = itemView.tv_note
        val ll_special = itemView.ll_special
        val tv_order_id = itemView.tv_order_id
        val tv_order_type = itemView.tv_order_type
    }


}