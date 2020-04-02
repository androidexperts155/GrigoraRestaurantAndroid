//package com.rvtechnologies.grigorahq.offers
//
//import android.os.Bundle
//import android.view.View.GONE
//import android.view.View.VISIBLE
//import androidx.appcompat.app.AppCompatActivity
//import com.google.gson.Gson
//import com.rvtechnologies.grigorahq.MyApplication
//import com.rvtechnologies.grigorahq.R
//import com.rvtechnologies.grigorahq.network.ConnectionNetwork
//import com.rvtechnologies.grigorahq.network.EventBroadcaster
//import com.rvtechnologies.grigorahq.network.NetworkConstants
//import com.rvtechnologies.grigorahq.services.models.RestaurantOfferModel
//import com.rvtechnologies.grigorahq.utils.BroadcastConstants
//import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.restaurant_offer_num
//import com.rvtechnologies.grigorahq.utils.CommonUtils
//import com.rvtechnologies.grigorahq.utils.PrefConstants
//import kotlinx.android.synthetic.main.activity_create_offer.*
//
//class CreateOfferActivity : AppCompatActivity(), EventBroadcaster {
//
//    override fun broadcast(code: Int, data: Any?) {
//        if (code == restaurant_offer_num) {
//            var pojo = Gson().fromJson(data.toString(), RestaurantOfferModel::class.java)
//            if (pojo.status) {
//                ConnectionNetwork.showSnack(
//                    false,
//                    this,
//                    parent_layout,
//                    getString(R.string.offer_applied)
//                )
//                CommonUtils.savePrefs(this, PrefConstants.OFFER, pojo.offer)
//                editText2.setText(pojo.offer)
//
//            }
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_create_offer)
//
//
//        textView18.setOnClickListener {
//            setApi(editText2.text.toString())
//        }
//
//
//
//        iv_back.setOnClickListener {
//            finish()
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        MyApplication.instance!!.deRegisterListener(this)
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//        MyApplication.instance!!.registerListener(this)
//
//    }
//
//
//
//}
