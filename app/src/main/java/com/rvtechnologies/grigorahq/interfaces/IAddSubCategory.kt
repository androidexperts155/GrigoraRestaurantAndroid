package com.rvtechnologies.grigorahq.utils

import com.rvtechnologies.grigorahq.services.models.DishModel

interface IAddSubCategory {
    fun onAddOnUpdated(
        subCatPosition: Int,
        addOnPosition: Int,
        itemSubCategory: DishModel.Data.ItemCategory.ItemSubCategory
    )
}