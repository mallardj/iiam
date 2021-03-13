package com.app.iiam.medications

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.iiam.R
import com.app.iiam.database.entities.Medications
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_abilities.view.*
import kotlinx.android.synthetic.main.item_medications.view.*
import kotlinx.android.synthetic.main.item_medications.view.fmItemImage
import kotlinx.android.synthetic.main.item_medications.view.ivItemImage
import kotlinx.android.synthetic.main.item_medications.view.lItemMain
import kotlinx.android.synthetic.main.item_medications.view.tvItemName
import kotlinx.android.synthetic.main.item_medications.view.tvItemNameTitle
import kotlinx.android.synthetic.main.item_medications.view.tvItemTitle

class MedicationsAdapter(
    var mDataList: List<Medications>,
    private val mListener: OnItemClickListener
) : RecyclerView.Adapter<MedicationsAdapter.MedicationsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationsViewHolder {
        return MedicationsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_medications, parent, false))
    }

    override fun getItemCount(): Int = mDataList.size

    override fun onBindViewHolder(holder: MedicationsViewHolder, position: Int) {
        with(mDataList[position]) {

            if (TextUtils.isEmpty(medicationsPicture)) {
                holder.tvItemName.visibility = View.VISIBLE
                holder.tvItemNameTitle.visibility = View.VISIBLE
                holder.fmItemImage.visibility = View.GONE
            } else {
                holder.tvItemName.visibility = View.GONE
                holder.tvItemNameTitle.visibility = View.GONE
                holder.fmItemImage.visibility = View.VISIBLE
            }

            Glide.with(holder.ivItemImage.context)
                .asBitmap()
                .load(medicationsPicture)
                .placeholder(R.drawable.ic_comment_dumy)
                .centerCrop()
                .into(holder.ivItemImage)

            holder.tvItemName.text = medicationsName

            holder.tvItemTitle.text = medicationsName

            holder.tvItemDoes.text = medicationsDose

            holder.tvItemDoesUnit.text = medicationsDoseUnit

            holder.tvItemAdditionalDoesInfo.text = medicationsDoseInfo
            if (TextUtils.isEmpty(holder.tvItemAdditionalDoesInfo.text.toString().trim())) {
                holder.tvItemAdditionalDoesInfo.visibility = View.GONE
                holder.tvItemAdditionalDoesInfoTitle.visibility = View.GONE
            } else {
                holder.tvItemAdditionalDoesInfo.visibility = View.VISIBLE
                holder.tvItemAdditionalDoesInfoTitle.visibility = View.VISIBLE
            }

            holder.tvItemRoute.text = medicationsRoute

            holder.tvItemAdditionalRouteInfo.text = medicationsRouteInfo
            if (TextUtils.isEmpty(holder.tvItemAdditionalRouteInfo.text.toString().trim())) {
                holder.tvItemAdditionalRouteInfo.visibility = View.GONE
                holder.tvItemAdditionalRouteInfoTitle.visibility = View.GONE
            } else {
                holder.tvItemAdditionalRouteInfo.visibility = View.VISIBLE
                holder.tvItemAdditionalRouteInfoTitle.visibility = View.VISIBLE
            }

            holder.tvItemFrequency.text = medicationsFrequency

        }

        holder.fmItemImage.setOnClickListener {
            mListener.onMedicationsImageClick(mDataList[position].medicationsPicture.toString())
        }

        holder.lItemMain.setOnClickListener {
            mListener.onItemClick(mDataList[position])
        }

    }

    inner class MedicationsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lItemMain = view.lItemMain
        val fmItemImage = view.fmItemImage
        val ivItemImage = view.ivItemImage
        val tvItemTitle = view.tvItemTitle
        val tvItemDoes = view.tvItemDoes
        val tvItemDoesUnit = view.tvItemDoesUnit
        val tvItemAdditionalDoesInfoTitle = view.tvItemAdditionalDoesInfoTitle
        val tvItemAdditionalDoesInfo = view.tvItemAdditionalDoesInfo
        val tvItemRoute = view.tvItemRoute
        val tvItemAdditionalRouteInfoTitle = view.tvItemAdditionalRouteInfoTitle
        val tvItemAdditionalRouteInfo = view.tvItemAdditionalRouteInfo
        val tvItemFrequency = view.tvItemFrequency
        val tvItemNameTitle = view.tvItemNameTitle
        val tvItemName = view.tvItemName
    }

    interface OnItemClickListener {
        fun onItemClick(medications: Medications)
        fun onMedicationsImageClick(pictureUri: String)
    }

}