package com.app.iiam.aboutme

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.iiam.R
import com.app.iiam.database.entities.Note
import com.app.iiam.utils.NOTE_FORMATTED_DATE
import com.app.iiam.utils.setDate
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_notes.view.*

class NotesAdapter(
    var mDataList: List<Note>,
    private val mListener: OnItemClickListener
) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_notes, parent, false))
    }

    override fun getItemCount(): Int = mDataList.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        with(mDataList[position]) {

            if (noteMediaType.equals(holder.ivItemImage.context.getString(R.string.image))) {
                if (TextUtils.isEmpty(notePicture)) {
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
            } else if (noteMediaType.equals(holder.ivItemImage.context.getString(R.string.video))) {
                if (TextUtils.isEmpty(noteVideo)) {
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
                .load(notePicture)
                .placeholder(R.drawable.ic_comment_dumy)
                .centerCrop()
                .into(holder.ivItemImage)

            holder.tvItemName.text = noteTitle

            holder.tvItemTitle.text = noteTitle

            holder.tvItemDate.text = noteDate.toString().setDate(noteDate!!, NOTE_FORMATTED_DATE)

            holder.tvItemComment.text = noteComment
        }

        holder.fmItemImage.setOnClickListener {
            if (mDataList[position].noteMediaType.equals(holder.ivItemImage.context.getString(R.string.image))) {
                mListener.onNotesImageClick(mDataList[position].notePicture.toString())
            } else {
                mListener.onNotesVideoClick(mDataList[position].notePicture.toString(), mDataList[position].noteVideo.toString())
            }
        }

        holder.lItemMain.setOnClickListener {
            mListener.onItemClick(mDataList[position])
        }

    }

    inner class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lItemMain = view.lItemMain
        val fmItemImage = view.fmItemImage
        val ivItemImage = view.ivItemImage
        val tvItemDate = view.tvItemDate
        val tvItemTitle = view.tvItemTitle
        val tvItemComment = view.tvItemComment
        val ivPlay = view.ivPlay
        val tvItemNameTitle = view.tvItemNameTitle
        val tvItemName = view.tvItemName
    }

    interface OnItemClickListener {
        fun onItemClick(note: Note)
        fun onNotesImageClick(pictureUri: String)
        fun onNotesVideoClick(pictureUri: String, videoUri: String)
    }

}