package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName

data class OrderDetailsModel(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("item_choices")
    val itemChoices: Any,
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
)