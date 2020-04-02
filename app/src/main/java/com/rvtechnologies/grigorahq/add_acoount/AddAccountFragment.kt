package com.rvtechnologies.grigorahq.add_acoount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.services.models.GetAccountdetailsModel
import com.rvtechnologies.grigorahq.services.models.SaveAccountModel
import com.rvtechnologies.grigorahq.utils.BroadcastConstants
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.get_aacount_details
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.save_account_details_num
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.PrefConstants
import kotlinx.android.synthetic.main.fragment_add_account.*

class AddAccountFragment : Fragment(), EventBroadcaster {
    override fun broadcast(code: Int, data: Any?) {
        if (code == get_aacount_details) {
            var pojo = Gson().fromJson(data.toString(), GetAccountdetailsModel::class.java)
            if (pojo.status) {
                if (data != null) {
                    ll_card.visibility = VISIBLE
                    account_name.text = pojo.data.name
                    ed_account_name.setText(pojo.data.name)
                    ed_account_number.setText(pojo.data.accountNumber)
                    ed_bank_code.setText(pojo.data.bankCode)
                    acoount_code.text =
                        "XXX" + pojo.data.bankCode.substring(pojo.data.bankCode.length - 1)
                    account_no.text =
                        "XXXXXXX" + pojo.data.accountNumber.substring(pojo.data.accountNumber.length - 3)
                    button.text = getString(R.string.update_bank_details)
                    CommonUtils.savePrefs(activity, PrefConstants.RECIEPT_NUMBER, pojo.data.receipt_id)
                    CommonUtils.savePrefs(activity, PrefConstants.ACCOUNT_NUMBER, pojo.data.accountNumber)

                }

            } else {
                ll_card.visibility = GONE
                ll_enter_details.visibility = GONE
                button.text = getString(R.string.add_account)

            }
        } else if (code == save_account_details_num) {
            var pojo = Gson().fromJson(data.toString(), SaveAccountModel::class.java)
            if (pojo.status) {
                ConnectionNetwork.showSnack(
                    false,
                    activity!!,
                    parent_bank,
                    pojo.message
                )
                ll_enter_details.visibility = GONE
                ll_card.visibility = VISIBLE
                account_name.text = pojo.data.name
                acoount_code.text =
                    "XXX" + pojo.data.bankCode.substring(pojo.data.bankCode.length - 1)
                account_no.text =
                    "XXXXXXX" + pojo.data.accountNumber.substring(pojo.data.accountNumber.length - 3)
                button.text = getString(R.string.update_bank_details)
                CommonUtils.savePrefs(activity, PrefConstants.RECIEPT_NUMBER, pojo.data.receipt_id)
                CommonUtils.savePrefs(activity, PrefConstants.ACCOUNT_NUMBER, pojo.data.accountNumber)


            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setApi()
        button.setOnClickListener {
            if (button.text.equals(R.string.update_bank_details)) {
                ll_enter_details.visibility = VISIBLE
                button.text = getString(R.string.update)

            } else if (button.text.equals(getString(R.string.add_account))) {
                ll_enter_details.visibility = VISIBLE
                button.text = getString(R.string.submit)

            } else if (button.text.equals(getString(R.string.submit))) {
                if (saveAccountvarify())
                    saveAccount()

            } else if (button.text.equals(getString(R.string.update_bank_details))) {
                ll_enter_details.visibility = VISIBLE
                button.text = getString(R.string.save)

            } else if (button.text.equals(getString(R.string.save))) {
                if (saveAccountvarify())
                    saveAccount()
            }
        }
    }

    private fun saveAccountvarify(): Boolean {
        if (ed_account_number.text.toString().isNullOrEmpty() || ed_account_name.text.toString().isNullOrEmpty() || ed_bank_code.text.toString().isNullOrEmpty()) {
            ConnectionNetwork.showSnack(
                false,
                activity!!,
                parent_bank,
                getString(R.string.required_fields)
            )
            return false
        }
        return true
    }

    private fun setApi() {
        var headerMAp = HashMap<String, String>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity!!, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")

        ConnectionNetwork.getData(
            NetworkConstants.get_account_details,
            headerMAp,
            activity!!,
            parent_bank,
            BroadcastConstants.get_aacount_details
        )
    }

    private fun saveAccount() {
        var headerMAp = HashMap<String, Any?>()
        var data = HashMap<String, Any?>()

        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity!!, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        data.put("account_number", ed_account_number.text.toString())
        data.put("name", ed_account_name.text.toString())
        data.put("bank_code", ed_bank_code.text.toString())

        ConnectionNetwork.postFormData(
            NetworkConstants.save_account_details,
            headerMAp,
            data,
            "",
            activity!!,
            parent_bank,
            BroadcastConstants.save_account_details_num
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
            AddAccountFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}
