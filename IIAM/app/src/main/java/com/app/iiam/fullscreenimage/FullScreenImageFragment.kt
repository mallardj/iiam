package com.app.iiam.fullscreenimage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.iiam.R
import com.app.iiam.utils.Const
import com.bumptech.glide.Glide
import com.yanzhenjie.album.mvp.BaseFragment
import kotlinx.android.synthetic.main.fragment_full_screen_image.*

class FullScreenImageFragment : BaseFragment() {

    private var fileName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_screen_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        handleClickListner()
    }

    private fun initView() {

        fileName = arguments?.getString(Const.FILE_NAME)

        Glide.with(this)
            .load(fileName)
            .placeholder(R.drawable.ic_comment_dumy)
            .into(ivImage)
    }

    private fun handleClickListner() {
    }
}