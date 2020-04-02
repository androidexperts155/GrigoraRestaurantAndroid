package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName

data class LogoutModel(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)