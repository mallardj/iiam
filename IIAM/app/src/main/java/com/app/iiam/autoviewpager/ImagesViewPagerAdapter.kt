package com.app.iiam.autoviewpager

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import com.app.iiam.R
import com.bumptech.glide.Glide

class ImagesViewPagerAdapter(
    var context: Context,
    var mDataList: List<Uri>,
    private val mListener: OnItemClickListener
) : PagerAdapter() {

    internal var layoutInflater: LayoutInflater

    init {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return mDataList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = layoutInflater.inflate(R.layout.item_pager, container, false)

        val ivItemPagerImage = itemView.findViewById<ImageView>(R.id.ivItemPagerImage)
        val rvItemPager = itemView.findViewById<RelativeLayout>(R.id.rvItemPager)

        Glide.with(context)
            .asBitmap()
            .load(mDataList[position])
            .placeholder(R.drawable.ic_comment_dumy)
            .centerCrop()
            .into(ivItemPagerImage)

        rvItemPager.setOnClickListener {
            mListener.onItemClick()
        }

        container.addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

    interface OnItemClickListener {
        fun onItemClick()
    }

}