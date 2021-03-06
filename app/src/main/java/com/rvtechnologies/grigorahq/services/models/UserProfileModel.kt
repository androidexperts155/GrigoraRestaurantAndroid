package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName

data class UserProfileModel(
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
        @SerializedName("address_proof")
        val addressProof: Any,
        @SerializedName("approved")
        val approved: String,
        @SerializedName("avg_ratings")
        val avgRatings: Float,
        @SerializedName("busy_status")
        val busyStatus: String,
        @SerializedName("closing_time")
        val closingTime: String,
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
        @SerializedName("facebook_id")
        val facebookId: Any,
        @SerializedName("franchisee_proof")
        val franchiseeProof: String,
        @SerializedName("french_address")
        val frenchAddress: String,
        @SerializedName("french_name")
        val frenchName: String,
        @SerializedName("from_referal")
        val fromReferal: Any,
        @SerializedName("full_time")
        val fullTime: String,
        @SerializedName("google_id")
        val googleId: Any,
        @SerializedName("id")
        val id: Int,
        @SerializedName("id_proof")
        val idProof: String,
        @SerializedName("image")
        val image: String,
        @SerializedName("instagram_id")
        val instagramId: Any,
        @SerializedName("language")
        val language: String,
        @SerializedName("latitude")
        val latitude: String,
        @SerializedName("license_image")
        val licenseImage: String,
        @SerializedName("longitude")
        val longitude: String,
        @SerializedName("my_referal")
        val myReferal: Any,
        @SerializedName("name")
        val name: String,
        @SerializedName("no_of_seats")
        val noOfSeats: Int,
        @SerializedName("notification")
        val notification: String,
        @SerializedName("offer")
        val offer: Int,
        @SerializedName("opening_time")
        val openingTime: String,
        @SerializedName("otp")
        val otp: Any,
        @SerializedName("otp_expire_time")
        val otpExpireTime: Any,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("pickup")
        val pickup: String,
        @SerializedName("preparing_time")
        val preparingTime: Int,
        @SerializedName("promo_id")
        val promoId: Int,
        @SerializedName("pure_veg")
        val pureVeg: String,
        @SerializedName("receipt_id")
        val receiptId: String,
        @SerializedName("role")
        val role: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("table_booking")
        val tableBooking: String,
        @SerializedName("total_rating")
        val totalRating: Int,
        @SerializedName("twiter_id")
        val twiterId: Any,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("username")
        val username: String,
        @SerializedName("wallet")
        val wallet: String
    )
}