package com.rvtechnologies.grigorahq.forgot_password

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.services.models.ForgotPasswordModel
import com.rvtechnologies.grigorahq.utils.BroadcastConstants
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.forgot_password_num
import kotlinx.android.synthetic.main.activity_forogt_passord.*
import kotlinx.android.synthetic.main.dialog_simple_msg.*

class ForgotPasswordActivity : AppCompatActivity(), EventBroadcaster {
    override fun broadcast(code: Int, data: Any?) {
        if (code == forgot_password_num) {
            var pojo = Gson().fromJson(data.toString(), ForgotPasswordModel::class.java)
            if (pojo.status) {
                showEmailDialog()
            } else {
                ConnectionNetwork.showSnack(
                    false,
                    this,
                    forgot_parent_layout,
                    getString(R.string.went_wrong)
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
        setContentView(R.layout.activity_forogt_passord)
        textViewSend.setOnClickListener {
            if (setValidation()) {
                setForgotApi()
            }
        }
        iv_back.setOnClickListener {
            finish()
        }
    }

    private fun setForgotApi() {
        var data = HashMap<String, Any?>()

        data.put("email", ed_forgot_email.text.toString())


        ConnectionNetwork.postFormData(
            NetworkConstants.forgot_password,
            data,
            this,
            forgot_parent_layout,
            BroadcastConstants.forgot_password_num
        )

    }
    private fun showEmailDialog() {
        val mDialogView =
            LayoutInflater.from(this).inflate(R.layout.dialog_simple_msg, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()
        mAlertDialog.tv_msg.text = getString(R.string.forgot_success_msg)
        mAlertDialog.tv_ok.setOnClickListener {
           finish()
        }
    }

    private fun setValidation(): Boolean {
        if (ed_forgot_email.text.toString().isEmpty()) {
            ConnectionNetwork.showSnack(
                false,
                this,
                forgot_parent_layout,
                getString(R.string.please_enter_address)
            )
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(ed_forgot_email.text).matches()) {
            ConnectionNetwork.showSnack(
                false,
                this,
                forgot_parent_layout,
                getString(R.string.please_enter_valid_email_address)
            )
            return false
        }
        return true
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
