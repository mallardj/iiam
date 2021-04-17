package com.app.iiam.addmedications

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.iiam.R
import com.app.iiam.adapter.SpinnerArrayAdapter
import com.app.iiam.addnote.MediaBottomSheetFragment
import com.app.iiam.base.BaseActivity
import com.app.iiam.database.entities.Medications
import com.app.iiam.database.viewmodel.MedicationsViewModel
import com.app.iiam.preference.SharedPrefsManager
import com.app.iiam.utils.Const
import com.app.iiam.utils.logInfo
import com.bumptech.glide.Glide
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import kotlinx.android.synthetic.main.activity_add_medications.*
import kotlinx.android.synthetic.main.toolbar_home.*
import lv.chi.photopicker.PhotoPickerFragment
import java.io.IOException
import java.util.*

class AddMedicationsActivity : BaseActivity(), PhotoPickerFragment.Callback, MediaBottomSheetFragment.ItemClickListener {

    private lateinit var medicationsViewModel: MedicationsViewModel
    private var uri: Uri? = null
    private var mDoesList = mutableListOf<String>()
    private var mRouteList = mutableListOf<String>()
    private var isEditMedication = false
    private var mMedicationId: Long = 0

    companion object {
        private const val TAG = "AddMedicationsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medications)

        initView()
        handleClickListner()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {

        medicationsViewModel = ViewModelProvider(this).get(MedicationsViewModel::class.java)

        isEditMedication = intent.getBooleanExtra(Const.IS_EDIT_MEDICATIONS, false)

        tvToolbarTitle.visibility = View.GONE
        ivToolbarRightIcon.visibility = View.GONE
        ivBack.visibility = View.VISIBLE
        tvToolbarName.visibility = View.VISIBLE

        if (isEditMedication) {
            mMedicationId = intent.getLongExtra(Const.ID_MEDICATIONS, 0)
            tvToolbarName.setText(getString(R.string.edit_medications_))
            getMedicationsDetailsFromDB()
        } else {
            tvToolbarName.setText(getString(R.string.add_medications_))
        }

        mDoesList.add(getString(R.string.dose_unit))
        mDoesList.add(getString(R.string.mg))
        mDoesList.add(getString(R.string.mg_ml))
        mDoesList.add(getString(R.string.mcg_ml))
        mDoesList.add(getString(R.string.u_ml))
        mDoesList.add(getString(R.string.other))
        spDoesUnit.adapter = SpinnerArrayAdapter(this, mDoesList)

        mRouteList.add(getString(R.string.select_route))
        mRouteList.add(getString(R.string.oral))
        mRouteList.add(getString(R.string.injection))
        mRouteList.add(getString(R.string.ocular))
        mRouteList.add(getString(R.string.otic))
        mRouteList.add(getString(R.string.other))
        spRoute.adapter = SpinnerArrayAdapter(this, mRouteList)

        etAdditionalDoesInfo.setOnTouchListener(View.OnTouchListener { v, event ->
            if (etAdditionalDoesInfo.hasFocus()) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_SCROLL -> {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                        return@OnTouchListener true
                    }
                }
            }
            false
        })

        etAdditionalRouteInfo.setOnTouchListener(View.OnTouchListener { v, event ->
            if (etAdditionalRouteInfo.hasFocus()) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_SCROLL -> {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                        return@OnTouchListener true
                    }
                }
            }
            false
        })

        etFrequency.setOnTouchListener(View.OnTouchListener { v, event ->
            if (etFrequency.hasFocus()) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_SCROLL -> {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                        return@OnTouchListener true
                    }
                }
            }
            false
        })

    }

    private fun getMedicationsDetailsFromDB() {
        showLoader()

        medicationsViewModel.getMedications(mMedicationId).observe(this, Observer { medication ->
            medication.let {

                uri = Uri.parse(it.medicationsPicture)
                Glide.with(this)
                        .asBitmap()
                        .load(uri)
                        .placeholder(R.drawable.ic_add_picture)
                        .centerCrop()
                        .into(tvAddPicture)

                etMedicationName.setText(it.medicationsName)
                etMedicationName.setSelection(etMedicationName.text.toString().trim().length)

                etDose.setText(it.medicationsDose)

                if (it.medicationsDoseUnit.equals(getString(R.string.mg))) {
                    spDoesUnit.setSelection(1)
                } else if (it.medicationsDoseUnit.equals(getString(R.string.mg_ml))) {
                    spDoesUnit.setSelection(2)
                } else if (it.medicationsDoseUnit.equals(getString(R.string.mcg_ml))) {
                    spDoesUnit.setSelection(3)
                } else if (it.medicationsDoseUnit.equals(getString(R.string.u_ml))) {
                    spDoesUnit.setSelection(4)
                } else if (it.medicationsDoseUnit.equals(getString(R.string.other))) {
                    spDoesUnit.setSelection(5)
                }

                etAdditionalDoesInfo.setText(it.medicationsDoseInfo)

                if (it.medicationsRoute.equals(getString(R.string.oral))) {
                    spRoute.setSelection(1)
                } else if (it.medicationsRoute.equals(getString(R.string.injection))) {
                    spRoute.setSelection(2)
                } else if (it.medicationsRoute.equals(getString(R.string.ocular))) {
                    spRoute.setSelection(3)
                } else if (it.medicationsRoute.equals(getString(R.string.otic))) {
                    spRoute.setSelection(4)
                } else if (it.medicationsRoute.equals(getString(R.string.other))) {
                    spRoute.setSelection(5)
                }

                etAdditionalRouteInfo.setText(it.medicationsRouteInfo)

                etFrequency.setText(it.medicationsFrequency)
            }
        })

        hideLoader()
    }

    @SuppressLint("DefaultLocale")
    private fun handleClickListner() {
        ivBack.setOnClickListener {
            onBackPressed()
        }

        tvAddPicture.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Const.MEDIA_TYPE, if (uri != null) uri.toString() else "")
            bundle.putBoolean(Const.MULTI_MEDIA, false)
            val mediaBottomSheetFragment = MediaBottomSheetFragment.newInstance(bundle)
            mediaBottomSheetFragment.show(supportFragmentManager, MediaBottomSheetFragment.TAG)
        }

        btnSubmit.setOnClickListener {
            if (TextUtils.isEmpty(etMedicationName.text.toString().trim())) {
                showError(
                        String.format(
                                getString(R.string.validation_required_field),
                                getString(R.string.medication_name).toLowerCase()
                        )
                )
            } else if (etMedicationName.text.toString().trim().length < 2) {
                showError(
                        String.format(
                                getString(
                                        R.string.validation_min_length,
                                        getString(R.string.medication_name),
                                        resources.getInteger(R.integer.min_name)
                                )
                        )
                )
            } else if (TextUtils.isEmpty(etDose.text.toString().trim())) {
                showError(
                        String.format(
                                getString(R.string.validation_required_field),
                                getString(R.string.dose).toLowerCase()
                        )
                )
            } else if (spDoesUnit.selectedItemPosition == 0) {
                showError(
                        String.format(
                                getString(R.string.validation_selection),
                                getString(R.string.dose_unit).toLowerCase()
                        )
                )
            } else if (spRoute.selectedItemPosition == 0) {
                showError(
                        String.format(
                                getString(R.string.validation_selection),
                                getString(R.string.route).toLowerCase()
                        )
                )
            } else if (TextUtils.isEmpty(etFrequency.text.toString().trim())) {
                showError(
                        String.format(
                                getString(R.string.validation_required_field),
                                getString(R.string.frequency).toLowerCase()
                        )
                )
            } else {
                showLoader()
                if (isEditMedication) {
                    val thread = Thread(Runnable {
                        medicationsViewModel.updateMedications(
                                SharedPrefsManager.getLong(Const.USER_ID),
                                if (uri != null) uri.toString() else "",
                                etMedicationName.text.toString().trim(),
                                etDose.text.toString().trim(),
                                mDoesList[spDoesUnit.selectedItemPosition],
                                etAdditionalDoesInfo.text.toString().trim(),
                                mRouteList[spRoute.selectedItemPosition],
                                etAdditionalRouteInfo.text.toString().trim(),
                                etFrequency.text.toString().trim(),
                                mMedicationId
                        )

                        hideLoader()
                        finish()
                    })
                    thread.start()
                } else {
                    val medications = Medications(
                            SharedPrefsManager.getLong(Const.USER_ID),
                            if (uri != null) uri.toString() else "",
                            etMedicationName.text.toString().trim(),
                            etDose.text.toString().trim(),
                            mDoesList[spDoesUnit.selectedItemPosition],
                            etAdditionalDoesInfo.text.toString().trim(),
                            mRouteList[spRoute.selectedItemPosition],
                            etAdditionalRouteInfo.text.toString().trim(),
                            etFrequency.text.toString().trim()
                    )
                    medicationsViewModel.insert(medications)

                    hideLoader()
                    finish()
                }
            }
        }

    }

    override fun onSelectImage() {
        openPicker()
    }

    override fun onSelectVideo() {

    }

    override fun onRemoveMedia() {
        uri = null
        Glide.with(this)
            .asBitmap()
            .load(R.drawable.ic_add_picture)
            .placeholder(R.drawable.ic_add_picture)
            .centerCrop()
            .into(tvAddPicture)
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
        uri = photos[0]
        Glide.with(this)
            .asBitmap()
            .load(uri)
            .placeholder(R.drawable.ic_add_picture)
            .centerCrop()
            .into(tvAddPicture)

        try {
            val linesFound = mutableListOf<String>()
            val image = InputImage.fromFilePath(baseContext, uri)
            val recognizer = TextRecognition.getClient()
            val result = recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    for (block in visionText.textBlocks) {
                        val blockText = block.text
                        val blockCornerPoints = block.cornerPoints
                        val blockFrame = block.boundingBox
                        print("Block:")
                        print(blockText)
                        for (line in block.lines) {
                            linesFound.add(line.text)
                            val lineCornerPoints = line.cornerPoints
                            val lineFrame = line.boundingBox
                            for (element in line.elements) {
                                linesFound.add(element.text)
//                                val elementCornerPoints = element.cornerPoints
//                                val elementFrame = element.boundingBox
                                print("Element:")
                                print(element.text)

                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                }
            val data = ArrayList<String>(linesFound)
            DetectedFields.data = data
            DetectedFields.uri = uri
            val intent = Intent(this@AddMedicationsActivity, AutofillActivity::class.java)
            intent.putExtra("mylist", data)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}