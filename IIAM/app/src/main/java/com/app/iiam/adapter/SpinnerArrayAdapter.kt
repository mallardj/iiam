package com.app.iiam.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.app.iiam.R
import kotlinx.android.synthetic.main.activity_biographic_information.view.*
import kotlinx.android.synthetic.main.item_spinner.view.*

class SpinnerArrayAdapter(
    context: Context, mList: List<String>
) : ArrayAdapter<String>(context, 0, mList) {

    override fun getView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }

    override fun getDropDownView(position: Int, recycledView: View?, parent: ViewGroup): View {
        return this.createView(position, recycledView, parent)
    }

    private fun createView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val list = getItem(position)
        val view = recycledView ?: LayoutInflater.from(context).inflate(R.layout.item_spinner, parent, false)

        if (position == 0) {
            view.tvSpinner.setTextColor(ContextCompat.getColor(context, R.color.colorHint))
        } else {
            view.tvSpinner.setTextColor(ContextCompat.getColor(context, R.color.colorText))
        }

        view.tvSpinner.text = list
        return view
    }
}