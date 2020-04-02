package com.rvtechnologies.grigorahq.complete_profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.navigation.NavigationActivity
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.network.NetworkConstants.Companion.get_cuisine
import com.rvtechnologies.grigorahq.services.models.CompleteProfileModel
import com.rvtechnologies.grigorahq.services.models.CusinesModel
import com.rvtechnologies.grigorahq.utils.BroadcastConstants
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.complete_profile_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.get_cuisine_num
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.IOnRecyclerItemClick
import com.rvtechnologies.grigorahq.utils.PrefConstants
import kotlinx.android.synthetic.main.activity_select_cusines.*


class SelectCusinesActivity : AppCompatActivity(), EventBroadcaster, IOnRecyclerItemClick {
    var lat: Double = 0.0
    var lng: Double = 0.0
    var pure_veg: Int = 1
    var address: String = ""
    var position: Int = 0
    var cusine_list: ArrayList<String> = ArrayList()
    lateinit var cusines_comma_seprate: String
    var list: ArrayList<CusinesModel.Data> = ArrayList()

    override fun onLongClick(item: Any, itemView: View) {

    }

    override fun onSwitchAvail(item: Any, status: Int) {


    }

    override fun onClick(item: Any, adapterPosition: Int) {
        position = adapterPosition
        if (item is CusinesModel.Data) {
            list.get(adapterPosition).isChecked = !list.get(adapterPosition).isChecked
            rv_cusine.adapter!!.notifyItemChanged(adapterPosition)
        }

//        numbers = IntArray(cusine_list.size)
//        for (i in 0..cusine_list.size - 1) {
//
//            var OneLetter: Int = cusine_list[i]
//
//            var number: Int = 0
//            numbers[number] = OneLetter
//
//        }


    }


    override fun broadcast(code: Int, data: Any?) {
        if (code == get_cuisine_num) {
            var pojo = Gson().fromJson(data.toString(), CusinesModel::class.java)
            if (pojo.status) {
                if (list.size > 0) {
                    list.clear()
                }
                list.addAll(pojo.data)
                rv_cusine.adapter = CusinesAdapter(list, this)
            }

        } else if (code == complete_profile_num) {
            var pojo = Gson().fromJson(data.toString(), CompleteProfileModel::class.java)
            if (pojo.status) {
                CommonUtils.savePrefs(this, PrefConstants.ADDRESS, pojo.data.address)

                ConnectionNetwork.showSnack(
                    false,
                    this,
                    parent_layout_complete,
                    getString(R.string.profile_completed_successfully)
                )

                startActivity(Intent(this, NavigationActivity::class.java))
                finish()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_cusines)
        rv_cusine.layoutManager = LinearLayoutManager(this)

        if (intent != null) {
            lat = intent.getDoubleExtra("lat", 0.0)
            lng = intent.getDoubleExtra("lng", 0.0)
            pure_veg = intent.getIntExtra("pure_veg", 1)
            address = intent.getStringExtra("address")

        }
        btnBack.setOnClickListener {
            finish()
        }

        btn_back_complte.setOnClickListener {
            finish()
        }
        btn_finish_complte.setOnClickListener {

            for (i in 0..list.size) {
                if (list.get(i).isChecked == true)
                    cusine_list.add(list.get(i).id.toString())
            }
            val idList = cusine_list.toString()

            cusines_comma_seprate = idList.substring(1, idList.length - 1).replace(", ", ",")

            setCusinesApi()
        }
        setApi()
    }

    override fun onPause() {
        super.onPause()
        MyApplication.instance!!.deRegisterListener(this)

    }

    override fun onResume() {
        super.onResume()
        MyApplication.instance!!.registerListener(this)
        setApi()
    }


    fun setApi() {
        var headerMAp = HashMap<String, String>()
        headerMAp.put(
            "Authorization", CommonUtils.getPrefValue(this, PrefConstants.TOKEN)
        )
        headerMAp.put("Accept", "application/json")

        ConnectionNetwork.getData(
            get_cuisine,
            headerMAp,
            this,
            parent_layout_complete,
            get_cuisine_num
        )


    }

    fun setCusinesApi() {
        var data = HashMap<String, Any?>()

        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(this, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")

        data.put("address", address)
        data.put("lat", lat)
        data.put("long", lng)
        data.put("cuisines", cusines_comma_seprate)
        data.put("pure_veg", pure_veg)
        ConnectionNetwork.postFormData(
            NetworkConstants.complete_profile,
            headerMAp,
            data,
            getString(R.string.load_cusins),

            this,
            parent_layout_complete,
            BroadcastConstants.complete_profile_num
        )
    }

}
