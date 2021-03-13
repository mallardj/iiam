package com.app.iiam.loaders

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.app.iiam.R
import com.bumptech.glide.Glide
import lv.chi.photopicker.loader.ImageLoader

class GlideImageLoader : ImageLoader {

    override fun loadImage(context: Context, view: ImageView, uri: Uri) {
        Glide.with(context)
            .asBitmap()
            .load(uri)
            .placeholder(R.drawable.ic_comment_dumy)
            .centerCrop()
            .into(view)
    }
}