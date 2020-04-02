package com.rvtechnologies.grigorahq.complete_profile

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.navigation.NavigationActivity
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.services.models.CompleteProfileModel
import com.rvtechnologies.grigorahq.utils.BroadcastConstants
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.CommonUtils.changeToUTC
import com.rvtechnologies.grigorahq.utils.PrefConstants
import kotlinx.android.synthetic.main.activity_complete_profile.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class CompleteProfileAtivity : AppCompatActivity(), EventBroadcaster {
    var lat = ""
    var lng = ""
    var address = ""
    var pickup: Int = 0
    var veg_only: Int = 0
    private var full_time: String = "0"
    var openingTime = ""
    var closingTime = ""
    override fun broadcast(code: Int, data: Any?) {
        if (code == BroadcastConstants.complete_profile_num) {
            var pojo = Gson().fromJson(data.toString(), CompleteProfileModel::class.java)
            if (pojo.status) {
                CommonUtils.savePrefs(this, PrefConstants.ADDRESS, pojo.data.address)
                CommonUtils.savePrefs(
                    this,
                    PrefConstants.OPENING_TIME,
                    CommonUtils.change24To12(CommonUtils.changeUTC_to_local(pojo.data.opening_time!!))
                )
                CommonUtils.savePrefs(
                    this,
                    PrefConstants.CLOSING_TIME,
                    CommonUtils.change24To12(CommonUtils.changeUTC_to_local(pojo.data.closing_time!!))
                )
                ConnectionNetwork.showSnack(
                    false,
                    this,
                    ll_main,
                    getString(R.string.profile_completed_successfully)
                )
                startActivity(Intent(this, NavigationActivity::class.java))
                finish()
            }
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
        setContentView(R.layout.activity_complete_profile)

        Glide.with(this).load(CommonUtils.getPrefValue(this, PrefConstants.IMAGE))
            .apply(RequestOptions.circleCropTransform()).into(iv_cam)

        textInputSerach.setOnClickListener {
            //            startActivityForResult(Intent(this, SearchAddressActivity::class.java), 2)
            if (!Places.isInitialized()) {
                this.let { Places.initialize(it, "AIzaSyDo51Rxb7QMQ_bKj3hBmZ6Ty8p7BYp7oWU") }
                val fields: List<Place.Field?>
                fields = listOf(
                    Place.Field.ID,
                    Place.Field.LAT_LNG,
                    Place.Field.NAME,
                    Place.Field.ADDRESS
                )
                var intent: Intent = Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields
                    ).setTypeFilter(TypeFilter.ADDRESS)
                    .build(this)
                startActivityForResult(intent, 101)
            }
        }
        btnBack_complete.setOnClickListener {
            finish()
        }
        chk_pickup.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                pickup = 1
            } else {
                pickup = 0
            }
        }
        chk_veg.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                veg_only = 1
            } else {
                veg_only = 0
            }
        }

        open.setOnClickListener {
            timePicker(open, "open")
        }
        close.setOnClickListener {
            timePicker(close, "close")
        }
        check_all_time.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                full_time = "1"
                close.isEnabled = false
                open.isEnabled = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    check_all_time.setTextColor(resources.getColor(R.color.text_primary, theme))
                } else {
                    check_all_time.setTextColor(resources.getColor(R.color.text_primary))

                }
            } else {
                full_time = "0"
                close.isEnabled = true
                open.isEnabled = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    check_all_time.setTextColor(resources.getColor(R.color.grey, theme))
                } else {
                    check_all_time.setTextColor(resources.getColor(R.color.grey))

                }
            }
        }


        tvContinue_profile.setOnClickListener {
            if (validation()) {
                setApi()
            }

        }

    }

    private fun timePicker(textView: TextView?, time_type: String) {
        val cal = Calendar.getInstance()
        val timeSetListner = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            textView!!.text =
                CommonUtils.change24To12(SimpleDateFormat("HH:mm aa").format(cal.time))
            if (time_type.equals("open")) {
                openingTime = SimpleDateFormat("HH:mm").format(cal.time)
            } else
                closingTime = SimpleDateFormat("HH:mm").format(cal.time)

            if (!open.text.isEmpty() && open.text.toString().equals(
                    close.text.toString(),
                    ignoreCase = true
                )
            ) {
                ConnectionNetwork.showSnack(
                    false,
                    this,
                    ll_main,
                    getString(R.string.error_closing_time)
                )
            }
        }
        TimePickerDialog(
            this,
            timeSetListner,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            false
        ).show()

    }

    private fun validation(): Boolean {
        if (textInputSerach.text.isNullOrEmpty()) {

            ConnectionNetwork.showSnack(
                false,
                this,
                ll_main,
                getString(R.string.please_enter_address)
            )
            return false
        } else if (ed_approx_time.text.isNullOrEmpty()) {
            ConnectionNetwork.showSnack(
                false,
                this,
                ll_main,
                getString(R.string.enter_approx_preparing_time)
            )
        } else if (open.text.toString().equals(close.text.toString(), ignoreCase = true)) {
            ConnectionNetwork.showSnack(
                false,
                this,
                ll_main,
                getString(R.string.error_closing_time)
            )
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            try {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                lat = place.latLng?.latitude.toString()
                lng = place.latLng?.longitude.toString()
                address = place.address.toString()
                Log.e("lat", lat + " lng " + lng)
                textInputSerach.text = address
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setApi() {
        var data = HashMap<String, Any?>()
        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(this, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        data.put("address", textInputSerach.text.toString())
        data.put("lat", lat)
        data.put("long", lng)
        data.put("pickup", pickup)
        data.put("full_time", full_time)
        data.put("opening_time", changeToUTC(openingTime))
        data.put("closing_time", changeToUTC(closingTime))
        data.put("pure_veg", veg_only)
        data.put("preparing_time", ed_approx_time.text.toString())
        ConnectionNetwork.postFormData(
            NetworkConstants.complete_profile,
            headerMAp,
            data,
            "",
            this,
            ll_main,
            BroadcastConstants.complete_profile_num
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
