package com.app.iiam.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun showLoader() {
        (activity as BaseActivity).showLoader()
    }

    fun hideLoader() {
        (activity as BaseActivity).hideLoader()
    }

    fun showToast(stringResId: String?) {
        (activity as BaseActivity).showToast(stringResId)
    }

    fun showError(stringResId: String?) {
        (activity as BaseActivity).showError(stringResId)
    }

    fun isEmailInvalid(email: String): Boolean {
        return (activity as BaseActivity).isEmailInvalid(email)
    }

    fun isEmoji(text: String): Boolean {
        return (activity as BaseActivity).isEmoji(text)
    }

}