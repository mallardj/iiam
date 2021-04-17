package com.app.iiam.abilities

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.iiam.R
import com.app.iiam.database.entities.Abilities
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_abilities.view.*

class AbilitiesAdapter(
    var mDataList: List<Abilities>,
    private val mListener: OnItemClickListener
) : RecyclerView.Adapter<AbilitiesAdapter.AbilitiesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbilitiesViewHolder {
        return AbilitiesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_abilities, parent, false))
    }

    override fun getItemCount(): Int = mDataList.size

    override fun onBindViewHolder(holder: AbilitiesViewHolder, position: Int) {
        with(mDataList[position]) {

            if (abilitiesMediaType.equals(holder.ivItemImage.context.getString(R.string.image))) {
                if (TextUtils.isEmpty(abilitiesPicture)) {
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
            } else if (abilitiesMediaType.equals(holder.ivItemImage.context.getString(R.string.video))) {
                if (TextUtils.isEmpty(abilitiesVideo)) {
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
                .load(abilitiesPicture)
                .placeholder(R.drawable.ic_comment_dumy)
                .centerCrop()
                .into(holder.ivItemImage)

            holder.tvItemName.text = abilitiesAbility

            holder.tvItemTitle.text = abilitiesAbility

            holder.tvItemTask.text = abilitiesTask

            holder.tvItemComment.text = abilitiesComment
        }

        holder.fmItemImage.setOnClickListener {
            if (mDataList[position].abilitiesMediaType.equals(holder.ivItemImage.context.getString(R.string.image))) {
                mListener.onAbilitiesImageClick(mDataList[position].abilitiesPicture.toString())
            } else {
                mListener.onAbilitiesVideoClick(mDataList[position].abilitiesPicture.toString(), mDataList[position].abilitiesVideo.toString())
            }
        }

        holder.lItemMain.setOnClickListener {
            mListener.onItemClick(mDataList[position])
        }

    }

    inner class AbilitiesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lItemMain = view.lItemMain
        val fmItemImage = view.fmItemImage
        val ivItemImage = view.ivItemImage
        val tvItemTitle = view.tvItemTitle
        val tvItemTask = view.tvItemTask
        val tvItemComment = view.tvItemComment
        val ivPlay = view.ivPlay
        val tvItemNameTitle = view.tvItemNameTitle
        val tvItemName = view.tvItemName
    }

    interface OnItemClickListener {
        fun onItemClick(abilities: Abilities)
        fun onAbilitiesImageClick(pictureUri: String)
        fun onAbilitiesVideoClick(pictureUri: String, videoUri: String)
    }

}