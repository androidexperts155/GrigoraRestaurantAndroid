package com.rvtechnologies.grigorahq.orders

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.rvtechnologies.grigora.utils.IRecyclerItemClick
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.home.HomeMapFragment
import com.rvtechnologies.grigorahq.navigation.NavigationActivity
import com.rvtechnologies.grigorahq.navigation.NavigationActivity.Companion.toolbar
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.network.NetworkConstants.Companion.complete_pickup_order
import com.rvtechnologies.grigorahq.orders.adapter.ScheduleOrderAdapter
import com.rvtechnologies.grigorahq.reasons.RejectReasonsActivity
import com.rvtechnologies.grigorahq.services.models.*
import com.rvtechnologies.grigorahq.utils.BroadcastConstants
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.complete_pickup_orde_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.noti_new_order
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.restaurant_accept_order_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.restaurant_new_orders_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.start_preparing_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.update_time_num
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.PrefConstants
import com.rvtechnologies.grigorarestaurant.adapter.NewOrdersAdapter
import kotlinx.android.synthetic.main.dialog_set_prepare_time.view.*
import kotlinx.android.synthetic.main.dialog_simple_msg.*
import kotlinx.android.synthetic.main.dialog_swipe_refresh.*
import kotlinx.android.synthetic.main.fragment_orders.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class OrderFragment : Fragment(), EventBroadcaster, IRecyclerItemClick {

    private lateinit var mRandom: Random
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    private var id: String = ""
    private var order_status: String = ""
    private var order_type: String = ""
    private var update_timer: String = ""
    private lateinit var order_btn: ImageView
    val mListNew: ArrayList<RestaurantNewOrdersModel.Data> = ArrayList()
    val mListSchedule: ArrayList<RestaurantNewOrdersModel.Data> = ArrayList()
    var frag_type = ""
   lateinit var tv_timer:TextView

    override fun onItemClick(item: Any, s: String, button: ImageView) {
        if (item is RestaurantNewOrdersModel.Data) {
            id = item.id.toString()
            order_status = s
            order_type=item.orderType
            order_btn = button
            if (s.equals(""))
                setCallingToCustomer(item.userPhone)
        }
        if (order_status.equals("2"))
            startActivity(
                Intent(activity, RejectReasonsActivity::class.java).putExtra(
                    "orderId", id).putExtra("orderType",order_type)
            )
        else
            showSetTimeDialog()
    }


    override fun onClick(item: Any, type: String, position: Int,tv:TextView) {
        tv_timer=tv
        if (type.equals("dispatch", ignoreCase = true)) {
            if (item is RestaurantNewOrdersModel.Data)
                setAllmostPreparedApi(item.id)
        } else if (type.equals("complete", ignoreCase = true)) {
            if (item is RestaurantNewOrdersModel.Data)
                setCompletePickupOrder(item.id)
        } else if (type.equals("add_timer")) {
            if (item is RestaurantNewOrdersModel.Data)

                showUpdateSetTimeDialog(item.id, item.preparingTime,tv_timer)

        } else if (type.equals("add_timer_reached")) {
            showTimeLimitOverDialog()
        } else if (type.equals("preparing")) {
            if (item is RestaurantNewOrdersModel.Data)
                showPreaparingDialog(item.id, getString(R.string.start_prepare_this_order), false)
        }

    }

    private fun showPreaparingDialog(id: Int, msg: String, onlyDismiss: Boolean) {
        val mDialogView =
            LayoutInflater.from(activity).inflate(R.layout.dialog_simple_msg, null)
        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()
        mAlertDialog.tv_msg.text = msg

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            mAlertDialog.tv_ok.setTextColor(resources.getColor(R.color.sky_blue, activity!!.theme))
        else
            mAlertDialog.tv_ok.setTextColor(resources.getColor(R.color.sky_blue))

        mAlertDialog.tv_ok.setOnClickListener {
            mAlertDialog.dismiss()
            if (!onlyDismiss) {
                startPreparingApi(id)
            }
        }
    }

    private fun startPreparingApi(id: Int) {
        var data = HashMap<String, Any?>()

        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        data.put("order_id", id)

        ConnectionNetwork.postFormData(
            NetworkConstants.start_preparing,
            headerMAp,
            data,
            "",
            activity!!,
            constraintLayout,
            BroadcastConstants.start_preparing_num
        )
    }

    private fun showTimeLimitOverDialog() {
        val mDialogView =
            LayoutInflater.from(activity).inflate(R.layout.dialog_simple_msg, null)
        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()
        mAlertDialog.tv_msg.text = getString(R.string.you_cannot_add_more_time)
        mAlertDialog.tv_ok.setOnClickListener {
            mAlertDialog.dismiss()
        }

    }

    private fun setUpdateTimer(id: Int, timer: Any, tv: TextView) {
        var data = HashMap<String, Any?>()

        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        data.put("order_id", id)
        data.put("time", timer)

        ConnectionNetwork.postFormData(
            NetworkConstants.update_time,
            headerMAp,
            data,
            "",
            activity!!,
            constraintLayout,
            BroadcastConstants.update_time_num
        )

    }

    private fun setCompletePickupOrder(id: Int) {
        var headerMAp = HashMap<String, String>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        ConnectionNetwork.getData(
            complete_pickup_order + id,
            headerMAp,
            activity!!,
            constraintLayout,
            BroadcastConstants.complete_pickup_orde_num
        )
    }


    private fun showSetTimeDialog() {
        val mDialogView =
            LayoutInflater.from(activity).inflate(R.layout.dialog_set_prepare_time, null)
        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()
        mDialogView.set_time.setOnClickListener {
            if (mDialogView.ed_time.text.isNullOrEmpty()) {
                ConnectionNetwork.showSnack(
                    false,
                    activity!!,
                    constraintLayout,
                    getString(R.string.set_time_error))
            } else {
                mAlertDialog.dismiss()
                if (mDialogView.ed_time.text.toString().toInt() > 60) {
                    showPreaparingDialog(0, getString(R.string.time_not_be_more_than), true)
                } else
                    setAcceptApi(mDialogView.ed_time.text.toString())
            }
        }
    }

    private fun showUpdateSetTimeDialog(
        id: Int,
        preparingTime: Int,
        tv: TextView
    ) {
        val mDialogView =
            LayoutInflater.from(activity).inflate(R.layout.dialog_update_prepare_time, null)
        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mDialogView.set_time.setOnClickListener {
            if (mDialogView.ed_time.text.isNullOrEmpty()) {
                CommonUtils.showToast(activity, getString(R.string.set_time_error))
            } else {
                mAlertDialog.dismiss()
                if (mDialogView.ed_time.text.toString().toFloat() > preparingTime / 2) {

                    showPreaparingDialog(
                        0,
                        getString(R.string.time_must_not_be_more_than) + preparingTime / 2 + getString(
                            R.string.minutes
                        ),
                        true
                    )
                } else {
                    if (mDialogView.ed_time.text.toString().toInt() > 60) {
                        ConnectionNetwork.showSnack(
                            false,
                            activity!!,
                            constraintLayout,
                            getString(R.string.time_not_be_more_than)
                        )
                    } else {
                        update_timer=mDialogView.ed_time.text.toString()
                        setUpdateTimer(id,update_timer ,tv)
                    }
                }
            }
        }
    }

    private fun setCallingToCustomer(userPhone: String) {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:" + userPhone)//change the number
        startActivity(callIntent)

    }


    @SuppressLint("SetTextI18n")
    override fun broadcast(code: Int, data: Any?) {
        if (code == restaurant_new_orders_num) {
            var pojo = Gson().fromJson(data.toString(), RestaurantNewOrdersModel::class.java)
            if (pojo.status) {
                if (mListNew.size > 0) {
                    mListNew.clear()
                }
                if (mListSchedule.size > 0)
                    mListSchedule.clear()

                for (i in 0 until pojo.data.size) {
                    if (pojo.data.get(i).isSchedule == ("0")) {
                        mListNew.add(pojo.data.get(i))
                    } else {
                        mListSchedule.add(pojo.data.get(i))
                    }
                }
                rvOrders.layoutManager = LinearLayoutManager(activity)

                if (frag_type.equals("new")) {
                    if (mListNew.size > 0) {
                        iv_empty.visibility = GONE
                        tv_nodata.visibility = GONE
                        rvOrders.visibility = VISIBLE
                        rvOrders.adapter = NewOrdersAdapter(mListNew, this)
                        iv_list.visibility = VISIBLE
                        tv_title.text = resources.getString(R.string.available_orders)
                    }
                    else {
                        iv_empty.visibility = VISIBLE
                        tv_nodata.visibility = VISIBLE
                        rvOrders.visibility = GONE
                        iv_empty.setImageResource(R.drawable.no_order)
                        tv_nodata.text=getString(R.string.no_order_available) }
                } else {
                    if (mListSchedule.size > 0) {
                        iv_empty.visibility = GONE
                        tv_nodata.visibility = GONE

                        rvOrders.visibility = VISIBLE
                        rvOrders.adapter = ScheduleOrderAdapter(mListSchedule, this)
                    }
                    else {
                        iv_empty.visibility = VISIBLE
                        tv_nodata.visibility = VISIBLE
                        rvOrders.visibility = GONE

                        iv_empty.setImageResource(R.drawable.schedule_new)
                        tv_nodata.text=getString(R.string.no_order_schedule)
                    }
                    tv_title.text = resources.getString(R.string.schedule_orders)
                    iv_list.visibility = GONE
                }
            } else {
                tv_nodata.visibility = VISIBLE
                iv_empty.visibility = VISIBLE
                rvOrders.visibility = GONE
                if (frag_type.equals("new")) {
                    tv_nodata.text = getString(R.string.no_order_available)
                    iv_empty.setImageResource(R.drawable.no_order)
                } else {
                    tv_nodata.text = getString(R.string.no_order_schedule)
                    iv_empty.setImageResource(R.drawable.schedule_new)
                }

            }
        }
        else if (code == restaurant_accept_order_num) {
            var pojo = Gson().fromJson(data.toString(), OrderAccepModel::class.java)
            if (pojo.status) {
                if (pojo.message.equals("Orders Rejected By Restaurant.")) {
                    setOrdersApi(NetworkConstants.restaurant_new_orders, restaurant_new_orders_num)
                } else if (pojo.message.equals("Orders Accepted By Restaurant.")) {
                    setOrdersApi(NetworkConstants.restaurant_new_orders, restaurant_new_orders_num)
                }

                if (pojo.data.orderStatus == "2") {
                    tv_accept.visibility = GONE
                    tv_accept.setText(getString(R.string.driver_acceptance) + pojo.data.id)
                } else {
                    tv_accept.visibility = GONE
                }
            }
        }

        else if (code == noti_new_order) {
            setOrdersApi(NetworkConstants.restaurant_new_orders, restaurant_new_orders_num)
            tv_accept.visibility = GONE

        } else if (code == BroadcastConstants.order_almost_prepared_num) {
            var pojo = Gson().fromJson(data.toString(), OrderAllmostCompleteModel::class.java)
            if (pojo.status) {

                setOrdersApi(NetworkConstants.restaurant_new_orders, restaurant_new_orders_num)

            }


        } else if (code == complete_pickup_orde_num) {
            setOrdersApi(NetworkConstants.restaurant_new_orders, restaurant_new_orders_num)
        } else if (code == update_time_num) {
            var pojo = Gson().fromJson(data.toString(), UpdateTimeMode::class.java)
            if (pojo.status) {
//                countdown(update_timer.toLong(),tv_timer)
                setOrdersApi(NetworkConstants.restaurant_new_orders, restaurant_new_orders_num)
            }

        } else if (code == start_preparing_num) {
            var pojo = Gson().fromJson(data.toString(), PreparingModel::class.java)
            if (pojo.status) {
                setOrdersApi(NetworkConstants.restaurant_new_orders, restaurant_new_orders_num)
            }
        }
    }


//    fun countdown(
//        time: Long,
//        textView: TextView    ) {
//
//
//        object : CountDownTimer(time * 1000, 1000) {
//            override fun onFinish() {
//
//                }
//
//
//            override fun onTick(millisUntilFinished: Long) {
//                val seconds = (millisUntilFinished / 1000) % 60;
//                val minutes = ((millisUntilFinished / (1000 * 60)) % 60);
//                val hours = ((millisUntilFinished / (1000 * 60 * 60)) % 24);
//                val newtime = "" + hours + ":" + minutes + ":" + seconds;
//
//                if (newtime.equals("0:0:0")) {
//                    textView.text = getString(R.string.time_left) + "00:00:00"
//                } else if ((hours.toString().length == 1) && (minutes.toString().length == 1) && (seconds.toString().length == 1)) {
//                    textView.text =
//                        getString(R.string.time_left) + "0" + hours + ":0" + minutes + ":0" + seconds
//                } else if ((hours.toString().length == 1) && minutes.toString().length == 1) {
//                    textView.text =
//                        getString(R.string.time_left) + "0" + hours + ":0" + minutes + ":" + seconds
//                } else if (hours.toString().length == 1 && seconds.toString().length == 1) {
//                    textView.text =
//                       getString(R.string.time_left) + "0" + hours + ":" + minutes + ":0" + seconds
//                } else if (minutes.toString().length == 1 && seconds.toString().length == 1) {
//                    textView.text =
//                       getString(R.string.time_left) + hours + ":0" + minutes + ":0" + seconds
//                } else if (hours.toString().length == 1) {
//                    textView.text =
//                        getString(R.string.time_left) + "0" + hours + ":" + minutes + ":" + seconds
//                } else if (minutes.toString().length == 1) {
//                    textView.text =
//                       getString(R.string.time_left) + hours + ":0" + minutes + ":" + seconds
//                } else if (seconds.toString().length == 1) {
//                    textView.text =
//                       getString(R.string.time_left) + hours + ":" + minutes + ":0" + seconds
//                } else {
//                    textView.text =
//                      getString(R.string.time_left) + hours + ":" + minutes + ":" + seconds
//                }
//
//
//            }
//
//        }.start();
//
//    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvOrders.layoutManager = LinearLayoutManager(activity)
        toolbar = (activity as Activity).findViewById(R.id.toolbar);
        toolbar!!.visibility = GONE
        mRandom = Random()
        mHandler = Handler()
        iv_list.setOnClickListener {
            fragmentManager!!
                .beginTransaction()
                .replace(R.id.content_frame, HomeMapFragment.newInstance())
                .commit()
        }
        setOrdersApi(NetworkConstants.restaurant_new_orders, restaurant_new_orders_num)


        if (frag_type.equals("new")) {
            iv_list.visibility = VISIBLE
            tv_title.text = resources.getString(R.string.available_orders)
        } else {
            tv_title.text = resources.getString(R.string.schedule_orders)

            iv_list.visibility = GONE
        }
        swiperefresh.setOnRefreshListener {
            mRunnable = Runnable {
                setOrdersApi(NetworkConstants.restaurant_new_orders, restaurant_new_orders_num)
                swiperefresh.isRefreshing = false
                ll_dialog.visibility = VISIBLE
            }
            mHandler.postDelayed(mRunnable, 3000)
            ll_dialog.visibility = GONE

        }

        iv_menu.setOnClickListener {
            NavigationActivity.drawer_layout =
                (activity as Activity).findViewById(R.id.drawerlayout);
            if (NavigationActivity.drawer_layout!!.isDrawerVisible(GravityCompat.START)) {
                NavigationActivity.drawer_layout!!.closeDrawer(GravityCompat.START)
            } else {
                NavigationActivity.drawer_layout!!.openDrawer(GravityCompat.START)
            }
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

    private fun setAcceptApi(preparing_time: String) {
        var data = HashMap<String, Any?>()
        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        data.put("order_id", id)
        data.put("status", order_status)
        if (!preparing_time.isNullOrEmpty()) {
            data.put("preparing_time", preparing_time)
        }
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


    private fun setAllmostPreparedApi(id: Int) {
        var data = HashMap<String, Any?>()

        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        data.put("order_id", id)
        ConnectionNetwork.postFormData(
            NetworkConstants.order_almost_prepared,
            headerMAp,
            data,
            "",
            activity!!,
            constraintLayout,
            BroadcastConstants.order_almost_prepared_num
        )
    }

    override fun onResume() {
        super.onResume()
        MyApplication.instance!!.registerListener(this)
        setOrdersApi(NetworkConstants.restaurant_new_orders, restaurant_new_orders_num)

    }


    override fun onPause() {
        super.onPause()
        MyApplication.instance!!.deRegisterListener(this)
    }


    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(fragment_type: String): OrderFragment {
            return OrderFragment().apply {
                frag_type = fragment_type

            }
        }
    }

}