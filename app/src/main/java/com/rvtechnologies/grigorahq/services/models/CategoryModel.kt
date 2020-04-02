package com.rvtechnologies.grigorahq.services.models


import com.google.gson.annotations.SerializedName

data class CategoryModel(
    @SerializedName("item_sub_category")
    var itemSubCategory: ArrayList<ItemSubCategory> = ArrayList<ItemSubCategory>(),
    @SerializedName("name")
    var name: String,
    @SerializedName("selection")
    var selection: String
) {
    data class ItemSubCategory(
        @SerializedName("add_on_price")
        var addOnPrice: String,
        @SerializedName("name")
        var name: String
    )
}