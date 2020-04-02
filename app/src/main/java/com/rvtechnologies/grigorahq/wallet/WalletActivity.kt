package com.rvtechnologies.grigorahq.wallet

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.navigation.NavigationActivity
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.services.models.PayoutModel
import com.rvtechnologies.grigorahq.services.models.WalletModel
import com.rvtechnologies.grigorahq.utils.BroadcastConstants
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.payout_num
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.PrefConstants
import kotlinx.android.synthetic.main.dialog_redeem.view.*
import kotlinx.android.synthetic.main.fragment_wallet.*
import kotlinx.android.synthetic.main.wallet_fillter.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class WalletActivity : Fragment(), EventBroadcaster {
    val mList: ArrayList<WalletModel.Data.History> = ArrayList()
    val mListFillter: ArrayList<WalletModel.Data.History> = ArrayList()

    override fun broadcast(code: Int, data: Any?) {

        if (code == BroadcastConstants.wallet_history_num) {
            var pojo = Gson().fromJson(data.toString(), WalletModel::class.java)
            if (pojo.status) {
                tv_money.text = resources.getString(R.string.naira_sign) + pojo.data.wallet
                tv_points.text =
                    (pojo.data.wallet.toFloat() * pojo.data.nairaToPoints).toString() + resources.getString(
                        R.string.points
                    )
                tv_wallet_id.text = pojo.data.walletId
                if (mList.size > 0) {
                    mList.clear()

                }
                if (pojo.data.history.size > 0) {
                    tv_empty.visibility = GONE
                    recyclerViewEarnings.visibility = VISIBLE
                    mList.addAll(pojo.data.history)
                } else {
                    tv_empty.visibility = VISIBLE
                    recyclerViewEarnings.visibility = GONE
                }
                recyclerViewEarnings.adapter = WalletAdapter(mList)
            } else {
                ConnectionNetwork.showSnack(
                    false,
                    activity!!,
                    parent_layout,
                    getString(R.string.went_wrong)
                )
            }
        } else if (code == BroadcastConstants.payout_num) {
            var pojo = Gson().fromJson(data.toString(), PayoutModel::class.java)
            if (pojo.status) {
                ed_ammount.setText("")
                getWalletApi()
                ConnectionNetwork.showSnack(
                    false,
                    activity!!,
                    parent_layout,
                    pojo.message
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getWalletApi()

        add_account.setOnClickListener {
            if (CommonUtils.getPrefValue(activity, PrefConstants.RECIEPT_NUMBER).isEmpty()) {
                showSetTimeDialog(getString(R.string.please_add_account_details), false)

            } else if (ed_ammount.text.isEmpty()) {

                showSetTimeDialog(resources.getString(R.string.amount_is_required), false)

            } else if (ed_ammount.text.toString().equals("0")) {
                showSetTimeDialog(getString(R.string.please_add_amount_more_than_0), false)

            } else
                showSetTimeDialog(
                    resources.getString(R.string.naira_sign) + ed_ammount.text.toString() + " " + resources.getString(
                        R.string.will_be_credit_to_your_account
                    ), true
                )
        }
        recyclerViewEarnings.layoutManager = LinearLayoutManager(activity)
        iv_back.setOnClickListener {
            NavigationActivity.drawer_layout =
                (activity as Activity).findViewById(R.id.drawerlayout);
            if (NavigationActivity.drawer_layout!!.isDrawerVisible(GravityCompat.START)) {
                NavigationActivity.drawer_layout!!.closeDrawer(GravityCompat.START)
            } else {
                NavigationActivity.drawer_layout!!.openDrawer(GravityCompat.START)
            }
        }

        tv_filter.setOnClickListener {
            wallet_filter.visibility = VISIBLE
        }
        tv_all.setOnClickListener {
            tv_filter.setText(resources.getString(R.string.all))
            wallet_filter.visibility = GONE
            recyclerViewEarnings.visibility= VISIBLE
            tv_empty.visibility= GONE
            recyclerViewEarnings.adapter = WalletAdapter(mList)
        }
        tv_choose.setOnClickListener {
            wallet_filter.visibility = GONE
            setDateFillter()
        }

    }

    private fun setDateFillter() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val MONTHS = arrayOf(
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
        )


        val dpd = DatePickerDialog(
            activity!!,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                tv_filter.setText("" + dayOfMonth + " " + MONTHS[monthOfYear] + ", " + year)
                var day = ""
                var month = ""
                if (dayOfMonth.toString().length == 1) {
                    day = "0" + dayOfMonth.toString()
                }
                if (monthOfYear.toString().length == 1) {
                    month = "0" + (monthOfYear + 1).toString()
                } else {
                    month = (monthOfYear + 1).toString()
                }
                mListFillter.clear()
                var compareDate = "" + year + "-" + month + "-" + day
//                "2020-03-09 05:23:43"
                if (mList.size > 0) {
                    for (i in 0 until mList.size) {
                        if (compareDate.equals(
                                mList.get(i).createdAt.substring(0, 10),
                                ignoreCase = true
                            )
                        ) {
                            mListFillter.add(mList.get(i))
                        }

                    }
                    if(mListFillter.size>0){
                        recyclerViewEarnings.visibility= VISIBLE
                        tv_empty.visibility= GONE
                        recyclerViewEarnings.adapter = WalletAdapter(mListFillter)
                    }else{
                        recyclerViewEarnings.visibility= GONE
                        tv_empty.visibility= VISIBLE

                    }
                }

            },
            year,
            month,
            day
        )
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());

        dpd.show()
    }


    private fun showSetTimeDialog(msg: String, isWithdraw: Boolean) {
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_redeem, null)
        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mDialogView.tv_amount.text = msg


        mDialogView.add.setOnClickListener {
            if (isWithdraw) {
                payoutAmount(ed_ammount.text.toString())
                mAlertDialog.dismiss()
            } else {
                mAlertDialog.dismiss()
            }

        }
    }


    private fun payoutAmount(amount: String) {
        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        var data = HashMap<String, Any?>()
        data.put("amount", amount)
        data.put("receipt_id", CommonUtils.getPrefValue(activity, PrefConstants.RECIEPT_NUMBER))
        ConnectionNetwork.postFormData(
            NetworkConstants.payout,
            headerMAp,
            data,
            "",
            activity!!,
            parent_layout,
            payout_num
        )
    }

    private fun getWalletApi() {
        var headerMAp = HashMap<String, String>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")

        ConnectionNetwork.getData(
            NetworkConstants.wallet_history,
            headerMAp,
            activity!!,
            parent_layout,
            BroadcastConstants.wallet_history_num
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

    companion object {
        @JvmStatic
        fun newInstance() =
            WalletActivity().apply {
                arguments = Bundle().apply {}
            }
    }
}
