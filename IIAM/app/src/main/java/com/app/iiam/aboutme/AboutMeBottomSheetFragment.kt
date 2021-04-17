package com.app.iiam.aboutme

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import com.app.iiam.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AboutMeBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var mListener: ItemClickListener? = null

    companion object {
        val TAG = "AboutMeBottomSheetFragment"
        lateinit var titleForBioGraphicInfo: String
        private var bundle = Bundle()
        fun newInstance(bundle: Bundle, titleForBioGraphicInfo: String): AboutMeBottomSheetFragment {
            this.titleForBioGraphicInfo = titleForBioGraphicInfo
            this.bundle = bundle
            return AboutMeBottomSheetFragment()
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
        return inflater.inflate(R.layout.fragment_about_me_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.tvAddBiographicInformation).setOnClickListener(this)
        view.findViewById<View>(R.id.tvAddNote).setOnClickListener(this)
        view.findViewById<View>(R.id.tvCancel).setOnClickListener(this)
        val tvAddBiographicInformation = view.findViewById<View>(R.id.tvAddBiographicInformation) as AppCompatTextView
        tvAddBiographicInformation.text = titleForBioGraphicInfo

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
            R.id.tvAddBiographicInformation -> {
                mListener?.onAddBiographicInformation()
            }
            R.id.tvAddNote -> {
                mListener?.onAddNote()
            }
            R.id.tvCancel -> {

            }
        }
        dismiss()
    }

    interface ItemClickListener {
        fun onAddBiographicInformation()
        fun onAddNote()
    }
}