package com.rvtechnologies.grigorarestaurant.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.rvtechnologies.grigora.utils.IRecyclerItemClick
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.orders.adapter.OrderFoodAdapter
import com.rvtechnologies.grigorahq.services.models.RestaurantNewOrdersModel
import com.rvtechnologies.grigorahq.utils.CommonUtils.change24To12
import com.rvtechnologies.grigorahq.utils.CommonUtils.changeUTC_to_local
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.new_order_item.view.*
import kotlinx.android.synthetic.main.no_driver_assigned.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NewOrdersAdapter(
    val list: ArrayList<RestaurantNewOrdersModel.Data> = ArrayList(),
    var iRecyclerItemClick: IRecyclerItemClick
) :
    RecyclerView.Adapter<NewOrdersAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context

        var pojo = list.get(position)
        holder.textView5.setText(pojo.userName)
        holder.tv_order_id.setText(context.getString(R.string.order_id) + pojo.id)
        var orderTime = changeUTC_to_local(pojo.createdAt)
        holder.textView3.setText(
            holder.itemView.context.getString(R.string.date) + orderTime.substring(0, 10) + "\n" + context.getString(R.string.time) + change24To12(orderTime.substring(11,19)))
        holder.textView4.setText(context.getString(R.string.total) + context.getString(R.string.naira_sign) + pojo.finalPrice)
        holder.textView4.visibility = GONE
        holder.textView2.setText(pojo.deliveryAddress)
        holder.textView8.setText(context.getString(R.string.total_items) + pojo.quantity)


        val d = Date(pojo.timeRemaining * 1000L)
        val df = SimpleDateFormat("HH:mm:ss") // HH for 0-23

        df.setTimeZone(TimeZone.getTimeZone("GMT"))
        val time: String = df.format(d)


        when (pojo.orderStatus) {
            0 -> {
                if (pojo.orderType.equals("2")) {
                    holder.tv_order_type.text =
                        context.getString(R.string.order_type_customer_pickup)
                } else {
                    holder.tv_order_type.text =
                        context.getString(R.string.order_type_driver_service)
                }
                holder.ll_response.visibility = VISIBLE
                holder.tv_order_status.text=context.resources.getText(R.string.new_order)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.tv_order_status.setBackgroundColor(context.resources.getColor(R.color.new_,context.theme))
                }else{
                    holder.tv_order_status.setBackgroundColor(context.resources.getColor(R.color.new_))

                }

                holder.ll_dispatch.visibility = GONE
            }
            2 -> {
                holder.ll_response.visibility = GONE
                holder.ll_dispatch.visibility = VISIBLE
                holder.tv_timer.visibility = GONE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.tv_order_status.setBackgroundColor(context.resources.getColor(R.color.wating,context.theme))
                }else
                    holder.tv_order_status.setBackgroundColor(context.resources.getColor(R.color.wating))


                if (pojo.orderType.equals("2")) {
                    holder.tv_order_type.text =
                        context.getString(R.string.order_type_customer_pickup)
                    holder.tv_dispatch.text = context.resources.getString(R.string.start_preparing)
                    holder.tv_order_status.visibility = GONE
                    holder.tv_dispatch.setOnClickListener {
                        iRecyclerItemClick.onClick(
                            pojo,
                            "preparing",
                            holder.adapterPosition,
                            holder.tv_timer
                        ) // dispatch
                    }
                } else {
                    holder.tv_order_status.visibility = VISIBLE
                    holder.tv_order_status.text=context.resources.getText(R.string.wait_driver_caps)
                    holder.tv_order_type.text =
                        context.getString(R.string.order_type_driver_service)
                    holder.tv_dispatch.text = context.resources.getString(R.string.wait_driver)
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

                    holder.tv_dispatch.setOnClickListener {
                        val mDialogView =
                            LayoutInflater.from(context).inflate(R.layout.no_driver_assigned, null)
                        val mBuilder = AlertDialog.Builder(context)
                            .setView(mDialogView)

                        val mAlertDialog = mBuilder.show()
                        mAlertDialog.getWindow()!!
                            .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        mAlertDialog.tv_title.setText(context.resources.getString(R.string.no_driver_assigned))
                        mAlertDialog.tv_msg.setText(context.resources.getString(R.string.no_any_driver_has_been_accept_this_order_yet))
                        mAlertDialog.tv_ok.setOnClickListener {
                            mAlertDialog.dismiss()
                        }

                    }
                }


            }
            3 -> {
                holder.ll_response.visibility = GONE
                holder.ll_dispatch.visibility = VISIBLE
                holder.tv_timer.visibility = GONE
                holder.tv_order_status.text=context.resources.getText(R.string.ongoing_order)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.tv_order_status.setBackgroundColor(context.resources.getColor(R.color.ongoing,context.theme))
                }else
                    holder.tv_order_status.setBackgroundColor(context.resources.getColor(R.color.ongoing))


                if (pojo.orderType.equals("2")) {
                    holder.tv_order_type.text =
                        context.getString(R.string.order_type_customer_pickup)
                } else {
                    holder.tv_order_type.text =context.getString(R.string.order_type_driver_service)
                    holder.tv_dispatch.text = context.resources.getString(R.string.start_preparing)
                    holder.tv_dispatch.setOnClickListener {
                        iRecyclerItemClick.onClick(
                            pojo,
                            "preparing",
                            holder.adapterPosition,
                            holder.tv_timer
                        ) // dispatch
                    }
                }
            }
            4 -> {
                holder.tv_timer.visibility = GONE
                holder.tv_order_status.text=context.resources.getText(R.string.ongoing_order)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.tv_order_status.setBackgroundColor(context.resources.getColor(R.color.ongoing,context.theme))
                }else
                    holder.tv_order_status.setBackgroundColor(context.resources.getColor(R.color.ongoing))


                if (pojo.orderType.equals("2")) {
                    holder.tv_order_type.text =
                        context.getString(R.string.order_type_customer_pickup)
                } else {
                    holder.ll_response.visibility = GONE
                    holder.ll_dispatch.visibility = VISIBLE
                    holder.tv_order_type.text =
                        context.getString(R.string.order_type_driver_service)

                    holder.tv_dispatch.text = context.resources.getString(R.string.order_picked_up)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        holder.tv_dispatch.setBackgroundColor(
                            context.resources.getColor(
                                R.color.quantum_orange,
                                context.theme
                            )
                        )
                    } else {
                        holder.tv_dispatch.setBackgroundColor(
                            context.resources.getColor(
                                R.color.quantum_orange
                            )
                        )
                    }
                }

            }

            7 -> {
                holder.ll_response.visibility = GONE
                holder.ll_dispatch.visibility = VISIBLE
                holder.tv_order_status.text=context.resources.getText(R.string.ongoing_order)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.tv_order_status.setBackgroundColor(context.resources.getColor(R.color.ongoing,context.theme))
                }else
                    holder.tv_order_status.setBackgroundColor(context.resources.getColor(R.color.ongoing))


                holder.tv_timer.visibility = GONE
                if (pojo.orderType.equals("2")) {
                    holder.tv_order_type.text =
                        context.getString(R.string.order_type_customer_pickup)
                    holder.tv_dispatch.setText(context.resources.getString(R.string.dispatched))
                    holder.tv_dispatch.setOnClickListener {
                        val mDialogView =
                            LayoutInflater.from(context).inflate(R.layout.no_driver_assigned, null)
                        val mBuilder = AlertDialog.Builder(context)
                            .setView(mDialogView)

                        val mAlertDialog = mBuilder.show()
                        mAlertDialog.getWindow()!!
                            .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        mAlertDialog.tv_title.setText(context.getString(R.string.order_dispatched))
                        mAlertDialog.tv_msg.setText(context.getString(R.string.you_have_already_dispatched_this_order))
                        mAlertDialog.tv_ok.setOnClickListener {
                            mAlertDialog.dismiss()
                        }
                    }
//                    holder.tv_dispatch.setOnClickListener {
//                        iRecyclerItemClick.onClick(
//                            pojo,
//                            "complete",
//                            holder.adapterPosition
//                        ) // dispatch
//                    }
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
                } else {
                    holder.tv_order_type.text =
                        context.getString(R.string.order_type_driver_service)
                    holder.tv_dispatch.setText(context.getString(R.string.dispatched))

                    holder.tv_dispatch.setOnClickListener {
                        val mDialogView =
                            LayoutInflater.from(context).inflate(R.layout.no_driver_assigned, null)
                        val mBuilder = AlertDialog.Builder(context)
                            .setView(mDialogView)

                        val mAlertDialog = mBuilder.show()
                        mAlertDialog.getWindow()!!
                            .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        mAlertDialog.tv_title.setText(context.getString(R.string.order_dispatched))
                        mAlertDialog.tv_msg.setText(context.getString(R.string.you_have_already_dispatched_this_order))
                        mAlertDialog.tv_ok.setOnClickListener {
                            mAlertDialog.dismiss()
                        }
                    }
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
            9 -> {
                holder.ll_response.visibility = GONE
                holder.ll_dispatch.visibility = VISIBLE
                holder.tv_timer.visibility = VISIBLE
                holder.tv_order_type.visibility = VISIBLE
                holder.tv_order_status.text=context.resources.getText(R.string.ongoing_order)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.tv_order_status.setBackgroundColor(context.resources.getColor(R.color.ongoing,context.theme))
                }else
                    holder.tv_order_status.setBackgroundColor(context.resources.getColor(R.color.ongoing))

                if (pojo.orderType.equals("2")) {
                    holder.tv_order_type.text = context.getString(R.string.order_type_customer_pickup)
                } else {
                    holder.tv_order_type.text = context.getString(R.string.order_type_driver_service)
                }
                countdown(
                    pojo.timeRemaining.toLong(),
                    holder.tv_timer,
                    holder.tv_dispatch,
                    context,
                    pojo,
                    holder.adapterPosition
                )

                holder.tv_dispatch.text = context.resources.getString(R.string.ready_to_dispatch)
                holder.tv_dispatch.setOnClickListener {
                    iRecyclerItemClick.onClick(
                        pojo,
                        "dispatch",
                        holder.adapterPosition,
                        holder.tv_timer
                    ) // dispatch
                }
            }
        }

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
            holder.ll_special.visibility = GONE
        } else {
            holder.tv_note.setText(pojo.deliveryNote)
        }
        holder.rvOrder.adapter =
            OrderFoodAdapter(pojo.orderDetails)


        holder.button4.setOnClickListener {

            iRecyclerItemClick.onItemClick(pojo, "1", holder.button4) //accepted
        }
        holder.button5.setOnClickListener {

            iRecyclerItemClick.onItemClick(pojo, "2", holder.button5) // rejected

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
        val textView4 = itemView.textView4
        val button4 = itemView.button4
        val button5 = itemView.button5
        val textView5 = itemView.textView5
        val textView3 = itemView.textView3
        val textView2 = itemView.textView2
        val textView8 = itemView.textView8
        val profilePic = itemView.profilePic
        val tv_timer = itemView.tv_timer
        val tv_dispatch = itemView.tv_dispatch
        val ll_dispatch = itemView.ll_dispatch
        val ll_response = itemView.ll_response
        val tv_note = itemView.tv_note
        val ll_special = itemView.ll_special
        val tv_order_id = itemView.tv_order_id
        val tv_order_type = itemView.tv_order_type
        val tv_order_status = itemView.tv_order_status
    }


    fun countdown(
        time: Long,
        textView: TextView,
        tv_dispatch: TextView,
        context: Context,
        pojo: RestaurantNewOrdersModel.Data,
        adapterPosition: Int
    ) {

        if (time == 0L) {
            textView.text = context.getString(R.string.time_over_add_time)
            textView.setOnClickListener {

                iRecyclerItemClick.onClick(
                    pojo,
                    "add timer",
                    adapterPosition, textView
                )
            }
        }
        object : CountDownTimer(time * 1000, 1000) {
            override fun onFinish() {
                if (pojo.update_preparing_time.equals("1")) {

                    textView.text = context.getString(R.string.time_over_add_time)
                    textView.setOnClickListener {
                        iRecyclerItemClick.onClick(
                            pojo,
                            "add_timer_reached",
                            adapterPosition,
                            textView
                        )
                    }
                } else {
                    textView.text = context.getString(R.string.time_over_add_time)
                    textView.setOnClickListener {

                        iRecyclerItemClick.onClick(
                            pojo,
                            "add_timer",
                            adapterPosition,textView
                        )
                    }
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000) % 60;
                val minutes = ((millisUntilFinished / (1000 * 60)) % 60);
                val hours = ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                val newtime = "" + hours + ":" + minutes + ":" + seconds;

                if (newtime.equals("0:0:0")) {
                    textView.text = context.getString(R.string.time_left) + "00:00:00"
                } else if ((hours.toString().length == 1) && (minutes.toString().length == 1) && (seconds.toString().length == 1)) {
                    textView.text =
                        context.getString(R.string.time_left) + "0" + hours + ":0" + minutes + ":0" + seconds
                } else if ((hours.toString().length == 1) && minutes.toString().length == 1) {
                    textView.text =
                        context.getString(R.string.time_left) + "0" + hours + ":0" + minutes + ":" + seconds
                } else if (hours.toString().length == 1 && seconds.toString().length == 1) {
                    textView.text =
                        context.getString(R.string.time_left) + "0" + hours + ":" + minutes + ":0" + seconds
                } else if (minutes.toString().length == 1 && seconds.toString().length == 1) {
                    textView.text =
                        context.getString(R.string.time_left) + hours + ":0" + minutes + ":0" + seconds
                } else if (hours.toString().length == 1) {
                    textView.text =
                        context.getString(R.string.time_left) + "0" + hours + ":" + minutes + ":" + seconds
                } else if (minutes.toString().length == 1) {
                    textView.text =
                        context.getString(R.string.time_left) + hours + ":0" + minutes + ":" + seconds
                } else if (seconds.toString().length == 1) {
                    textView.text =
                        context.getString(R.string.time_left) + hours + ":" + minutes + ":0" + seconds
                } else {
                    textView.text =
                        context.getString(R.string.time_left) + hours + ":" + minutes + ":" + seconds
                }


            }

        }.start();

    }


}
