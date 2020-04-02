package com.rvtechnologies.grigorahq.navigation

import android.app.Activity
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rvtechnologies.grigorahq.R

class NavigationAdapter(activity: Activity, items: Array<String>, icons: TypedArray) :
    RecyclerView.Adapter<NavigationAdapter.ViewHolder>() {
    var activity: Activity
    var items: Array<String>
    var icons: TypedArray
    lateinit var listener: ItemClickListener

    init {
        this.activity = activity
        this.items = items
        this.icons = icons

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_navigation, null)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewName.setText(items[position])
        holder.imageViewNavigationIcon.setImageResource(icons.getResourceId(position, -1))

        when (position) {
//            0 -> {
//                holder.imageViewNavigationIcon.background =
//                    ContextCompat.getDrawable(activity, R.drawable.home)
//            }
//            1 -> {
//                holder.imageViewNavigationIcon.background =
//                    ContextCompat.getDrawable(activity, R.drawable.menu_nav)
//            }
//            2 -> {
//                holder.imageViewNavigationIcon.background =
//                    ContextCompat.getDrawable(activity, R.drawable.custom_cusine)
//            }
//            3 -> {
//                holder.imageViewNavigationIcon.background =
//                    ContextCompat.getDrawable(activity, R.drawable.avail_orders)
//            }
//            4 -> {
//                holder.imageViewNavigationIcon.background =
//                    ContextCompat.getDrawable(activity, R.drawable.history)
//            }
//            5 -> {
//                holder.imageViewNavigationIcon.background =
//                    ContextCompat.getDrawable(activity, R.drawable.table_book)
//            }
//            6 -> {
//                holder.imageViewNavigationIcon.background =
//                    ContextCompat.getDrawable(activity, R.drawable.offers)
//            }
//            7 -> {
//                holder.imageViewNavigationIcon.background =
//                    ContextCompat.getDrawable(activity, R.drawable.setting)
//            }7 -> {
//                holder.imageViewNavigationIcon.background =
//                    ContextCompat.getDrawable(activity, R.drawable.logout)
//            }

        }


        holder.relativeLayout.setOnClickListener {
            listener.itemClick(position)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageViewNavigationIcon = itemView.findViewById<ImageView>(R.id.imageViewNavigationIcon)
        var textViewName = itemView.findViewById<TextView>(R.id.textViewItemName)
        var relativeLayout = itemView.findViewById<RelativeLayout>(R.id.relativeLayout)
    }


    fun itemClickHandler(listener: ItemClickListener) {
        this.listener = listener
    }

    interface ItemClickListener {
        fun itemClick(position: Int)
    }


}