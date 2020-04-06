package com.rvtechnologies.grigorahq.services.models

data class AddDishModel(
    val `data`: List<Data>,
    val message: String = "",
    val status: Boolean = false
) {
    data class Data(
        val approx_prep_time: String = "",
        val created_at: String = "",
        val cuisine_id: String = "",
        val description: String = "",
        val french_description: String = "",
        val french_name: String = "",
        val id: Int = 0,
        val image: String = "",
        val item_categories: List<ItemCategory>,
        val name: String = "",
        val offer_price: String = "",
        val price: String = "",
        val pure_veg: String = "",
        val restaurant_id: Int = 0,
        val status: String = "",
        val updated_at: String = ""
    ) {
        data class ItemCategory(
            val created_at: String = "",
            val french_name: String = "",
            val id: Int = 0,
            val item_id: Int = 0,
            val item_sub_category: ArrayList<ItemSubCategory>,
            var name: String = "",
            var selection: String = "",
            val status: String = "",
            val updated_at: String = ""
        ) {
            data class ItemSubCategory(
                val add_on_price: String = "",
                val created_at: String = "",
                val french_name: String = "",
                val id: Int = 0,
                val item_cat_id: Int = 0,
                val item_sub_sub_category: ArrayList<ItemSubSubCategory> ,
                var name: String = "",
                val status: String = "",
                val updated_at: String = ""
            ) {
                data class ItemSubSubCategory(
                    val add_on_price: String = "",
                    var name: String = ""
                )
            }
        }
    }
}