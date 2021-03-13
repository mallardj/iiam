package com.app.iiam.fullscreenimage

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.app.iiam.R
import com.app.iiam.autoviewpager.ImagesViewPagerAdapter
import com.app.iiam.utils.Const
import com.greennet.connect.home.findadvisor.adapter.FullScreenPagerAdapter
import com.yanzhenjie.album.mvp.BaseActivity
import kotlinx.android.synthetic.main.activity_full_screen_images.*

class FullScreenImagesActivity : BaseActivity(), ImagesViewPagerAdapter.OnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_images)

        initView()
        handleClickListner()
    }

    private fun initView() {

        val pictureUri = intent.getStringExtra(Const.PICTURE_URI)

        val mediaList: MutableList<String> = pictureUri!!.split(",").toMutableList()
        val mImagesList = mutableListOf<String>()
        for (i in 0 until mediaList.size) {
            mImagesList.add(mediaList[i])
        }

        val mFragmentList = mutableListOf<Fragment>()
        for (mImagesList in mImagesList) {
            val bundle = Bundle()
            bundle.putString(Const.FILE_NAME, mImagesList)
            val mFullScreenImageVideoFragment = FullScreenImageFragment()
            mFullScreenImageVideoFragment.arguments = bundle
            mFragmentList.add(mFullScreenImageVideoFragment)
        }

        val mFullScreenTipsPagerAdapter = FullScreenPagerAdapter(supportFragmentManager)
        mFullScreenTipsPagerAdapter.addFragment(mFragmentList)
        autoPagerImage.adapter = mFullScreenTipsPagerAdapter
        autoPagerImage.offscreenPageLimit = mImagesList.size
        //autoPagerImage.currentItem = mDataPosition

    }

    private fun handleClickListner() {
        ivClose.setOnClickListener {
            finish()
        }
    }

    override fun onItemClick() {

    }
}