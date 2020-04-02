package com.rvtechnologies.grigorahq.complete_profile.pojo

import com.google.gson.annotations.SerializedName

/**
 * Created by Android on 6/6/2017.
 */

class PlaceDetails_Pojo {
    @SerializedName("result")
    var result: Result = Result()

    @SerializedName("status")
    var status: String = ""

    inner class Result {
        @SerializedName("geometry")
        var geometry: Geometry = Geometry()

        inner class Geometry {
            @SerializedName("location")
            var location: Location = Location()

            inner class Location {
                @SerializedName("lat")
                var lat: String = ""

                @SerializedName("lng")
                var lng: String = ""
            }
        }
    }

}

