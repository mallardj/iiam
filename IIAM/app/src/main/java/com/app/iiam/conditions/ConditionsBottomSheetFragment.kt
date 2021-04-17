package com.app.iiam.conditions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.iiam.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ConditionsBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var mListener: ItemClickListener? = null

    companion object {
        val TAG = "ConditionsBottomSheetFragment"
        private var bundle = Bundle()
        fun newInstance(bundle: Bundle): ConditionsBottomSheetFragment {
            this.bundle = bundle
            return ConditionsBottomSheetFragment()
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
        return inflater.inflate(R.layout.fragment_conditions_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.tvAddConditions).setOnClickListener(this)
        view.findViewById<View>(R.id.tvCancel).setOnClickListener(this)
        //val tvCancel = view.findViewById<View>(R.id.tvCancel) as AppCompatTextView

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
            R.id.tvAddConditions -> {
                mListener?.onAddConditions()
            }
            R.id.tvCancel -> {

            }
        }
        dismiss()
    }

    interface ItemClickListener {
        fun onAddConditions()
    }
}