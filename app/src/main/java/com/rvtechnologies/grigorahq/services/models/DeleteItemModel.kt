package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName

data class DeleteItemModel(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)