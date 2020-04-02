package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OrderModel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) : Serializable {
    data class Data(
        @SerializedName("app_fee")
        val appFee: Int,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("delivery_fee")
        val deliveryFee: String,
        @SerializedName("delivery_time")
        val deliveryTime: Any,
        @SerializedName("driver_id")
        val driverId: Int,
        @SerializedName("end_lat")
        val endLat: Any,
        @SerializedName("end_long")
        val endLong: Any,
        @SerializedName("final_price")
        val finalPrice: Float,
        @SerializedName("id")
        val id: Int,
        @SerializedName("is_schedule")
        val isSchedule: String,
        @SerializedName("order_details")
        val orderDetails: List<OrderDetail>,
        @SerializedName("order_status")
        val orderStatus: String,
        @SerializedName("payment_data")
        val paymentData: String,
        @SerializedName("payment_method")
        val paymentMethod: String,
        @SerializedName("price_after_promo")
        val priceAfterPromo: Float,
        @SerializedName("price_before_promo")
        val priceBeforePromo: Float,
        @SerializedName("promocode")
        val promocode: String,
        @SerializedName("quantity")
        val quantity: Float,
        @SerializedName("request_time")
        val requestTime: String,
        @SerializedName("restaurant_id")
        val restaurantId: Int,
        @SerializedName("schedule_time")
        val scheduleTime: Any,
        @SerializedName("start_lat")
        val startLat: Any,
        @SerializedName("start_long")
        val startLong: Any,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user_email")
        val userEmail: String,
        @SerializedName("user_id")
        val userId: Int,
        @SerializedName("user_image")
        val userImage: String,
        @SerializedName("user_name")
        val userName: String,
        @SerializedName("user_phone")
        val userPhone: String
    ) : Serializable {
        data class OrderDetail(
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("item_id")
            val itemId: Int,
            @SerializedName("item_name")
            val itemName: String,
            @SerializedName("order_id")
            val orderId: Int,
            @SerializedName("price")
            val price: String,
            @SerializedName("quantity")
            val quantity: Int,
            @SerializedName("updated_at")
            val updatedAt: String
        ) : Serializable
    }
}