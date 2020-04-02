package com.rvtechnologies.grigorahq.services.models

import com.google.gson.annotations.SerializedName


data class ReasonsModel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String,

    @SerializedName("status")
    val status: Boolean
) {
    data class Data(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("french_message")
        val frenchMessage: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("message")
        val message: String,
        @SerializedName("type")
        val type: Int,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("order_type")
        val orderType: String,
        var isChecked: Boolean
    )
}