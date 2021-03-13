package com.app.iiam.signup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.iiam.R
import com.app.iiam.base.BaseActivity
import com.app.iiam.database.entities.User
import com.app.iiam.database.viewmodel.UserViewModel
import com.app.iiam.extensions.hideKeyboard
import com.app.iiam.main.MainActivity
import com.app.iiam.preference.SharedPrefsManager
import com.app.iiam.staticpages.StaticPageActivity
import com.app.iiam.utils.AsteriskPasswordTransformationMethod
import com.app.iiam.utils.Const
import com.app.iiam.utils.NetworkUtils
import com.app.iiam.utils.logInfo
import com.google.firebase.auth.FirebaseAuth
import com.library.util.common.KeyboardUtils
import com.library.util.spannable.SpannableHelper
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.etEmail
import kotlinx.android.synthetic.main.activity_signup.etPassword

class SignupActivity : BaseActivity() {

    private lateinit var userViewModel: UserViewModel
    private var mRegisteredUsersList = mutableListOf<User>()
    private lateinit var mAuth: FirebaseAuth

    companion object {
        private const val TAG = "SignupActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        initView()
        handleClickListner()
    }

    private fun initView() {

        mAuth = FirebaseAuth.getInstance()
        getUsersListFromDB()

        etPassword.transformationMethod = AsteriskPasswordTransformationMethod()
        etReTypePassword.transformationMethod = AsteriskPasswordTransformationMethod()

        KeyboardUtils.addKeyboardToggleListener(this@SignupActivity, object : KeyboardUtils.SoftKeyboardToggleListener {
            override fun onToggleSoftKeyboard(isVisible: Boolean) {
                if (isVisible) {
                    val param = btnCreateNewUser.layoutParams as ViewGroup.MarginLayoutParams
                    param.setMargins(0, 0, 0, 600)
                    btnCreateNewUser.layoutParams = param
                } else {
                    val param = btnCreateNewUser.layoutParams as ViewGroup.MarginLayoutParams
                    param.setMargins(0, 0, 0, 0)
                    btnCreateNewUser.layoutParams = param
                }
            }
        })

        val spannableHelper = SpannableHelper(this@SignupActivity)
        tvAgreeTerms.text =
            spannableHelper.getClickableSpanWithColor(getString(R.string.text_agree_terms_policies_sign_up),
                getString(R.string.terms_and_conditions), R.color.colorGreenBlue,
                object : SpannableHelper.ClickableSpanListener {
                    override fun onClick(widget: View) {
                        val intent = Intent(this@SignupActivity, StaticPageActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                    }
                })
        tvAgreeTerms.movementMethod = LinkMovementMethod.getInstance()

    }

    private fun getUsersListFromDB() {
        showLoader()

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        mRegisteredUsersList.clear()

        userViewModel.allUsers.observe(this, Observer { users ->
            users.let {
                logInfo(TAG, it.toString())
            }
            for (user in users) {
                mRegisteredUsersList.add(user)
            }
        })

        hideLoader()
    }

    @SuppressLint("DefaultLocale")
    private fun handleClickListner() {

        btnCreateNewUser.setOnClickListener {
            hideKeyboard()
            if (TextUtils.isEmpty(etEmail.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.email).toLowerCase()
                    )
                )
            } else if (isEmailInvalid(etEmail.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_invalid_field),
                        getString(R.string.email).toLowerCase()
                    )
                )
            } else if (TextUtils.isEmpty(etPassword.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.password).toLowerCase()
                    )
                )
            } else if (isEmoji(etPassword.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_invalid_field),
                        getString(R.string.password).toLowerCase()
                    )
                )
            } else if (etPassword.text.toString().trim().length < 6) {
                showError(
                    String.format(
                        getString(
                            R.string.validation_min_length,
                            getString(R.string.password),
                            resources.getInteger(R.integer.min_password)
                        )
                    )
                )
            } else if (etPassword.text.toString().trim().length > 16) {
                showError(
                    String.format(
                        getString(
                            R.string.validation_max_length,
                            getString(R.string.password),
                            resources.getInteger(R.integer.max_password)
                        )
                    )
                )
            } else if (TextUtils.isEmpty(etReTypePassword.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.re_type_password).toLowerCase()
                    )
                )
            } else if (etPassword.text.toString().trim() != etReTypePassword.text.toString().trim()) {
                showError(getString(R.string.validation_msg_confirm_password_and_password))
            } else {
                hideKeyboard()
                if (!NetworkUtils.isConnected) {
                    showError(getString(R.string.network_error))
                } else {
                    showLoader()
                    if (mRegisteredUsersList.size >= 3) {
                        hideLoader()
                        showError(getString(R.string.you_reached_three_users_limit))
                    } else {
                        signUp(etEmail.text.toString(), etPassword.text.toString())
                    }
                }
            }
        }
    }

    private fun signUp(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (!task.isSuccessful) {
                hideLoader()
                showError(task.exception!!.message)
            } else {
                val user = User(email, password)
                userViewModel.insert(user)
                getRegistedUsersDataFromDB()
            }
        }
    }

    private fun getRegistedUsersDataFromDB() {
        mRegisteredUsersList.clear()

        userViewModel.allUsers.observe(this, Observer { users ->
            users.let {
                logInfo(TAG, it.toString())
            }
            for (user in users) {
                mRegisteredUsersList.add(user)
            }

            for (i in 0 until mRegisteredUsersList.size) {
                if (etEmail.text.toString().equals(mRegisteredUsersList[i].userEmail)) {
                    signIn(mRegisteredUsersList[i].userId, mRegisteredUsersList[i].userEmail!!, mRegisteredUsersList[i].userPassword!!)
                }
            }

        })
    }

    private fun signIn(userId: Long, userEmail: String, userPassword: String) {
        mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this) { task ->
            if (!task.isSuccessful) {
                hideLoader()
                showError(task.exception!!.message)
            } else {
                SharedPrefsManager.setLong(Const.USER_ID, userId)
                SharedPrefsManager.setString(Const.USER_EMAIL, userEmail)
                SharedPrefsManager.setString(Const.USER_PASSWORD, userPassword)

                hideLoader()
                goToMainActivity()
            }
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

}