package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName

 data class NotificationDataModel(
    @SerializedName("app_fee")
    val appFee: String,
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
    @SerializedName("driver_fee")
    val driverFee: String,
    @SerializedName("end_lat")
    val endLat: String,
    @SerializedName("end_long")
    val endLong: String,
    @SerializedName("final_price")
    val finalPrice: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("notification_type")
    val notificationType: String="",
    @SerializedName("order_status")
    val orderStatus: String,
    @SerializedName("order_type")
    val orderType: String,
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
    @SerializedName("reference")
    val reference: String,
    @SerializedName("restaurant_id")
    val restaurantId: Int,
    @SerializedName("start_address")
    val startAddress: String,
    @SerializedName("start_lat")
    val startLat: String,
    @SerializedName("start_long")
    val startLong: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Int
)