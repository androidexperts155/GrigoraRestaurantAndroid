package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("token_type")
    val tokenType: String
) {
    data class Data(
        @SerializedName("address")
        val address: String,
        @SerializedName("approved")
        val approved: String,
        @SerializedName("busy_status")
        val busyStatus: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("device_token")
        val deviceToken: String,
        @SerializedName("device_type")
        val deviceType: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("email_verified_at")
        val emailVerifiedAt: Any,
        @SerializedName("french_address")
        val frenchAddress: String,
        @SerializedName("french_name")
        val frenchName: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("id_proof")
        val idProof: String,
        @SerializedName("image")
        val image: String,
        @SerializedName("language")
        val language: String,
        @SerializedName("latitude")
        val latitude: String,
        @SerializedName("longitude")
        val longitude: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("offer")
        val offer: String,
        @SerializedName("otp")
        val otp: Any,
        @SerializedName("otp_expire_time")
        val otpExpireTime: Any,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("promo_id")
        val promoId: Any,
        @SerializedName("pure_veg")
        val pureVeg: String,
        @SerializedName("role")
        val role: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("wallet")
        val wallet: String,
        @SerializedName("receipt_id")
        val receipt_id: String,
        @SerializedName("avg_ratings")
        var avg_ratings: String? = null,
        @SerializedName("opening_time")
        var opening_time: String? = null,
        @SerializedName("closing_time")
        var closing_time: String? = null,
        @SerializedName("pickup")
        var pickup: String? = null,
        @SerializedName("full_time")
        var full_time: String? = null

    )
}
