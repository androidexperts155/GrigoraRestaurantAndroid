package com.rvtechnologies.grigorahq.ui.login_signup.login_phone

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.rvtechnologies.grigorahq.ApiRepo
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.databinding.ActivityLoginPhoneBinding
import com.rvtechnologies.grigorahq.navigation.NavigationActivity
import com.rvtechnologies.grigorahq.services.models.LoginResponseModel
import com.rvtechnologies.grigorahq.ui.login_signup.fragments.LogInFragment
import com.rvtechnologies.grigorahq.utils.BroadcastConstants
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.CommonUtils.hideLoader
import com.rvtechnologies.grigorahq.utils.PrefConstants
import com.rvtechnologies.grigorahq.view_model.LoginFragmentViewModel
import kotlinx.android.synthetic.main.activity_login_phone.*

class PhoneLogin : AppCompatActivity() {
    companion object {
        fun newInstance() = PhoneLogin()
    }


    private lateinit var viewModel: LoginFragmentViewModel
    lateinit var activityLoginPhoneBinding : ActivityLoginPhoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        CommonUtils.changeStatusBarColor(
            ContextCompat.getColor(this, R.color.lightGrey),this)
        activityLoginPhoneBinding = DataBindingUtil.setContentView(this,R.layout.activity_login_phone)
        viewModel = ViewModelProviders.of(this).get(LoginFragmentViewModel::class.java)

        activityLoginPhoneBinding.phoneLoginFragment = this

//        viewModel?.loginResult?.observe(this, Observer { response ->
//            if (response is LoginResponseModel) {
//
//            } else {
//                CommonUtils.showMessage(parentView, response.toString())
//            }
//        })
//
//        viewModel?.isLoading?.observe(this, Observer { isLoading ->
//            if (isLoading) {
//                CommonUtils.showLoader(this, "Verifying")
//            } else {
//                CommonUtils.hideLoader()
//            }
//        })
    }


    fun toLogin() {
        startActivity(Intent(this, LogInFragment::class.java))
        finish()
    }

    fun toOTP() {
        if (isValidPhone()!!) {

            var auth = FirebaseAuth.getInstance()
            if (auth.currentUser != null)
                auth.signOut()

            startActivityForResult(
                Intent(this, OtpActivity::class.java).putExtra(
                    "phone",
                    activityLoginPhoneBinding.edNumber?.text.toString()
                ), BroadcastConstants.OTP_CODE
            )

        }
    }
    fun isValidPhone(): Boolean {
        return if (activityLoginPhoneBinding.edNumber.text.isNullOrBlank()) {
            CommonUtils.showMessage(parentView, "Invalid Phone " )
            false
        } else if (activityLoginPhoneBinding.edNumber.text.toString().length < 9) {
            CommonUtils.showMessage(parentView, "Invalid Phone " )
            false
        } else
            true
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == BroadcastConstants.OTP_CODE) {
            if (data?.extras?.containsKey("verified")!!)
                if (data?.getBooleanExtra("verified", false)!!)
                    phoneLogin()
        }

    }

    fun phoneLogin() {
        CommonUtils.showLoader(this, "Verifying")
        if (isValidPhone()) {
            ApiRepo.getInstance()
                .phoneLogin(
                    activityLoginPhoneBinding.edNumber.toString().trim()
                ) { success, result ->
                    hideLoader()
                    if (success) {
                        var pojo =
                            Gson().fromJson(
                                result as JsonElement,
                                LoginResponseModel::class.java
                            )
                        CommonUtils.showMessage(parentView, "Welcome " + pojo.data?.name)
                        saveData(pojo)
                    } else {
                        var pojo = result
                        CommonUtils.showMessage(parentView, pojo.toString())

                    }
                }
        }
    }


    private fun saveData(data: LoginResponseModel) {
        CommonUtils.savePrefs(this, PrefConstants.TOKEN, data.tokenType + " " + data.accessToken)
        CommonUtils.savePrefs(this, PrefConstants.ID, data.data?.id?.toString())
        CommonUtils.savePrefs(this, PrefConstants.NAME, data.data?.name?.toString())
        CommonUtils.savePrefs(this, PrefConstants.IMAGE, data.data?.image?.toString())

    }

}
