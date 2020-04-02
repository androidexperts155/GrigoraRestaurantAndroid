package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName

data class OrderAccepModel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) {
    data class Data(
        @SerializedName("app_fee")
        val appFee: Int,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("delivery_fee")
        val deliveryFee: String,
        @SerializedName("delivery_note")
        val deliveryNote: String,
        @SerializedName("delivery_time")
        val deliveryTime: Any,
        @SerializedName("driver_id")
        val driverId: Int,
        @SerializedName("end_lat")
        val endLat: Any,
        @SerializedName("end_long")
        val endLong: Any,
        @SerializedName("final_price")
        val finalPrice: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("is_schedule")
        val isSchedule: String,
        @SerializedName("order_status")
        val orderStatus: String,
        @SerializedName("payment_data")
        val paymentData: String,
        @SerializedName("payment_method")
        val paymentMethod: String,
        @SerializedName("price_after_promo")
        val priceAfterPromo: String,
        @SerializedName("price_before_promo")
        val priceBeforePromo: String,
        @SerializedName("promocode")
        val promocode: String,
        @SerializedName("quantity")
        val quantity: Int,
        @SerializedName("request_time")
        val requestTime: String,
        @SerializedName("restaurant_id")
        val restaurantId: Int,
        @SerializedName("schedule_time")
        val scheduleTime: String,
        @SerializedName("start_lat")
        val startLat: Any,
        @SerializedName("start_long")
        val startLong: Any,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user_id")
        val userId: Int
    )
}