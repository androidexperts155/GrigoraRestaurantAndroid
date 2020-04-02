package com.rvtechnologies.grigorahq.offers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.interfaces.IAdminOfferSelection
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.offers.adapter.AdminOfferAdapter
import com.rvtechnologies.grigorahq.services.models.AdminOfferPojo
import com.rvtechnologies.grigorahq.services.models.AdminOfferSelectionModel
import com.rvtechnologies.grigorahq.services.models.ResetPromoModel
import com.rvtechnologies.grigorahq.services.models.RestaurantOfferModel
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.assign_promo_to_restaurant_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.promocode_list_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.remove_promo_to_restaurant_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.restaurant_offer_num
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.PrefConstants
import kotlinx.android.synthetic.main.activity_add_offers.*
import kotlinx.android.synthetic.main.dialog_offer.view.*

class OffersFragment : Fragment(), EventBroadcaster, IAdminOfferSelection {
    var offerId: Int = 0
    override fun onItemClick(item: Any) {
        if (item is AdminOfferPojo.Data) {
            offerId = item.id
            setSelectionOfferApi(offerId)
        }
    }

    private fun setSelectionOfferApi(offerId: Int) {
        var data = HashMap<String, Any?>()
        var headerMAp = HashMap<String, Any?>()

        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        data.put("promo_id", offerId)

        ConnectionNetwork.postFormData(
            NetworkConstants.assign_promo_to_restaurant,
            headerMAp,
            data,
            "",
            activity!!,
            parentlayout,
            assign_promo_to_restaurant_num
        )
    }

    lateinit var list: ArrayList<AdminOfferPojo.Data>

    override fun broadcast(code: Int, data: Any?) {
        if (code == promocode_list_num) {
            var pojo = Gson().fromJson(data.toString(), AdminOfferPojo::class.java)
            if (pojo.status) {
                if (list.size > 0) {
                    list.clear()
                }
                if(pojo.data.size>0){
                    list.addAll(pojo.data)
                    rv_offer.adapter = AdminOfferAdapter(list, this)
                }
            }

        } else if (code == assign_promo_to_restaurant_num) {
            var pojo = Gson().fromJson(data.toString(), AdminOfferSelectionModel::class.java)
            if (pojo.status) {
                ConnectionNetwork.showSnack(
                    false,
                    activity!!,
                    parentlayout,
                    getString(R.string.promo_apply)
                )
            }
            setApi()
        } else if (code == restaurant_offer_num) {
            var pojo = Gson().fromJson(data.toString(), RestaurantOfferModel::class.java)
            if (pojo.status) {
                ConnectionNetwork.showSnack(
                    false,
                    activity!!,
                    parentlayout,
                    getString(R.string.offer_applied)
                )
                CommonUtils.savePrefs(activity!!, PrefConstants.OFFER, pojo.offer)
//                editText2.setText(pojo.offer)
            }
        }
        else if(code == remove_promo_to_restaurant_num){
            var pojo = Gson().fromJson(data.toString(),ResetPromoModel::class.java)
            if(pojo.status){
                setApi()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_add_offers, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView6.setOnClickListener {
            //            startActivity(Intent(activity!!, CreateOfferActivity::class.java))
            showOfferDialog()

        }
        list = ArrayList()
        rv_offer.layoutManager = LinearLayoutManager(activity)
        setApi()
    }

    private fun showOfferDialog() {
        val mDialogView = LayoutInflater.from(activity!!).inflate(R.layout.dialog_offer, null)
        val mBuilder = AlertDialog.Builder(activity!!)
            .setView(mDialogView)


        if (!CommonUtils.getPrefValue(activity, PrefConstants.OFFER).isNullOrEmpty()) {
            mDialogView.ed_offer_price.setText(
                CommonUtils.getPrefValue(
                    activity,
                    PrefConstants.OFFER
                )
            )
        }


        val mAlertDialog = mBuilder.show()

        mDialogView.tv_save_offer.setOnClickListener {
            if (mDialogView.ed_offer_price.text.isNullOrEmpty()) {
                ConnectionNetwork.showSnack(
                    false,
                    activity!!,
                    parentlayout,
                    getString(R.string.please_enter_offer)
                )
            } else {
                mAlertDialog.dismiss()
                setofferApi(mDialogView.ed_offer_price.text.toString())
            }
        }
    }


    fun setofferApi(offer: String) {
        var data = HashMap<String, Any?>()
        var headerMAp = HashMap<String, Any?>()

        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        data.put("offer", offer)

        ConnectionNetwork.postFormData(
            NetworkConstants.restaurant_offer,
            headerMAp,
            data,
            "",
            activity!!,
            parentlayout,
            restaurant_offer_num
        )
    }


    private fun setApi() {
        var headerMAp = HashMap<String, String>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity!!, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")

        ConnectionNetwork.getData(
            NetworkConstants.promocode_list + CommonUtils.getPrefValue(
                activity,
                PrefConstants.ID
            ),
            headerMAp,
            activity!!,
            parentlayout,
            promocode_list_num
        )
    }

    override fun onResume() {
        super.onResume()
        MyApplication.instance!!.registerListener(this)
    }

    override fun onPause() {
        super.onPause()
        MyApplication.instance!!.deRegisterListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(): OffersFragment {
            return OffersFragment().apply {
                arguments = Bundle().apply {
                }
            }
        }
    }

}
