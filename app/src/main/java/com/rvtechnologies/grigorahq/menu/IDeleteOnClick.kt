package com.rvtechnologies.grigorahq.menu

import androidx.appcompat.app.AlertDialog
import com.rvtechnologies.grigorahq.services.models.GetRestaurentItemsModel

interface IDeleteOnClick {

    fun deleteOnClick(
        pojo: GetRestaurentItemsModel.Data,
        dialog: AlertDialog
    )
}