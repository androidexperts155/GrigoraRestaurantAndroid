package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName

data class ForgotPasswordModel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) {
    data class Data(
        @SerializedName("address")
        val address: Any,
        @SerializedName("approved")
        val approved: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("device_token")
        val deviceToken: Any,
        @SerializedName("device_type")
        val deviceType: Any,
        @SerializedName("email")
        val email: String,
        @SerializedName("email_verified_at")
        val emailVerifiedAt: Any,
        @SerializedName("french_address")
        val frenchAddress: Any,
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
        val latitude: Any,
        @SerializedName("longitude")
        val longitude: Any,
        @SerializedName("name")
        val name: String,
        @SerializedName("otp")
        val otp: String,
        @SerializedName("otp_expire_time")
        val otpExpireTime: String,
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
        val wallet: String
    )
}