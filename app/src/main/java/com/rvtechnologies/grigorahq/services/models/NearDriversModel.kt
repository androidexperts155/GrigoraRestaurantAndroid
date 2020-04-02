package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName

data class NearDriversModel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) {

    data class Data(
        @SerializedName("average_rating")
        val average_rating: Double,
        @SerializedName("total_rating")
        val total_rating: Int,
        @SerializedName("distance")
        val distance: Double,
        @SerializedName("id")
        val id: Int,
        @SerializedName("latitude")
        val latitude: Double,
        @SerializedName("longitude")
        val longitude: Double,
        var markerType: String =""

    )
}