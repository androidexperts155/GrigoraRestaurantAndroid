package com.rvtechnologies.grigorahq.services.models

data class GetParentCategoryModel(
    val `data`: List<Data> = listOf(),
    val message: String = "",
    val status: Boolean = false
) {
    data class Data(
        val approved: String = "",
        val created_at: String = "",
        val end_date: Any? = Any(),
        val end_time: String = "",
        val french_name: String = "",
        val id: Int = 0,
        val image: Any? = Any(),
        val name: String = "",
        val restaurant_id: Int = 0,
        val special_day: Any? = Any(),
        val start_date: Any? = Any(),
        val start_time: String = "",
        val status: String = "",
        val updated_at: String = ""
    )
}