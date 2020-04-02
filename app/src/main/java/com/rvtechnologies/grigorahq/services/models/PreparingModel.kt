package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName

data class PreparingModel(
    @SerializedName("data")
    val data: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) {
    data class Data(
        @SerializedName("app_fee")
        val appFee: Int,
        @SerializedName("cancel_type")
        val cancelType: Any,
        @SerializedName("cart_id")
        val cartId: Int,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("delivery_address")
        val deliveryAddress: String,
        @SerializedName("delivery_fee")
        val deliveryFee: String,
        @SerializedName("delivery_note")
        val deliveryNote: Any,
        @SerializedName("delivery_time")
        val deliveryTime: Any,
        @SerializedName("dispatch")
        val dispatch: String,
        @SerializedName("driver_fee")
        val driverFee: String,
        @SerializedName("driver_id")
        val driverId: Int,
        @SerializedName("end_lat")
        val endLat: String,
        @SerializedName("end_long")
        val endLong: String,
        @SerializedName("final_price")
        val finalPrice: String,
        @SerializedName("group_order")
        val groupOrder: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("is_schedule")
        val isSchedule: String,
        @SerializedName("max_per_person")
        val maxPerPerson: Any,
        @SerializedName("notification_sent")
        val notificationSent: String,
        @SerializedName("order_accepted_time")
        val orderAcceptedTime: Any,
        @SerializedName("order_status")
        val orderStatus: String,
        @SerializedName("order_type")
        val orderType: String,
        @SerializedName("payment_data")
        val paymentData: String,
        @SerializedName("payment_method")
        val paymentMethod: String,
        @SerializedName("preparing_end_time")
        val preparingEndTime: String,
        @SerializedName("preparing_time")
        val preparingTime: String,
        @SerializedName("price_after_promo")
        val priceAfterPromo: String,
        @SerializedName("price_before_promo")
        val priceBeforePromo: String,
        @SerializedName("promocode")
        val promocode: String,
        @SerializedName("quantity")
        val quantity: Int,
        @SerializedName("reference")
        val reference: String,
        @SerializedName("request_time")
        val requestTime: String,
        @SerializedName("restaurant_id")
        val restaurantId: Int,
        @SerializedName("schedule_time")
        val scheduleTime: Any,
        @SerializedName("start_address")
        val startAddress: String,
        @SerializedName("start_lat")
        val startLat: String,
        @SerializedName("start_long")
        val startLong: String,
        @SerializedName("time_remaining")
        val timeRemaining: Any,
        @SerializedName("update_preparing_time")
        val updatePreparingTime: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user_id")
        val userId: Int
    )
}