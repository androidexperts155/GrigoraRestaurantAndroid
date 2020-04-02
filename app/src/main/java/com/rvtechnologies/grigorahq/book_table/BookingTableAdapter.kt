package com.rvtechnologies.grigorahq.book_table

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rvtechnologies.grigora.utils.IRecyclerItemClick
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.services.models.AvailableBookingModel
import kotlinx.android.synthetic.main.custom_book_table.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class BookingTableAdapter(
    val list: ArrayList<AvailableBookingModel.Data> = ArrayList(),
    var iRecyclerItemClick: IRecyclerItemClick
) :
    RecyclerView.Adapter<BookingTableAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_book_table,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        var pojo = list.get(position)
        val startTime = pojo.startTimeFrom
        var endTime = pojo.startTimeTo
        holder.tv_book_time.setText(changeFormat(startTime) + " TO " + changeFormat(endTime))
        holder.tv_book_date.setText(pojo.date)
        holder.tv_no_guest.setText(pojo.noOfSeats.toString())
        holder.tv_customer.setText(pojo.customerName)
        holder.tv_mobile.setText(pojo.customerPhone)
        if (pojo.bookingStatus.equals("2")) {
            holder.tv_cancel.visibility = GONE
            holder.tv_confirm.text = context.getString(R.string.confirmed)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.tv_confirm.background =
                    context.resources.getDrawable(R.drawable.bg_red_tl_bl_tweleve_dp, context.theme)
                holder.tv_cancel.setBackgroundColor(
                    context.resources.getColor(
                        R.color.white, context.theme
                    )

                )
                holder.tv_confirm.setTextColor(
                    context.resources.getColor(
                        R.color.white,
                        context.theme
                    )
                )
                holder.tv_cancel.setTextColor(
                    context.resources.getColor(
                        R.color.text_primary,
                        context.theme
                    )
                )

            } else {
                holder.tv_confirm.background =
                    context.resources.getDrawable(R.drawable.bg_red_tl_bl_tweleve_dp)
                holder.tv_cancel.setBackgroundColor(
                    context.resources.getColor(
                        R.color.white
                    )
                )
                holder.tv_confirm.setTextColor(context.resources.getColor(R.color.white))
                holder.tv_cancel.setTextColor(context.resources.getColor(R.color.text_primary))
            }

        } else if (pojo.bookingStatus.equals("1")) {
            holder.tv_cancel.setOnClickListener {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.tv_confirm.setBackgroundColor(
                        context.resources.getColor(
                            R.color.white,
                            context.theme
                        )
                    )
                    holder.tv_cancel.background =
                        context.resources.getDrawable(
                            R.drawable.bg_red_tl_bl_tweleve_dp,
                            context.theme
                        )
                    holder.tv_confirm.setTextColor(context.resources.getColor(R.color.text_primary))
                    holder.tv_cancel.setTextColor(context.resources.getColor(R.color.white))
                } else {
                    holder.tv_confirm.setBackgroundColor(
                        context.resources.getColor(
                            R.color.white
                        )
                    )
                    holder.tv_cancel.background =
                        context.resources.getDrawable(R.drawable.bg_red_tl_bl_tweleve_dp)
                    holder.tv_confirm.setTextColor(context.resources.getColor(R.color.text_primary))
                    holder.tv_cancel.setTextColor(context.resources.getColor(R.color.white))
                }
                iRecyclerItemClick.onClick(pojo, "cancel", holder.adapterPosition, holder.tv_cancel)
            }
            holder.tv_confirm.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.tv_confirm.background =
                        context.resources.getDrawable(
                            R.drawable.bg_red_tl_bl_tweleve_dp,
                            context.theme
                        )
                    holder.tv_cancel.setBackgroundColor(
                        context.resources.getColor(
                            R.color.white, context.theme
                        )

                    )
                    holder.tv_confirm.setTextColor(
                        context.resources.getColor(
                            R.color.white,
                            context.theme
                        )
                    )
                    holder.tv_cancel.setTextColor(
                        context.resources.getColor(
                            R.color.text_primary,
                            context.theme
                        )
                    )

                } else {
                    holder.tv_confirm.background =
                        context.resources.getDrawable(R.drawable.bg_red_tl_bl_tweleve_dp)
                    holder.tv_cancel.setBackgroundColor(
                        context.resources.getColor(
                            R.color.white
                        )
                    )
                    holder.tv_confirm.setTextColor(context.resources.getColor(R.color.white))
                    holder.tv_cancel.setTextColor(context.resources.getColor(R.color.text_primary))
                }
                iRecyclerItemClick.onClick(pojo, "confirm", holder.adapterPosition, holder.tv_cancel)


            }
        }

    }

    private fun changeFormat(time: String): String {
        var finalDate = ""
        try {
            val sdf = SimpleDateFormat("H:mm")
            val dateObj: Date = sdf.parse(time)
            finalDate = SimpleDateFormat("K:mm").format(dateObj)

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return finalDate
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_no_guest = itemView.tv_guest
        val tv_book_time = itemView.tv_book_time
        val tv_book_date = itemView.tv_book_date
        val tv_cancel = itemView.tv_cancel
        val tv_confirm = itemView.tv_confirm
        val tv_customer = itemView.tv_customer
        val tv_mobile = itemView.tv_mobile
    }
}