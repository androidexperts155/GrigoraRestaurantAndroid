package com.rvtechnologies.grigorahq.profile

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import co.csadev.kwikpicker.KwikPicker
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.navigation.NavigationActivity
import com.rvtechnologies.grigorahq.navigation.NavigationActivity.Companion.drawer_layout
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.network.NetworkConstants.Companion.edit_profile
import com.rvtechnologies.grigorahq.network.NetworkConstants.Companion.get_userinfo
import com.rvtechnologies.grigorahq.services.models.OnlineStatusModel
import com.rvtechnologies.grigorahq.services.models.UpdateProfileModel
import com.rvtechnologies.grigorahq.services.models.UserProfileModel
import com.rvtechnologies.grigorahq.utils.BroadcastConstants
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.available_status_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.edit_profile_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.get_user_info_num
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.CommonUtils.change24To12
import com.rvtechnologies.grigorahq.utils.CommonUtils.changeToUTC
import com.rvtechnologies.grigorahq.utils.CommonUtils.changeUTC_to_local
import com.rvtechnologies.grigorahq.utils.PrefConstants
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ProfileFragment : Fragment(), EventBroadcaster {
    var pickup: Int = 0
    private var full_time: String = "0"
    var veg_only: Int = 0
    var openingTime = ""
    var closingTime = ""

    override fun broadcast(code: Int, data: Any?) {
        if (code == available_status_num) {
            var pojo = Gson().fromJson(data.toString(), OnlineStatusModel::class.java)
            if (pojo.status) {

            }
        }
        else if (code == edit_profile_num) {
            var pojo = Gson().fromJson(data.toString(), UpdateProfileModel::class.java)
            if (pojo.status) {
                ConnectionNetwork.showSnack(
                    false,
                    activity!!,
                    parent_layout_profile,
                    getString(R.string.update_profile)
                )

                CommonUtils.savePrefs(activity, PrefConstants.ADDRESS, pojo.data.address)
                CommonUtils.savePrefs(activity, PrefConstants.EMAIL, pojo.data.email)
                CommonUtils.savePrefs(activity, PrefConstants.PHONE, pojo.data.phone)
                CommonUtils.savePrefs(activity, PrefConstants.IMAGE, pojo.data.image)
                CommonUtils.savePrefs(activity, PrefConstants.LAT, pojo.data.latitude)
                CommonUtils.savePrefs(activity, PrefConstants.LONG, pojo.data.longitude)
                CommonUtils.savePrefs(
                    activity,
                    PrefConstants.OPENING_TIME,
                    change24To12(changeUTC_to_local(pojo.data.openingTime))
                )
                CommonUtils.savePrefs(
                    activity,
                    PrefConstants.CLOSING_TIME,
                    change24To12(changeUTC_to_local(pojo.data.closingTime))
                )
                CommonUtils.savePrefs(activity, PrefConstants.FULL_TIME, pojo.data.full_time)
                CommonUtils.savePrefs(activity, PrefConstants.PURE_VEG, pojo.data.pureVeg)
                CommonUtils.savePrefs(activity, PrefConstants.PICKUP, pojo.data.pickup)
                ed_name_profile.isCursorVisible = false
                ed_address_profile.isCursorVisible = false
                ed_email_profile.isCursorVisible = false
                ed_phone_profile.isCursorVisible = false
                isTimeCleck = false
                iv_pic_profile.isEnabled = false
                tv_saveProfile.visibility = GONE
                iv_edit.visibility = VISIBLE
                startActivity(Intent(activity!!, NavigationActivity::class.java))
                activity!!.finish()
            }
        }
        else if (code == get_user_info_num) {
            var pojo = Gson().fromJson(data.toString(), UserProfileModel::class.java)
            if (pojo.status) {

                setProfile(pojo)

                CommonUtils.savePrefs(activity, PrefConstants.ADDRESS, pojo.data.address)
                CommonUtils.savePrefs(activity, PrefConstants.EMAIL, pojo.data.email)
                CommonUtils.savePrefs(activity, PrefConstants.PHONE, pojo.data.phone)
                CommonUtils.savePrefs(activity, PrefConstants.IMAGE, pojo.data.image)
                CommonUtils.savePrefs(activity, PrefConstants.LAT, pojo.data.latitude)
                CommonUtils.savePrefs(activity, PrefConstants.LONG, pojo.data.longitude)
                CommonUtils.savePrefs(
                    activity,
                    PrefConstants.OPENING_TIME,
                    change24To12(changeUTC_to_local(pojo.data.openingTime))
                )
                CommonUtils.savePrefs(
                    activity,
                    PrefConstants.CLOSING_TIME,
                    change24To12(changeUTC_to_local(pojo.data.closingTime))
                )
                CommonUtils.savePrefs(activity, PrefConstants.FULL_TIME, pojo.data.fullTime)
                CommonUtils.savePrefs(activity, PrefConstants.PURE_VEG, pojo.data.pureVeg)
                CommonUtils.savePrefs(activity, PrefConstants.PICKUP, pojo.data.pickup)
                ed_name_profile.isCursorVisible = false
                ed_address_profile.isCursorVisible = false
                ed_email_profile.isCursorVisible = false
                ed_phone_profile.isCursorVisible = false
                isTimeCleck = false
                iv_pic_profile.isEnabled = false
                tv_saveProfile.visibility = GONE
                iv_edit.visibility = VISIBLE
            }
        }
    }

    private var img_url: String = ""
    private var proof_img_url: String = ""
    private var status_on: Int = 2
    var lat = "0"
    var lng = "0"
    var address = ""
    var isTimeCleck = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getProfileInfo()
        iv_proof_profile.setOnClickListener {
            imageSelection(iv_proof_profile, "proof")
        }
        iv_pic_profile.setOnClickListener {
            imageSelection(iv_pic_profile, "profile")
        }


        open.setOnClickListener {
            if (isTimeCleck)
                timePicker(open, "open")
        }
        close.setOnClickListener {
            if (isTimeCleck)
                timePicker(close, "close")

        }
        iv_menu.setOnClickListener {
            drawer_layout =
                (activity as Activity).findViewById(R.id.drawerlayout);
            if (drawer_layout!!.isDrawerVisible(GravityCompat.START)) {
                drawer_layout!!.closeDrawer(GravityCompat.START)
            } else {
                drawer_layout!!.openDrawer(GravityCompat.START)
            }
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
        ed_address_profile.setOnClickListener {
            //                        startActivityForResult(Intent(activity, SearchAddressActivity::class.java), 2)
            context?.let { Places.initialize(it, "AIzaSyDo51Rxb7QMQ_bKj3hBmZ6Ty8p7BYp7oWU") }
            if (Places.isInitialized()) {
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
                    .build(activity!!)
                startActivityForResult(intent, 101)
            }
        }
        check_all_time.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                isTimeCleck = false
                full_time = "1"
                close.isEnabled = false
                open.isEnabled = false
                close.isClickable = false
                open.isClickable = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    check_all_time.setTextColor(
                        resources.getColor(
                            R.color.text_primary,
                            activity!!.theme
                        )
                    )
                    open.setTextColor(
                        resources.getColor(
                            R.color.grey,
                            activity!!.theme
                        )
                    )
                    close.setTextColor(
                        resources.getColor(
                            R.color.grey,
                            activity!!.theme
                        )
                    )
                } else {
                    check_all_time.setTextColor(resources.getColor(R.color.text_primary))
                    open.setTextColor(
                        resources.getColor(
                            R.color.grey
                        )
                    )
                    close.setTextColor(
                        resources.getColor(
                            R.color.grey
                        )
                    )
                }
            } else {
                full_time = "0"
                isTimeCleck = true
                close.isEnabled = true
                open.isEnabled = true
                close.isClickable = true
                open.isClickable = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    check_all_time.setTextColor(
                        resources.getColor(
                            R.color.grey,
                            activity!!.theme
                        )
                    )
                    open.setTextColor(
                        resources.getColor(
                            R.color.text_primary,
                            activity!!.theme
                        )
                    )
                    close.setTextColor(
                        resources.getColor(
                            R.color.text_primary,
                            activity!!.theme
                        )
                    )
                } else {
                    check_all_time.setTextColor(resources.getColor(R.color.grey))
                    open.setTextColor(
                        resources.getColor(
                            R.color.text_primary
                        )
                    )
                    close.setTextColor(
                        resources.getColor(
                            R.color.text_primary
                        )
                    )
                }
            }
        }

        toogle_on_off.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                status_on = 1
            } else {
                status_on = 0
            }
            setOnOffSatus(status_on)

        }
        iv_edit.setOnClickListener {
            ed_name_profile.requestFocus()
            ed_name_profile.isCursorVisible = true
            ed_address_profile.isCursorVisible = true
            ed_address_profile.isEnabled = true
            ed_email_profile.isEnabled = true
            ed_phone_profile.isEnabled = true

            ed_email_profile.isCursorVisible = true
            ed_phone_profile.isCursorVisible = true
            iv_pic_profile.isEnabled = true
            ed_name_profile.isPressed = true
            iv_proof_profile.isEnabled = false
            isTimeCleck = true

            check_all_time.isEnabled = true
            chk_veg.isEnabled = true
            chk_pickup.isEnabled = true
            open.isClickable = true
            close.isClickable = true

            iv_edit.visibility = GONE
            tv_saveProfile.visibility = VISIBLE

        }
        tv_saveProfile.setOnClickListener {
            if (verification()) {
            setUpdateProfileApi()
        }
    }

    }

    private fun timePicker(textView: TextView?, time_type: String) {
        val cal = Calendar.getInstance()
        val timeSetListner = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            textView!!.text = change24To12(SimpleDateFormat("HH:mm aa").format(cal.time))
            if (time_type.equals("open")) {
                openingTime = SimpleDateFormat("HH:mm").format(cal.time)
            } else
                closingTime = SimpleDateFormat("HH:mm").format(cal.time)
            if (!open.text.isEmpty() && open.text.toString()
                    .equals(close.text.toString(), ignoreCase = true)
            ) {
                ConnectionNetwork.showSnack(
                    false,
                    activity!!,
                    parent_layout_profile,
                    getString(R.string.error_closing_time)
                )
            }

        }
        TimePickerDialog(
            activity,
            timeSetListner,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            false
        ).show()

    }


    private fun setUpdateProfileApi() {
        var data = HashMap<String, Any>()
        var fileMap = HashMap<String, File>()
        var headerMAp = HashMap<String, String>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        data.put("name", ed_name_profile.text.toString())
        data.put("address", ed_address_profile.text.toString())
        data.put("pickup", pickup)
        data.put("full_time", full_time)
        data.put("pure_veg", veg_only)
        data.put("lat", lat)
        data.put("long", lng)
        data.put("phone", ed_phone_profile.text.toString())


        if (openingTime.isEmpty() || closingTime.isEmpty()) {
            data.put("opening_time", openingTime)
            data.put("closing_time", closingTime)
        } else {
            data.put("opening_time", changeToUTC(openingTime))
            data.put("closing_time", changeToUTC(closingTime))
        }
        if (img_url.isNullOrEmpty()) {
            data.put("image", File(img_url).absoluteFile)
        } else {
            fileMap.put("image", File(img_url).absoluteFile)
        }

        var list = ArrayList<String>()
        list.add("2")
        ConnectionNetwork.getData(
            edit_profile,
            headerMAp,
            ConnectionNetwork.convertRequestBodyfromMap(activity!!, data),
            ConnectionNetwork.converRequestBodyFromMapImage(fileMap, list),
            activity!!,
            parent_layout_profile,
            edit_profile_num
        )


    }

    private fun getProfileInfo() {
        var headerMAp = HashMap<String, String>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")

        ConnectionNetwork.getData(
            get_userinfo,
            headerMAp,
            activity!!,
            parent_layout_profile,
            get_user_info_num

        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                lat = place.latLng?.latitude.toString()
                lng = place.latLng?.longitude.toString()
                address = place.address.toString()
                Log.e("lat", lat + " lng " + lng)
                ed_address_profile.text = address
            }
        }
    }

    private fun verification(): Boolean {
        if (ed_name_profile.text.isNullOrEmpty() || ed_address_profile.text.isNullOrEmpty() || ed_phone_profile.text.isNullOrEmpty() || ed_email_profile.text.isNullOrEmpty()) {
            ConnectionNetwork.showSnack(
                false,
                activity!!,
                parent_layout_profile,
                getString(R.string.required_fields)
            )
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(ed_email_profile.text).matches()) {
            ConnectionNetwork.showSnack(
                false,
                activity!!,
                parent_layout_profile,
                getString(R.string.please_enter_valid_email_address)
            )
            return false

        } else if (open.text.toString().equals(close.text.toString(), ignoreCase = true)) {
            ConnectionNetwork.showSnack(
                false,
                activity!!,
                parent_layout_profile,
                getString(R.string.error_closing_time)
            )
            return false
        }


        return true
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    private fun setProfile(pojo: UserProfileModel) {
        isTimeCleck = false

        tv_saveProfile.visibility = GONE
        iv_edit.visibility = VISIBLE
        ed_name_profile.isCursorVisible = false
        iv_proof_profile.isEnabled = false
        ed_address_profile.isEnabled = false
        rate_profile.rating = pojo.data.avgRatings
        ed_name_profile.setText(pojo.data.name)
        ed_email_profile.setText(pojo.data.email)
        ed_address_profile.setText(pojo.data.address)
        ed_phone_profile.setText(pojo.data.phone)
        close.setText(change24To12(changeUTC_to_local(pojo.data.closingTime.substring(0, 5))))
        open.setText(change24To12(changeUTC_to_local(pojo.data.openingTime.substring(0, 5))))
        Log.e("Opening", CommonUtils.getPrefValue(activity, PrefConstants.OPENING_TIME))

        if (pojo.data.busyStatus.equals("0")) {
            ed_name_profile.setCompoundDrawablesWithIntrinsicBounds(
                null, null, ContextCompat.getDrawable(
                    activity!!,
                    R.drawable.ic_online_profile
                ), null
            )
        } else if (pojo.data.busyStatus.equals("1")) {
            ed_name_profile.setCompoundDrawablesWithIntrinsicBounds(
                null, null, ContextCompat.getDrawable(
                    activity!!,
                    R.drawable.ic_offline_profile
                ), null
            )
        }

        wallet_profile.setText(getString(R.string.naira_sign) + pojo.data.wallet)

//        wallet_profile.setOnClickListener {
//            startActivity(Intent(activity, WalletActivity::class.java))
//        }
        Glide.with(this).load(pojo.data.image)
            .into(iv_pic_profile)
//        Glide.with(this).load(CommonUtils.getPrefValue(activity, PrefConstants.IMAGE))
//            .into(iv_cover)
        Glide.with(this).load(pojo.data.licenseImage)
            .into(iv_proof_profile)

        check_all_time.isEnabled = false
        chk_veg.isEnabled = false
        chk_pickup.isEnabled = false
        ed_email_profile.isEnabled = false
        ed_phone_profile.isEnabled = false

        open.isClickable = false
        close.isClickable = false

        check_all_time.isChecked =
            pojo.data.fullTime.equals("1")
        chk_veg.isChecked = pojo.data.pureVeg.equals("1")
        chk_pickup.isChecked = pojo.data.pickup.equals("1")
    }


    fun imageSelection(image_view: ImageView, type: String) {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                val bottomSheetDialogFragment = KwikPicker.Builder(
                    activity!!,
                    imageProvider = { image_view, uri ->
                        Glide.with(activity!!)
                            .load(uri)
                            .into(image_view)
                    },
                    onImageSelectedListener = { uri: Uri ->
                        image_view.post {
                            if (uri.path != null && type.equals("profile")) {
                                Glide.with(activity!!)
                                    .load(uri).apply(RequestOptions.circleCropTransform())
                                    .into(image_view)
                                Glide.with(activity!!)
                                    .load(uri)
                                    .into(iv_cover)
                                img_url = uri.path!!
                            } else {
                                Glide.with(activity!!)
                                    .load(uri)
                                    .into(image_view)

                                proof_img_url = uri.path!!
                            }
                        }
                    },
                    peekHeight = 1200
                )
                    .create(activity!!)
                bottomSheetDialogFragment.show(fragmentManager!!)
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(
                    activity!!,
                    "Permission Denied\n" + deniedPermissions.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        TedPermission.with(activity)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .check()


    }


    override fun onPause() {
        super.onPause()
        MyApplication.instance!!.deRegisterListener(this)

    }

    override fun onResume() {
        super.onResume()
        MyApplication.instance!!.registerListener(this)

    }

    fun setOnOffSatus(statusOn: Int) {
        var data = HashMap<String, Any?>()

        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")

        data.put("status", statusOn)

        ConnectionNetwork.postFormData(
            NetworkConstants.available_status,
            headerMAp,
            data,
            "",
            activity!!,
            parent_layout_profile,
            BroadcastConstants.available_status_num
        )
    }


    companion object {

        @JvmStatic
        fun newInstance(): ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }
}
