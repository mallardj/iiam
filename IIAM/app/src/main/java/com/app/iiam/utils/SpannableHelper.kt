package com.library.util.spannable

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.app.iiam.utils.CustomTypefaceSpan
import timber.log.Timber

class SpannableHelper(val context: Context) {

    fun getSpannableString(label: String, partToSpan: String, colorRes: Int): SpannableString {
        Timber.d(
            "getSpannableString() called with: label = [$label], partToSpan = [$partToSpan], colorRes = [$colorRes]........index....." + label.indexOf(
                partToSpan
            ) + "......last index....." + (label.indexOf(partToSpan) + partToSpan.length)
        )
        val spannableString = SpannableString(label)
        if (!label.contains(partToSpan)) {
            Timber.e("getSpannableString: Error" + Throwable("label $label does not contain partToSpan : $partToSpan"))
            return spannableString
        }
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, colorRes)),
            label.indexOf(partToSpan),
            label.indexOf(partToSpan) + partToSpan.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    fun getBoldSpannedString(label: String, partToSpan: String): SpannableString {
        Timber.d("getBoldSpannedString() called with: label = [$label], partToSpan = [$partToSpan]")
        val spannableString = SpannableString(label)
        if (!label.contains(partToSpan)) {
            Timber.e("getSpannableString: Error" + Throwable("label $label does not contain partToSpan : $partToSpan"))
            return spannableString
        }
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            label.indexOf(partToSpan),
            label.indexOf(partToSpan) + partToSpan.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    /**
     * It set font in spannable. It set font only first position of text.
     *
     * @param label      label
     * @param partToSpan part of text
     * @param fontBlack  font
     * @return spannable string
     */
    fun getSpannableFont(label: String, partToSpan: String, fontBlack: Typeface?): SpannableString {
        val spannableString = SpannableString(label)
        if (!label.contains(partToSpan)) {
            return spannableString
        }
        spannableString.setSpan(CustomTypefaceSpan("Roboto", fontBlack), label.indexOf(partToSpan), label.indexOf(partToSpan) + partToSpan.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        return spannableString
    }

    /**
     * It set font in spannable. It set font on particular index.
     *
     * @param label      label
     * @param partToSpan part of text
     * @param fontBlack  font
     * @param index      index font
     * @return spannable string
     */
    fun getSpannableFont(label: String, partToSpan: String, fontBlack: Typeface?, index: Int): SpannableString {
        val spannableString = SpannableString(label)
        if (!label.contains(partToSpan)) {
            return spannableString
        }
        spannableString.setSpan(CustomTypefaceSpan("Roboto", fontBlack), index, partToSpan.length + index, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        return spannableString
    }

    fun getSpannableFontWithColor(
        label: String,
        partToApplyFontSpan: String,
        color: Int,
        fontBlack: Typeface?,
        vararg partToApplyColorSpan: String
    ): SpannableString {
        val spannableString = SpannableString(label)
        if (!label.contains(partToApplyFontSpan) || !label.contains(partToApplyColorSpan[0]) || !label.contains(partToApplyColorSpan[0])) {
            return spannableString
        }
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, color)),
            label.indexOf(partToApplyColorSpan[0]),
            label.indexOf(partToApplyColorSpan[0]) + partToApplyColorSpan[0].length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            CustomTypefaceSpan("Roboto", fontBlack),
            label.indexOf(partToApplyFontSpan),
            label.indexOf(partToApplyFontSpan) + partToApplyFontSpan.length,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )
        return spannableString
    }

    fun getClickableSpanWithColor(label: String, partToSpan: String, colorRes: Int, clickableSpanListener: ClickableSpanListener): SpannableString {
        Timber.d(
            "getSpannableString() called with: label = [$label], partToSpan = [$partToSpan], colorRes = [$colorRes]........index....." + label.indexOf(
                partToSpan
            ) + "......last index....." + (label.indexOf(partToSpan) + partToSpan.length)
        )
        val spannableString = SpannableString(label)
        if (!label.contains(partToSpan)) {
            Timber.e("getSpannableString: Error" + Throwable("label $label does not contain partToSpan : $partToSpan"))
            return spannableString
        }

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                clickableSpanListener.onClick(widget)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                clickableSpanListener.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        spannableString.setSpan(
            clickableSpan, label.indexOf(partToSpan),
            label.indexOf(partToSpan) + partToSpan.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, colorRes)),
            label.indexOf(partToSpan),
            label.indexOf(partToSpan) + partToSpan.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableString
    }

    interface ClickableSpanListener {
        fun onClick(widget: View)
        fun updateDrawState(ds: TextPaint)
    }
}