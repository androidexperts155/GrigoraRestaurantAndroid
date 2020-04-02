package com.rvtechnologies.grigorahq.orders.details

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.orders.adapter.inner_adapter.PastSubItemAdapter
import com.rvtechnologies.grigorahq.orders.adapter.inner_adapter.SubItemAdapter
import com.rvtechnologies.grigorahq.services.models.OrderAccepModel
import com.rvtechnologies.grigorahq.services.models.RestaurantNewOrdersModel
import com.rvtechnologies.grigorahq.services.models.RestaurantOnGoingOrdersModel
import com.rvtechnologies.grigorahq.services.models.RestaurantPastOrders
import com.rvtechnologies.grigorahq.utils.BroadcastConstants
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.PrefConstants
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.content_order_details.*
import kotlinx.android.synthetic.main.dialog_set_prepare_time.view.*


class NewOrderDetails : AppCompatActivity(), EventBroadcaster {
    override fun broadcast(code: Int, data: Any?) {
        if (code == BroadcastConstants.restaurant_accept_order_num) {
            var pojo = Gson().fromJson(data.toString(), OrderAccepModel::class.java)
            if (pojo.status) {
                if (pojo.message == "Orders Rejected By Restaurant.") {
                    tv_reject.text = getString(R.string.rejected)
                } else if (pojo.message == "Orders Accepted By Restaurant.") {
                    tv_accept.text = getString(R.string.accepted)
                }
//                ConnectionNetwork.showSnack(
//                    false,
//                    activity!!,
//                    constraintLayout,
//                    getString(R.string.order_accepted)
//                )
                if (pojo.data.orderStatus == "2") {
                    tv_accept.visibility = VISIBLE
                    tv_accept.text = getString(R.string.accepted)
                } else {
                    tv_accept.visibility = GONE

                }

            }
        }
    }

    lateinit var pojo: RestaurantNewOrdersModel.Data
    lateinit var pojoOn: RestaurantOnGoingOrdersModel.Data
    lateinit var pojoPast: RestaurantPastOrders.Data
    var finalPrice: String = ""
    var orderId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        setSupportActionBar(toolbar)
        btnBack.setOnClickListener { onBackPressed() }
        tv_accept.setOnClickListener {
            //            setAcceptApi("1")
            showSetTimeDialog()

        }
        tv_reject.setOnClickListener {
            setAcceptApi("2", "")
        }
        rvOrderDetails.layoutManager = LinearLayoutManager(this)
        if (intent != null) {
            if (intent.getStringExtra("type") == "new") {
                detailNewOrder()
            } else if (intent.getStringExtra("type") == "going") {
                detailOnGoingOrder()
            } else if (intent.getStringExtra("type") == "past") {
                detailPastOrder()
            }

        }
    }

    private fun showSetTimeDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_set_prepare_time, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()
        mDialogView.set_time.setOnClickListener {
            if (mDialogView.ed_time.text.isNullOrEmpty()) {
                CommonUtils.showToast(this, getString(R.string.set_time_error))
            } else {
                mAlertDialog.dismiss()
                setAcceptApi("1", mDialogView.ed_time.text.toString())
            }
        }
    }


    private fun detailPastOrder() {
        pojoPast = intent.getSerializableExtra("pojo_extra") as RestaurantPastOrders.Data
        rvOrderDetails.adapter = PastSubItemAdapter(pojoPast.orderDetails)

        button10.visibility = GONE
        button11.visibility = GONE

        orderId = pojoPast.id
        ll_cancel.visibility = GONE
        ll_accept.visibility = GONE
        rl_contact.visibility = GONE
        textView5.text = pojoPast.userName
        textViewAddress.text = pojoPast.deliveryAddress

        textView2.text =
            getString(R.string.date) + pojoPast.createdAt.substring(0, 10) + "\n" + getString(
                R.string.time
            ) + pojoPast.createdAt.substring(11, 19)
        textView3.text = getString(R.string.order_id) + ":  " + pojoPast.id.toString()
//                textView21.setText(pojoPast.userPhone)
//                textView19.setText(pojoPast.userEmail)
        textView26.text = pojoPast.deliveryFee.toString()
        var appFee = (pojoPast.finalPrice - pojoPast.deliveryFee) / 100 * pojoPast.appFee
        textView28.text = "₦ " + appFee.toString()
        if (pojoPast.deliveryNote.isNullOrEmpty()) {
            textView8.text = "N/A"
        } else {
            textView8.text = pojoPast.deliveryNote
        }
        textView31.text = "₦ " + (pojoPast.finalPrice - appFee).toString()
        textView24.text = "₦ " + pojoPast.priceBeforePromo.toString()
        textView30.text = "₦ " + (pojoPast.priceBeforePromo - pojoPast.priceAfterPromo).toString()
        Glide.with(this).load(pojoPast.userImage)
            .into(profilePic)

        rl_driver.visibility = GONE
    }

    private fun detailOnGoingOrder() {
        pojoOn =
            intent.getSerializableExtra("pojo_extra") as RestaurantOnGoingOrdersModel.Data
//        rvOrderDetails.adapter = OnGoingSubItemAdapter(pojoOn.orderDetails)

        cus_call.setOnClickListener {
            setCallingToCustomer(pojoOn.userPhone)
        }
        cus_email.setOnClickListener {
            setMail(pojoOn.userEmail)
        }
        orderId = pojoOn.id

        ll_accept.visibility = GONE
        ll_cancel.visibility = GONE
        textView5.text = pojoOn.userName
        textView2.text =
            getString(R.string.date) + pojoOn.createdAt.substring(0, 10) + "\n" + getString(
                R.string.time
            ) + pojoOn.createdAt.substring(11, 19)
        textViewAddress.text = pojoOn.deliveryAddress

        textView3.text = getString(R.string.order_id) + ":  " + pojoOn.id.toString()
        textView21.text = pojoOn.userPhone
//                textView19.setText(pojoOn.userEmail)
        textView26.text = "₦ " + pojoOn.deliveryFee.toString()
        if (pojoOn.deliveryNote.isNullOrEmpty()) {
            textView8.text = "N/A"
        } else {
            textView8.text = pojoOn.deliveryNote
        }
        var app_fee = (pojoOn.finalPrice - pojoOn.deliveryFee) / 100 * pojoOn.appFee
        textView28.text = "₦ " + app_fee.toString()
        textView31.text = " ₦ " + (pojoOn.finalPrice - app_fee)
        textView24.text = "₦ " + pojoOn.priceBeforePromo.toString()
        textView30.text = "₦ " + (pojoOn.priceBeforePromo - pojoOn.priceAfterPromo).toString()
        Glide.with(this).load(pojoOn.userImage).into(profilePic)
        driver_name.text = pojoOn.driverName
        Glide.with(this).load(pojoOn.driverImage).into(profilePic_driver)
        dri_call.setOnClickListener {
            setCallingToCustomer(pojoOn.driverPhone)
        }

    }

    private fun detailNewOrder() {
        pojo = intent.getSerializableExtra("pojo_extra") as RestaurantNewOrdersModel.Data
        rvOrderDetails.adapter = SubItemAdapter(pojo.orderDetails)
        button10.setOnClickListener {
            setCallingToCustomer(pojo.userPhone)
        }
        tv_accept.visibility = VISIBLE
        tv_reject.visibility = VISIBLE
        button11.setOnClickListener {
            setMail(pojo.userEmail)
        }
        textView5.text = pojo.userName
        textView2.text =
            getString(R.string.date) + pojo.createdAt.substring(0, 10) + "\n" + getString(
                R.string.time
            ) + pojo.createdAt.substring(11, 19)
        textView3.text = getString(R.string.order_id) + ":  " + pojo.id.toString()
        orderId = pojo.id
//                textView21.setText(pojo.userPhone)
//                textView19.setText(pojo.userEmail)
        textView26.text = pojo.deliveryFee.toString()
        if (pojo.deliveryNote.isNullOrEmpty()) {
            textView8.text = "N/A"
        } else {
            textView8.text = pojo.deliveryNote
        }
        textView8.text = pojo.deliveryNote
        var app_fee = (pojo.finalPrice - pojo.deliveryFee) / 100 * pojo.appFee
        textView28.text = "₦ " + app_fee.toString()
        textViewAddress.text = pojo.deliveryAddress
        textView31.text = " ₦ " + (pojo.finalPrice - app_fee)
        textView24.text = "₦ " + pojo.priceBeforePromo.toString()
        textView30.text = (pojo.priceBeforePromo - pojo.priceAfterPromo).toString()
        Glide.with(this).load(pojo.userImage).into(profilePic)
        rl_driver.visibility = GONE

    }

    private fun setMail(userEmail: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:" + userEmail)
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            startActivity(intent)
        }
    }


    private fun setCallingToCustomer(userPhone: String) {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:" + userPhone)//change the number
        startActivity(callIntent)
    }

    override fun onResume() {
        super.onResume()
        MyApplication.instance!!.registerListener(this)
    }

    override fun onPause() {
        super.onPause()
        MyApplication.instance!!.deRegisterListener(this)
    }

    private fun setAcceptApi(order_status: String, preparing_time: String) {
        var data = HashMap<String, Any?>()

        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(this, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")

        data.put("order_id", orderId.toString())
        data.put("status", order_status)
        if (order_status == "1") {
            data.put("preparing_time", preparing_time)
        }
        ConnectionNetwork.postFormData(
            NetworkConstants.restaurant_accept_order,
            headerMAp,
            data,
            "",
            this,
            pl,
            BroadcastConstants.restaurant_accept_order_num
        )
    }

}
