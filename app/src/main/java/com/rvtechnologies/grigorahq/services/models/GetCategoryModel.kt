package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetCategoryModel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) : Serializable {
    data class Data(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("french_name")
        val frenchName: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("restaurant_id")
        val restaurantId: Int,
        @SerializedName("status")
        val status: String,
        @SerializedName("updated_at")
        val updatedAt: String
    ) : Serializable
}