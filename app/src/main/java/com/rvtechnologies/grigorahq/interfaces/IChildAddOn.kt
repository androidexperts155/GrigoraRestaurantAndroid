package com.rvtechnologies.grigorahq.interfaces

import com.rvtechnologies.grigorahq.services.models.AddDishModel

interface IChildAddOn {

    fun onAddOnUpdated(pos: Int, categoryModel: AddDishModel.Data.ItemCategory.ItemSubCategory.ItemSubSubCategory)

    fun onChildAdOnAdded(catPosition: Int)
    fun onItemRemove(catPosition: Int)


}