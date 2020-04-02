package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName

data class AdminOfferPojo(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) {
    data class Data(
        @SerializedName("code")
        val code: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("french_name")
        val frenchName: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("no_of_attempts")
        val noOfAttempts: Int,
        @SerializedName("percentage")
        val percentage: Int,
        @SerializedName("status")
        val status: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("applied")
        val applied: Boolean
    )
}