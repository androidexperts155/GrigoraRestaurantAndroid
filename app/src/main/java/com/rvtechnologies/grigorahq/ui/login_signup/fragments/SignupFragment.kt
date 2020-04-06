package com.rvtechnologies.grigorahq.ui.login_signup.fragments

import android.Manifest
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import co.csadev.kwikpicker.KwikPicker
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.rvtechnologies.grigorahq.ApiRepo
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.ConnectionNetwork.Companion.getData
import com.rvtechnologies.grigorahq.network.ConnectionNetwork.Companion.showSnack
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants.Companion.get_brands
import com.rvtechnologies.grigorahq.network.NetworkConstants.Companion.signup
import com.rvtechnologies.grigorahq.services.models.GetBrandsModel
import com.rvtechnologies.grigorahq.services.models.LoginResponseModel
import com.rvtechnologies.grigorahq.services.models.SignupModel
import com.rvtechnologies.grigorahq.ui.login_signup.login_phone.OtpActivity
import com.rvtechnologies.grigorahq.utils.BroadcastConstants
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.get_brands_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.signup_num
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.CommonUtils.showToast
import com.rvtechnologies.grigorahq.utils.IOnRecyclerItemClick
import com.rvtechnologies.grigorahq.utils.PrefConstants
import kotlinx.android.synthetic.main.activity_login_phone.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class SignupFragment : AppCompatActivity(), EventBroadcaster, IOnRecyclerItemClick {

    private var img_url: String = ""
    private var brand_img_url: String = ""
    private var proof_img_url: String = ""
    private var franchisee_proof_url: String = ""
    private var otp_check: String = ""
    private var full_time: String = "0"
    val mList: ArrayList<GetBrandsModel.Data> = ArrayList()
    lateinit var mAdapter: BrandsFillterAdapter

    override fun broadcast(code: Int, data: Any?) {
        if (code == signup_num) {
            var pojo = Gson().fromJson(data.toString(), SignupModel::class.java)
            if (pojo.status) {
//                savePrefs(this, TOKEN, "Bearer " + pojo.accessToken)
//                savePrefs(this, NAME, pojo.data.name)
//                savePrefs(this, FRENCH_NAME, pojo.data.frenchName)
//                savePrefs(this, EMAIL, pojo.data.email)
//                savePrefs(this, PHONE, pojo.data.phone)
//                savePrefs(this, IMAGE, pojo.data.image)
//                savePrefs(this, PROOF, pojo.data.idProof)
//                savePrefs(this, ROLE, pojo.data.role)
//                savePrefs(this, CREATED_AT, pojo.data.createdAt)
//                savePrefs(this, ID, pojo.data.id.toString())

                showToast(this, "Signed up successfully...!")
                startActivity(Intent(this, LogInFragment::class.java).putExtra("type", "signup"))

            } else {
                showToast(this, "Something went wrong...!")
            }
        } else if (code == get_brands_num) {
            var pojo = Gson().fromJson(data.toString(), GetBrandsModel::class.java)
            if (mList.size > 0) {
                mList.clear()
            }
            if (pojo.status) {
                if (pojo.data.size > 0) {
                    mList.addAll(pojo.data)
                    mAdapter = BrandsFillterAdapter(mList, this)
                    rv_brands.adapter = mAdapter
                }
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
        setContentView(R.layout.fragment_sign_up)
        cv_brands.visibility = GONE

        rv_brands.layoutManager = LinearLayoutManager(this)

        setBrands()

        ed_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                cv_brands.visibility = VISIBLE
                filter(s.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        ed_phone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                otp_check=""
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })


        iv_cam.setOnClickListener {
            imageSelection(iv_cam, "profile")
        }
        iv_franchisee_proof.setOnClickListener {
            imageSelection(iv_franchisee_proof, "franchisee_proof")

        }
        iv_proof.setOnClickListener {
            imageSelection(iv_proof, "proof")

        }

        textViewLogin.setOnClickListener {
            startActivity(Intent(this, LogInFragment::class.java))
            finish()
        }
        textViewSignUp.setOnClickListener {
            if (validation()) {
                if(otp_check.isNullOrEmpty()){
                    toOTP()
                }else{
                    setApi()
                }

            }
        }

        tv_open.setOnClickListener {
            timePicker(tv_open)
        }
        tv_close.setOnClickListener {
            timePicker(tv_close)
        }

        check_all_time.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                full_time = "1"
                tv_close.isEnabled = false
                tv_open.isEnabled = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    check_all_time.setTextColor(resources.getColor(R.color.text_primary, theme))
                } else {
                    check_all_time.setTextColor(resources.getColor(R.color.text_primary))

                }
            } else {
                full_time = "0"
                tv_close.isEnabled = true
                tv_open.isEnabled = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    check_all_time.setTextColor(resources.getColor(R.color.grey, theme))
                } else {
                    check_all_time.setTextColor(resources.getColor(R.color.grey))

                }

            }
        }

    }

    private fun setBrands() {
        var headerMAp = HashMap<String, String>()
//        headerMAp.put("Authorization", CommonUtils.getPrefValue(this, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        getData(
            get_brands,
            headerMAp,
            this,
            parent_layout_signup,
            get_brands_num
        )

    }

    private fun filter(text: String) {
        //new array list that will hold the filtered data
        val filterdNames = ArrayList<GetBrandsModel.Data>()

        //looping through existing elements
        for (s in mList) {
            //if the existing elements contains the search input
            if (Pattern.compile(
                    Pattern.quote(text),
                    Pattern.CASE_INSENSITIVE
                ).matcher(s.name).find()
            ) {
                //adding the element to filtered list
//                if (filterdNames.size > 0) {
//                    filterdNames.clear()
//                } else
                filterdNames.add(s)

            }

        }
        if (!ed_name.text.isNullOrEmpty()) {

            //calling a method of the adapter class and passing the filtered list
            mAdapter.filterList(filterdNames)
        } else {
            cv_brands.visibility = GONE
        }
    }

    private fun timePicker(textView: TextView?) {
        val cal = Calendar.getInstance()
        val timeSetListner = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            textView!!.text = SimpleDateFormat("HH:mm").format(cal.time)
        }
        TimePickerDialog(
            this,
            timeSetListner,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()

    }


    private fun validation(): Boolean {
        if (ed_name.text.toString().length <= 0) {
            showSnack(
                false,
                this,
                parent_layout_signup,
                getString(R.string.please_enter_your_full_name)
            );
            return false
        } else if (ed_email.text.toString().length <= 0) {
            showSnack(
                false,
                this,
                parent_layout_signup,
                getString(R.string.please_enter_your_email_address)
            )
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(ed_email.text).matches()) {
            showSnack(
                false,
                this,
                parent_layout_signup,
                getString(R.string.please_enter_valid_email_address)
            )
            return false
        } else if (ed_phone.text.toString().length <= 0) {
            showSnack(
                false,
                this,
                parent_layout_signup,
                getString(R.string.please_enter_your_contact_number)
            )
            return false
        }
//        else if (full_time.equals("0")) {
//            if (tv_open.text.isNullOrEmpty()) {
//                showSnack(
//                    false,
//                    this,
//                    parent_layout_signup,
//                    getString(R.string.enter_opening_time)
//                )
//                return false
//            } else if (tv_close.text.isNullOrEmpty()) {
//
//                showSnack(
//                    false,
//                    this,
//                    parent_layout_signup,
//                    getString(R.string.enter_closing_time)
//                )
//                return false
//            }
//        }


        else if (ed_pass.text.toString().length <= 0) {
            showSnack(
                false,
                this,
                parent_layout_signup,
                getString(R.string.enter_password)
            )
            return false
        } else if (ed_pass.text.toString().length < 6) {
            showSnack(
                false,
                this,
                parent_layout_signup,
                getString(R.string.please_enter_atleast_six_characters)
            )
            return false
        } else if (ed_confirm_pass.text.toString().length <= 0) {
            showSnack(
                false,
                this,
                parent_layout_signup,
                getString(R.string.please_enter_confirm_password)
            )
            return false
        } else if (!ed_confirm_pass.text.toString().equals(ed_pass.text.toString())) {
            showSnack(
                false,
                this,
                parent_layout_signup,
                getString(R.string.entered_password_is_not_matching)
            )
            return false
        } else if (proof_img_url.length <= 0) {
            showSnack(
                false,
                this,
                parent_layout_signup,
                getString(R.string.please_select_your_proof_id_image)
            )
            return false
        }

//        else if (img_url.length <= 0) {
//            showSnack(
//                false,
//                this,
//                parent_layout_signup,
//                getString(R.string.please_select_your_profile_image)
//            )
//            return false
//        }
        return true
    }

    fun imageSelection(image_view: ImageView, type: String) {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                val bottomSheetDialogFragment = KwikPicker.Builder(
                    applicationContext,
                    imageProvider = { image_view, uri ->
                        Glide.with(applicationContext)
                            .load(uri)
                            .into(image_view)
                    },
                    onImageSelectedListener = { uri: Uri ->
                        image_view.post {
                            if (uri.path != null && type.equals("profile")) {
                                Glide.with(applicationContext)
                                    .load(uri)
                                    .into(image_view)
//                                iv_cam.visibility = View.GONE
                                image_view.scaleType = ImageView.ScaleType.FIT_XY

                                img_url = uri.path!!
                            } else if (uri.path != null && type.equals("franchisee_proof")) {
                                Glide.with(applicationContext)
                                    .load(uri)
                                    .into(image_view)
                                image_view.scaleType = ImageView.ScaleType.FIT_XY

                                franchisee_proof_url = uri.path!!
                            } else {
                                Glide.with(applicationContext)
                                    .load(uri)
                                    .into(image_view)

                                image_view.scaleType = ImageView.ScaleType.FIT_XY
                                iv_approve.visibility = VISIBLE

                                proof_img_url = uri.path!!
                            }
                        }
                    },
                    peekHeight = 1200
                )
                    .create(applicationContext)
                bottomSheetDialogFragment.show(supportFragmentManager)
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(
                    applicationContext,
                    "Permission Denied\n" + deniedPermissions.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        TedPermission.with(this)
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
        setBrands()
    }

    fun setApi() {
        var data = HashMap<String, Any>()
        var fileMap = HashMap<String, File>()
        var fileMap1 = HashMap<String, File>()
        var fileMap2 = HashMap<String, File>()
        var headerMAp = HashMap<String, String>()
        data.put("name", ed_name.text.toString())
        data.put("email", ed_email.text.toString())
        data.put("password", ed_pass.text.toString())
        data.put("password_confirmation", ed_confirm_pass.getText().toString())
        data.put("phone", ed_phone.text.toString())
//        data.put("full_time", full_time)
//        data.put("opening_time", tv_open.text.toString() + ":00")
//        data.put("closing_time", tv_close.text.toString() + ":00")
        data.put("role", "4")


        if (img_url.isNullOrEmpty()) {
            data.put("image", File(img_url).absoluteFile)
        } else {
            fileMap.put("image", File(img_url).absoluteFile)
        }


        if (img_url.isNullOrEmpty()) {
            data.put("image", File(img_url).absoluteFile)
        } else {
            fileMap.put("image", File(img_url).absoluteFile)
        }

        if (proof_img_url.isNullOrEmpty()) {
            data.put("id_proof", File(proof_img_url).absoluteFile)
        } else {
            fileMap1.put("id_proof", File(proof_img_url).absoluteFile)
        }
        if (franchisee_proof_url.isNullOrEmpty()) {
            data.put("franchisee_proof", File(franchisee_proof_url).absoluteFile)
        } else {
            fileMap2.put("franchisee_proof", File(franchisee_proof_url).absoluteFile)
        }

        if (!brand_img_url.isNullOrEmpty()) {
            data.put("image_url", brand_img_url)
        } else {
            data.put("image_url", "")

        }

        var list = ArrayList<String>()
        list.add("2")
        getData(
            signup,
            headerMAp,
            ConnectionNetwork.convertRequestBodyfromMap(this, data),
            ConnectionNetwork.converRequestBodyFromMapImage(fileMap, list),
            ConnectionNetwork.converRequestBodyFromMapImage(fileMap1, list),
            ConnectionNetwork.converRequestBodyFromMapImage(fileMap2, list),
//            ConnectionNetwork.converRequestBodyFromMapImage(fileMap3, list),
            this,
            parent_layout_signup,
            signup_num
        )


    }

    companion object {
        val TAG = SignupFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(): SignupFragment = SignupFragment()
    }

    override fun onClick(item: Any, adapterPosition: Int) {
        if (item is GetBrandsModel.Data) {
            brand_img_url = item.image
            Glide.with(this).load(item.image).apply(RequestOptions.circleCropTransform())
                .into(iv_cam)
//            iv_cam.visibility= GONE
            ed_name.setText(item.name)
            cv_brands.visibility = GONE
        }
    }

    override fun onLongClick(item: Any) {
    }

    override fun onSwitchAvail(item: Any, status: Int) {

    }

    override fun onFeatured(item: Any, status: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun toOTP() {
        if (isValidPhone()!!) {

            var auth = FirebaseAuth.getInstance()
            if (auth.currentUser != null)
                auth.signOut()

            startActivityForResult(
                Intent(this, OtpActivity::class.java).putExtra(
                    "phone",
                    ed_phone?.text.toString()
                ), BroadcastConstants.OTP_CODE
            )

        }
    }
    fun isValidPhone(): Boolean {
        return if (ed_phone.text.isNullOrBlank()) {
            CommonUtils.showMessage(parentView, getString(R.string.invalid_phn) )
            false
        } else if (ed_phone.text.toString().length < 9) {
            CommonUtils.showMessage(parentView, getString(R.string.invalid_phn) )
            false
        } else
            true
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data!=null) {
            if (requestCode == BroadcastConstants.OTP_CODE) {
                if (data?.extras?.containsKey("verified")!!)
                    if (data?.getBooleanExtra("verified", false)!!)
//                    phoneLogin()
                        otp_check = "verified"

            }
        }else{
            CommonUtils.showMessage(parentView, getString(R.string.invalid_phn))
        }

    }

    fun phoneLogin() {
        CommonUtils.showLoader(this, "Verifying")
        if (isValidPhone()) {
            ApiRepo.getInstance()
                .phoneLogin(
                    ed_phone.toString().trim()
                ) { success, result ->
                    CommonUtils.hideLoader()
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