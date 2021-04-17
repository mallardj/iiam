package com.app.iiam.records

import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.iiam.R
import com.app.iiam.autoviewpager.ImagesViewPagerAdapter
import com.app.iiam.database.entities.Records
import com.app.iiam.utils.NOTE_FORMATTED_DATE
import com.app.iiam.utils.setDate
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_records.view.*

class RecordsAdapter(
    var mDataList: List<Records>,
    private val mListener: OnItemClickListener
) : RecyclerView.Adapter<RecordsAdapter.RecordsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordsViewHolder {
        return RecordsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_records, parent, false))
    }

    override fun getItemCount(): Int = mDataList.size

    override fun onBindViewHolder(holder: RecordsViewHolder, position: Int) {
        with(mDataList[position]) {

            if (TextUtils.isEmpty(recordsPicture)) {
                Glide.with(holder.ivItemImage.context)
                    .asBitmap()
                    .load(recordsPicture)
                    .placeholder(R.drawable.ic_comment_dumy)
                    .centerCrop()
                    .into(holder.ivItemImage)

                holder.fmItemImageNoData.visibility = View.GONE
                holder.fmItemImage.visibility = View.GONE
                holder.tvItemName.visibility = View.VISIBLE
                holder.tvItemNameTitle.visibility = View.VISIBLE
            } else {
                val mediaList: MutableList<String> = recordsPicture!!.split(",").toMutableList()
                val mUriList = mutableListOf<Uri>()
                mUriList.clear()
                for (i in 0 until mediaList.size) {
                    mUriList.add(Uri.parse(mediaList[i]))
                }

                val mImageAdapter = ImagesViewPagerAdapter(holder.tabImage.context, mUriList, object : ImagesViewPagerAdapter.OnItemClickListener {
                    override fun onItemClick() {
                        mListener.onRecordsImageClick(mDataList[position].recordsPicture.toString())
                    }
                })
                holder.tabImage.setupWithViewPager(holder.autoPagerImage)
                holder.autoPagerImage.adapter = mImageAdapter

                holder.fmItemImageNoData.visibility = View.GONE
                holder.fmItemImage.visibility = View.VISIBLE
                holder.tvItemName.visibility = View.GONE
                holder.tvItemNameTitle.visibility = View.GONE
            }

            holder.tvItemTitleNoData.text = recordsDocumentsName

            holder.tvItemTitle.text = recordsDocumentsName

            holder.tvItemName.text = recordsDocumentsName

            holder.tvItemDate.text = recordsDate.toString().setDate(recordsDate!!, NOTE_FORMATTED_DATE)

            holder.tvItemPagesInDocument.text = recordsPagesInDocument
        }

        holder.fmItemImageNoData.setOnClickListener {
            mListener.onRecordsImageClick(mDataList[position].recordsPicture.toString())
        }

        holder.lItemMain.setOnClickListener {
            mListener.onItemClick(mDataList[position])
        }

    }

    inner class RecordsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lItemMain = view.lItemMain
        val tvItemDate = view.tvItemDate
        val tvItemPagesInDocument = view.tvItemPagesInDocument

        val fmItemImage = view.fmItemImage
        val autoPagerImage = view.autoPagerImage
        val tabImage = view.tabImage
        val tvItemTitle = view.tvItemTitle

        val fmItemImageNoData = view.fmItemImageNoData
        val ivItemImage = view.ivItemImage
        val tvItemTitleNoData = view.tvItemTitleNoData

        val tvItemNameTitle = view.tvItemNameTitle
        val tvItemName = view.tvItemName

    }

    interface OnItemClickListener {
        fun onItemClick(records: Records)
        fun onRecordsImageClick(pictureUri: String)
    }

}