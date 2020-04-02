package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName

data class OrderAllmostCompleteModel(
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
        @SerializedName("delivery_address")
        val deliveryAddress: String,
        @SerializedName("delivery_fee")
        val deliveryFee: String,
        @SerializedName("delivery_note")
        val deliveryNote: String,
        @SerializedName("delivery_time")
        val deliveryTime: Any,
        @SerializedName("dispatch")
        val dispatch: String,
        @SerializedName("driver_id")
        val driverId: Any,
        @SerializedName("end_lat")
        val endLat: String,
        @SerializedName("end_long")
        val endLong: String,
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
        @SerializedName("preparing_time")
        val preparingTime: Any,
        @SerializedName("price_after_promo")
        val priceAfterPromo: String,
        @SerializedName("price_before_promo")
        val priceBeforePromo: String,
        @SerializedName("promocode")
        val promocode: String,
        @SerializedName("quantity")
        val quantity: Int,
        @SerializedName("request_time")
        val requestTime: Any,
        @SerializedName("restaurant_id")
        val restaurantId: Int,
        @SerializedName("schedule_time")
        val scheduleTime: Any,
        @SerializedName("start_address")
        val startAddress: Any,
        @SerializedName("start_lat")
        val startLat: String,
        @SerializedName("start_long")
        val startLong: String,
        @SerializedName("time_remaining")
        val timeRemaining: Any,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user_id")
        val userId: Int
    )
}