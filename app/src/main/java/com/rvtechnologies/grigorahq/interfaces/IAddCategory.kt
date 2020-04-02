package com.rvtechnologies.grigorahq.utils

import com.rvtechnologies.grigorahq.services.models.DishModel

interface IAddCategory {
    fun onCategoryUpdated(pos: Int, categoryModel: DishModel.Data.ItemCategory)

    fun onAdOnAdded(catPosition: Int)
    fun onItemRemove(catPosition: Int)


}