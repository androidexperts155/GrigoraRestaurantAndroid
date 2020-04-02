package com.rvtechnologies.grigorahq.orders

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.navigation.ViewPagerAdapter
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.services.models.UpdateTokenModel
import com.rvtechnologies.grigorahq.utils.BroadcastConstants
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.PrefConstants
import kotlinx.android.synthetic.main.content_home.*


class AllOrdersFragment : Fragment(), EventBroadcaster {
    override fun broadcast(code: Int, data: Any?) {
        if (code == BroadcastConstants.update_user_token_num) {
            var pojo = Gson().fromJson(data.toString(), UpdateTokenModel::class.java)
            if (pojo.status) {
                Log.e("token update", pojo.message)
            } else {
                Log.e("token update", "error")

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getToken()
        setAdapter();
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.content_home, container, false)
    }

    fun setAdapter() {
        rg_tabs.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            if (i == R.id.rb_new) {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    rb_new.setBackgroundDrawable(resources.getDrawable(R.drawable.new_tab))
                    rb_ongoing.setBackgroundColor(resources.getColor(R.color.orders_bg))
                    rb_past.setBackgroundDrawable(resources.getDrawable(R.drawable.past_tab_unselect))
                    rb_ongoing.setTextColor(resources.getColor(R.color.dark_gray))
                    rb_past.setTextColor(resources.getColor(R.color.dark_gray))
                    rb_new.setTextColor(resources.getColor(R.color.white))

                } else {
                    rb_new.background = ContextCompat.getDrawable(activity!!, R.drawable.new_tab);
                    rb_ongoing.setBackgroundColor(
                        ContextCompat.getColor(
                            activity!!,
                            R.color.orders_bg
                        )
                    )
                    rb_past.setBackground(
                        ContextCompat.getDrawable(
                            activity!!,
                            R.drawable.past_tab_unselect
                        )
                    )
                    rb_ongoing.setTextColor(ContextCompat.getColor(activity!!, R.color.dark_gray))
                    rb_past.setTextColor(ContextCompat.getColor(activity!!, R.color.dark_gray))
                    rb_new.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
                }
                viewPager!!.currentItem = 0
            } else if (i == R.id.rb_past) {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    rb_new.setBackgroundDrawable(resources.getDrawable(R.drawable.new_tab_unselect))
                    rb_ongoing.setBackgroundColor(resources.getColor(R.color.orders_bg))
                    rb_past.setBackgroundDrawable(resources.getDrawable(R.drawable.past_tab))
                    rb_ongoing.setTextColor(resources.getColor(R.color.dark_gray))
                    rb_past.setTextColor(resources.getColor(R.color.white))
                    rb_new.setTextColor(resources.getColor(R.color.dark_gray))


                } else {
                    rb_new.background =
                        ContextCompat.getDrawable(activity!!, R.drawable.new_tab_unselect);
                    rb_ongoing.setBackgroundColor(
                        ContextCompat.getColor(
                            activity!!,
                            R.color.orders_bg
                        )
                    )
                    rb_past.setBackground(
                        ContextCompat.getDrawable(
                            activity!!,
                            R.drawable.past_tab
                        )
                    )

                    rb_ongoing.setTextColor(ContextCompat.getColor(activity!!, R.color.dark_gray))
                    rb_past.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
                    rb_new.setTextColor(ContextCompat.getColor(activity!!, R.color.dark_gray))
                }
                viewPager!!.currentItem = 2
            } else if (i == R.id.rb_ongoing) {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    rb_new.setBackgroundDrawable(resources.getDrawable(R.drawable.new_tab_unselect))
                    rb_ongoing.setBackgroundColor(resources.getColor(R.color.orders_bg))
                    rb_past.setBackgroundDrawable(resources.getDrawable(R.drawable.past_tab_unselect))

                    rb_ongoing.setTextColor(resources.getColor(R.color.white))
                    rb_past.setTextColor(resources.getColor(R.color.dark_gray))
                    rb_new.setTextColor(resources.getColor(R.color.dark_gray))


                } else {
                    rb_new.background =
                        ContextCompat.getDrawable(activity!!, R.drawable.new_tab_unselect);
                    rb_ongoing.setBackgroundColor(
                        ContextCompat.getColor(
                            activity!!,
                            R.color.colorAccent
                        )
                    )
                    rb_past.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                            activity!!,
                            R.drawable.past_tab_unselect
                        )
                    )

                    rb_past.setTextColor(ContextCompat.getColor(activity!!, R.color.dark_gray))
                    rb_ongoing.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
                    rb_new.setTextColor(ContextCompat.getColor(activity!!, R.color.dark_gray))
                }
                viewPager!!.currentItem = 1
            }


        })

//        tabs!!.addTab(tabs!!.newTab().setText(R.string.tab_text_1))
//        tabs!!.addTab(tabs!!.newTab().setText(R.string.tab_text_2))
//        tabs!!.addTab(tabs!!.newTab().setText(R.string.tab_text_3))

        val adapter = ViewPagerAdapter(activity!!, fragmentManager!!, 3)
        viewPager.offscreenPageLimit = 0
        viewPager!!.adapter = adapter

        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e("vp_index", position.toString())

                if (position == 0) {
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        rb_new.setBackgroundDrawable(resources.getDrawable(R.drawable.new_tab))
                        rb_ongoing.setBackgroundColor(resources.getColor(R.color.orders_bg))
                        rb_past.setBackgroundDrawable(resources.getDrawable(R.drawable.past_tab_unselect))
                        rb_ongoing.setTextColor(resources.getColor(R.color.dark_gray))
                        rb_past.setTextColor(resources.getColor(R.color.dark_gray))
                        rb_new.setTextColor(resources.getColor(R.color.white))

                    } else {
                        rb_new.background =
                            ContextCompat.getDrawable(activity!!, R.drawable.new_tab);
                        rb_ongoing.setBackgroundColor(
                            ContextCompat.getColor(
                                activity!!,
                                R.color.orders_bg
                            )
                        )
                        rb_past.setBackground(
                            ContextCompat.getDrawable(
                                activity!!,
                                R.drawable.past_tab_unselect
                            )
                        )
                        rb_ongoing.setTextColor(
                            ContextCompat.getColor(
                                activity!!,
                                R.color.dark_gray
                            )
                        )
                        rb_past.setTextColor(ContextCompat.getColor(activity!!, R.color.dark_gray))
                        rb_new.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
                    }
                } else if (position == 2) {
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        rb_new.setBackgroundDrawable(resources.getDrawable(R.drawable.new_tab_unselect))
                        rb_ongoing.setBackgroundColor(resources.getColor(R.color.orders_bg))
                        rb_past.setBackgroundDrawable(resources.getDrawable(R.drawable.past_tab))
                        rb_ongoing.setTextColor(resources.getColor(R.color.dark_gray))
                        rb_past.setTextColor(resources.getColor(R.color.white))
                        rb_new.setTextColor(resources.getColor(R.color.dark_gray))


                    } else {
                        rb_new.background =
                            ContextCompat.getDrawable(activity!!, R.drawable.new_tab_unselect);
                        rb_ongoing.setBackgroundColor(
                            ContextCompat.getColor(
                                activity!!,
                                R.color.orders_bg
                            )
                        )
                        rb_past.setBackground(
                            ContextCompat.getDrawable(
                                activity!!,
                                R.drawable.past_tab
                            )
                        )

                        rb_ongoing.setTextColor(
                            ContextCompat.getColor(
                                activity!!,
                                R.color.dark_gray
                            )
                        )
                        rb_past.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
                        rb_new.setTextColor(ContextCompat.getColor(activity!!, R.color.dark_gray))
                    }
                } else if (position == 1) {
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        rb_new.setBackgroundDrawable(resources.getDrawable(R.drawable.new_tab_unselect))
                        rb_ongoing.setBackgroundColor(resources.getColor(R.color.orders_bg))
                        rb_past.setBackgroundDrawable(resources.getDrawable(R.drawable.past_tab_unselect))

                        rb_ongoing.setTextColor(resources.getColor(R.color.white))
                        rb_past.setTextColor(resources.getColor(R.color.dark_gray))
                        rb_new.setTextColor(resources.getColor(R.color.dark_gray))


                    } else {
                        rb_new.background =
                            ContextCompat.getDrawable(activity!!, R.drawable.new_tab_unselect);
                        rb_ongoing.setBackgroundColor(
                            ContextCompat.getColor(
                                activity!!,
                                R.color.colorAccent
                            )
                        )
                        rb_past.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                activity!!,
                                R.drawable.past_tab_unselect
                            )
                        )

                        rb_past.setTextColor(ContextCompat.getColor(activity!!, R.color.dark_gray))
                        rb_ongoing.setTextColor(ContextCompat.getColor(activity!!, R.color.white))
                        rb_new.setTextColor(ContextCompat.getColor(activity!!, R.color.dark_gray))
                    }
                }

            }

        })


//        for (i in 0 until tabs.tabCount) {
//            val tab = (tabs.getChildAt(0) as ViewGroup).getChildAt(i)
//            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
//            p.setMargins(0, 0, 20, 0)
//            tab.requestLayout()
//        }

//        tabs!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                viewPager!!.currentItem = tab.position
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {
//
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab) {
//
//            }
//        })

    }

    companion object {
        @JvmStatic
        fun newInstance(): AllOrdersFragment {
            return AllOrdersFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        FirebaseApp.initializeApp(activity!!);

        FirebaseInstanceId.getInstance()
            .instanceId.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("exc:", "getInstanceId failed", task.exception)
                return@OnCompleteListener
            }
            task.result!!.token

            setTokenApi(task.result!!.token)
        })
    }

    private fun setTokenApi(token: String) {
        Log.e("checkToken", token)
        var data = HashMap<String, Any?>()

        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        data.put("device_token", token)
        data.put("device_type", "0")
        data.put("device_id", device_id())

        ConnectionNetwork.postFormData(
            NetworkConstants.update_user_token,
            headerMAp,
            data,
            "",
            activity!!,
            parent_layout,
            BroadcastConstants.update_user_token_num
        )
    }

    fun device_id(): String {

        Log.e(
            "device_id", Settings.Secure.getString(
                activity!!.getContentResolver(),
                Settings.Secure.ANDROID_ID
            )
        )
        return Settings.Secure.getString(
            activity!!.getContentResolver(),
            Settings.Secure.ANDROID_ID
        );

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
