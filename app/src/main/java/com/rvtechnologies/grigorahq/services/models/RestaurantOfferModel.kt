package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName

data class RestaurantOfferModel(
    @SerializedName("message")
    val message: String,
    @SerializedName("offer")
    val offer: String,
    @SerializedName("status")
    val status: Boolean
)