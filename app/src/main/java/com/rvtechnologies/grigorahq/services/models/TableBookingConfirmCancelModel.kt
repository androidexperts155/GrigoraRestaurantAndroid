package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName

data class TableBookingConfirmCancelModel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) {
    data class Data(
        @SerializedName("booking_status")
        val bookingStatus: String,
        @SerializedName("cancel_reason")
        val cancelReason: Any,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("date")
        val date: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("no_of_seats")
        val noOfSeats: Int,
        @SerializedName("restaurant_id")
        val restaurantId: Int,
        @SerializedName("start_time_from")
        val startTimeFrom: String,
        @SerializedName("start_time_to")
        val startTimeTo: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user_id")
        val userId: Int
    )
}