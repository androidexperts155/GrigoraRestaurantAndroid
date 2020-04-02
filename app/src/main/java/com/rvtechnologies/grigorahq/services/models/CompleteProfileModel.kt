package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName

data class CompleteProfileModel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) {
    data class Data(
        @SerializedName("address")
        val address: String,
        @SerializedName("approved")
        val approved: String,
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
        val idProof: Any,
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
        @SerializedName("opening_time")
        val opening_time: String,
        @SerializedName("closing_time")
        val closing_time: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("wallet")
        val wallet: String
    )
}