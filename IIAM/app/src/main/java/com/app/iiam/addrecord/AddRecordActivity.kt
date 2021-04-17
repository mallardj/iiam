package com.app.iiam.addrecord

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.iiam.R
import com.app.iiam.addnote.MediaBottomSheetFragment
import com.app.iiam.autoviewpager.ImagesViewPagerAdapter
import com.app.iiam.base.BaseActivity
import com.app.iiam.database.entities.Records
import com.app.iiam.database.viewmodel.RecordsViewModel
import com.app.iiam.preference.SharedPrefsManager
import com.app.iiam.utils.*
import kotlinx.android.synthetic.main.activity_add_record.*
import kotlinx.android.synthetic.main.toolbar_home.*
import lv.chi.photopicker.PhotoPickerFragment
import java.util.*

class AddRecordActivity : BaseActivity(), PhotoPickerFragment.Callback, ImagesViewPagerAdapter.OnItemClickListener, MediaBottomSheetFragment.ItemClickListener {

    private lateinit var recordsViewModel: RecordsViewModel

    //private var uri: Uri? = null
    private var mCalendar: Calendar? = null
    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null
    private var timeInMillis: Long? = null
    private var isEditRecords = false
    private var mRecordsId: Long = 0
    private var mUriList = mutableListOf<Uri>()
    private var mImageAdapter: ImagesViewPagerAdapter? = null

    companion object {
        private const val TAG = "AddRecordActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_record)

        initView()
        handleClickListner()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {

        recordsViewModel = ViewModelProvider(this).get(RecordsViewModel::class.java)

        isEditRecords = intent.getBooleanExtra(Const.IS_EDIT_RECORDS, false)

        tvToolbarTitle.visibility = View.GONE
        ivToolbarRightIcon.visibility = View.GONE
        ivBack.visibility = View.VISIBLE
        tvToolbarName.visibility = View.VISIBLE

        mImageAdapter = ImagesViewPagerAdapter(tabImage.context, mUriList, this)
        tabImage.setupWithViewPager(autoPagerImage)
        autoPagerImage.adapter = mImageAdapter

        if (isEditRecords) {
            mRecordsId = intent.getLongExtra(Const.ID_RECORDS, 0)
            tvToolbarName.setText(getString(R.string.edit_records))
            getRecordDetailsFromDB()
        } else {
            tvToolbarName.setText(getString(R.string.add_records))
        }

        etDocumentName.setOnTouchListener(View.OnTouchListener { v, event ->
            if (etDocumentName.hasFocus()) {
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

        etPagesInDocument.setOnTouchListener(View.OnTouchListener { v, event ->
            if (etPagesInDocument.hasFocus()) {
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

    private fun getRecordDetailsFromDB() {
        showLoader()

        recordsViewModel.getRecords(mRecordsId).observe(this, Observer { record ->
            record.let {

                if (TextUtils.isEmpty(it.recordsPicture)) {
                    tvAddPicture.visibility = View.VISIBLE
                    fmRecord.visibility = View.GONE
                } else {
                    val mediaList: MutableList<String> = it.recordsPicture!!.split(",").toMutableList()
                    mUriList.clear()
                    for (i in 0 until mediaList.size) {
                        mUriList.add(Uri.parse(mediaList[i]))
                    }
                    mImageAdapter?.notifyDataSetChanged()

                    tvAddPicture.visibility = View.GONE
                    fmRecord.visibility = View.VISIBLE
                }

                /*if (mUriList.size != 0) {
                    tvAddPicture.visibility = View.GONE
                    fmRecord.visibility = View.VISIBLE
                }*/

                etDocumentName.setText(it.recordsDocumentsName)
                etDocumentName.setSelection(etDocumentName.text.toString().trim().length)

                timeInMillis = it.recordsDate
                etDate.setText(it.recordsDate.toString().setDate(it.recordsDate!!, NOTE_FORMATTED_DATE))

                etPagesInDocument.setText(it.recordsPagesInDocument)

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
            bundle.putString(Const.MEDIA_TYPE, if (mUriList.size != 0) getString(R.string.image) else "")
            bundle.putBoolean(Const.MULTI_MEDIA, false)
            val mediaBottomSheetFragment = MediaBottomSheetFragment.newInstance(bundle)
            mediaBottomSheetFragment.show(supportFragmentManager, MediaBottomSheetFragment.TAG)
        }

        etDate.setOnClickListener {
            showDatePicker()
        }

        btnSubmit.setOnClickListener {
            if (TextUtils.isEmpty(etDocumentName.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.document_name).toLowerCase()
                    )
                )
            } else if (TextUtils.isEmpty(etDate.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_selection),
                        getString(R.string.date).toLowerCase()
                    )
                )
            } else if (TextUtils.isEmpty(etPagesInDocument.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.pages_in_document).toLowerCase()
                    )
                )
            } else {
                showLoader()

                val mImagesStringList = mutableListOf<String>()
                for (i in 0 until mUriList.size) {
                    mImagesStringList.add(mUriList[i].toString())
                }

                if (isEditRecords) {
                    val thread = Thread(Runnable {
                        recordsViewModel.updateRecords(
                            SharedPrefsManager.getLong(Const.USER_ID),
                            if (mImagesStringList.size != 0) TextUtils.join(",", mImagesStringList) else "",
                            etDocumentName.text.toString().trim(),
                            timeInMillis!!,
                            etPagesInDocument.text.toString().trim(),
                            mRecordsId
                        )

                        hideLoader()
                        finish()
                    })
                    thread.start()
                } else {
                    val records = Records(
                        SharedPrefsManager.getLong(Const.USER_ID),
                        if (mImagesStringList.size != 0) TextUtils.join(",", mImagesStringList) else "",
                        etDocumentName.text.toString().trim(),
                        timeInMillis,
                        etPagesInDocument.text.toString().trim()
                    )
                    recordsViewModel.insert(records)

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
        mUriList.clear()
        mImageAdapter?.notifyDataSetChanged()
        fmRecord.visibility = View.GONE
        tvAddPicture.visibility = View.VISIBLE
    }

    private fun openPicker() {
        PhotoPickerFragment.newInstance(
            multiple = true,
            allowCamera = true,
            maxSelection = 10,
            theme = R.style.ChiliPhotoPicker_Dark
        ).show(supportFragmentManager, "picker")
    }

    override fun onImagesPicked(photos: ArrayList<Uri>) {
        logInfo(TAG, photos.joinToString(separator = "\n") { it.toString() })
        mUriList.clear()
        mUriList.addAll(photos)
        /*uri = photos[0]
        Glide.with(this@AddRecordActivity)
            .asBitmap()
            .load(uri)
            .placeholder(R.drawable.ic_add_picture)
            .centerCrop()
            .into(tvAddPicture)*/
        if (mUriList.size != 0) {
            tvAddPicture.visibility = View.GONE
            fmRecord.visibility = View.VISIBLE
        }
        mImageAdapter?.notifyDataSetChanged()
    }

    override fun onItemClick() {
        val bundle = Bundle()
        bundle.putString(Const.MEDIA_TYPE, if (mUriList.size != 0) getString(R.string.image) else "")
        bundle.putBoolean(Const.MULTI_MEDIA, false)
        val mediaBottomSheetFragment = MediaBottomSheetFragment.newInstance(bundle)
        mediaBottomSheetFragment.show(supportFragmentManager, MediaBottomSheetFragment.TAG)
    }

    private fun showDatePicker() {
        mCalendar = Calendar.getInstance()
        mYear = Calendar.getInstance().get(Calendar.YEAR)
        mMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        var mYearParam = 0
        var mMonthParam = 0
        var mDayParam = 0

        if (isEditRecords) {
            mYearParam = timeInMillis.toString().getYear(timeInMillis!!)
            mMonthParam = (timeInMillis.toString().getMonth(timeInMillis!!)) - 1
            mDayParam = timeInMillis.toString().getDay(timeInMillis!!)
        } else {
            mYearParam = mYear!!
            mMonthParam = mMonth!! - 1
            mDayParam = mDay!!
        }

        val datePickerDialog = DatePickerDialog(
            this@AddRecordActivity,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                mMonth = monthOfYear + 1
                mYear = year
                mDay = dayOfMonth

                val strDate = mYear.toString() + "-" + mMonth + "-" + mDay
                etDate.setText(strDate.convertDate(NOTE_SIMPLE_DATE, NOTE_FORMATTED_DATE, strDate))

                mCalendar?.set(year, monthOfYear, dayOfMonth)
                timeInMillis = mCalendar?.timeInMillis
                logInfo(TAG, "timeInMillis: ${timeInMillis}")
            }, mYearParam, mMonthParam, mDayParam
        )
        /*val calendar = Calendar.getInstance()
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis())*/
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}