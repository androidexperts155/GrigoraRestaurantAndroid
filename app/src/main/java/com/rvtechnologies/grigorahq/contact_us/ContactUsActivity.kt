package com.rvtechnologies.grigorahq.contact_us

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.services.models.ContactUsModel
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.contact_us_num
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.PrefConstants
import kotlinx.android.synthetic.main.activity_contact_us.*
import kotlinx.android.synthetic.main.dialog_contact_us.view.*

class ContactUsActivity : AppCompatActivity(), EventBroadcaster {
    override fun broadcast(code: Int, data: Any?) {
        if (code == contact_us_num) {
            var pojo = Gson().fromJson(data.toString(), ContactUsModel::class.java)
            if (pojo.status) {
                showSetTimeDialog()
            } else {
                ConnectionNetwork.showSnack(
                    false,
                    this,
                    parent_layout_contact,
                   pojo.message
                )
            }
        }
    }

    var select_type: String = "0"
    var order_id: String = ""
    var pos = 0

    var contact_type: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setContentView(R.layout.activity_contact_us)
        setContactype()
        iv_back.setOnClickListener {
            finish()
        }
        cv_call.setOnClickListener {
            setCallingToCustomer(tv_phno.text.toString())
        }
        tv_phno.setOnClickListener {
            setCallingToCustomer("10101010101010")
        }
        cv_email.setOnClickListener {
            setMail("grigora@gmail.com")
        }
        tv_email.setOnClickListener {
            setMail(tv_email.text.toString())
        }
        btn_submit.setOnClickListener {
            if (select_type.equals("0")) {
                ConnectionNetwork.showSnack(
                    false,
                    this,
                    parent_layout_contact,
                    getString(R.string.please_select_type)
                )
            } else {
                if (select_type.equals("complaint")) {
                    if (ed_order_id.text.isNullOrEmpty() || ed_description.text.isNullOrEmpty()) {
                        ConnectionNetwork.showSnack(
                            false,
                            this,
                            parent_layout_contact,
                            getString(R.string.required_fields)
                        )
                    } else {
                        setSettingsApi()
                    }
                } else {
                    if (ed_description.text.isNullOrEmpty()) {
                        ConnectionNetwork.showSnack(
                            false,
                            this,
                            parent_layout_contact,
                            getString(R.string.required_fields)
                        )
                    } else {
                        setSettingsApi()
                    }

                }
            }
        }
    }

    private fun setSettingsApi() {
        order_id = ed_order_id.text.toString()
        var data = HashMap<String, Any?>()

        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(this, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")

        data.put("order_id", order_id)
        data.put("sender_type", "2")
        data.put("contact_type", select_type)
        data.put("description", ed_description.text.toString())
        ConnectionNetwork.postFormData(
            NetworkConstants.contact_us,
            headerMAp,
            data,
            getString(R.string.sending_request),
            this,
            parent_layout_contact,
            contact_us_num
        )

    }


    private fun setContactype() {
        contact_type.add(getString(R.string.please_select_type_contact))
        contact_type.add(getString(R.string.complaint))
        contact_type.add(getString(R.string.feedback))
        contact_type.add(getString(R.string.Suggestions))
        contact_type.add(getString(R.string.refund))


        spinner.adapter = ConatctTypeAdapter(contact_type)

        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                try {
                    if (position == 0) {
                        select_type = "0"
                    } else if (position == 1) {
                        select_type = "1"
                    } else if (position == 2) {
                        select_type = "2"
                    } else if (position == 3) {
                        select_type = "3"
                    } else if (position == 4) {
                        select_type = "4"
                    }

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }

            }

        })
    }

    private fun setMail(userEmail: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:" + userEmail)
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            startActivity(intent)
        }
    }


    private fun setCallingToCustomer(userPhone: String) {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:" + userPhone)//change the number
        startActivity(callIntent)

    }

    override fun onPause() {
        super.onPause()
        MyApplication.instance!!.deRegisterListener(this)

    }

    override fun onResume() {
        super.onResume()
        MyApplication.instance!!.registerListener(this)

    }

    private fun showSetTimeDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_contact_us, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()

        mDialogView.ok.setOnClickListener {
            mAlertDialog.dismiss()
            finish()
        }
    }
}
