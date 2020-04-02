package com.rvtechnologies.grigorahq.orders

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.rvtechnologies.grigora.utils.IRecyclerItemClick
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.orders.adapter.PastOrdersAdapter
import com.rvtechnologies.grigorahq.services.models.*
import com.rvtechnologies.grigorahq.utils.BroadcastConstants
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.restaurant_accept_order_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.restaurant_past_orders_num
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.PrefConstants
import kotlinx.android.synthetic.main.fragment_orders.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class PastOrderFragment : Fragment(), EventBroadcaster, IRecyclerItemClick {

    private lateinit var id: String
    private lateinit var order_status: String
    private lateinit var order_btn: ImageView
    val mListNew: ArrayList<RestaurantNewOrdersModel.Data> = ArrayList()
    val mList: ArrayList<RestaurantOnGoingOrdersModel.Data> = ArrayList()
    val mListPast: ArrayList<RestaurantPastOrders.Data> = ArrayList()

    private lateinit var mRandom: Random
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    override fun onItemClick(item: Any, s: String, button: ImageView) {
        if (item is OrderModel.Data) {
            id = item.id.toString()
            order_status = s
            order_btn = button
            if (s.equals(""))
                setCallingToCustomer(item.userPhone)
        }
    }

    override fun onClick(item: Any, type: String, position: Int,tv:TextView) {

    }

    private fun setCallingToCustomer(userPhone: String) {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:" + userPhone)//change the number
        startActivity(callIntent)

    }


    @SuppressLint("SetTextI18n")
    override fun broadcast(code: Int, data: Any?) {
        if (code == restaurant_past_orders_num) {
            var pojo = Gson().fromJson(data.toString(), RestaurantPastOrders::class.java)
            if (pojo.status) {
                if (mListPast.size > 0) {
                    mListPast.clear()
                }
                mListPast.addAll(pojo.data)

                rvOrders.adapter = PastOrdersAdapter(mListPast, this)
            } else {
                tv_nodata.visibility = VISIBLE
                iv_empty.visibility = VISIBLE
                tv_nodata.text=getString(R.string.no_history_avliable)
                iv_empty.setImageResource(R.drawable.no_history)
                rvOrders.visibility = GONE

            }


        } else if (code == restaurant_accept_order_num) {
            var pojo = Gson().fromJson(data.toString(), OrderAccepModel::class.java)
            if (pojo.status) {
                if (pojo.message.equals("Orders Rejected By Restaurant.")) {
                } else if (pojo.message.equals("Orders Accepted By Restaurant.")) {
                }

//                ConnectionNetwork.showSnack(
//                    false,
//                    activity!!,
//                    constraintLayout,
//                    getString(R.string.order_accepted)
//                )
                if (pojo.data.orderStatus == "2") {
                    tv_accept.visibility = VISIBLE
                    tv_accept.setText(getString(R.string.driver_acceptance) + pojo.data.id)
                } else {
                    tv_accept.visibility = GONE

                }

            }
        } else if (code == BroadcastConstants.noti_past) {
            var notification_msg = data.toString()
            if (notification_msg.contains("Order Delivered")) {
                setOrdersApi(NetworkConstants.restaurant_past_orders, restaurant_past_orders_num)
            }
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvOrders.layoutManager = LinearLayoutManager(activity)
        rl_toolbar.visibility = GONE
        mRandom = Random()
        mHandler = Handler()
//
        setOrdersApi(NetworkConstants.restaurant_past_orders, restaurant_past_orders_num)
        swiperefresh.setOnRefreshListener {
            mRunnable = Runnable {
                setOrdersApi(NetworkConstants.restaurant_past_orders, restaurant_past_orders_num)

                swiperefresh.isRefreshing = false
            }
            mHandler.postDelayed(
                mRunnable, 3000
            )

        }


    }

    private fun setOrdersApi(url: String, url_num: Int) {
        var headerMAp = HashMap<String, String>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")

        ConnectionNetwork.getData(
            url,
            headerMAp,
            activity!!,
            constraintLayout,
            url_num
        )
    }

    private fun setAcceptApi() {
        var data = HashMap<String, Any?>()

        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")

        data.put("order_id", id)
        data.put("status", order_status)

        ConnectionNetwork.postFormData(
            NetworkConstants.restaurant_accept_order,
            headerMAp,
            data,
            "",
            activity!!,
            constraintLayout,
            restaurant_accept_order_num
        )
    }


//    fun setApi() {
//        var headerMAp = HashMap<String, String>()
//        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
//        headerMAp.put("Accept", "application/json")
//        if (checkConnection(activity!!)) {
//            showLoader(activity!!, "")
//            val retroInterface = ApiClient.getApiServices()
//            retroInterface.getrestaurant_orders(headerMAp)
//                .enqueue(object : Callback<OrderModel> {
//                    @SuppressLint("SetTextI18n")
//                    override fun onResponse(
//                        call: Call<OrderModel>,
//                        response: Response<OrderModel>
//                    ) {
//                        hideLoader()
//                        if (response.isSuccessful()) {
//                            if (response.body()!!.status) {
//                                if (arguments?.getInt(ARG_SECTION_NUMBER) == 0) {
//                                    if (mList.size > 0) {
//                                        mList.clear()
//                                    }
//                                    for (i in 0..response.body()!!.data.size - 1) {
//                                        if (response.body()!!.data[i].orderStatus == "0") {
//                                            mList.add(response.body()!!.data[i])
//                                            Log.e("new order list", mList.size.toString())
//                                        }
//
//                                    }
//
//                                    if (mList.size == 0) {
//                                        tv_nodata.visibility = VISIBLE
//                                        rvOrders.visibility = GONE
//                                        tv_nodata.setText(getString(R.string.no_orders))
//
//                                    } else {
//                                        tv_nodata.visibility = GONE
//                                        rvOrders.visibility = VISIBLE
//                                        rvOrders.adapter =
//                                            NewOrdersAdapter(mList, this@OrderFragment)
//
//
//                                    }
//
//                                } else if (arguments?.getInt(ARG_SECTION_NUMBER) == 1) {
//                                    if (mList.size > 0) {
//                                        mList.clear()
//                                    }
//                                    for (i in 0..response.body()!!.data.size - 1) {
//                                        if (response.body()!!.data[i].orderStatus == "2" || response.body()!!.data[i].orderStatus == "3" || response.body()!!.data[i].orderStatus == "4")
//                                            mList.add(response.body()!!.data[i])
//                                    }
//                                    if (mList.size == 0) {
//                                        tv_nodata.visibility = VISIBLE
//                                        rvOrders.visibility = GONE
//                                        tv_nodata.setText(getString(R.string.no_ongoing))
//
//
//                                    } else {
//                                        tv_nodata.visibility = GONE
//                                        rvOrders.visibility = VISIBLE
//                                        rvOrders.adapter =
//                                            OngoingOrdersAdapter(mList, this@OrderFragment)
//                                    }
//
//                                } else if (arguments?.getInt(ARG_SECTION_NUMBER) == 2) {
//                                    if (mList.size > 0) {
//                                        mList.clear()
//                                    }
//                                    for (i in 0..response.body()!!.data.size - 1) {
//                                        if (response.body()!!.data[i].orderStatus == "5")
//                                            mList.add(response.body()!!.data[i])
//                                    }
//
//                                    if (mList.size == 0) {
//                                        tv_nodata.visibility = VISIBLE
//                                        rvOrders.visibility = GONE
//                                        tv_nodata.setText(getString(R.string.no_history))
//                                    } else {
//                                        tv_nodata.visibility = GONE
//                                        rvOrders.visibility = VISIBLE
//                                        rvOrders.adapter =
//                                            PastOrdersAdapter(mList, this@OrderFragment)
//
//
//                                    }
//                                }
//
//                            } else {
//                                tv_nodata.visibility = VISIBLE
//                                rvOrders.visibility = GONE
//
//                            }
//
//                        }
//
//                    }
//
//                    override fun onFailure(call: Call<OrderModel>, t: Throwable) {
//                        hideLoader()
//                        ConnectionNetwork.showSnack(
//                            false,
//                            activity!!,
//                            constraintLayout,
//                            getString(R.string.went_wrong)
//                        )
//
//
//                    }
//                })
//
//        } else {
//            showSnack(false, activity!!, constraintLayout, "")
//        }
//
//    }


    override fun onResume() {
        super.onResume()
        MyApplication.instance!!.registerListener(this)
//        setApi()

    }


    override fun onPause() {
        super.onPause()
        MyApplication.instance!!.deRegisterListener(this)
    }


    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(): PastOrderFragment {
            return PastOrderFragment().apply {
            }
        }

    }
}