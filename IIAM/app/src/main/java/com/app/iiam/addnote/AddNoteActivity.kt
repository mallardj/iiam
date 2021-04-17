package com.app.iiam.addnote

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.iiam.R
import com.app.iiam.base.BaseActivity
import com.app.iiam.database.entities.Note
import com.app.iiam.database.viewmodel.NoteViewModel
import com.app.iiam.preference.SharedPrefsManager
import com.app.iiam.utils.*
import com.bumptech.glide.Glide
import com.yanzhenjie.album.Action
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumFile
import com.yanzhenjie.album.api.widget.Widget
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.toolbar_home.*
import lv.chi.photopicker.PhotoPickerFragment
import java.util.*

class AddNoteActivity : BaseActivity(), PhotoPickerFragment.Callback, MediaBottomSheetFragment.ItemClickListener {

    private lateinit var noteViewModel: NoteViewModel
    private var uri: Uri? = null
    private var mCalendar: Calendar? = null
    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDay: Int? = null
    private var timeInMillis: Long? = null
    private var isEditNote = false
    private var mNodeId: Long = 0
    private var mVideo: String? = null
    private var path: String? = null
    private var mMediaType = ""
    //private var mAlbumFiles = mutableListOf<AlbumFile?>()

    companion object {
        private const val TAG = "AddNoteActivity"
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        initView()
        handleClickListner()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        isEditNote = intent.getBooleanExtra(Const.IS_EDIT_NOTE, false)

        tvToolbarTitle.visibility = View.GONE
        ivToolbarRightIcon.visibility = View.GONE
        ivBack.visibility = View.VISIBLE
        tvToolbarName.visibility = View.VISIBLE

        etComment.setOnTouchListener(View.OnTouchListener { v, event ->
            if (etComment.hasFocus()) {
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

        if (isEditNote) {
            mNodeId = intent.getLongExtra(Const.ID_NOTE, 0)
            tvToolbarName.setText(getString(R.string.edit_note))
            getNoteDataFromDB()
        } else {
            tvToolbarName.setText(getString(R.string.add_note))
        }
    }

    private fun getNoteDataFromDB() {
        showLoader()

        noteViewModel.getNote(mNodeId).observe(this, Observer { note ->
            note.let {

                if (it.noteMediaType.equals(getString(R.string.image))) {
                    mMediaType = getString(R.string.image)
                    uri = Uri.parse(it.notePicture)
                    Glide.with(this)
                        .asBitmap()
                        .load(uri)
                        .placeholder(R.drawable.ic_add_media)
                        .centerCrop()
                        .into(tvAddPicture)

                    ivPlay.visibility = View.GONE
                } else if (it.noteMediaType.equals(getString(R.string.video))) {
                    mMediaType = getString(R.string.video)
                    path = it.notePicture
                    mVideo = it.noteVideo
                    Glide.with(this)
                        .asBitmap()
                        .load(path)
                        .placeholder(R.drawable.ic_add_media)
                        .centerCrop()
                        .into(tvAddPicture)

                    ivPlay.visibility = View.VISIBLE
                } else {
                    mMediaType = ""
                    ivPlay.visibility = View.GONE
                }

                timeInMillis = it.noteDate
                etDate.setText(it.noteDate.toString().setDate(it.noteDate!!, NOTE_FORMATTED_DATE))

                etTitle.setText(it.noteTitle)
                etTitle.setSelection(etTitle.text.toString().trim().length)

                etComment.setText(it.noteComment)
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
            bundle.putString(Const.MEDIA_TYPE, mMediaType)
            bundle.putBoolean(Const.MULTI_MEDIA, true)
            val mediaBottomSheetFragment = MediaBottomSheetFragment.newInstance(bundle)
            mediaBottomSheetFragment.show(supportFragmentManager, MediaBottomSheetFragment.TAG)
        }

        etDate.setOnClickListener {
            showDatePicker()
        }

        btnSubmit.setOnClickListener {
            if (TextUtils.isEmpty(etDate.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_selection),
                        getString(R.string.date).toLowerCase()
                    )
                )
            } else if (TextUtils.isEmpty(etTitle.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.title).toLowerCase()
                    )
                )
            } else if (etTitle.text.toString().trim().length < 2) {
                showError(
                    String.format(
                        getString(
                            R.string.validation_min_length,
                            getString(R.string.title),
                            resources.getInteger(R.integer.min_name)
                        )
                    )
                )
            } else if (TextUtils.isEmpty(etComment.text.toString().trim())) {
                showError(
                    String.format(
                        getString(R.string.validation_required_field),
                        getString(R.string.comment).toLowerCase()
                    )
                )
            } else if (etComment.text.toString().trim().length < 2) {
                showError(
                    String.format(
                        getString(
                            R.string.validation_min_length,
                            getString(R.string.comment),
                            resources.getInteger(R.integer.min_name)
                        )
                    )
                )
            } else {
                if (mMediaType.equals(getString(R.string.image))) {
                    showLoader()
                    if (isEditNote) {
                        val thread = Thread(Runnable {
                            noteViewModel.updateNote(
                                SharedPrefsManager.getLong(Const.USER_ID),
                                mMediaType,
                                if (uri != null) uri.toString() else "",
                                "",
                                timeInMillis!!,
                                etTitle.text.toString().trim(),
                                etComment.text.toString().trim(),
                                mNodeId
                            )

                            hideLoader()
                            finish()
                        })
                        thread.start()
                    } else {
                        val note = Note(
                            SharedPrefsManager.getLong(Const.USER_ID),
                            mMediaType,
                            if (uri != null) uri.toString() else "",
                            "",
                            timeInMillis,
                            etTitle.text.toString().trim(),
                            etComment.text.toString().trim()
                        )
                        noteViewModel.insert(note)

                        hideLoader()
                        finish()
                    }
                } else if (mMediaType.equals(getString(R.string.video))) {
                    showLoader()
                    if (isEditNote) {
                        val thread = Thread(Runnable {
                            noteViewModel.updateNote(
                                SharedPrefsManager.getLong(Const.USER_ID),
                                mMediaType,
                                if (path != null) path.toString() else "",
                                if (mVideo != null) mVideo.toString() else "",
                                timeInMillis!!,
                                etTitle.text.toString().trim(),
                                etComment.text.toString().trim(),
                                mNodeId
                            )

                            hideLoader()
                            finish()
                        })
                        thread.start()
                    } else {
                        val note = Note(
                            SharedPrefsManager.getLong(Const.USER_ID),
                            mMediaType,
                            if (path != null) path.toString() else "",
                            if (mVideo != null) mVideo.toString() else "",
                            timeInMillis,
                            etTitle.text.toString().trim(),
                            etComment.text.toString().trim()
                        )
                        noteViewModel.insert(note)

                        hideLoader()
                        finish()
                    }
                } else {
                    showLoader()
                    if (isEditNote) {
                        val thread = Thread(Runnable {
                            noteViewModel.updateNote(
                                SharedPrefsManager.getLong(Const.USER_ID),
                                mMediaType,
                                "",
                                "",
                                timeInMillis!!,
                                etTitle.text.toString().trim(),
                                etComment.text.toString().trim(),
                                mNodeId
                            )

                            hideLoader()
                            finish()
                        })
                        thread.start()
                    } else {
                        val note = Note(
                            SharedPrefsManager.getLong(Const.USER_ID),
                            mMediaType,
                            "",
                            "",
                            timeInMillis,
                            etTitle.text.toString().trim(),
                            etComment.text.toString().trim()
                        )
                        noteViewModel.insert(note)

                        hideLoader()
                        finish()
                    }
                }
            }
        }

    }

    override fun onSelectImage() {
        openImagePicker()
    }

    override fun onSelectVideo() {
        openVideoPicker()
    }

    override fun onRemoveMedia() {
        removeMedia()
    }

    private fun openImagePicker() {
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
            .placeholder(R.drawable.ic_add_media)
            .centerCrop()
            .into(tvAddPicture)

        mMediaType = getString(R.string.image)
        ivPlay.visibility = View.GONE
    }

    @SuppressLint("NewApi")
    private fun openVideoPicker() {
        Album.video(this) // Video selection.
            .multipleChoice()
            .camera(true)
            .columnCount(3)
            .selectCount(1)
            .widget(
                Widget.newDarkBuilder(this)
                    .title("Select Video")
                    .statusBarColor(getColor(R.color.colorPrimary))
                    .toolBarColor(getColor(R.color.colorPrimary))
                    .mediaItemCheckSelector(getColor(R.color.colorWhite), getColor(R.color.colorLight))
                    .bucketItemCheckSelector(getColor(R.color.colorLight), getColor(R.color.colorPrimary))
                    .build()
            )
            .onResult(object : Action<ArrayList<AlbumFile?>?> {
                override fun onAction(result: ArrayList<AlbumFile?>) {
                    /*mMimeType = video/mp4
                    mPath = /storage/emulated/0/Movies/20200909_164319511_703ae065b4ce9cf6eb4ead01bb12fbf4.mp4
                    mThumbPath = /storage/emulated/0/AlbumCache/2aeeb020e628d4de37f8c3b6dd7a93e5.album*/

                    path = result[0]?.thumbPath
                    mVideo = result[0]?.path

                    Glide.with(this@AddNoteActivity)
                        .asBitmap()
                        .load(path)
                        .placeholder(R.drawable.ic_add_media)
                        .centerCrop()
                        .into(tvAddPicture)

                    mMediaType = getString(R.string.video)
                    ivPlay.visibility = View.VISIBLE

                }
            })
            .onCancel(object : Action<String?> {
                override fun onAction(result: String) {
                    logInfo(TAG, "Cancel: ${result}")
                }
            })
            .start()
    }

    private fun removeMedia() {
        if (mMediaType.equals(getString(R.string.image))) {
            uri = null
        } else if (mMediaType.equals(getString(R.string.video))) {
            path = null
            mVideo = null
        }
        Glide.with(this)
            .asBitmap()
            .load(R.drawable.ic_add_media)
            .placeholder(R.drawable.ic_add_media)
            .centerCrop()
            .into(tvAddPicture)

        mMediaType = ""
        ivPlay.visibility = View.GONE

    }

    private fun showDatePicker() {
        mCalendar = Calendar.getInstance()
        mYear = Calendar.getInstance().get(Calendar.YEAR)
        mMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        var mYearParam = 0
        var mMonthParam = 0
        var mDayParam = 0

        if (isEditNote) {
            mYearParam = timeInMillis.toString().getYear(timeInMillis!!)
            mMonthParam = (timeInMillis.toString().getMonth(timeInMillis!!)) - 1
            mDayParam = timeInMillis.toString().getDay(timeInMillis!!)
        } else {
            mYearParam = mYear!!
            mMonthParam = mMonth!! - 1
            mDayParam = mDay!!
        }

        val datePickerDialog = DatePickerDialog(
            this,
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