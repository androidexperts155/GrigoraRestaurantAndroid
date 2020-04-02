package com.rvtechnologies.grigorahq.wallet

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.services.models.WalletModel
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.CommonUtils.change24To12
import com.rvtechnologies.grigorahq.utils.CommonUtils.changeUTC_to_local
import com.rvtechnologies.grigorahq.utils.PrefConstants
import com.squareup.picasso.Picasso

class WalletAdapter(val mList: ArrayList<WalletModel.Data.History>) :
    RecyclerView.Adapter<WalletAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.custom_wallet,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var pojo = mList.get(position)

        if (pojo.type.equals("7")) {
            holder.ll_credit.visibility = VISIBLE
            holder.rl_withdraw.visibility = GONE
            var time = pojo.createdAt
            holder.order_id.text =
                holder.itemView.context.resources.getString(R.string.order_id) + pojo.orderId.toString()
            var date_time = change24To12(holder.itemView.context, changeUTC_to_local(time))
            holder.tv_date.text = date_time.substring(0, 8) + "," + date_time.substring(9, date_time.length)
            holder.customer_name.text = pojo.customerName

            if (pojo.customerAddress.isNullOrEmpty()) {
                holder.tv_address.text = holder.itemView.context.getString(R.string.not_availiable)
            } else
                holder.tv_address.text = pojo.customerAddress

            holder.tv_amount.text =
                holder.itemView.context.resources.getString(R.string.naira_sign) + pojo.amount
        } else {
            if (pojo.orderId == 0) {

                holder.ll_credit.visibility = GONE
                holder.rl_withdraw.visibility = VISIBLE
                holder.tv_ref.text = holder.itemView.context.getString(R.string.ref)+pojo.id
                holder.account_number.text = "XXXXXXX" + pojo.userAccountNumber.substring(
                    pojo.userAccountNumber.length - 3
                )
                var date_time = change24To12(holder.itemView.context,  changeUTC_to_local(pojo.createdAt))
                holder.tv_date_bank.text =
                    date_time.substring(0, 8) + "," + date_time.substring(9, date_time.length)
                holder.tv_withdrawer.text =
                    holder.itemView.context.resources.getString(R.string.naira_sign) + pojo.amount

            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var order_id = itemView.findViewById<TextView>(R.id.textView9)
        var tv_date = itemView.findViewById<TextView>(R.id.tv_date)
        var customer_name = itemView.findViewById<TextView>(R.id.textView10)
        var tv_address = itemView.findViewById<TextView>(R.id.textView11)
        var tv_date_bank = itemView.findViewById<TextView>(R.id.tv_date_bank)
        var tv_ref = itemView.findViewById<TextView>(R.id.tv_ref)
        var account_number = itemView.findViewById<TextView>(R.id.account_number)
        var tv_withdrawer = itemView.findViewById<TextView>(R.id.tv_withdrawer)
        var tv_amount = itemView.findViewById<TextView>(R.id.textView12)
        var circleImageView2 = itemView.findViewById<ImageView>(R.id.circleImageView2)
        var ll_credit = itemView.findViewById<RelativeLayout>(R.id.ll_credit)
        var rl_withdraw = itemView.findViewById<RelativeLayout>(R.id.rl_withdraw)
    }

}