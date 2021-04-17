package com.app.iiam.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.app.iiam.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import java.util.regex.Pattern

abstract class BaseActivity : AppCompatActivity() {

    private var progressDialog: Dialog? = null

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun showLoader() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.progress_dialog_layout, null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        progressDialog = dialogBuilder.create()
        progressDialog?.let {
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.show()
        }
    }

    fun hideLoader() {
        progressDialog?.let { alertDialog ->
            if (alertDialog.isShowing) { //  || !(alertDialog.cancel() as Boolean)) {
                alertDialog.dismiss()
            }
        }
    }

    fun showToast(stringResId: String?) {
        Toast.makeText(this, stringResId, Toast.LENGTH_LONG).show()
    }

    /**
     * show error
     */
    @SuppressLint("WrongConstant")
    fun showError(stringResId: String?) {
        if (stringResId == null) return
        val snackBar = Snackbar.make(findViewById(android.R.id.content), stringResId, BaseTransientBottomBar.LENGTH_LONG)
        val view = snackBar.view
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
        val tv = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        //tv.maxLines = 3
        val typeface = ResourcesCompat.getFont(this, R.font.helvetica_neue_medium)
        tv.typeface = typeface
        tv.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
        snackBar.show()
    }

    /**
     * validates email using regex
     */
    fun isEmailInvalid(email: String): Boolean {
        val regex =
            "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(email)
        return !matcher.matches()
    }

    /**
     * Checks if string contains any emoji
     */
    fun isEmoji(text: String): Boolean {
        return !isAsciiPrintable(text)
    }

    private fun isAsciiPrintable(cs: CharSequence?): Boolean {
        if (cs == null) {
            return false
        }
        val sz = cs.length
        for (i in 0 until sz) {
            val ch = cs[i]
            if (!(ch.toInt() >= 32 && ch.toInt() < 127)) {
                return false
            }
        }
        return true
    }

}