package com.rvtechnologies.grigorahq.settings

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.services.models.ChangePasswordModel
import com.rvtechnologies.grigorahq.utils.BroadcastConstants
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.PrefConstants
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity(), EventBroadcaster {
    override fun broadcast(code: Int, data: Any?) {
        if (code == BroadcastConstants.change_password_num) {
            var pojo = Gson().fromJson(data.toString(), ChangePasswordModel::class.java)
            if (pojo.status) {
                ConnectionNetwork.showSnack(
                    false,
                    this,
                    parent_layout_reset,
                    pojo.message
                )
                finish()
            } else {
                ConnectionNetwork.showSnack(
                    false,
                    this,
                    parent_layout_reset,
                    pojo.message
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setContentView(R.layout.activity_reset_password)
        iv_back.setOnClickListener {
            finish()
        }

        tv_resetPass.setOnClickListener {
            if (varification()) {
                resetApi()
            }
        }
    }

    private fun varification(): Boolean {
        if (ed_old_pass.text.isNullOrEmpty() || ed_new_pass.text.isNullOrEmpty()) {
            ConnectionNetwork.showSnack(
                false,
                this,
                parent_layout_reset,
                getString(R.string.required_fields)
            )
            return false
        }
        return true
    }


    private fun resetApi() {
        var data = HashMap<String, Any?>()

        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(this, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")

        data.put("old_password", ed_old_pass.text.toString())
        data.put("password", ed_new_pass.text.toString())

        ConnectionNetwork.postFormData(
            NetworkConstants.change_password,
            headerMAp,
            data,
            "",

            this,
            parent_layout_reset,
            BroadcastConstants.change_password_num
        )


    }


    override fun onPause() {
        super.onPause()
        MyApplication.instance!!.deRegisterListener(this)

    }

    override fun onResume() {
        super.onResume()
        MyApplication.instance!!.registerListener(this)

    }
}
