package com.app.iiam.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.iiam.R
import com.app.iiam.listner.ItemClickListener
import com.app.iiam.models.DrawerDataModel

class DrawerItemAdapter(
    val dataLists: ArrayList<DrawerDataModel>,
    private val itemClick: ItemClickListener<DrawerDataModel>
) :
    RecyclerView.Adapter<DrawerItemAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerItemAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_navigation, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: DrawerItemAdapter.ViewHolder, position: Int) {
        holder.bindItems(dataLists[position], position, itemClick)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return dataLists.size
    }

    //the class is hodling the view
    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bindItems(
            data: DrawerDataModel,
            position: Int,
            itemClick: ItemClickListener<DrawerDataModel>
        ) {
            val tvNavText = itemView.findViewById(R.id.tvNavigation) as TextView
            val ivNavigation = itemView.findViewById(R.id.ivNavigation) as ImageView
            val llNavigation = itemView.findViewById(R.id.llNavigation) as LinearLayout
            tvNavText.text = data.text
            ivNavigation.setImageDrawable(ContextCompat.getDrawable(ivNavigation.context, data.icon))
            llNavigation.setOnClickListener {
                itemClick.onItemClicked(position, data)
            }
        }
    }
}