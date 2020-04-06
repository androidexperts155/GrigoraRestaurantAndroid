package com.rvtechnologies.grigorahq.services.models

data class ParentCategoryModel(
    val `data`: Data = Data(),
    val message: String = "",
    val status: Boolean = false
) {
    data class Data(
        val approved: String = "",
        val created_at: String = "",
        val end_time: String = "",
        val french_name: String = "",
        val id: Int = 0,
        val name: String = "",
        val restaurant_id: Int = 0,
        val start_time: String = "",
        val updated_at: String = ""
    )
}