package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName

data class EditItemModel(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) {
    data class Data(
        @SerializedName("category_id")
        val categoryId: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("french_description")
        val frenchDescription: String,
        @SerializedName("french_name")
        val frenchName: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("in_offer")
        val inOffer: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("offer_price")
        val offerPrice: String,
        @SerializedName("price")
        val price: String,
        @SerializedName("pure_veg")
        val pureVeg: String,
        @SerializedName("restaurant_id")
        val restaurantId: Int,
        @SerializedName("status")
        val status: String,
        @SerializedName("updated_at")
        val updatedAt: String
    )
}