package com.rvtechnologies.grigorahq.ui.login_signup.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.complete_profile.CompleteProfileAtivity
import com.rvtechnologies.grigorahq.forgot_password.ForgotPasswordActivity
import com.rvtechnologies.grigorahq.navigation.NavigationActivity
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.services.models.LoginModel
import com.rvtechnologies.grigorahq.ui.login_signup.login_phone.PhoneLogin
import com.rvtechnologies.grigorahq.utils.BroadcastConstants
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.PrefConstants
import kotlinx.android.synthetic.main.approval_dialog.view.*
import kotlinx.android.synthetic.main.fragment_login.*

class LogInFragment : AppCompatActivity(), EventBroadcaster {
    var type: String = ""

    override fun broadcast(code: Int, data: Any?) {

        if (code == BroadcastConstants.login_num) {
            Log.e("check", "login")

            var pojo = Gson().fromJson(data.toString(), LoginModel::class.java)

            if (pojo.status) {

                CommonUtils.savePrefs(this, PrefConstants.TOKEN, "Bearer " + pojo.accessToken)
                CommonUtils.savePrefs(this, PrefConstants.NAME, pojo.data.name)
                CommonUtils.savePrefs(this, PrefConstants.FRENCH_NAME, pojo.data.frenchName)
                CommonUtils.savePrefs(
                    this,
                    PrefConstants.FRENCH_ADDRESS,
                    pojo.data.frenchAddress
                )
                CommonUtils.savePrefs(this, PrefConstants.ADDRESS, pojo.data.address)
                CommonUtils.savePrefs(this, PrefConstants.EMAIL, pojo.data.email)
                CommonUtils.savePrefs(this, PrefConstants.PHONE, pojo.data.phone)
                CommonUtils.savePrefs(this, PrefConstants.IMAGE, pojo.data.image)
                CommonUtils.savePrefs(this, PrefConstants.PROOF, pojo.data.idProof)
                CommonUtils.savePrefs(this, PrefConstants.ROLE, pojo.data.role)
                CommonUtils.savePrefs(this, PrefConstants.CREATED_AT, pojo.data.createdAt)
                CommonUtils.savePrefs(this, PrefConstants.LAT, pojo.data.latitude)
                CommonUtils.savePrefs(this, PrefConstants.LONG, pojo.data.longitude)
                CommonUtils.savePrefs(this, PrefConstants.PURE_VEG, pojo.data.pureVeg)
                CommonUtils.savePrefs(this, PrefConstants.OFFER, pojo.data.offer)
                CommonUtils.savePrefs(this, PrefConstants.ID, pojo.data.id.toString())
                CommonUtils.savePrefs(this, PrefConstants.WALLET, pojo.data.wallet)
                CommonUtils.savePrefs(this, PrefConstants.RATING, pojo.data.avg_ratings)
                CommonUtils.savePrefs(this, PrefConstants.RECIEPT_NUMBER, pojo.data.receipt_id)

                CommonUtils.savePrefs(this, PrefConstants.FULL_TIME, pojo.data.full_time)
                CommonUtils.savePrefs(this, PrefConstants.PICKUP, pojo.data.pickup)
                CommonUtils.savePrefs(this, PrefConstants.PURE_VEG, pojo.data.pureVeg)
                CommonUtils.savePrefs(this, PrefConstants.BUSY_STATUS, pojo.data.busyStatus)
                Log.e("check", pojo.data.busyStatus)

//                    CommonUtils.savePrefs(this, PrefConstants.WALLET, pojo.data.)

                CommonUtils.showToast(this, getString(R.string.login_successfully))
                if (CommonUtils.getPrefValue(this, PrefConstants.ADDRESS).isNullOrEmpty()) {
                    startActivity(Intent(this, CompleteProfileAtivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this, NavigationActivity::class.java))
                    finishAffinity()
                }

            } else {
                CommonUtils.showToast(this, getString(R.string.went_wrong))
            }
        } else if (data is String) {
            CommonUtils.showToast(this, data)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setContentView(R.layout.fragment_login)
        if (intent != null && !intent.getStringExtra("type").isNullOrEmpty()) {
            type = intent.getStringExtra("type")
            if (type.equals("signup")) {
                val mDialogView = LayoutInflater.from(this).inflate(R.layout.approval_dialog, null)
                val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)

                val mAlertDialog = mBuilder.show()

                mDialogView.btn_ok.setOnClickListener {
                    mAlertDialog.dismiss()
                }
            }

        }

        login_back.setOnClickListener {
            finish()
        }
        phone_Login.setOnClickListener {
            startActivity(Intent(this, PhoneLogin::class.java))
            finish()
        }

        textViewLogin.setOnClickListener {
            if (setValidation()) {
                setApi()
            }
        }

        tv_sign_up.setOnClickListener {
            startActivity(Intent(this, SignupFragment::class.java))
            finish()
        }

        tv_forgot_login.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

    }


    override fun onPause() {
        super.onPause()
        MyApplication.instance!!.deRegisterListener(this)

    }

    override fun onResume() {
        super.onResume()
        MyApplication.instance!!.registerListener(this)
    }

    fun setApi() {
        var data = HashMap<String, Any?>()

        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Accept", "application/json")
        data.put("role", "4")
        data.put("username", ed_email.text.toString())
        data.put("password", ed_pass.text.toString())

        ConnectionNetwork.postFormData(
            NetworkConstants.login,
            headerMAp,
            data,
            getString(R.string.login),
            this,
            parent_layout,
            BroadcastConstants.login_num
        )


    }

    private fun setValidation(): Boolean {
        if (ed_email.text.toString().length <= 0) {
            ConnectionNetwork.showSnack(
                false,
                this,
                parent_layout,
                getString(R.string.please_enter_your_email_address)
            )
            return false
        } else if (ed_pass.text.toString().length <= 0) {
            ConnectionNetwork.showSnack(
                false,
                this,
                parent_layout,
                getString(R.string.please_enter_your_password)
            )
            return false
        }
        return true
    }

    companion object {
        val TAG = LogInFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): LogInFragment = LogInFragment()
    }

}