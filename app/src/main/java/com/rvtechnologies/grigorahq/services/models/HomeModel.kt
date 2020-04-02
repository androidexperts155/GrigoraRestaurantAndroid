package com.rvtechnologies.grigorahq.services.models

import com.google.gson.annotations.SerializedName

data class HomeModel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean,
    var markerType: String="blackMarker"

) {
    data class Data(
        @SerializedName("created_at")
        var createdAt: String,
        @SerializedName("delivery_address")
        val deliveryAddress: String,
        @SerializedName("delivery_note")
        val deliveryNote: String?,
        @SerializedName("end_lat")
        val endLat: Double,
        @SerializedName("end_long")
        val endLong: Double,
       var marker_id: String="0",
       var markerType: String="greenMarker",
        @SerializedName("id")
        val id: Int,
        @SerializedName("order_details")
        val orderDetails: ArrayList<OrderDetail>,
        @SerializedName("order_status")
        val orderStatus: Int,
        @SerializedName("user_email")
        val userEmail: String,
        @SerializedName("user_french_name")
        val userFrenchName: String,
        @SerializedName("user_id")
        val userId: Int,
        @SerializedName("user_image")
        val userImage: String,
        @SerializedName("user_name")
        val userName: String,
        @SerializedName("user_phone")
        val userPhone: String
    ) {
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
        ) {
            data class ItemChoice(
                @SerializedName("french_name")
                val frenchName: String,
                @SerializedName("id")
                val id: String,
                @SerializedName("item_sub_category")
                val itemSubCategory: List<ItemSubCategory>,
                @SerializedName("name")
                val name: String,
                @SerializedName("selection")
                val selection: String
            ) {
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
                )
            }
        }
    }
}