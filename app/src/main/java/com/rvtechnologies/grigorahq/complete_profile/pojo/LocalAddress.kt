package com.rvtechnologies.grigorahq.complete_profile.pojo


/**
 * Created by Rakesh on 30/11/18.
 */
class LocalAddress {
    var apartment_no = ""
    var business_name = ""
    var address = ""
    var zipcode = ""
    var city = ""
    var state = ""
    var delivery_note = ""
    var type = ""
    var short_address = ""
    var status = false
    var delivery_geo_address = DeliveryGeoAddress()


    inner class DeliveryGeoAddress {
        var coordinates = ArrayList<Double>()
    }
}