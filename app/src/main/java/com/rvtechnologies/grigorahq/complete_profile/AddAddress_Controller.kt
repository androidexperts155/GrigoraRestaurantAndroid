package com.rvtechnologies.grigorahq.complete_profile

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.AsyncTask
import android.os.Handler
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.rvtechnologies.grigorahq.complete_profile.adapter.AutoCompleteAdapter
import com.rvtechnologies.grigorahq.complete_profile.pojo.LocalAddress
import com.rvtechnologies.grigorahq.complete_profile.pojo.PlaceDetails_Pojo
import com.rvtechnologies.grigorahq.complete_profile.pojo.PlacePredictions_Pojo
import com.rvtechnologies.grigorahq.services.api.ApiClient
import com.rvtechnologies.grigorahq.utils.CommonMethods.addressConversion
import com.rvtechnologies.grigorahq.utils.CommonUtils
import kotlinx.android.synthetic.main.activity_search_address.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*


class AddAddress_Controller(
    internal var view: SearchAddressActivity,
    var addressToUpdate: LocalAddress = LocalAddress()
) {

    var placesList: MutableList<PlacePredictions_Pojo.Predictions> = ArrayList()
    var temp: String = ""
    lateinit var localAddress: LocalAddress

    init {
        Handler().postDelayed(object : Runnable {
            override fun run() {
                localAddress = LocalAddress()
                handleVisibility(1)
            }
        }, 500)
    }

    lateinit var mGeocoder: Geocoder

    inner class ExternalThread(private val count: Int, var s: CharSequence) :
        AsyncTask<String, String, String>() {
        internal var txt = ""

        override fun onPreExecute() {
            super.onPreExecute()
            txt = view.ed_search.getText().toString()
        }

        override fun doInBackground(vararg params: String): String? {
            if (count != 0) {
                if (txt.length > 2) {
                    val call =
                        ApiClient.getApiServices().getLocations(getPlaceAutoCompleteUrl(txt))
                    call.enqueue(object : Callback<PlacePredictions_Pojo> {
                        override fun onResponse(
                            call: Call<PlacePredictions_Pojo>,
                            response: retrofit2.Response<PlacePredictions_Pojo>
                        ) {
                            if (response.body()!!.status.equals("OK")) {
                                placesList.clear()
                                for (i in 0 until response.body()!!.predictions.size) {
                                    val structuredFormatting =
                                        PlacePredictions_Pojo().Predictions().StructuredFormatting()
                                    structuredFormatting.main_text =
                                        response.body()!!.predictions[i].structured_formatting.main_text
                                    val predictions = PlacePredictions_Pojo().Predictions()
                                    predictions.description =
                                        response.body()!!.predictions[i].description
                                    predictions.place_id = response.body()!!.predictions[i].place_id
                                    predictions.structured_formatting = structuredFormatting

                                    placesList.add(predictions)
                                }
                                view.list_search.adapter = AutoCompleteAdapter(
                                    placesList,
                                    (view as AppCompatActivity?)!!
                                )
                            }
                        }

                        override fun onFailure(call: Call<PlacePredictions_Pojo>, t: Throwable) {
                            Log.e("aa", "-=-=-=-=" + t.message)
                        }
                    })
                }
            }
            return ""
        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
        }
    }

    fun getPlaceAutoCompleteUrl(input: String): String {
        val urlString = StringBuilder()
        urlString.append("https://maps.googleapis.com/maps/api/place/autocomplete/json")
        urlString.append("?input=")
        try {
            urlString.append(URLEncoder.encode(input, "utf8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

//        urlString.append("&location=")
//        urlString.append(
//            "${localAddress.delivery_geo_address.coordinates.get(1)},${localAddress.delivery_geo_address.coordinates.get(
//                0
//            )}"
//        )
        urlString.append("&radius=500&language=en")
        urlString.append("&key=" + "AIzaSyDDfWNtHlQ9VE5xe-lELER8j--VEqqTbUg")
        Log.d("FINAL URL:::   ", urlString.toString())
        return urlString.toString()
    }

    fun listItemClicked(position: Int) {
        view.currentLocation = false

        handleVisibility(3)
        temp = placesList[position].structured_formatting.main_text
        view.ed_search.setText(placesList[position].structured_formatting.main_text)

        view.ed_search.setSelection(view.ed_search.text.toString().length)

        temp = placesList[position].structured_formatting.main_text
        (view.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            (view as AppCompatActivity).getWindow().getDecorView().getApplicationWindowToken(),
            0
        )
        CommonUtils.showLoader(view as AppCompatActivity, "")
        if (view.ed_search.getText().toString() == temp) {
            val call = ApiClient.getApiServices()
                .getLocationDetails(getPlaceDetailsUrl(placesList[position].place_id))
            call.enqueue(object : Callback<PlaceDetails_Pojo> {
                override fun onResponse(
                    call: Call<PlaceDetails_Pojo>,
                    response: retrofit2.Response<PlaceDetails_Pojo>
                ) {
                    if (response.body()!!.status.equals("OK")) {
                        try {
                            CommonUtils.hideLoader()

                            mGeocoder = Geocoder(
                                view.getApplicationContext(),
                                Locale.getDefault()
                            )
                            try {
                                val addresses = mGeocoder.getFromLocation(
                                    java.lang.Double.parseDouble(response.body()!!.result.geometry.location.lat),
                                    java.lang.Double.parseDouble(response.body()!!.result.geometry.location.lng),
                                    1
                                )
                                if (addresses != null && addresses.size > 0) {
                                    addressToUpdate.address = addresses[0].getAddressLine(0)
                                    addressToUpdate.city = addresses[0].locality
                                    addressToUpdate.state = addresses[0].adminArea
                                    addressToUpdate.delivery_geo_address.coordinates.add(addresses[0].longitude)
                                    addressToUpdate.delivery_geo_address.coordinates.add(addresses[0].latitude)

                                    Log.e(
                                        "Lat Lng",
                                        "" + addresses[0].longitude + " " + addresses[0].latitude
                                    )
                                    val intent = Intent()
                                    intent.putExtra("address", addresses[0].getAddressLine(0))
                                        .putExtra("lng", addresses[0].longitude)
                                        .putExtra("lat", addresses[0].latitude)
                                    view.setResult(2, intent)
                                    view.finish()//finishing activity

                                    CommonUtils.hideLoader()
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }


                        } catch (e: Exception) {
                            CommonUtils.hideLoader()
                        }
                    }
                }

                override fun onFailure(call: Call<PlaceDetails_Pojo>, t: Throwable) {
                    Log.e("aa", "-=-=-=-=" + t.message)
                }
            })
        }
        placesList.clear()
    }

    fun getPlaceDetailsUrl(input: String): String {
        val urlString = StringBuilder()
        urlString.append("https://maps.googleapis.com/maps/api/place/details/json?placeid=")
        urlString.append(input)
        urlString.append("&key=" + "AIzaSyBUxnaQYt2gfojKC3lcYgtG88ERduqyjIU")
        Log.e("FINAL URL:::   ", urlString.toString())
        return urlString.toString()
    }

    fun connectAddAddress() {
        var jsonObject: JSONObject

        jsonObject = addressConversion(localAddress)

        CommonUtils.showLoader(view.applicationContext, "")


    }


    fun handleVisibility(case: Int) {
        when (case) {
//                normal case when screen opens or address is added
            1 -> {
                TransitionManager.beginDelayedTransition(view.parent_layout_main)
                view.list_search.visibility = View.GONE
            }

//                when user search for location
            2 -> {
                TransitionManager.beginDelayedTransition(view.parent_layout_main)
                view.list_search.visibility = View.VISIBLE
            }
//                when user have clicked one of addresses
            3 -> {
                TransitionManager.beginDelayedTransition(view.parent_layout_main)
                view.list_search.visibility = View.GONE

            }
        }

    }

}