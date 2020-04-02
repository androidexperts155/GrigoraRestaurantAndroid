package com.rvtechnologies.grigorahq.navigation

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.QuizFragment
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.add_acoount.AddAccountFragment
import com.rvtechnologies.grigorahq.book_table.BookTableFragment
import com.rvtechnologies.grigorahq.categories.Categories
import com.rvtechnologies.grigorahq.custom_cuisine.AddCuisineFragment
import com.rvtechnologies.grigorahq.home.HomeMapFragment
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.ConnectionNetwork.Companion.showSnack
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.offers.OffersFragment
import com.rvtechnologies.grigorahq.orders.OrderFragment
import com.rvtechnologies.grigorahq.orders.PastOrderFragment
import com.rvtechnologies.grigorahq.profile.ProfileFragment
import com.rvtechnologies.grigorahq.services.models.LogoutModel
import com.rvtechnologies.grigorahq.services.models.OnlineStatusModel
import com.rvtechnologies.grigorahq.services.models.UpdateTokenModel
import com.rvtechnologies.grigorahq.settings.SettingsFragment
import com.rvtechnologies.grigorahq.ui.login_signup.fragments.LogInFragment
import com.rvtechnologies.grigorahq.utils.BroadcastConstants
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.logout_num
import com.rvtechnologies.grigorahq.utils.CommonMethods.getVersionNumber
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.CommonUtils.delPrefValue
import com.rvtechnologies.grigorahq.utils.CommonUtils.getPrefValue
import com.rvtechnologies.grigorahq.utils.PrefConstants
import com.rvtechnologies.grigorahq.wallet.WalletActivity
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*


class NavigationActivity : AppCompatActivity(), NavigationAdapter.ItemClickListener,
    EventBroadcaster {
    var drawer_layout: DrawerLayout? = null
    var toolbar: Toolbar? = null
    private var status_on: Int = 2

    override fun itemClick(position: Int) {
        closeOrOpenDrawer()
        Handler().postDelayed({
            when (position) {
                0 -> {
//                    toolbar.visibility = VISIBLE
//                    view_toolbar.visibility = GONE
//                    tv_title.setText(getString(R.string.home))
//                    replaceFragment(HomeMapFragment.newInstance())

                    toolbar!!.visibility = VISIBLE
                    iv_map.visibility = GONE
                    view_toolbar.visibility = GONE
                    iv_reset.visibility = GONE
                    iv_list.visibility = VISIBLE
                    tv_title.setText(getString(R.string.available_orders))
                    replaceFragment(HomeMapFragment.newInstance())
//                    startActivity(Intent(this, NavigationActivity::class.java))
                }
                1 -> {
//                    toolbar.visibility = VISIBLE
//                    view_toolbar.visibility = GONE
//                    tv_title.setText(getString(R.string.home))
//                    replaceFragment(HomeMapFragment.newInstance())

                    toolbar!!.visibility = VISIBLE
                    iv_map.visibility = GONE
                    view_toolbar.visibility = VISIBLE
                    iv_list.visibility = GONE
                    iv_reset.visibility = GONE
                    tv_title.setText(getString(R.string.schedule_orders))
                    replaceFragment(OrderFragment.newInstance("schdule"))
                }
                2 -> {
                    toolbar!!.visibility = VISIBLE
                    iv_list.visibility = GONE
                    iv_reset.visibility = GONE
                    view_toolbar.visibility = VISIBLE
                    tv_title.setText(getString(R.string.dishes))
                    replaceFragment(Categories.newInstance())
                }
                3 -> {
                    toolbar!!.visibility = VISIBLE
                    view_toolbar.visibility = VISIBLE
                    iv_list.visibility = GONE
                    iv_reset.visibility = GONE
                    tv_title.setText(getString(R.string.add_custom_cuisine))
                    replaceFragment(AddCuisineFragment.newInstance())
//                    startActivity(Intent(this, RejectReasonsActivity::class.java))
                }
                4 -> {
                    view_toolbar.visibility = VISIBLE
                    toolbar!!.visibility = VISIBLE
                    iv_list.visibility = GONE
                    iv_reset.visibility = GONE
                    tv_title.setText(getString(R.string.history))
                    replaceFragment(PastOrderFragment.newInstance())
                }
                5 -> {
                    toolbar!!.visibility = VISIBLE
                    view_toolbar.visibility = VISIBLE
                    iv_list.visibility = GONE
                    iv_reset.visibility = GONE
                    tv_title.setText(getString(R.string.table_book))
                    replaceFragment(BookTableFragment.newInstance())
                }
                6 -> {
                    toolbar!!.visibility = VISIBLE
                    view_toolbar.visibility = VISIBLE
                    iv_list.visibility = GONE
                    iv_reset.visibility = VISIBLE
                    tv_title.setText(getString(R.string.offer))
                    replaceFragment(OffersFragment.newInstance())
//                    tv_title.setText(getString(R.string.bank_details))
//                    replaceFragment(AddAccountFragment.newInstance())
                }
                7 -> {
                    toolbar!!.visibility = VISIBLE
                    view_toolbar.visibility = VISIBLE
                    iv_list.visibility = GONE
                    iv_reset.visibility = GONE
                    tv_title.setText(getString(R.string.settings))
                    replaceFragment(SettingsFragment.newInstance())
                }
                8 -> {
                    toolbar!!.visibility = GONE
                    view_toolbar.visibility = GONE
                    iv_list.visibility = GONE
                    iv_reset.visibility = GONE
                    tv_title.setText(getString(R.string.quiz))
                    replaceFragment(QuizFragment.newInstance())
                }
                9 -> {
                    toolbar!!.visibility = VISIBLE
                    view_toolbar.visibility = VISIBLE
                    iv_list.visibility = GONE
                    iv_reset.visibility = GONE
                    tv_title.setText(getString(R.string.add_account))
                    replaceFragment(AddAccountFragment.newInstance())
                }
                10 -> {
                    toolbar!!.visibility = GONE
                    view_toolbar.visibility = GONE
                    iv_list.visibility = GONE
                    iv_reset.visibility = GONE
                    tv_title.setText(getString(R.string.gora_pouch))
                    replaceFragment(WalletActivity.newInstance())
                }
                11 -> {
                    var alertDialog = AlertDialog.Builder(this).also {
                        it.setTitle(getString(R.string.logout))
                        it.setMessage(getString(R.string.are_you_sure_to_log_out))
                        it.setPositiveButton(
                            getString(R.string.yes),
                            object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    dialog!!.dismiss()
                                    setLogoutApi()

                                }
                            })
                        it.setNegativeButton(
                            getString(R.string.no),
                            object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    dialog!!.dismiss()
                                }
                            })
                    }
                    alertDialog.show()
                }
            }
        }, 200)


    }

    override fun broadcast(code: Int, data: Any?) {
        if (code == logout_num) {
            var pojo = Gson().fromJson(data.toString(), LogoutModel::class.java)
            if (pojo.status == 1) {
                Log.e("check", "logout")
                showSnack(false, this, drawerlayout, pojo.message)
                startActivity(Intent(this, LogInFragment::class.java))
                delPrefValue(this)
                finishAffinity()
            }

        } else if (code == BroadcastConstants.update_user_token_num) {
            var pojo = Gson().fromJson(data.toString(), UpdateTokenModel::class.java)
            if (pojo.status) {
                Log.e("token update", pojo.message)
            } else {
                Log.e("token update", "error")

            }
        } else if (code == BroadcastConstants.available_status_num) {
            var pojo = Gson().fromJson(data.toString(), OnlineStatusModel::class.java)
            if (pojo.status) {
                Log.e("check", "status")

                if (pojo.message.equals("offline", ignoreCase = true)) {
                    tv_avail.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_offline
                        ), null, null, null
                    )
                    CommonUtils.savePrefs(this, PrefConstants.BUSY_STATUS, "1")


                } else if (pojo.message.equals("online", ignoreCase = true)) {

                    tv_avail.setCompoundDrawablesWithIntrinsicBounds(
                        ContextCompat.getDrawable(
                            this,
                            R.drawable.ic_dot
                        ), null, null, null
                    )
                    CommonUtils.savePrefs(this, PrefConstants.BUSY_STATUS, "0")


                }
                showSnack(false, this, drawerlayout, pojo.message)


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
        setContentView(R.layout.activity_home)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawer_layout = findViewById(R.id.drawerlayout)
        tv_version.setText(getString(R.string.version_name) + getVersionNumber(this))
        toolbar!!.visibility = VISIBLE
        iv_map.visibility = GONE
        iv_list.visibility = VISIBLE
        tv_title.setText(getString(R.string.available_orders))
        replaceFragment(HomeMapFragment.newInstance())
        recyclerNavigation.isNestedScrollingEnabled = true

        if (intent != null) {
            if (intent.getStringExtra("type") != null) {
                if (intent.getStringExtra("type")!!.equals("new_order")) {
                    toolbar!!.visibility = VISIBLE
                    iv_map.visibility = GONE
                    view_toolbar.visibility = GONE
                    iv_list.visibility = VISIBLE
                    tv_title.setText(getString(R.string.available_orders))
                    replaceFragment(OrderFragment.newInstance("new"))
                } else if (intent.getStringExtra("type")!!.equals("Scheduled_order")) {
                    toolbar!!.visibility = VISIBLE
                    iv_map.visibility = GONE
                    view_toolbar.visibility = VISIBLE
                    iv_list.visibility = GONE
                    tv_title.setText(getString(R.string.schedule_orders))
                    replaceFragment(OrderFragment.newInstance("schdule"))
                } else if (intent.getStringExtra("type")!!.equals("past_order")) {
                    view_toolbar.visibility = VISIBLE
                    toolbar!!.visibility = VISIBLE
                    iv_list.visibility = GONE
                    tv_title.setText(getString(R.string.history))
                    replaceFragment(PastOrderFragment.newInstance())
                }else if (intent.getStringExtra("type")!!.equals("book_table")) {
                    toolbar!!.visibility = VISIBLE
                    view_toolbar.visibility = VISIBLE
                    iv_list.visibility = GONE
                    iv_reset.visibility = GONE
                    tv_title.setText(getString(R.string.table_book))
                    replaceFragment(BookTableFragment.newInstance())
                }
            }
        }

        setUpadpter()
        menuClick()
        setUpData()
        getToken()
    }


    fun setUpData() {
        Glide.with(this).load(getPrefValue(this, PrefConstants.IMAGE)).into(circularImageView)
        textViewName.setText(getPrefValue(this, PrefConstants.NAME))

        if (getPrefValue(this, PrefConstants.BUSY_STATUS).equals("0")) {
            on_off.isChecked = true
            Log.e("check", "status TRUE")

            tv_avail.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_dot
                ), null, null, null
            )

        } else if (getPrefValue(this, PrefConstants.BUSY_STATUS).equals("1")) {
            on_off.isChecked = false
            Log.e("check", "status FALSE")

            tv_avail.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_offline
                ), null, null, null
            )

        }

        if (getPrefValue(this, PrefConstants.RATING).isNullOrEmpty()) {
            rate_nav.rating = 0f
            tv_rate.text = "0.0"
        } else {
            if (getPrefValue(this, PrefConstants.RATING).equals("0")) {
                tv_rate.text = "0.0"
            } else
                tv_rate.text = getPrefValue(this, PrefConstants.RATING)
            rate_nav.rating = getPrefValue(this, PrefConstants.RATING).toFloat()
        }
        balance_nav.setText(
            getString(R.string.naira_sign) + getPrefValue(this, PrefConstants.WALLET)
        )
    }


    fun menuClick() {
        iv_menu.setOnClickListener {
            closeOrOpenDrawer()
        }

        iv_list.setOnClickListener {
            replaceFragment(OrderFragment.newInstance("new"))
        }
        iv_reset.setOnClickListener {
            removePromo()
        }

        rl_profile.setOnClickListener {
            closeOrOpenDrawer()
            toolbar!!.visibility = GONE
            tv_title.setText(getString(R.string.profile))
            replaceFragment(ProfileFragment.newInstance())
        }
        on_off.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                status_on = 0
                Log.e("check", "status 0")
            } else {
                status_on = 1
                Log.e("check", "status 1")
            }
            setOnOffSatus(status_on)
        }
    }

    private fun removePromo() {
        var data = HashMap<String, Any?>()

        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(this, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        data.put("promo_id", "0")

        ConnectionNetwork.postFormData(
            NetworkConstants.remove_promo_to_restaurant,
            headerMAp,
            data,
            "",
            this,
            drawerlayout,
            BroadcastConstants.remove_promo_to_restaurant_num
        )
    }

    fun setUpadpter() {
        var navigationsItems = resources.getStringArray(R.array.navigation_items)
        var icons = getResources().obtainTypedArray(R.array.navigation_icons)
        var adapter = NavigationAdapter(this, navigationsItems, icons)
        adapter.itemClickHandler(this)
        recyclerNavigation.layoutManager = LinearLayoutManager(this)
        recyclerNavigation.adapter = adapter
    }


    private fun setLogoutApi() {
        var headerMAp = HashMap<String, String>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(this, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        ConnectionNetwork.logout(NetworkConstants.logout, headerMAp, this, drawerlayout, logout_num)
    }

    private fun setOnOffSatus(statusOn: Int) {
        var data = HashMap<String, Any?>()
        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(this, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        data.put("status", statusOn)
        ConnectionNetwork.postFormData(
            NetworkConstants.available_status,
            headerMAp,
            data,
            "",
            this,
            drawerlayout,
            BroadcastConstants.available_status_num
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

    fun replaceFragment(fragment: Fragment) {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit()
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        FirebaseApp.initializeApp(this);

        FirebaseInstanceId.getInstance()
            .instanceId.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("exc:", "getInstanceId failed", task.exception)
                return@OnCompleteListener
            }
            task.result!!.token
            Log.e("Device Token : ", task.result!!.token)
            setTokenApi(task.result!!.token)
        })
    }

    private fun setTokenApi(token: String) {
        Log.e("checkToken", token)
        var data = HashMap<String, Any?>()

        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(this, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        data.put("device_token", token)
        data.put("device_type", "0")
        data.put("device_id", device_id())

        ConnectionNetwork.postFormData(
            NetworkConstants.update_user_token,
            headerMAp,
            data,
            "",
            this,
            drawerlayout,
            BroadcastConstants.update_user_token_num
        )
    }

    fun device_id(): String {

        Log.e(
            "device_id", Settings.Secure.getString(
                this.getContentResolver(),
                Settings.Secure.ANDROID_ID
            )
        )
        return Settings.Secure.getString(
            this.getContentResolver(),
            Settings.Secure.ANDROID_ID
        );

    }

    fun closeOrOpenDrawer() {
        if (drawer_layout!!.isDrawerVisible(GravityCompat.START)) {
            drawer_layout!!.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout!!.openDrawer(GravityCompat.START)
        }
    }

    companion object {
        var drawer_layout: DrawerLayout? = null
        var toolbar: Toolbar? = null


    }


}



