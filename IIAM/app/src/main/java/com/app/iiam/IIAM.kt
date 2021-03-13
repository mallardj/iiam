package com.app.iiam

import android.app.Application
import com.app.iiam.loaders.GlideImageLoader
import com.app.iiam.preference.SharedPrefsManager
import com.app.iiam.utils.MediaLoader
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumConfig
import lv.chi.photopicker.ChiliPhotoPicker

class IIAM : Application() {

    var TAG = "IIAM"

    companion object {
        lateinit var instance: IIAM

        fun getAppContext(): IIAM {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        SharedPrefsManager.initialize(this)

        ChiliPhotoPicker.init(
            loader = GlideImageLoader(),
            authority = "com.app.iiam.provider"
        )

        Album.initialize(
            AlbumConfig.newBuilder(this)
            .setAlbumLoader(MediaLoader())
            .build())

    }
}