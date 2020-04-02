package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName

data class CusinesModel(
    @SerializedName("data")
    val data: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) {
    data class Data(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("french_name")
        val frenchName: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("icon")
        val icon: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @Transient
        var isChecked: Boolean
    )
}