package com.rvtechnologies.grigora.utils

import android.widget.ImageView
import android.widget.TextView

interface IRecyclerItemClick {
    fun onItemClick(item: Any, s: String, button5: ImageView)
    fun onClick(item: Any, type: String, position: Int,tv:TextView)
}