package com.app.iiam.fullscreenimage

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.app.iiam.R
import com.app.iiam.base.BaseActivity
import com.app.iiam.utils.Const
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_full_screen_image_video.*
import kotlinx.android.synthetic.main.item_video_controller.*

class FullScreenImageVideoActivity : BaseActivity() {

    private var fileType: String? = null
    private var exoPlayer: SimpleExoPlayer? = null
    private var fullScreen = false
    private var isSoundOn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image_video)

        initView()
        handleClickListner()
    }

    private fun initView() {

        val videoUri = intent.getStringExtra(Const.VIDEO_URI)

        if (TextUtils.isEmpty(videoUri)) {
            fileType = getString(R.string.image)

            epVideoView.visibility = View.GONE
            rlImage.visibility = View.VISIBLE

            val pictureUri = intent.getStringExtra(Const.PICTURE_URI)

            Glide.with(this@FullScreenImageVideoActivity)
                .load(pictureUri)
                .placeholder(R.drawable.ic_comment_dumy)
                .into(ivImage)
        } else {
            fileType = getString(R.string.video)

            rlImage.visibility = View.GONE
            epVideoView.visibility = View.VISIBLE

            val mediaSource = ProgressiveMediaSource.Factory(DefaultDataSourceFactory(this, Util.getUserAgent(this, getString(R.string.app_name))), DefaultExtractorsFactory()).createMediaSource(Uri.parse(videoUri))
            //val mediaSource = ExtractorMediaSource.Factory(DefaultDataSourceFactory(this, Util.getUserAgent(this, getString(R.string.app_name)))).setExtractorsFactory(DefaultExtractorsFactory()).createMediaSource(Uri.parse(videoUri))
            //val mediaSource = ProgressiveMediaSource.Factory(DefaultHttpDataSourceFactory(Util.getUserAgent(this, getString(R.string.app_name)))).createMediaSource(Uri.parse(videoUri))
            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, DefaultRenderersFactory(this), DefaultTrackSelector(), DefaultLoadControl())
            exoPlayer?.seekTo(0, 0)
            exoPlayer?.prepare(mediaSource, false, false)
            epVideoView.player = exoPlayer

        }
    }

    private fun handleClickListner() {
        ivClose.setOnClickListener {
            finish()
        }

        exo_fullscreen_icon.setOnClickListener {
            if (fullScreen) {
                exo_fullscreen_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_open))
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE)
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                val params = epVideoView.getLayoutParams() as LinearLayout.LayoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = (200 * getApplicationContext().resources.displayMetrics.density).toInt()
                epVideoView.setLayoutParams(params)
                fullScreen = false
            } else {
                exo_fullscreen_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_close))

                getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                )

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)

                val params = epVideoView.getLayoutParams() as LinearLayout.LayoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.MATCH_PARENT
                epVideoView.setLayoutParams(params)

                fullScreen = true
            }
        }

        exo_sound_icon.setOnClickListener {
            if (isSoundOn) {
                exoPlayer?.volume = 0.0f
                exo_sound_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_volume_off))
                isSoundOn = false
            } else {
                exoPlayer?.volume = 100.0f
                exo_sound_icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_volume_on))
                isSoundOn = true
            }
        }

    }

    override fun onPause() {
        super.onPause()
        if (fileType.equals(getString(R.string.video))) {
            exoPlayer?.playWhenReady = false
            //exoPlayer?.release()
        }
    }

    override fun onDestroy() {
        exoPlayer?.release()
        super.onDestroy()
    }

}