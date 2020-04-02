package com.rvtechnologies.grigorahq.book_table

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.rvtechnologies.grigora.utils.IRecyclerItemClick
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.ConnectionNetwork.Companion.showSnack
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.network.NetworkConstants.Companion.new_bookings
import com.rvtechnologies.grigorahq.services.models.AvailableBookingModel
import com.rvtechnologies.grigorahq.services.models.TableBookingConfirmCancelModel
import com.rvtechnologies.grigorahq.services.models.TableBookingModel
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.accept_booking_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.booking_available_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.change_restaurant_booking_status_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.new_bookings_num
import com.rvtechnologies.grigorahq.utils.BroadcastConstants.Companion.noti_book_table
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.PrefConstants
import kotlinx.android.synthetic.main.fragment_book_table.*


class BookTableFragment : Fragment(), EventBroadcaster, IRecyclerItemClick {

    var list = ArrayList<AvailableBookingModel.Data>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        avail_switch.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                ll_add_guest.visibility = VISIBLE
                setBookingTable(1)
            } else {
                ll_add_guest.visibility = GONE
                setBookingTable(0)

            }
        }
        btn_submit.setOnClickListener {

            if (ed_no_guest.text.isNullOrEmpty()) {
                showSnack(false, activity!!, rl_parent, getString(R.string.error_guest_capacity))
            } else {
                saveCapacity()
            }
        }
        rv_booking.layoutManager = LinearLayoutManager(activity)
        rv_booking.isNestedScrollingEnabled = false
        getBookings()
    }

    private fun setBookingTable(i: Int) {
        var data = HashMap<String, Any?>()
        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        data.put("status", i)

        ConnectionNetwork.postFormData(
            NetworkConstants.change_restaurant_booking_status,
            headerMAp,
            data,
            "",
            activity!!,
            rl_parent,
            change_restaurant_booking_status_num
        )
    }

    private fun getBookings() {
        var headerMAp = HashMap<String, String>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")

        ConnectionNetwork.getData(
            new_bookings,
            headerMAp,
            activity!!,
            rl_parent,
            new_bookings_num
        )
    }

    private fun saveCapacity() {
        var data = HashMap<String, Any?>()
        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        data.put("no_of_seats", ed_no_guest.text.toString())

        ConnectionNetwork.postFormData(
            NetworkConstants.booking_available,
            headerMAp,
            data,
            "",
            activity!!,
            rl_parent,
            booking_available_num
        )
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            BookTableFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


    override fun broadcast(code: Int, data: Any?) {
        if (code == booking_available_num) {
            var pojo = Gson().fromJson(data.toString(), TableBookingModel::class.java)
            if (pojo.status) {
                showSnack(false, activity!!, rl_parent, getString(R.string.successfull))
            }
        } else if (code == new_bookings_num) {
            var pojo = Gson().fromJson(data.toString(), AvailableBookingModel::class.java)
            if (pojo.status) {
                if (pojo.tableBooking=="1") {
                    ll_add_guest.visibility = VISIBLE
                    avail_switch.isChecked = true
                    ed_no_guest.setText(pojo.noOfSeats.toString())
                }
                list.clear()
                if (pojo.data.size > 0) {
                    list.addAll(pojo.data)
                    rv_booking.adapter = BookingTableAdapter(list, this)
                }
            }

        }
        else if (code == accept_booking_num) {
            var pojo = Gson().fromJson(data.toString(), TableBookingConfirmCancelModel::class.java)
            if (pojo.status) {
                if (pojo.message.equals("Booking rejected By Restaurant.")) {
                    ConnectionNetwork.showCommonDialog(
                        activity!!,
                        "Booking Canceled",
                        "Table Booking",
                        "Done"
                    )
                    getBookings()

                } else if (pojo.message.equals("Booking Accepted By Restaurant.")) {
                    ConnectionNetwork.showCommonDialog(
                        activity!!,
                        "Booking Accepted",
                        "Table Booking",
                        "Done"
                    )

                    getBookings()

                }
            }
        }

        else if(code== noti_book_table){
            getBookings()
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

    override fun onItemClick(item: Any, s: String, button5: ImageView) {

    }

    override fun onClick(item: Any, type: String, position: Int, tv: TextView) {
        if (item is AvailableBookingModel.Data) {
            if (type.equals("cancel", ignoreCase = true)) {
                setBookingCancelConfirm("2", item.id)
            } else if (type.equals("confirm", ignoreCase = true)) {
                setBookingCancelConfirm("1", item.id)

            }

        }
    }


    private fun setBookingCancelConfirm(type: String, id: Int) {
        var data = HashMap<String, Any?>()
        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        data.put("booking_id", id)
        data.put("status", type)

        ConnectionNetwork.postFormData(
            NetworkConstants.accept_booking,
            headerMAp,
            data,
            "",
            activity!!,
            rl_parent,
            accept_booking_num
        )
    }
}
