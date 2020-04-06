package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DishModel(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
) : Serializable {

    data class Data(
        @SerializedName("category_id")
        val categoryId: Int,
        @SerializedName("category_name")
        val categoryName: String,
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
        @SerializedName("item_categories")
        val itemCategories: List<ItemCategory>,
        @SerializedName("name")
        val name: String,
        @SerializedName("offer_price")
        val offerPrice: Float,
        @SerializedName("price")
        val price: String,
        @SerializedName("approx_prep_time")
        val approx_prep_time: String,
        @SerializedName("pure_veg")
        val pureVeg: String,
        @SerializedName("restaurant_id")
        val restaurantId: Int,
        @SerializedName("status")
        val status: String,
        @SerializedName("featured")
        val featured: String,
        @SerializedName("updated_at")
        val updatedAt: String
    ) : Serializable {

        data class ItemCategory(
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("french_name")
            val frenchName: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("item_id")
            val itemId: Int,
            @SerializedName("item_sub_category")
            val itemSubCategory: ArrayList<ItemSubCategory>,
            @SerializedName("name")
            var name: String,
            @SerializedName("selection")
            var selection: String,
            @SerializedName("status")
            val status: String,
            @SerializedName("updated_at")
            val updatedAt: String
        ) : Serializable {
            data class ItemSubCategory(
                @SerializedName("add_on_price")
                var addOnPrice: String,
                @SerializedName("created_at")
                val createdAt: String,
                @SerializedName("french_name")
                val frenchName: String,
                @SerializedName("id")
                val id: Int,
                @SerializedName("item_cat_id")
                val itemCatId: Int,
                @SerializedName("name")
                var name: String,
                @SerializedName("status")
                val status: String,
                @SerializedName("updated_at")
                val updatedAt: String
            ) : Serializable
        }
    }
}