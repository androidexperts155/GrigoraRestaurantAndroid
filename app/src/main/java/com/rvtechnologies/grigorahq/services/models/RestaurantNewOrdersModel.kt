package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RestaurantNewOrdersModel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) : Serializable {
    data class Data(
        @SerializedName("app_fee")
        val appFee: Float,
        @SerializedName("created_at")
        var createdAt: String,
        @SerializedName("delivery_address")
        val deliveryAddress: String,
        @SerializedName("delivery_fee")
        val deliveryFee: Float,
        @SerializedName("delivery_note")
        val deliveryNote: String="",
        @SerializedName("delivery_time")
        val deliveryTime: Any,
        @SerializedName("dispatch")
        val dispatch: Int,
        @SerializedName("driver_id")
        val driverId: Any,
        @SerializedName("end_lat")
        val endLat: String,
        @SerializedName("end_long")
        val endLong: String,
        @SerializedName("final_price")
        val finalPrice: Float,
        @SerializedName("id")
        val id: Int,
        @SerializedName("is_schedule")
        val isSchedule: String,
        @SerializedName("order_details")
        val orderDetails: ArrayList<OrderDetail>,
        @SerializedName("order_status")
        val orderStatus: Int,
        @SerializedName("payment_data")
        val paymentData: String,
        @SerializedName("payment_method")
        val paymentMethod: String,
        @SerializedName("preparing_end_time")
        val preparingEndTime: Any,
        @SerializedName("preparing_time")
        val preparingTime: Int,
        @SerializedName("price_after_promo")
        val priceAfterPromo: Float,
        @SerializedName("price_before_promo")
        val priceBeforePromo: Float,
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
        @SerializedName("start_address")
        val startAddress: Any,
        @SerializedName("start_lat")
        val startLat: String,
        @SerializedName("start_long")
        val startLong: String,
        @SerializedName("timeRemaining")
        val timeRemaining: Int,
        @SerializedName("update_preparing_time")
        val update_preparing_time: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user_address")
        val userAddress: String,
        @SerializedName("user_email")
        val userEmail: String,
        @SerializedName("user_id")
        val userId: Int,
        @SerializedName("user_image")
        val userImage: String="",
        @SerializedName("user_name")
        var userName: String,
        @SerializedName("order_type")
        val orderType: String,

        @SerializedName("user_phone")
        val userPhone: String
    ) : Serializable {
        data class OrderDetail(
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("item_choices")
            val itemChoices: List<ItemChoice>,
            @SerializedName("item_french_name")
            val itemFrenchName: String,
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
        ) : Serializable {
            data class ItemChoice(
                @SerializedName("french_name")
                val frenchName: String,
                @SerializedName("id")
                val id: Int,
                @SerializedName("item_sub_category")
                val itemSubCategory: List<ItemSubCategory>,
                @SerializedName("name")
                val name: String,
                @SerializedName("selection")
                val selection: String
            ) : Serializable {
                data class ItemSubCategory(
                    @SerializedName("add_on_price")
                    val addOnPrice: String,
                    @SerializedName("french_name")
                    val frenchName: String,
                    @SerializedName("id")
                    val id: Int,
                    @SerializedName("item_choice_name")
                    val itemChoiceName: String,
                    @SerializedName("name")
                    val name: String
                ) : Serializable
            }
        }
    }
}