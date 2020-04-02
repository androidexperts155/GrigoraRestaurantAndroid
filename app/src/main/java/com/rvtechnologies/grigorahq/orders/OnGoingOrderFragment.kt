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
import com.rvtechnologies.grigorahq.services.models.OrderAllmostCompleteModel
import com.rvtechnologies.grigorahq.services.models.RestaurantOnGoingOrdersModel
import com.rvtechnologies.grigorahq.utils.BroadcastConstants
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.order_almost_prepared_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.restaurant_ongoing_orders_num
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.PrefConstants
import com.rvtechnologies.grigorarestaurant.adapter.OngoingOrdersAdapter
import kotlinx.android.synthetic.main.fragment_orders.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class OnGoingOrderFragment : Fragment(), EventBroadcaster, IRecyclerItemClick {




    override fun onItemClick(item: Any, s: String, button5: ImageView) {

    }

    override fun onClick(item: Any, type: String, position: Int,tv:TextView) {
        if (item is RestaurantOnGoingOrdersModel.Data) {
            id = item.id.toString()
        }


        setAllmostPreparedApi()
    }


    private lateinit var id: String
    private lateinit var order_status: String
    private lateinit var order_btn: TextView
    val mList: ArrayList<RestaurantOnGoingOrdersModel.Data> = ArrayList()


    private fun setCallingToCustomer(userPhone: String) {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:" + userPhone)//change the number
        startActivity(callIntent)

    }


    @SuppressLint("SetTextI18n")
    override fun broadcast(code: Int, data: Any?) {
        if (code == restaurant_ongoing_orders_num) {
            var pojo =
                Gson().fromJson(data.toString(), RestaurantOnGoingOrdersModel::class.java)

            if (pojo.status) {
                if (mList.size > 0) {
                    mList.clear()
                }


                mList.addAll(pojo.data)
                rvOrders.adapter = OngoingOrdersAdapter(mList, this)


            } else {
                tv_nodata.visibility = VISIBLE
                rvOrders.visibility = GONE

            }
        } else if (code == order_almost_prepared_num) {
            var pojo = Gson().fromJson(data.toString(), OrderAllmostCompleteModel::class.java)
            if (pojo.status) {
                setOrdersApi(
                    NetworkConstants.restaurant_ongoing_orders,
                    restaurant_ongoing_orders_num
                )
            }


        } else if (code == BroadcastConstants.noti_ongoing) {

            setOrdersApi(
                NetworkConstants.restaurant_ongoing_orders,
                restaurant_ongoing_orders_num
            )
            tv_accept.visibility = GONE
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


//        setOrdersApi(NetworkConstants.restaurant_new_orders, restaurant_new_orders_num)

        setOrdersApi(
            NetworkConstants.restaurant_ongoing_orders,
            restaurant_ongoing_orders_num
        )




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

    private fun setAllmostPreparedApi() {
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
            order_almost_prepared_num
        )
    }


    override fun onResume() {
        super.onResume()
        MyApplication.instance!!.registerListener(this)

    }


    override fun onPause() {
        super.onPause()
        MyApplication.instance!!.deRegisterListener(this)
    }


    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(): OnGoingOrderFragment {
            return OnGoingOrderFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }
}