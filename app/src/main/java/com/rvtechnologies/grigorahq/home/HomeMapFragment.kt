package com.rvtechnologies.grigorahq.home

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.rvtechnologies.grigorahq.MyApplication
import com.rvtechnologies.grigorahq.R
import com.rvtechnologies.grigorahq.navigation.NavigationActivity
import com.rvtechnologies.grigorahq.network.ConnectionNetwork
import com.rvtechnologies.grigorahq.network.EventBroadcaster
import com.rvtechnologies.grigorahq.network.LocationDetail
import com.rvtechnologies.grigorahq.network.NetworkConstants
import com.rvtechnologies.grigorahq.reasons.RejectReasonsActivity
import com.rvtechnologies.grigorahq.services.models.HomeModel
import com.rvtechnologies.grigorahq.services.models.NearDriversModel
import com.rvtechnologies.grigorahq.services.models.OrderAccepModel
import com.rvtechnologies.grigorahq.utils.ApiConstants
import com.rvtechnologies.grigorahq.utils.BroadcastConstants
import com.rvtechnologies.grigorahq.utils.CommonUtils
import com.rvtechnologies.grigorahq.utils.CommonUtils.change24To12
import com.rvtechnologies.grigorahq.utils.CommonUtils.changeUTC_to_local
import com.rvtechnologies.grigorahq.utils.PrefConstants
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.custom_order_menu.*
import kotlinx.android.synthetic.main.custom_order_menu.view.*
import kotlinx.android.synthetic.main.dialog_set_prepare_time.view.*
import kotlinx.android.synthetic.main.dialog_simple_msg.*
import kotlinx.android.synthetic.main.fragment_home_map.*
import java.text.DecimalFormat


class HomeMapFragment : Fragment(), EventBroadcaster, OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var alertDialog: AlertDialog
    lateinit var markerPojo: HomeModel
    var restLat=""
    var restLng=""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_map, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.frg_map) as SupportMapFragment? //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment!!.getMapAsync(this)
        NavigationActivity.toolbar = (activity as Activity).findViewById(R.id.toolbar);
        NavigationActivity.toolbar!!.visibility = VISIBLE
        connectSocket()
        sendLocation()
        setOrdersMarkers()
    }

    fun setOrdersMarkers() {
        var headerMAp = HashMap<String, String>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")

        ConnectionNetwork.getData(
            NetworkConstants.current_orders_location,
            headerMAp,
            activity!!,
            home_parent,
            BroadcastConstants.current_orders_location_num
        )
    }


    protected fun createMarker(
        pojo: HomeModel,
        pos: Int,
        iconResID: Int
    ): Marker? {
        val redMarker: Marker = mMap.addMarker(
            MarkerOptions()
                .position(LatLng(pojo.data.get(pos).endLat, pojo.data.get(pos).endLong))
                .anchor(1f, 1f)
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(iconResID))
        )
        pojo.data.get(pos).marker_id = redMarker.id
        pojo.data.get(pos).markerType = "redMarker"



        return redMarker
    }

    protected fun createGreenMarker(
        pojo: HomeModel,
        pos: Int,
        iconResID: Int
    ): Marker? {
        val greenMarker: Marker = mMap.addMarker(
            MarkerOptions()
                .position(LatLng(pojo.data.get(pos).endLat, pojo.data.get(pos).endLong))
                .anchor(1f, 1f)
                .title(getString(R.string.order_accepted))
                .snippet(getString(R.string.order_id) + pojo.data.get(pos).id)
                .icon(BitmapDescriptorFactory.fromResource(iconResID))
        )
        pojo.data.get(pos).markerType = "greenMarker"

        return greenMarker
    }

    protected fun currentLocation(
        markerPojo: HomeModel,
        latitude: Double,
        longitude: Double,
        title: String?,
        iconResID: Int
    ): Marker? {
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(latitude, longitude),
                12f
            )
        )
        mMap.animateCamera(CameraUpdateFactory.zoomIn())
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12f), 1000, null)


        val blackMarker: Marker = mMap.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet("")
                .icon(BitmapDescriptorFactory.fromResource(iconResID))
        )
        markerPojo.markerType = "blackMarker"
        return blackMarker
    }

    private fun showOrderDialog(
        order_id: String,
        name: String,
        img: String,
        address: String,
        date: String,
        deliveryNote: String?,
        list: ArrayList<HomeModel.Data.OrderDetail>
    ) {
        val mDialogView =
            LayoutInflater.from(activity).inflate(R.layout.custom_order_menu, null)
        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()
        alertDialog = mAlertDialog
        mAlertDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (deliveryNote.isNullOrEmpty()) {
            mAlertDialog.textView32.visibility = GONE
            mAlertDialog.textView8.visibility = GONE
            mAlertDialog.view.visibility = GONE
        } else {
            mAlertDialog.textView8.setText(deliveryNote)

        }
        mAlertDialog.rvOrderDetails.adapter = HomeDialogAdapter(list)
        mDialogView.textView5.setText(name)
        mDialogView.tv_order_id.setText(activity!!.resources.getString(R.string.order_id) + order_id)
        mDialogView.textViewAddress.setText(address)
//        mDialogView.cus_call.setOnClickListener {
//            setCallingToCustomer(phn)
//        }
        mDialogView.ll_accept.setOnClickListener {
            showSetTimeDialog(order_id,mAlertDialog)
        }
        mDialogView.ll_reject.setOnClickListener {
            //            setAcceptApi(order_id, "2", "")
            mAlertDialog.dismiss()
            startActivity(
                Intent(activity, RejectReasonsActivity::class.java).putExtra(
                    "orderId",
                    order_id
                )
            )
        }
//        mDialogView.cus_email.setOnClickListener {
////            setMail(email)
////        }change24To12
        val orderTime= change24To12(changeUTC_to_local( date))
//        2:03 PM  25 Mar 2020
                    mDialogView . textView2 . setText (
                        getString(R.string.time) +  orderTime.substring(
                            0,
                           7
                        ) +"\n" +getString(R.string.date) + orderTime.substring(8,20)
                    )

//        mDialogView.button10.setText(phn)
                    Glide . with (activity!!).load(img).placeholder(R.drawable.ic_user_profile)
                .into(mDialogView.profilePic)

                    mDialogView . iv_cross_dialog . setOnClickListener {
                mAlertDialog.dismiss()
            }
    }


    private fun showSetTimeDialog(orderId: String, mDialog: AlertDialog) {
        val mDialogView =
            LayoutInflater.from(activity).inflate(R.layout.dialog_set_prepare_time, null)
        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mDialogView.set_time.setOnClickListener {
            if (mDialogView.ed_time.text.isNullOrEmpty()) {
                CommonUtils.showToast(activity, getString(R.string.set_time_error))
            } else {
                if(mDialogView.ed_time.text.toString().toInt()>60) {
//                    mDialog.dismiss()
                    showErrorDialog(getString(R.string.time_not_be_more_than))
                }else{
                    mAlertDialog.dismiss()
                    setAcceptApi(orderId, "1", mDialogView.ed_time.text.toString())

                }
            }
        }
    }


    private fun showErrorDialog(msg:String) {
        val mDialogView =
            LayoutInflater.from(activity).inflate(R.layout.dialog_simple_msg, null)
        val mBuilder = AlertDialog.Builder(activity)
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()
        mAlertDialog.tv_msg.text = msg
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            mAlertDialog.tv_ok.setTextColor(resources.getColor(R.color.sky_blue,activity!!.theme))
        else
            mAlertDialog.tv_ok.setTextColor(resources.getColor(R.color.sky_blue))

        mAlertDialog.tv_ok.setOnClickListener {
            mAlertDialog.dismiss()

        }
    }
    private fun setAcceptApi(orderId: String, order_status: String, preparing_time: String) {
        var data = HashMap<String, Any?>()

        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")

        data.put("order_id", orderId)
        data.put("status", order_status)
        if (order_status == "1") {
            data.put("preparing_time", preparing_time)
        }
        ConnectionNetwork.postFormData(
            NetworkConstants.restaurant_accept_order,
            headerMAp,
            data,
            "",
            activity!!,
            home_parent,
            BroadcastConstants.restaurant_accept_order_num
        )
    }

    private fun getDriversApi() {
        var data = HashMap<String, Any?>()
        var headerMAp = HashMap<String, Any?>()
        headerMAp.put("Authorization", CommonUtils.getPrefValue(activity, PrefConstants.TOKEN))
        headerMAp.put("Accept", "application/json")
        data.put("latitude", CommonUtils.getPrefValue(activity, PrefConstants.LAT))
        data.put("longitude", CommonUtils.getPrefValue(activity, PrefConstants.LONG))
        ConnectionNetwork.postFormData(
            NetworkConstants.near_by_drivers,
            headerMAp,
            data,
            "",
            activity!!,
            home_parent,
            BroadcastConstants.near_by_drivers_num
        )
    }


    private fun setMail(userEmail: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:" + userEmail)
        if (intent.resolveActivity(activity!!.getPackageManager()) != null) {
            startActivity(intent)
        }
    }


    private fun setCallingToCustomer(userPhone: String) {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.parse("tel:" + userPhone)//change the number
        startActivity(callIntent)
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            HomeMapFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun broadcast(code: Int, data: Any?) {
        if (code == BroadcastConstants.current_orders_location_num) {
            markerPojo = Gson().fromJson(data.toString(), HomeModel::class.java)
            if (markerPojo.status) {
                CommonUtils.savePrefs(activity, PrefConstants.LAT, markerPojo.latitude.toString())
                CommonUtils.savePrefs(activity, PrefConstants.LONG, markerPojo.longitude.toString())
                if (markerPojo.data.size > 0) {
                    mMap.clear()

                    getDriversApi()

                    for (i in 0 until markerPojo.data.size) {

                        if (markerPojo.data.get(i).orderStatus == 0) {
                            createMarker(
                                markerPojo,
                                i,
                                R.drawable.red_marker)
                        } else {
                            createGreenMarker(
                                markerPojo,
                                i,
                                R.drawable.green_marker
                            )
                        }
                    }
                }

                currentLocation(
                    markerPojo,
                    markerPojo.latitude,
                    markerPojo.longitude,
                    getString(R.string.your_location),
                    R.drawable.black_marker
                )
            }

        }

        else if (code == BroadcastConstants.restaurant_accept_order_num) {
            var pojo = Gson().fromJson(data.toString(), OrderAccepModel::class.java)
            if (pojo.status) {
                if (pojo.message.equals("Orders Rejected By Restaurant.")) {
                    alertDialog.dismiss()
                    setOrdersMarkers()


                } else if (pojo.message.equals("Orders Accepted By Restaurant.")) {
                    alertDialog.dismiss()
                    setOrdersMarkers()

                }

            }
        }
        else if (code == BroadcastConstants.near_by_drivers_num) {
            var pojo = Gson().fromJson(data.toString(), NearDriversModel::class.java)
            if (pojo.status) {
                if (pojo.data.size > 0) {
                    for (i in 0 until pojo.data.size) {
                        if (pojo.data.get(i).average_rating < 5) {
                            currentDriverLocation(
                                pojo,
                                i,
                                getString(R.string.near_by_driver),
                                R.drawable.biker
                            )
                        }else if(pojo.data.get(i).total_rating == 5){
                            currentDriverLocation(
                                pojo,
                                i,
                                getString(R.string.near_by_driver),
                                R.drawable.green_driver_marker)
                        }
                    }
                }

            }

        }
        else if(code== BroadcastConstants.noti_new_order){
            setOrdersMarkers()

        }

    }

    private fun currentDriverLocation(
        driverMarkerPojo: NearDriversModel,
        i: Int,
        title: String,
        biker: Int
    ): Marker? {
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    driverMarkerPojo.data.get(i).latitude,
                    driverMarkerPojo.data.get(i).longitude
                ),
                12f
            )
        )
        mMap.animateCamera(CameraUpdateFactory.zoomIn())
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12f), 1000, null)


        val driverMarker: Marker = mMap.addMarker(
            MarkerOptions()
                .position(
                    LatLng(
                        driverMarkerPojo.data.get(i).latitude,
                        driverMarkerPojo.data.get(i).longitude
                    )
                )
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(getString(R.string.distance) + roundTwoDecimals(driverMarkerPojo.data.get(i).distance) + "Km")
                .icon(BitmapDescriptorFactory.fromResource(biker))
        )
        driverMarkerPojo.data.get(i).markerType = "driverMarker"
        return driverMarker
    }


    fun roundTwoDecimals(distance: Double): String {
        var twoDForm = DecimalFormat("#.##")
        return twoDForm.format(distance) + " "
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        mMap.setOnMarkerClickListener(this)

        googleMap.setMapStyle(
            MapStyleOptions(
                getResources()
                    .getString(R.string.dark_mode_style)
            )
        )
    }

    override fun onResume() {
        super.onResume()
        MyApplication.instance!!.registerListener(this)
        connectSocket()
        sendLocation()
        setOrdersMarkers()
    }

    private fun connectSocket() {
        var mOptions = IO.Options()
        mOptions.query = "id=" + CommonUtils.getPrefValue(activity, PrefConstants.ID)
        MyApplication.mSocket = IO.socket(ApiConstants.SOCKET_URL, mOptions)
        MyApplication.mSocket!!.on(Socket.EVENT_CONNECT, onConnect)
        MyApplication.mSocket!!.on(Socket.EVENT_DISCONNECT, onDisconnect)
        MyApplication.mSocket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        MyApplication.mSocket!!.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
        MyApplication.mSocket!!.connect()
        //GadeaApp.mSocket.on("123", onNewLocation)
    }

    //<editor-fold desc="socket listener implemented here">
    var onConnectError = object : Emitter.Listener {
        override fun call(vararg args: Any?) {
            Log.e("Error connecting", args[0].toString());
        }
    }


    var onConnect = object : Emitter.Listener {
        override fun call(vararg args: Any?) {
            Log.e("connected", "mysocket");
        }

    }


    var onDisconnect = object : Emitter.Listener {
        override fun call(vararg args: Any?) {
            Log.e("diconnected", "mysocket");
        }
    }

    fun sendLocation() {
        try {
            var detail = LocationDetail();
            detail.latitude = CommonUtils.getPrefValue(activity, PrefConstants.LAT).toDouble()
            detail.longitude = CommonUtils.getPrefValue(activity, PrefConstants.LONG).toDouble()
            var gson = Gson();
            var jsonString = gson.toJson(detail);
            MyApplication.mSocket!!.emit(ApiConstants.GET_NEAR_BY_RESTAURANT, jsonString);
            // Toast.makeText(this,"data send",Toast.LENGTH_LONG).show();
        } catch (e: java.lang.Exception) {
            e.printStackTrace();
        }
    }

    override fun onPause() {
        super.onPause()
        MyApplication.instance!!.deRegisterListener(this)
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        for (i in 0 until markerPojo.data.size) {
            if (p0!!.id == markerPojo.data.get(i).marker_id) {
                showOrderDialog(
                    markerPojo.data.get(i).id.toString(),
                    markerPojo.data.get(i).userName,
                    markerPojo.data.get(i).userImage,
                    markerPojo.data.get(i).deliveryAddress,
                    markerPojo.data.get(i).createdAt,
                    markerPojo.data.get(i).deliveryNote,
                    markerPojo.data.get(i).orderDetails
                )
            }
            if (markerPojo.data.get(i).markerType.equals("greenMarker")) {

                p0.showInfoWindow()

            }
            if (markerPojo.data.get(i).markerType.equals("driverMarker")) {

                p0.showInfoWindow()

            }
        }



        if (markerPojo.markerType.equals("blackMarker")) {
            p0!!.showInfoWindow()
        }

        return true
    }
}


