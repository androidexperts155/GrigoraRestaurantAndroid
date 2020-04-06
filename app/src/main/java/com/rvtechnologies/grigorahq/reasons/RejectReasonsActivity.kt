package com.rvtechnologies.grigorahq.reasons

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.network.NetworkConstants.Companion.cancel_reasons
import com.rvtechnologies.grigorahq.services.models.OrderAccepModel
import com.rvtechnologies.grigorahq.services.models.ReasonsModel
import com.rvtechnologies.grigorahq.utils.BroadcastConstants
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.cancel_reasons_num
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.IOnRecyclerItemClick
import com.rvtechnologies.grigorahq.utils.PrefConstants
import kotlinx.android.synthetic.main.activity_reject_reasons.*

class RejectReasonsActivity : AppCompatActivity(), EventBroadcaster, IOnRecyclerItemClick {

    var reasonList = ArrayList<ReasonsModel.Data>()
    var orderId: String? = "1"
    var orderType: String? = "0"
    var reason_id = 0

    override

    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setContentView(R.layout.activity_reject_reasons)
        rv_reasons.layoutManager = LinearLayoutManager(this)
        getReasons()
        if (intent != null) {
            orderId = intent.getStringExtra("orderId")
            orderType = intent.getStringExtra("orderType")
        }
        iv_back.setOnClickListener {
            finish()
        }
        btn_reason.setOnClickListener {
            setRejectApi()
        }
    }

    private fun getReasons() {
        var headerMAp = HashMap<String, String>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(this, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")

        ConnectionNetwork.getData(
            cancel_reasons,
            headerMAp,
            this,
            parent_view,
            cancel_reasons_num
        )
    }

    override fun broadcast(code: Int, data: Any?) {
        if (code == cancel_reasons_num) {
            var pojo = Gson().fromJson(data.toString(), ReasonsModel::class.java)
            if (pojo.status) {
                if(reasonList.size>0)
                reasonList.clear()
                if (pojo.data.size > 0) {
                    for(i in 0 until pojo.data.size){
                        if(orderType=="2"){
                            if(pojo.data.get(i).type !=4){
                                reasonList.add(pojo.data.get(i))

                            }
                        }else{
                            reasonList.add(pojo.data.get(i))
                        }
                    }
                    rv_reasons.adapter = ReasonsAdapter(reasonList, this)
                    btn_reason.visibility = VISIBLE
                }
            }

        } else if (code == BroadcastConstants.restaurant_accept_order_num) {
            var pojo = Gson().fromJson(data.toString(), OrderAccepModel::class.java)
            if (pojo.status) {
                ConnectionNetwork.showDialogUnAuthorized(
                    this,
                    getString(R.string.order_id) + pojo.data.id,
                    getString(R.string.order_canceled),
                    getString(R.string.ok),
                    false
                )

            }
        }
    }


    override fun onResume() {
        super.onResume()
        MyApplication.instance!!.registerListener(this)
    }

    override fun onPause() {
        super.onPause()
        MyApplication.instance!!.deRegisterListener(this)
    }

    override fun onClick(item: Any, adapterPosition: Int) {

        reason_id = adapterPosition
        Log.e("selected offer is ", reason_id.toString())


    }

    private fun setRejectApi() {
        var data = HashMap<String, Any?>()

        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(this, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
//        data.put("order_id", orderId)
        data.put("order_id", orderId)
        data.put("reason_id", reason_id)
        data.put("status", "2")

        ConnectionNetwork.postFormData(
            NetworkConstants.restaurant_accept_order,
            headerMAp,
            data,
            "",
            this,
            parent_view,
            BroadcastConstants.restaurant_accept_order_num
        )
    }

    override fun onLongClick(item: Any) {
    }

    override fun onSwitchAvail(item: Any, status: Int) {

    }

    override fun onFeatured(item: Any, status: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
