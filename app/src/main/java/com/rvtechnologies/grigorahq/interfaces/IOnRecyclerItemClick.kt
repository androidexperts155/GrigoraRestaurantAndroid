package com.rvtechnologies.grigorahq.utils

import android.view.View

interface IOnRecyclerItemClick {
    fun onClick(item: Any, adapterPosition: Int)
    fun onLongClick(item: Any, itemView: View)
    fun onSwitchAvail(item: Any, status: Int)
}