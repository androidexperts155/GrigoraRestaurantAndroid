package com.rvtechnologies.grigorahq.services.models

import com.google.gson.annotations.SerializedName


data class GetAccountdetailsModel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) {
    data class Data(
        @SerializedName("account_number")
        val accountNumber: String,
        @SerializedName("bank_code")
        val bankCode: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("receipt_id")
        val receipt_id: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user_id")
        val userId: Int
    )
}