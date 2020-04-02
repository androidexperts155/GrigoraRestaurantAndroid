package com.rvtechnologies.grigorahq.orders.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rvtechnologies.grigorahq.R
import kotlinx.android.synthetic.main.activity_order_details.*

class PastOrderDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_past_order_details)
        setSupportActionBar(toolbar)
        btnBack.setOnClickListener { onBackPressed() }

    }

}
