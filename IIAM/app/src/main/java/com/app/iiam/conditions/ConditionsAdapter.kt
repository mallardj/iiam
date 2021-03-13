package com.app.iiam.conditions

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.iiam.R
import com.app.iiam.database.entities.Conditions
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_conditions.view.*

class ConditionsAdapter(
    var mDataList: List<Conditions>,
    private val mListener: OnItemClickListener
) : RecyclerView.Adapter<ConditionsAdapter.ConditionsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConditionsViewHolder {
        return ConditionsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_conditions, parent, false))
    }

    override fun getItemCount(): Int = mDataList.size

    override fun onBindViewHolder(holder: ConditionsViewHolder, position: Int) {
        with(mDataList[position]) {

            if (conditionsMediaType.equals(holder.ivItemImage.context.getString(R.string.image))) {
                if (TextUtils.isEmpty(conditionsPicture)) {
                    holder.ivPlay.visibility = View.GONE
                    holder.tvItemName.visibility = View.VISIBLE
                    holder.tvItemNameTitle.visibility = View.VISIBLE
                    holder.fmItemImage.visibility = View.GONE
                } else {
                    holder.ivPlay.visibility = View.GONE
                    holder.tvItemName.visibility = View.GONE
                    holder.tvItemNameTitle.visibility = View.GONE
                    holder.fmItemImage.visibility = View.VISIBLE
                }
            } else if (conditionsMediaType.equals(holder.ivItemImage.context.getString(R.string.video))) {
                if (TextUtils.isEmpty(conditionsVideo)) {
                    holder.ivPlay.visibility = View.GONE
                    holder.tvItemName.visibility = View.VISIBLE
                    holder.tvItemNameTitle.visibility = View.VISIBLE
                    holder.fmItemImage.visibility = View.GONE
                } else {
                    holder.ivPlay.visibility = View.VISIBLE
                    holder.tvItemName.visibility = View.GONE
                    holder.tvItemNameTitle.visibility = View.GONE
                    holder.fmItemImage.visibility = View.VISIBLE
                }
            } else {
                holder.ivPlay.visibility = View.GONE
                holder.tvItemName.visibility = View.VISIBLE
                holder.tvItemNameTitle.visibility = View.VISIBLE
                holder.fmItemImage.visibility = View.GONE
            }

            Glide.with(holder.ivItemImage.context)
                .asBitmap()
                .load(conditionsPicture)
                .placeholder(R.drawable.ic_comment_dumy)
                .centerCrop()
                .into(holder.ivItemImage)

            holder.tvItemName.text = conditionsName

            holder.tvItemTitle.text = conditionsName

            holder.tvItemSymptoms.text = conditionsSymptoms

            holder.tvItemManagement.text = conditionsManagement

            holder.tvItemAdditionalComment.text = conditionsAdditionalComments
            if (TextUtils.isEmpty(holder.tvItemAdditionalComment.text.toString().trim())) {
                holder.tvItemAdditionalComment.visibility = View.GONE
                holder.tvItemAdditionalCommentTitle.visibility = View.GONE
            } else {
                holder.tvItemAdditionalComment.visibility = View.VISIBLE
                holder.tvItemAdditionalCommentTitle.visibility = View.VISIBLE
            }
        }

        holder.fmItemImage.setOnClickListener {
            if (mDataList[position].conditionsMediaType.equals(holder.ivItemImage.context.getString(R.string.image))) {
                mListener.onConditionsImageClick(mDataList[position].conditionsPicture.toString())
            } else {
                mListener.onConditionsVideoClick(mDataList[position].conditionsPicture.toString(), mDataList[position].conditionsVideo.toString())
            }
        }

        holder.lItemMain.setOnClickListener {
            mListener.onItemClick(mDataList[position])
        }

    }

    inner class ConditionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lItemMain = view.lItemMain
        val fmItemImage = view.fmItemImage
        val ivItemImage = view.ivItemImage
        val tvItemTitle = view.tvItemTitle
        val tvItemSymptoms = view.tvItemSymptoms
        val tvItemManagement = view.tvItemManagement
        val tvItemAdditionalCommentTitle = view.tvItemAdditionalCommentTitle
        val tvItemAdditionalComment = view.tvItemAdditionalComment
        val ivPlay = view.ivPlay
        val tvItemNameTitle = view.tvItemNameTitle
        val tvItemName = view.tvItemName
    }

    interface OnItemClickListener {
        fun onItemClick(conditions: Conditions)
        fun onConditionsImageClick(pictureUri: String)
        fun onConditionsVideoClick(pictureUri: String, videoUri: String)
    }

}