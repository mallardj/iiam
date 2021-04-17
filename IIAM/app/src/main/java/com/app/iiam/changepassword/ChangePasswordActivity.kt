package com.app.iiam.changepassword

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.app.iiam.R
import com.app.iiam.base.BaseActivity
import com.app.iiam.database.viewmodel.*
import com.app.iiam.extensions.hideKeyboard
import com.app.iiam.main.MainActivity
import com.app.iiam.preference.SharedPrefsManager
import com.app.iiam.utils.AsteriskPasswordTransformationMethod
import com.app.iiam.utils.Const
import com.app.iiam.utils.NetworkUtils
import com.app.iiam.utils.logInfo
import com.app.iiam.welcome.WelcomeActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.toolbar_home.*

class ChangePasswordActivity : BaseActivity() {

    private var mUserId: Long = 0
    private var mUserEmail = ""
    private var mUserPassword = ""
    private lateinit var mAuth: FirebaseAuth

    private lateinit var userViewModel: UserViewModel
    private lateinit var userDetailsViewModel: UserDetailsViewModel
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var abilitiesViewModel: AbilitiesViewModel
    private lateinit var medicationsViewModel: MedicationsViewModel
    private lateinit var conditionsViewModel: ConditionsViewModel
    private lateinit var recordsViewModel: RecordsViewModel

    companion object {
        private const val TAG = "ChangePasswordActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        initView()
        handleClickListner()
    }

    private fun initView() {

        tvToolbarTitle.visibility = View.GONE
        ivToolbarRightIcon.visibility = View.GONE
        ivBack.visibility = View.VISIBLE
        tvToolbarName.setText(getString(R.string.change_password))
        tvToolbarName.visibility = View.VISIBLE

        mAuth = FirebaseAuth.getInstance()

        etCurrentPassword.transformationMethod = AsteriskPasswordTransformationMethod()
        etNewPassword.transformationMethod = AsteriskPasswordTransformationMethod()
        etConfirmNewPassword.transformationMethod = AsteriskPasswordTransformationMethod()

        mUserId = SharedPrefsManager.getLong(Const.USER_ID)
        mUserEmail = SharedPrefsManager.getString(Const.USER_EMAIL)
        mUserPassword = SharedPrefsManager.getString(Const.USER_PASSWORD)
    }

    @SuppressLint("DefaultLocale")
    private fun handleClickListner() {
        ivBack.setOnClickListener {
            onBackPressed()
        }

        btnSubmit.setOnClickListener {
            if (TextUtils.isEmpty(etCurrentPassword.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.current_password).toLowerCase()
                    )
                )
            } else if (isEmoji(etCurrentPassword.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_invalid_field),
                        getString(R.string.current_password).toLowerCase()
                    )
                )
            } else if (etCurrentPassword.text.toString().trim().length < 6) {
                showError(
                    String.format(
                        getString(
                            R.string.validation_min_length,
                            getString(R.string.current_password),
                            resources.getInteger(R.integer.min_password)
                        )
                    )
                )
            } else if (etCurrentPassword.text.toString().trim().length > 16) {
                showError(
                    String.format(
                        getString(
                            R.string.validation_max_length,
                            getString(R.string.current_password),
                            resources.getInteger(R.integer.max_password)
                        )
                    )
                )
            } else if (TextUtils.isEmpty(etNewPassword.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.new_password).toLowerCase()
                    )
                )
            } else if (isEmoji(etNewPassword.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_invalid_field),
                        getString(R.string.new_password).toLowerCase()
                    )
                )
            } else if (etNewPassword.text.toString().trim().length < 6) {
                showError(
                    String.format(
                        getString(
                            R.string.validation_min_length,
                            getString(R.string.new_password),
                            resources.getInteger(R.integer.min_password)
                        )
                    )
                )
            } else if (etNewPassword.text.toString().trim().length > 16) {
                showError(
                    String.format(
                        getString(
                            R.string.validation_max_length,
                            getString(R.string.new_password),
                            resources.getInteger(R.integer.max_password)
                        )
                    )
                )
            } else if (TextUtils.isEmpty(etConfirmNewPassword.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.confirm_new_password).toLowerCase()
                    )
                )
            } else if (etNewPassword.text.toString().trim() != etConfirmNewPassword.text.toString().trim()) {
                showError(getString(R.string.validation_msg_new_password_and_password))
            } else {
                hideKeyboard()
                if (!NetworkUtils.isConnected) {
                    showError(getString(R.string.network_error))
                } else {
                    showLoader()
                    if (mUserPassword.equals(etCurrentPassword.text.toString().trim())) {
                        /*userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
                        val thread = Thread(Runnable {
                            userViewModel.updatePassword(SharedPrefsManager.getString(Const.USER_EMAIL), etNewPassword.text.toString().trim(), SharedPrefsManager.getLong(Const.USER_ID))
                            SharedPrefsManager.setString(Const.USER_PASSWORD, etNewPassword.text.toString().trim())

                            hideLoader()
                            finish()
                        })
                        thread.start()*/
                        updatePassword(etNewPassword.text.toString().trim())
                    } else {
                        hideLoader()
                        showError(getString(R.string.password_is_incorrect))
                    }
                }
            }
        }
    }

    private fun updatePassword(password: String) {
        mAuth.signInWithEmailAndPassword(SharedPrefsManager.getString(Const.USER_EMAIL), etCurrentPassword.text.toString().trim()).addOnCompleteListener(this) { tasksignin ->
            if (!tasksignin.isSuccessful) {
                hideLoader()
                if ((tasksignin.exception as FirebaseAuthInvalidUserException).errorCode.equals(getString(R.string.error_user_not_found))) {
                    showToast(tasksignin.exception!!.message)
                    deleteAccount()
                } else {
                    showError(tasksignin.exception!!.message)
                }
            } else {
                val mFirebaseUser = mAuth.currentUser
                if (mFirebaseUser != null) {
                    mFirebaseUser.updatePassword(password).addOnCompleteListener(OnCompleteListener<Void?> { task ->
                        if (task.isSuccessful) {
                            if (task.isSuccessful) {
                                userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
                                val thread = Thread(Runnable {
                                    userViewModel.updatePassword(
                                        SharedPrefsManager.getString(Const.USER_EMAIL),
                                        etNewPassword.text.toString().trim(),
                                        SharedPrefsManager.getLong(Const.USER_ID)
                                    )
                                    SharedPrefsManager.setString(Const.USER_PASSWORD, etNewPassword.text.toString().trim())

                                    hideLoader()
                                    finish()
                                })
                                thread.start()
                            } else {
                                hideLoader()
                                showError(task.exception!!.message)
                            }
                        } else {
                            hideLoader()
                            showError(task.exception!!.message)
                        }
                    })
                } else {
                    hideLoader()
                    showError(getString(R.string.we_cant_find_requested_user))
                }
            }
        }
    }

    private fun deleteAccount() {

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        abilitiesViewModel = ViewModelProvider(this).get(AbilitiesViewModel::class.java)
        medicationsViewModel = ViewModelProvider(this).get(MedicationsViewModel::class.java)
        conditionsViewModel = ViewModelProvider(this).get(ConditionsViewModel::class.java)
        recordsViewModel = ViewModelProvider(this).get(RecordsViewModel::class.java)
        userDetailsViewModel = ViewModelProvider(this).get(UserDetailsViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val thread = Thread(Runnable {
            noteViewModel.deleteAllNotes(mUserId)
            abilitiesViewModel.deleteAllAbilities(mUserId)
            medicationsViewModel.deleteAllMedications(mUserId)
            conditionsViewModel.deleteAllConditions(mUserId)
            recordsViewModel.deleteAllRecords(mUserId)
            userDetailsViewModel.deleteAllUserDetails(mUserId)
            userViewModel.deleteUser(mUserId)

            SharedPrefsManager.clearPrefs()
        })
        thread.start()

        val intent = Intent(this, WelcomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}