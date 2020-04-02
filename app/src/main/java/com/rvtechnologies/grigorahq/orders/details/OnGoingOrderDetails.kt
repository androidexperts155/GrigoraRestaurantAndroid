package com.rvtechnologies.grigorahq.orders.details

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.rvtechnologies.grigorahq.R
import kotlinx.android.synthetic.main.activity_ongoing_order_details.*
import kotlinx.android.synthetic.main.content_ongoing_order_details.*


class OnGoingOrderDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ongoing_order_details)
        setSupportActionBar(toolbar)
        var list = ArrayList<String>()
        list.add("Preparing")
        list.add("Order Dispatched")
        spnStatus.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
        btnBack.setOnClickListener { onBackPressed() }

    }

}
