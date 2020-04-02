package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName
 
data class AvailableBookingModel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("no_of_seats")
    val noOfSeats: Int,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("table_booking")
    val tableBooking: String
) {
    data class Data(
        @SerializedName("booking_status")
        val bookingStatus: String,
        @SerializedName("cancel_reason")
        val cancelReason: Any,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("customer_french_name")
        val customerFrenchName: String,
        @SerializedName("customer_image")
        val customerImage: String,
        @SerializedName("customer_name")
        val customerName: String,
        @SerializedName("customer_phone")
        val customerPhone: String,
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