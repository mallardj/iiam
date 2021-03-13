package com.app.iiam.addnote

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import com.app.iiam.R
import com.app.iiam.utils.Const
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MediaBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var mListener: ItemClickListener? = null
    private var mMediaType = ""
    private var isMultiMedia = true

    companion object {
        val TAG = "MediaBottomSheetFragment"
        private var bundle = Bundle()
        fun newInstance(bundle: Bundle): MediaBottomSheetFragment {
            this.bundle = bundle
            return MediaBottomSheetFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_media_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvImage = view.findViewById<View>(R.id.tvImage) as AppCompatTextView
        val tvVideo = view.findViewById<View>(R.id.tvVideo) as AppCompatTextView
        val viewVideo = view.findViewById<View>(R.id.viewVideo)
        val tvRemoveMedia = view.findViewById<View>(R.id.tvRemoveMedia) as AppCompatTextView
        val viewRemoveMedia = view.findViewById<View>(R.id.viewRemoveMedia)
        val tvCancel = view.findViewById<View>(R.id.tvCancel) as AppCompatTextView

        tvImage.setOnClickListener(this)
        tvVideo.setOnClickListener(this)
        tvRemoveMedia.setOnClickListener(this)
        tvCancel.setOnClickListener(this)

        isMultiMedia = bundle.getBoolean(Const.MULTI_MEDIA, true)
        mMediaType = bundle.getString(Const.MEDIA_TYPE) ?: ""

        if (!isMultiMedia) {
            tvRemoveMedia.text = getString(R.string.remove_image)
            tvVideo.visibility = View.GONE
            viewVideo.visibility = View.GONE
        } else {
            tvRemoveMedia.text = getString(R.string.remove_media)
            tvVideo.visibility = View.VISIBLE
            viewVideo.visibility = View.VISIBLE
        }

        if (TextUtils.isEmpty(mMediaType)) {
            tvRemoveMedia.visibility = View.GONE
            viewRemoveMedia.visibility = View.GONE
        } else {
            tvRemoveMedia.visibility = View.VISIBLE
            viewRemoveMedia.visibility = View.VISIBLE
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ItemClickListener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement ItemClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tvImage -> {
                mListener?.onSelectImage()
            }
            R.id.tvVideo -> {
                mListener?.onSelectVideo()
            }
            R.id.tvRemoveMedia -> {
                mListener?.onRemoveMedia()
            }
            R.id.tvCancel -> {

            }
        }
        dismiss()
    }

    interface ItemClickListener {
        fun onSelectImage()
        fun onSelectVideo()
        fun onRemoveMedia()
    }
}