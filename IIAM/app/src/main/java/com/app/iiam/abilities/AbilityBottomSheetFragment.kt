package com.app.iiam.abilities

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.iiam.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AbilityBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var mListener: ItemClickListener? = null

    companion object {
        val TAG = "AbilityBottomSheetFragment"
        private var bundle = Bundle()
        fun newInstance(bundle: Bundle): AbilityBottomSheetFragment {
            this.bundle = bundle
            return AbilityBottomSheetFragment()
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
        return inflater.inflate(R.layout.fragment_ability_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.tvEditAbility).setOnClickListener(this)
        view.findViewById<View>(R.id.tvDeleteAbility).setOnClickListener(this)
        view.findViewById<View>(R.id.tvCancel).setOnClickListener(this)

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
            R.id.tvEditAbility -> {
                mListener?.onEditData()
            }
            R.id.tvDeleteAbility -> {
                mListener?.onDeleteData()
            }
            R.id.tvCancel -> {

            }
        }
        dismiss()
    }

    interface ItemClickListener {
        fun onEditData()
        fun onDeleteData()
    }
}