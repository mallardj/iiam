package com.app.iiam.biographicinformation

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.iiam.R
import com.app.iiam.adapter.SpinnerArrayAdapter
import com.app.iiam.base.BaseActivity
import com.app.iiam.database.entities.UserDetails
import com.app.iiam.database.viewmodel.UserDetailsViewModel
import com.app.iiam.preference.SharedPrefsManager
import com.app.iiam.utils.Const
import com.app.iiam.utils.logInfo
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_biographic_information.*
import kotlinx.android.synthetic.main.toolbar_home.*
import lv.chi.photopicker.PhotoPickerFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BiographicInformationActivity : BaseActivity(), PhotoPickerFragment.Callback {

    private lateinit var userDetailsViewModel: UserDetailsViewModel
    private var userId: Long = 0
    private lateinit var userEmail: String
    private var isInsert = false
    private var mGenderList = mutableListOf<String>()
    private var uri: Uri? = null

    companion object {
        private const val TAG = "BiographicInformationActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biographic_information)

        initView()
        handleClickListner()

        getUserDetailsFromDB()
    }

    private fun getUserDetailsFromDB() {
        showLoader()

        userDetailsViewModel = ViewModelProvider(this).get(UserDetailsViewModel::class.java)

        userDetailsViewModel.getUserDetails(SharedPrefsManager.getLong(Const.USER_ID)).observe(this, Observer { userDetailsViewModel ->
            userDetailsViewModel.let {
                if (userDetailsViewModel == null) {
                    isInsert = true
                    logInfo(TAG, "No data")
                } else {
                    isInsert = false
                    logInfo(TAG, it.userName)

                    etName.setText(it.userName)
                    etName.setSelection(etName.text.toString().trim().length)
                    etPreferredName.setText(it.userPreferredName)
                    etAge.setText(it.userAge)
                    etAllergies.setText(it.userAllergies)
                    etPhoneNumber.setText(it.userPhoneNumber)
                    etEmergencyContact.setText(it.userEmergencyContact)
                    if (it.userGender.equals(getString(R.string.male))) {
                        spGender.setSelection(1)
                    } else if (it.userGender.equals(getString(R.string.female))) {
                        spGender.setSelection(2)
                    } else {
                        spGender.setSelection(3)
                    }
                    uri = Uri.parse(it.userProfilePicture)
                    Glide.with(this@BiographicInformationActivity)
                        .asBitmap()
                        .load(it.userProfilePicture)
                        .placeholder(R.drawable.ic_dummy_image)
                        .circleCrop()
                        .into(tvProfilePicture)
                }
            }
        })

        hideLoader()

    }

    private fun initView() {

        tvToolbarTitle.visibility = View.GONE
        ivToolbarRightIcon.visibility = View.GONE
        ivBack.visibility = View.VISIBLE
        tvToolbarName.setText(getString(R.string.biographic_info))
        tvToolbarName.visibility = View.VISIBLE

        mGenderList.add(getString(R.string.gender))
        mGenderList.add(getString(R.string.male))
        mGenderList.add(getString(R.string.female))
        mGenderList.add(getString(R.string.other))
        spGender.adapter = SpinnerArrayAdapter(this@BiographicInformationActivity, mGenderList)

        userId = SharedPrefsManager.getLong(Const.USER_ID)
        userEmail = SharedPrefsManager.getString(Const.USER_EMAIL)
        etEmail.setText(userEmail)
    }

    @SuppressLint("DefaultLocale")
    private fun handleClickListner() {
        ivBack.setOnClickListener {
            onBackPressed()
        }

        tvProfilePicture.setOnClickListener {
            openPicker()
        }

        btnSubmit.setOnClickListener {
            if (uri == null) {
                showError(
                    String.format(
                        getString(R.string.validation_selection),
                        getString(R.string.profile_picture).toLowerCase()
                    )
                )
            } else if (TextUtils.isEmpty(etName.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.name).toLowerCase()
                    )
                )
            } else if (etName.text.toString().trim().length < 2) {
                showError(
                    String.format(
                        getString(
                            R.string.validation_min_length,
                            getString(R.string.name),
                            resources.getInteger(R.integer.min_name)
                        )
                    )
                )
            } else if (etName.text.toString().trim().length > 30) {
                showError(
                    String.format(
                        getString(
                            R.string.validation_max_length,
                            getString(R.string.name),
                            resources.getInteger(R.integer.max_name)
                        )
                    )
                )
            } else if (TextUtils.isEmpty(etPreferredName.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.preferred_name).toLowerCase()
                    )
                )
            } else if (etPreferredName.text.toString().trim().length < 2) {
                showError(
                    String.format(
                        getString(
                            R.string.validation_min_length,
                            getString(R.string.preferred_name),
                            resources.getInteger(R.integer.min_name)
                        )
                    )
                )
            } else if (etPreferredName.text.toString().trim().length > 30) {
                showError(
                    String.format(
                        getString(
                            R.string.validation_max_length,
                            getString(R.string.preferred_name),
                            resources.getInteger(R.integer.max_name)
                        )
                    )
                )
            } else if (TextUtils.isEmpty(etAge.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.age).toLowerCase()
                    )
                )
            } else if (spGender.selectedItemPosition == 0) {
                showError(
                    String.format(
                        getString(R.string.validation_selection),
                        getString(R.string.gender).toLowerCase()
                    )
                )
            } else if (TextUtils.isEmpty(etAllergies.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.allergies).toLowerCase()
                    )
                )
            } else if (TextUtils.isEmpty(etPhoneNumber.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.phone_number).toLowerCase()
                    )
                )
            } else if (etPhoneNumber.text.toString().trim().length < 8) {
                showError(
                    String.format(
                        getString(
                            R.string.validation_min_digits,
                            getString(R.string.phone_number),
                            resources.getInteger(R.integer.min_phone_number)
                        )
                    )
                )
            } else if (etPhoneNumber.text.toString().trim().length > 10) {
                showError(
                    String.format(
                        getString(
                            R.string.validation_max_digits,
                            getString(R.string.phone_number),
                            resources.getInteger(R.integer.max_phone_number)
                        )
                    )
                )
            } else if (TextUtils.isEmpty(etEmergencyContact.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.emergency_contact).toLowerCase()
                    )
                )
            } else if (etEmergencyContact.text.toString().trim().length < 8) {
                showError(
                    String.format(
                        getString(
                            R.string.validation_min_digits,
                            getString(R.string.emergency_contact),
                            resources.getInteger(R.integer.min_phone_number)
                        )
                    )
                )
            } else if (etEmergencyContact.text.toString().trim().length > 10) {
                showError(
                    String.format(
                        getString(
                            R.string.validation_max_digits,
                            getString(R.string.emergency_contact),
                            resources.getInteger(R.integer.max_phone_number)
                        )
                    )
                )
            } else {
                showLoader()

                if (isInsert) {
                    val userDetails = UserDetails(
                        userId,
                        etName.text.toString().trim(),
                        etPreferredName.text.toString().trim(),
                        etAge.text.toString().trim(),
                        mGenderList[spGender.selectedItemPosition],
                        etAllergies.text.toString().trim(),
                        etPhoneNumber.text.toString().trim(),
                        etEmergencyContact.text.toString().trim(),
                        uri.toString()
                    )
                    userDetailsViewModel.insert(userDetails)

                    hideLoader()
                    finish()
                } else {
                    val thread = Thread(Runnable {
                        userDetailsViewModel.updateUserDetails(
                            userId,
                            etName.text.toString().trim(),
                            etPreferredName.text.toString().trim(),
                            etAge.text.toString().trim(),
                            mGenderList[spGender.selectedItemPosition],
                            etAllergies.text.toString().trim(),
                            etPhoneNumber.text.toString().trim(),
                            etEmergencyContact.text.toString().trim(),
                            uri.toString()
                        )

                        hideLoader()
                        finish()
                    })
                    thread.start()
                }
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun openPicker() {
        PhotoPickerFragment.newInstance(
            multiple = true,
            allowCamera = true,
            maxSelection = 1,
            theme = R.style.ChiliPhotoPicker_Dark
        ).show(supportFragmentManager, "picker")
    }

    override fun onImagesPicked(photos: ArrayList<Uri>) {
        logInfo(TAG, photos.joinToString(separator = "\n") { it.toString() })
        /*Glide.with(this@BiographicInformationActivity)
            .asBitmap()
            .load(photos[0])
            .placeholder(R.drawable.ic_dummy_image)
            .circleCrop()
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                    mBitmap = bitmap
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    logInfo(TAG, "No image")
                }
            })*/
        uri = photos[0]
        Glide.with(this@BiographicInformationActivity)
            .asBitmap()
            .load(uri)
            .placeholder(R.drawable.ic_dummy_image)
            .circleCrop()
            .into(tvProfilePicture)
    }

}